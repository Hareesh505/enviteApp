package com.envite.serviceImpl;

import com.envite.model.User;
import com.envite.model.UserRole;
import com.envite.repositories.UserRepository;
import com.envite.security.LoggedInUserDetails;
import com.envite.security.UserDO;
import com.envite.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by Admin on 1/7/2017.
 */
@Service("userService")
public class UserServiceImpl implements UserService {

    final private Logger logger = LoggerFactory.getLogger(UserServiceImpl.class);

    // Constant used to indicate session has changed, so that redis session manager
    // can be updated
    public static final String SESSION_CHANGED = "__changed__";

    @Autowired
    UserRepository userRepository;

    @Override
    public User findByUserName(String userName){
        return userRepository.findUserByUserName(userName);
    }

    @Override
    @Transactional
    public Map<String, Object> registerUser(String userName, String password, String fullName) {
        Map<String,Object> returnMap = new HashMap();
        User user = findByUserName(userName);
        if(user == null) {
            user = new User();
            user.setUserName(userName);
            user.setFirstName(fullName);
            user.setPassword(password);
            user.setCreatedDate(new Date());

            UserRole userRole = new UserRole();
            userRole.setType("ROLE_USER");
            userRole.setUser(user);
            user.addUserRole(userRole);
            userRepository.save(user);
        } else {
            returnMap.put("error","User Already Exists");
        }
        return returnMap;
    }

    @Override
    public Map<String, Object> updateUserDetails(String firstName, String lastName, String phoneNumber, String email, UserDO userDO){
        Map<String,Object> returnMap = new HashMap();
        User user = findByUserName(userDO.getUserName());
        if(user != null){
            user.setFirstName(firstName);
            user.setLastName(lastName);
            user.setPhoneNumber(phoneNumber);
            user.setEmail(email);
            userRepository.save(user);
            returnMap.put("userJSON", new UserDO(user));
        } else {
            returnMap.put("error","User Not available");
        }
        return  returnMap;
    }

    /**
     * Get current user details.
     * User Id, user name and user role.<br>
     * In case of integrated user <br>
     *
     * @see UserDO
     */
    @Override
    public UserDO getCurrentUserDO() {
        UserDO user = null;
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        Boolean userDOChanged = Boolean.FALSE;
        if(authentication != null && authentication.getPrincipal() instanceof LoggedInUserDetails){
            LoggedInUserDetails userDetails = (LoggedInUserDetails) authentication.getPrincipal();
            if(userDetails != null){
                user = userDetails.getUserDO();
                Long courseId = null;
                if(user!=null){
                    if(user.getCurrentUserRole() == null ){
                        String currentUserRole = null;
                        for(GrantedAuthority auth : authentication.getAuthorities()){
                            if(auth.getAuthority() != null) {
                                if (auth.getAuthority().startsWith("ROLE_")) {
                                    currentUserRole = auth.getAuthority();
                                }
                            }
                        }
                        user.setCurrentUserRole(currentUserRole);
                        userDOChanged = Boolean.TRUE;
                    }
                }
            }
        }
        if( userDOChanged ) {
            setSessionChangedAttribute();
        }
        //FIXED If Current context parameters (csid, cid) Read from user context for
        return user;
    }

    /**
     * This method sets the Session Changed attribute.
     */
    public void setSessionChangedAttribute(){
        try {
            RequestAttributes attr =  RequestContextHolder.currentRequestAttributes();
            attr.setAttribute(SESSION_CHANGED, true, 1);
        } catch (IllegalStateException e){
            logger.warn("Session not available", e);
        }
    }
}
