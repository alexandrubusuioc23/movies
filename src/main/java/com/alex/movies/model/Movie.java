package com.alex.movies.model;

import java.time.LocalTime;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.PastOrPresent;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import lombok.Data;

@Data
@Document
public class Movie {

	@Id	
	private String id;
	@NotEmpty
	private String title;
	@NotEmpty
	private String director;
	@NotEmpty
	private String genre;
	@NotEmpty
	private String rating;
	@NotEmpty
	private String cast;
	@PastOrPresent
	private LocalTime duration;

	public String findDetails() {
		return "=" + title + "=" + director + "=" + genre + "=" + rating + "=" + cast + "=" + duration;
	}

	public void updateMovie(Movie movie) {
		if (movie.getCast() != null && !movie.getCast().isEmpty())
			this.setCast(movie.getCast());
		if (movie.getDirector() != null && !movie.getDirector().isEmpty())
			this.setDirector(movie.getDirector());
		if (movie.getDuration() != null && movie.getDuration().compareTo(LocalTime.parse("00:00")) != 1)
			this.setDuration(movie.getDuration());
		if (movie.getGenre() != null && !movie.getGenre().isEmpty())
			this.setGenre(movie.getGenre());
		if (movie.getRating() != null && !movie.getRating().isEmpty())
			this.setRating(movie.getRating());
		if (movie.getTitle() != null && !movie.getTitle().isEmpty())
			this.setTitle(movie.getTitle());
	}
}
