package com.softito.projectmanagement.Models;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Getter
@Setter
@Table(name = "Projects")
public class Project {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    public Long Id;

    @Column(name = "Project_Name")
    public String ProjectName;
    @Column(name = "Description")
    public String ProjectDesc;
    @Column(name = "Project_ManagerMail")
    public String managerMail;
    @Column(name = "Project_Status")
    public boolean ProjectStatus;


    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    public Project(String name,String projectDesc, String managermail,boolean projectStatus){
        this.ProjectName = name;
        this.ProjectDesc = projectDesc;
        this.managerMail = managermail;
        this.ProjectStatus = projectStatus;
    }


}
