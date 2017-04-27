package com.envite.repositories;

import com.envite.model.User;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

import java.io.Serializable;

/**
 * Created by Admin on 1/7/2017.
 */
@Repository
public interface  UserRepository extends PagingAndSortingRepository<User,Serializable> {
    User findUserByUserName(String userName);
}
