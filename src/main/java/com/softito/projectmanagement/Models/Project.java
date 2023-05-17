package com.softito.projectmanagement.Models;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;

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

    @ManyToMany
    @JoinTable(name = "project_user",
            joinColumns = @JoinColumn(name = "project_id"),
            inverseJoinColumns = @JoinColumn(name = "user_id"))
    private List<User> users;


    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "project_id")
    private List<Task> tasks = new ArrayList<>();

    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "project_id")
    private List<Risk> risks = new ArrayList<>();

    public void addTask(Task task) {
        tasks.add(task);
    }

    public void removeTask(Task task) {
        tasks.remove(task);
    }

    public int getRemainingDaysForTask(Task task) {
        LocalDate startDate = task.getStartDate();
        LocalDate endDate = startDate.plusDays(task.getDurationDays());
        LocalDate currentDate = LocalDate.now();
        return Math.max(0, (int) ChronoUnit.DAYS.between(currentDate, endDate));
    }


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
