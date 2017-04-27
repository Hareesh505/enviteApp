package com.envite.controller;

import com.envite.model.User;
import com.envite.security.LoggedInUserDetails;
import com.envite.security.UserDO;
import com.envite.security.UserWebAuthenticationDetails;
import com.envite.service.UserService;
import com.envite.utils.EVUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.RememberMeServices;
import org.springframework.security.web.authentication.rememberme.TokenBasedRememberMeServices;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.springframework.web.context.support.WebApplicationContextUtils;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by banda on 4/26/2017.
 */
@Controller
public class LoginController {
    final Logger logger = LoggerFactory.getLogger(LoginController.class);

    @Autowired
    @Qualifier("authenticationManager")
    AuthenticationManager authenticationManager;

    @Autowired
    UserService userService;

    @RequestMapping(method = RequestMethod.POST, value = "/ev/login")
    @ResponseBody
    public String login(@RequestParam("j_username") String username,
                        @RequestParam("j_password") String password,
                        HttpServletRequest request, HttpServletResponse response, HttpSession session,
                        @RequestParam(value = "_spring_security_remember_me", required = false) Boolean isRememberMe) {
        Map<String, Object> model = new HashMap();
        session.invalidate();
        session = request.getSession(true);
        return authenticateAndLoadUser(username, password, request, response, isRememberMe, model);
    }

    private String authenticateAndLoadUser(String username,String password, HttpServletRequest request, HttpServletResponse response, Boolean isRememberMe, Map<String, Object> model) {
        UserWebAuthenticationDetails userWebAuthenticationDetails = new UserWebAuthenticationDetails(request);

        if (username != null && !"".equals(username) && password != null && !"".equals(password)) {
            UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(username, password);
            authentication.setDetails(userWebAuthenticationDetails);
            SecurityContextHolder.getContext().setAuthentication(authentication);
            try{
                ServletRequestAttributes attr = (ServletRequestAttributes) RequestContextHolder.currentRequestAttributes();
                Authentication auth = authenticationManager.authenticate(authentication);

                //get userDetails from userDetailsService and set to authToken
                UserDetailsService userDetailsService = (UserDetailsService) WebApplicationContextUtils.getWebApplicationContext(request.getSession().getServletContext()).getBean("authenticationUserDetailsService");

                UserDetails userDetails = userDetailsService.loadUserByUsername(username);
                UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(userDetails, password, userDetails.getAuthorities());
                SecurityContextHolder.getContext().setAuthentication(authToken);

                if (isRememberMe != null && isRememberMe) {
                    //  Register the authentication event - explicitly. This happens using filter for user regular login.
                    //  SecurityContextHolder.getContext().setAuthentication(authToken);
                    //	SWConcurrentSessionControlStrategy concurrentSessionControlStrategy = (SWConcurrentSessionControlStrategy) WebApplicationContextUtils.getWebApplicationContext(request.getSession().getServletContext()).getBean("sas");
                    //	concurrentSessionControlStrategy.onAuthentication(authToken, request, response);
                    //	Invoke remember me service to generate cookie.
                    RememberMeServices rememberMeServices = (RememberMeServices) WebApplicationContextUtils.getWebApplicationContext(request.getSession().getServletContext()).getBean("rememberMeServiceAlias");
                    rememberMeServices.loginSuccess(request, response, authToken);
                }
                UserDO userDO = userService.getCurrentUserDO();
                model.put("userJSON", userDO);
            } catch (Exception ex){
                logger.error("exception while authenticate " , ex);
                model.put("error",ex.getMessage());
            }
        } else {
            model.put("error","please provide valid data");
        }
        return EVUtils.convertObjectTOJSONString(model);
    }

    @RequestMapping(value = "/ev/registerUser" , method = RequestMethod.POST)
    @ResponseBody
    public String registerUser(@RequestParam("userName") String userName,
                               @RequestParam("password") String password,
                               @RequestParam("fullName") String fullName,
                               HttpServletRequest request, HttpServletResponse response, HttpSession session) {
        Map<String, Object> model = new HashMap();
        model = userService.registerUser(userName,password,fullName);
        User user = null;
        if(model.get("error") == null){
            return authenticateAndLoadUser(userName,password,request,response,null,model);
        } else {
            return EVUtils.convertObjectTOJSONString(model);
        }
    }

    /**
     * This checks if there is an already authenticated user. if so returns the userJSON.
     * Otherwise returns empty json.
     *
     * @return
     */
    @RequestMapping(value = "/ev/isAuthenticated", produces = "application/json; charset=UTF-8")
    @ResponseBody
    public String isUserAuthenticated(HttpServletRequest request, HttpServletResponse response) {
        HashMap<String,Object> responseJSON = new HashMap();
        UserDO user = userService.getCurrentUserDO();
        if (SecurityContextHolder.getContext() != null
                && SecurityContextHolder.getContext().getAuthentication() != null) {
            if (SecurityContextHolder.getContext().getAuthentication().getPrincipal() instanceof LoggedInUserDetails) {
                if (SecurityContextHolder.getContext().getAuthentication().isAuthenticated()) {
                    responseJSON.put("userJSON",userService.getCurrentUserDO());
                }
            }
        }
        return EVUtils.convertObjectTOJSONString(responseJSON);
    }

    @RequestMapping("/logout")
    public String logout(HttpServletRequest request, HttpServletResponse response,ModelMap model){

        SecurityContextHolder.clearContext();
        Cookie terminate = new Cookie(TokenBasedRememberMeServices.SPRING_SECURITY_REMEMBER_ME_COOKIE_KEY, null);
        terminate.setMaxAge(0);
        terminate.setPath("/");
        response.addCookie(terminate);

        HttpSession session = request.getSession(false);
        if (session != null) {
            session.invalidate();
        }
        Cookie jsessionterminate = new Cookie("JSESSIONID",null);
        jsessionterminate.setMaxAge(0);
        jsessionterminate.setPath("/");
        response.addCookie(jsessionterminate);

        return "redirect:"+"/login";
    }
}
