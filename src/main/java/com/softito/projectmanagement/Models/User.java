package com.softito.projectmanagement.Models;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Getter
@Setter
@Entity
@Table(name = "Users")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long Id;

    @Column(name = "Username")
    private String Username;

    private String role;
    @Column(name = "email")
    private String Email;
    @Column(name = "password")
    private String Password;
    @Column(name = "Name")
    private String FName;
    @Column(name = "Surname")
    private String LName;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
    private List<Project> projects = new ArrayList<>();

    public User(String email, String fName, String lName, String password,String username) {
        this.Email = email;
        this.FName = fName;
        this.LName = lName;
        this.Password = password;
        this.Username = username;
    }
    public User(){

    }
}
