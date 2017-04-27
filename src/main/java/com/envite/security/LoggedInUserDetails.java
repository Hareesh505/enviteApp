package com.envite.security;

import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.User;

import java.util.Collection;

/**
 * Created by banda on 4/26/2017.
 */
@Data
@EqualsAndHashCode(callSuper=false)
public class LoggedInUserDetails extends User {

    UserDO userDO;

    public LoggedInUserDetails(String userName, String password , Collection<? extends GrantedAuthority> authorities) {
        super(userName, password, authorities);
    }
}
