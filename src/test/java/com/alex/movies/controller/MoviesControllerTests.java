package com.alex.movies.controller;

import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.json.AutoConfigureJsonTesters;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;

import com.alex.movies.model.Movie;
import com.alex.movies.repository.MovieRepository;
import com.alex.movies.service.MovieService;

@ExtendWith(SpringExtension.class)
@AutoConfigureJsonTesters
@WebMvcTest(MovieController.class)
public class MoviesControllerTests {

	@Autowired
	private MockMvc mockMvc;
	@MockBean
	private MovieService service;
	@MockBean
	private MovieRepository repo;

	private List<Movie> movies = new ArrayList<>();

	private void setup() {
		Movie movie1 = Movie.builder().title("Triunghiul mortii").director("Sergiu Nicolaescu")
				.genre("Istoric, Actiune").rating("8.5").cast("Sergiu Nicolaescu").duration(LocalTime.parse("02:40:00"))
				.build();
		movies.add(movie1);
		Movie movie2 = Movie.builder().title("Ringul").director("Sergiu Nicolaescu").genre("Istoric, Actiune")
				.rating("7.5").cast("Sergiu Nicolaescu").duration(LocalTime.parse("02:40:00")).build();
		movies.add(movie2);
	}

	@Test
	public void getAllMovies() throws Exception {
		setup();
		when(service.getAll()).thenReturn(movies);
		mockMvc.perform(get("http://localhost:8080/api/movies/getAll").accept(MediaType.APPLICATION_JSON))
				.andExpect(status().isOk()).andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
				.andExpect(jsonPath("$[*]", hasSize(2))).andExpect(jsonPath("$[1].title", is("Ringul")))
				.andExpect(jsonPath("$[1].director", is("Sergiu Nicolaescu")))
				.andExpect(jsonPath("$[1].genre", is("Istoric, Actiune"))).andExpect(jsonPath("$[1].rating", is("7.5")))
				.andExpect(jsonPath("$[1].cast", is("Sergiu Nicolaescu")))
				.andExpect(jsonPath("$[1].duration", is("02:40:00")));
	}

	@Test
	public void getMoviesByActor() throws Exception {
		setup();
		when(service.findMoviesByActor("Sergiu Nicolaescu")).thenReturn(movies);
		mockMvc.perform(get("http://localhost:8080/api/movies//findByActor/Sergiu Nicolaescu")
				.accept(MediaType.APPLICATION_JSON)).andExpect(status().isOk())
				.andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
				.andExpect(jsonPath("$[*]", hasSize(2))).andExpect(jsonPath("$[0].title", is("Triunghiul mortii")))
				.andExpect(jsonPath("$[0].director", is("Sergiu Nicolaescu")))
				.andExpect(jsonPath("$[0].genre", is("Istoric, Actiune"))).andExpect(jsonPath("$[0].rating", is("8.5")))
				.andExpect(jsonPath("$[0].cast", is("Sergiu Nicolaescu")))
				.andExpect(jsonPath("$[0].duration", is("02:40:00")));
	}

}
