package com.envite.model;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import javax.persistence.*;
import java.util.Date;

/**
 * Created by Hareesh on 1/7/2017.
 */
@Data
@EqualsAndHashCode(of={"id"})
@Entity
@Table(name="e_user_role")
public class UserRole {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @ManyToOne
    @JoinColumn(name="user_id")
    private User user;

    @Column(name="type")
    private String type;

    @Column(name="created_by")
    private Long createdUserId;

    @Column(name = "created_date")
    private Date createdDate;

    @Column(name = "modified_by")
    private Long modifiedUserId;

    @Column(name = "modified_date")
    private Date modifiedDate;

    @Column(name = "status")
    private Integer status;

}
