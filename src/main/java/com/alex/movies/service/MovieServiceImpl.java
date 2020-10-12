package com.alex.movies.service;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.time.LocalTime;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.alex.movies.model.Movie;
import com.alex.movies.repository.MovieRepository;
import com.opencsv.CSVReader;

@Service
public class MovieServiceImpl implements MovieService {

	@Autowired
	MovieRepository repo;

	@Override
	public List<Movie> getAll() {
		return (List<Movie>) repo.findAll();
	}

	@Override
	public boolean save(Movie movie) {
		Optional<Movie> movieDB = repo.findByTitleAndDirector(movie.getTitle(), movie.getDirector());
		if (movieDB.isPresent()) {
			return false;
		}
		repo.save(movie);
		return true;
	}

	@Override
	public boolean delete(String id) {
		if (repo.findById(id).isEmpty()) {
			return false;
		}
		repo.deleteById(id);
		return true;

	}

	@Override
	public boolean edit(Movie m, String id) {
		Optional<Movie> movie = repo.findById(id);
		if (movie.isEmpty()) {
			return false;
		}
		movie.get().updateMovie(m);
		repo.save(movie.get());
		return true;
	}

	@Override
	public void store(MultipartFile info) throws IOException {
		try (CSVReader csvReader = new CSVReader(
				(Reader) (new InputStreamReader(new ByteArrayInputStream(info.getBytes()))))) {
			String[] values = null;
			int i = 0;
			while ((values = csvReader.readNext()) != null) {
				if (i == 0) {
					i++;
					continue;
				}
				Movie movie = Movie.builder()
						.title(values[0])
						.director(values[1])
						.genre(values[2])
						.rating(values[3])
						.cast(values[4])
						.duration(LocalTime.parse(values[5]))
						.build();
				repo.save(movie);

			}

		}
	}

	public List<Movie> findMoviesByActor(String actor) {
		List<Movie> list = (List<Movie>) repo.findAll();
		return list.stream().filter(
				m -> (Arrays.asList(m.getCast().split(",")).stream().map(q -> q.trim()).collect(Collectors.toList()))
						.contains(actor))
				.collect(Collectors.toList());
	}

}
