package com.softito.projectmanagement.Repostitories;

import com.softito.projectmanagement.Models.Project;
import com.softito.projectmanagement.Models.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProjectRepository extends JpaRepository<Project,Long> {
}
