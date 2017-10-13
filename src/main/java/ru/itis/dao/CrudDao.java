package ru.itis.dao;

import java.util.List;


public interface CrudDao<M , I> {
    List<M> findAll();
    void save(M model);
    M find(I id);
    void delete(I id);
    void update(M model, I id);
}
