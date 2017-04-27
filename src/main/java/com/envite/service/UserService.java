package com.envite.service;

import com.envite.model.User;
import com.envite.security.UserDO;

import java.util.Map;

/**
 * Created by Admin on 1/7/2017.
 */
public interface UserService {

    User findByUserName(String userName);

    Map<String, Object> registerUser(String userName, String password, String fullName);

    UserDO getCurrentUserDO();
}
