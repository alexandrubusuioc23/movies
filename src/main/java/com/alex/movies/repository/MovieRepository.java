package com.alex.movies.repository;

import java.util.Optional;

import org.springframework.data.mongodb.repository.MongoRepository;

import com.alex.movies.model.Movie;

public interface MovieRepository extends MongoRepository<Movie, String> {

	Optional<Movie> findByTitleAndDirector(String title, String director);

}
