package com.alex.movies.security;

import java.util.List;

import com.alex.movies.model.Person;

public interface UserService {

    public boolean save(Person user);
    public List<Person> findAll();
    public boolean delete(String id);
    public Person findOne(String username);
    public Person findById(String id);
    public boolean update(Person user,String id);
}
