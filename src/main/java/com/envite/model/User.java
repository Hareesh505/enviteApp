package com.envite.model;

import lombok.Data;
import lombok.ToString;

import javax.persistence.*;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

/**
 * Created by Hareesh on 1/7/2017.
 */
@Data
@Entity
@ToString(exclude={"userRole"})
@Table(name="e_user")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(name="FIRSTNAME")
    private String firstName;

    @Column(name="LASTNAME")
    private String lastName;

    @Column(name="EMAIL")
    private String email;

    @Column(name="USERNAME")
    private String userName;

    @Column(name="password")
    private String password;

    @Column(name="created_date")
    private Date createdDate;

    @Column(name="modified_date")
    private Date modifiedDate;

    @Column(name="TELEPHONE")
    private String phoneNumber;

    @Column(name="title")
    private String title;

    @Column(name="status")
    private Boolean status;


    @OneToMany(mappedBy="user", fetch = FetchType.EAGER, cascade = {CascadeType.ALL}, orphanRemoval=true)
    private Set<UserRole> userRole;

    public void addUserRole(UserRole userRole){
        if(getUserRole() == null){
            setUserRole(new HashSet<UserRole>());
        }
        getUserRole().add(userRole);
    }
}
