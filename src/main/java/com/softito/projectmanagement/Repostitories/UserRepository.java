package com.softito.projectmanagement.Repostitories;

import com.softito.projectmanagement.Models.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface UserRepository extends JpaRepository<User,Long> {

    //@Query("SELECT u FROM User u WHERE u.isDelete = false")
    //public List<User> getAllUsers();

    //@Query("SELECT u FROM User u WHERE u.email = :email")
    //public User findByEmail(@Param("email") String email);
}
