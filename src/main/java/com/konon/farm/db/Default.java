package com.konon.farm.db;

import java.util.List;

public interface Default<T> {

    List<T> getAll();

    void update(T entity);

    void delete(Integer id);

    void insert(T entity);
}
