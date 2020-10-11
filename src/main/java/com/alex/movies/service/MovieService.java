package com.alex.movies.service;

import java.io.IOException;
import java.util.List;

import org.springframework.web.multipart.MultipartFile;

import com.alex.movies.model.Movie;

public interface MovieService {

	public List<Movie> getAll();

	public boolean save(Movie movie);

	public boolean delete(String id);

	public boolean edit(Movie movie, String id);

	public void store(MultipartFile info) throws IOException;

	public List<Movie> findMoviesByActor(String actor);

}
