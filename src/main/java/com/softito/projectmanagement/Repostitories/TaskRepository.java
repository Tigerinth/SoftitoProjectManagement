package com.softito.projectmanagement.Repostitories;

import com.softito.projectmanagement.Models.Task;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TaskRepository extends JpaRepository<Task,Long> {
}
