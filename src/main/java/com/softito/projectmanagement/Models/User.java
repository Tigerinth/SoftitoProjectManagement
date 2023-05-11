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
    public Long Id;

    @Column(name = "Username")
    public String Username;

    public String role;
    @Column(name = "email")
    public String Email;
    @Column(name = "password")
    public String Password;
    @Column(name = "Name")
    public String FName;
    @Column(name = "Surname")
    public String LName;

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
