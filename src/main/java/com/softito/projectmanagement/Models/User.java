package com.softito.projectmanagement.Models;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Getter
@Setter
@Entity
@Table(name = "Users")
public class User {
    @Id
    public Long Id;

    public String Email;

    public String Password;
    public String FName;
    public String LName;


    public User(String email, String fName, String lName, String password) {
        this.Email = email;
        this.FName = fName;
        this.LName = lName;
        this.Password = password;
    }
}
