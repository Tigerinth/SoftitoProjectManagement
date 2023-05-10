package com.softito.projectmanagement.Repostitories;

import java.util.List;

public interface IRepositoryService<T> {

    T add(T entity);
    List<T> getAll();
    T getById(Long id);
    boolean delete(Long id);
    String update(Long id,T entity);
}
