package com.softito.projectmanagement.Services;

import com.softito.projectmanagement.Models.User;
import com.softito.projectmanagement.Repostitories.IRepositoryService;
import com.softito.projectmanagement.Repostitories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserRepositoryService implements IRepositoryService<User> {

    @Autowired
    private UserRepository userRepository;

    @Override
    public User add(User entity) {
        return userRepository.save(entity);
    }

    @Override
    public List<User> getAll() {
        return userRepository.findAll();
        //return userRepository.getAllUsers();
    }

    @Override
    public User getById(Long id) {
        return null;
    }

    @Override
    public boolean delete(Long id) {
        return false;
    }

    @Override
    public String update(Long id, User entity) {
        return null;
    }


}
