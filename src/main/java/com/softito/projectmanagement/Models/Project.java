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
    private Long Id;

    @Column(name = "Project_Name")
    private String ProjectName;
    @Column(name = "Description")
    private String ProjectDesc;
    @Column(name = "Project_ManagerMail")
    private String managerMail;
    @Column(name = "Project_Status")
    private boolean ProjectStatus;

    @Column(name = "inviteid")
    private String inviteid;


    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    public Project(String name,String projectDesc, String managermail,String inviteid,boolean projectStatus){
        this.ProjectName = name;
        this.ProjectDesc = projectDesc;
        this.managerMail = managermail;
        this.ProjectStatus = projectStatus;
        this.inviteid =inviteid;
    }
    public Project() {
    }


}
