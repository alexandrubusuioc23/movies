package com.alex.movies.repository;

import java.util.Optional;

import org.springframework.data.mongodb.repository.MongoRepository;

import com.alex.movies.model.Person;

public interface UserRepository extends MongoRepository<Person,String> {

	Optional<Person> findByUsername(String username);

	Optional<Person> findByFirstNameAndLastNameAndUsername(String firstName, String lastName, String username);

}
