package com.softito.projectmanagement.Services;

import com.softito.projectmanagement.Models.Project;
import com.softito.projectmanagement.Models.User;
import com.softito.projectmanagement.Repostitories.IRepositoryService;
import com.softito.projectmanagement.Repostitories.ProjectRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ProjectRepositoryService implements IRepositoryService<Project> {
    @Autowired
    ProjectRepository projectRepository;
    @Override
    public Project add(Project entity) {
        return null;
    }

    @Override
    public List<Project> getAll() {
        return null;
    }

    @Override
    public Project getById(Long id) {
        return null;
    }

    @Override
    public boolean delete(Long id) {
        return false;
    }

    @Override
    public String update(Long id, Project entity) {
        return null;
    }
    public Project findByEmail(String invitedid) {
        return projectRepository.findByinvitedid(invitedid);
    }
}
