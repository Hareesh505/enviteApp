package com.envite.security;

import com.envite.model.User;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * Created by banda on 4/26/2017.
 */
@Data
public class UserDO implements Serializable {
    public UserDO(User user) {
        this.userId = user.getId();
        this.email = user.getEmail();
        this.title = user.getTitle();
        this.firstName = user.getFirstName();
        this.lastName = user.getLastName();
        this.userName = user.getUserName();
        this.createdDate = user.getCreatedDate();
        this.status = user.getStatus();
        this.modifiedDate = user.getModifiedDate();
        this.phoneNumber = user.getPhoneNumber();
        this.fullName = user.getFirstName();
        if(user.getLastName() != null) {
            this.fullName += user.getLastName();
        }
    }
    private Long userId;

    private String firstName;

    private String lastName;

    private String email;

    private String userName;

    private Date createdDate;

    private Date modifiedDate;

    private String phoneNumber;

    private String title;

    private Boolean status;

    private String currentUserRole;

    private String fullName;
}
