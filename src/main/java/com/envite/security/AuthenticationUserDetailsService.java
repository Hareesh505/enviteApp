package com.envite.security;

import com.envite.model.User;
import com.envite.model.UserRole;
import com.envite.repositories.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.StandardPasswordEncoder;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;

import javax.persistence.NonUniqueResultException;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

/**
 * Created by Admin on 1/9/2017.
 */
public class AuthenticationUserDetailsService implements UserDetailsService {
    final Logger logger = LoggerFactory.getLogger(AuthenticationUserDetailsService.class);

    @Autowired
    private UserRepository userRepository;

    /*@Autowired
    StandardPasswordEncoder encoder;*/

    @SuppressWarnings("deprecation")
    @Override
    public UserDetails loadUserByUsername(String userName) throws UsernameNotFoundException {
        Long districtId = null;
        try{
            String proxyPassword = null;
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            if(authentication!=null){
                Object userWebAuthenticationDetails = authentication.getDetails();
                if (userWebAuthenticationDetails != null && userWebAuthenticationDetails instanceof UserWebAuthenticationDetails){
                    proxyPassword = ((UserWebAuthenticationDetails)userWebAuthenticationDetails).getProxyPassword();
                }
            }
            User user = null;
            /*RequestAttributes attr = RequestContextHolder.currentRequestAttributes();
            if(attr != null){
                districtId = (Long) attr.getAttribute("districtId",1);
            }*/

            try {
                user = userRepository.findUserByUserName(userName);
            } catch (Exception e) {
                if(e.getCause() != null && e.getCause() instanceof NonUniqueResultException){
                    throw new UsernameNotFoundException("Multiple users found with name "+userName,e.getCause());
                }
            }


            if (user == null) {
                throw new UsernameNotFoundException("User with name "+userName+" not found. ");
            } else if (user.getStatus() != null && Boolean.FALSE.equals(user.getStatus())){
                throw new UsernameNotFoundException("User with name "+userName+" is deactivated, please contact support team. ");
            }

            String password = user.getPassword();
            if(proxyPassword != null){
                password = proxyPassword;//encoder.encode(proxyPassword);
            }

            LoggedInUserDetails userDetails = new LoggedInUserDetails(user.getUserName(), password, getAuthorities(user.getUserRole()));
            UserDO userDO = new UserDO(user);
            userDetails.setUserDO(userDO);
            return userDetails;
        }catch(Exception ex){
          throw new UsernameNotFoundException(ex.getMessage());
        }
    }

    public List<GrantedAuthority> getAuthorities(Set<UserRole> userRoles) {
        List<GrantedAuthority> authList = new ArrayList<GrantedAuthority>();
        if(userRoles!=null && !userRoles.isEmpty()){
            for(UserRole userRole: userRoles){
                authList.add(new SimpleGrantedAuthority(userRole.getType()));
            }
        }
        return authList;
    }
}
