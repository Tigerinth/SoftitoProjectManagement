package com.softito.projectmanagement.Models;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.time.LocalDate;
import java.util.Date;

@Entity
@Getter
@Setter
@Table(name = "Tasks")
public class Task {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "taskname")
    private String taskName;

    @Column(name = "taskdescription")
    private String taskDescription;

    @Column(name = "durationdays")
    private int durationDays;

    @Column(name = "start_date")
    private LocalDate startDate;

    @Column(name = "isCompleted")
    private boolean isCompleted;

    @Column(name = "completeduser")
    private String completedUser;

    // Diğer özellikler ve ilişkiler

    public Task(String taskName, String taskDescription,int durationDays,boolean iscompleted) {
        this.taskName = taskName;
        this.taskDescription = taskDescription;
        this.durationDays = durationDays;
        this.isCompleted = iscompleted;
        this.startDate = LocalDate.now();
        this.completedUser = "";
    }

    public Task() {
    }
}
