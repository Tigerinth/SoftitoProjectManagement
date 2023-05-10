package com.softito.projectmanagement.Models;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Getter
@Setter
@Table(name = "Projects")
public class Project {

    @Id
    public Long Id;

    public String ProjectName;
    public String ProjectDesc;

}
