package com.envite.security;

import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springframework.security.web.authentication.WebAuthenticationDetails;

import javax.servlet.http.HttpServletRequest;

/**
 * Created by Admin on 1/9/2017.
 */
@Data
@EqualsAndHashCode(callSuper=false)
public class UserWebAuthenticationDetails extends WebAuthenticationDetails {

    private static final long serialVersionUID = 3337693725322469090L;

    //private Long customerId;

    private String proxyPassword;

    //private Long institutionId;

    public UserWebAuthenticationDetails(HttpServletRequest context) {
        super(context);
    }
}

