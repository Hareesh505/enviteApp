package com.envite.security;

import org.springframework.security.authentication.AuthenticationDetailsSource;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;

import javax.servlet.http.HttpServletRequest;

/**
 * Created by Admin on 1/9/2017.
 */
public class UserWebAuthenticationDetailsSource implements AuthenticationDetailsSource<HttpServletRequest, UserWebAuthenticationDetails> {
    @Override
    public UserWebAuthenticationDetails buildDetails(HttpServletRequest context) {
        UserWebAuthenticationDetails userWebAuthenticationDetails= new UserWebAuthenticationDetails(context);

        UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(new Object(), new Object());
        authentication.setDetails(userWebAuthenticationDetails);
        SecurityContextHolder.getContext().setAuthentication(authentication);

        return userWebAuthenticationDetails;
    }
}
