package com.alex.movies.controller;

import java.util.List;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.alex.movies.model.Movie;
import com.alex.movies.model.ResponseMessage;
import com.alex.movies.service.MovieService;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;

@Api(value = "MovieController", description = "REST APIs related to Movie Entity!!!!")
@RestController
@RequestMapping(path = "/api/movies")
public class MovieController {

	@Autowired
	MovieService movieService;

	@ApiOperation(value = "Get all Movies in the System ", response = Iterable.class, tags = "getAllMovies")
	@ApiResponses(value = { @ApiResponse(code = 200, message = "Success|OK"),
			@ApiResponse(code = 401, message = "not authorized!"), @ApiResponse(code = 403, message = "forbidden!!!"),
			@ApiResponse(code = 404, message = "not found!!!") })
	@GetMapping(path = "/getAll")
	public ResponseEntity<List<Movie>> getAllMovies() {
		return new ResponseEntity<>(movieService.getAll(), HttpStatus.OK);
	}

	@ApiOperation(value = "Save a Movie in the System ", response = Iterable.class, tags = "saveMovie")
	@ApiResponses(value = { @ApiResponse(code = 200, message = "Success|OK"),
			@ApiResponse(code = 401, message = "not authorized!"), @ApiResponse(code = 403, message = "forbidden!!!"),
			@ApiResponse(code = 404, message = "not found!!!") })
	@PostMapping(path = "/save")
	public ResponseEntity<Movie> saveMovie(@RequestBody @Valid Movie movie) {
		boolean saved = movieService.save(movie);
		if (saved) {
			return new ResponseEntity<>(HttpStatus.CREATED);
		} else {
			return new ResponseEntity<>(HttpStatus.CONFLICT);
		}
	}

	@ApiOperation(value = "Edit a Movie", response = ResponseEntity.class, tags = "editMovie")
	@ApiResponses(value = { @ApiResponse(code = 200, message = "Success|OK"),
			@ApiResponse(code = 401, message = "not authorized!"), @ApiResponse(code = 403, message = "forbidden!!!"),
			@ApiResponse(code = 404, message = "not found!!!") })
	@PatchMapping(path = "/edit/{id}")
	public ResponseEntity<Movie> editMovie(@RequestBody Movie movie, @PathVariable String id) {
		boolean edited = movieService.edit(movie, id);
		if (edited) {
			return new ResponseEntity<>(HttpStatus.OK);
		} else {
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);
		}
	}

	@ApiOperation(value = "Delete a specific Movie ", response = Void.class, tags = "deleteMovie")
	@ApiResponses(value = { @ApiResponse(code = 200, message = "Success|OK"),
			@ApiResponse(code = 401, message = "not authorized!"), @ApiResponse(code = 403, message = "forbidden!!!"),
			@ApiResponse(code = 404, message = "not found!!!") })
	@DeleteMapping(path = "/delete/{id}")
	public ResponseEntity<Movie> deleteMovie(@PathVariable String id) {
		boolean deleted = movieService.delete(id);
		if (deleted) {
			return new ResponseEntity<>(HttpStatus.OK);
		} else {
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);
		}
	}

	@ApiOperation(value = "Upload File in the System ", response = Iterable.class, tags = "uploadFile")
	@ApiResponses(value = { @ApiResponse(code = 200, message = "Success|OK"),
			@ApiResponse(code = 401, message = "not authorized!"), @ApiResponse(code = 403, message = "forbidden!!!"),
			@ApiResponse(code = 404, message = "not found!!!") })
	@PostMapping("/upload")
	public ResponseEntity<ResponseMessage> uploadFile(MultipartFile file) {
		String message = "";
		try {
			movieService.store(file);

			message = "Uploaded the file successfully: " + file.getOriginalFilename();
			return ResponseEntity.status(HttpStatus.OK).body(new ResponseMessage(message));
		} catch (Exception e) {
			message = "Could not upload the file: " + file.getOriginalFilename() + "!";
			return ResponseEntity.status(HttpStatus.EXPECTATION_FAILED).body(new ResponseMessage(message));
		}
	}

	@ApiOperation(value = "Get list of Movies for a specific Actor ", response = Iterable.class, tags = "findMoviesByActor")
	@ApiResponses(value = { @ApiResponse(code = 200, message = "Success|OK"),
			@ApiResponse(code = 401, message = "not authorized!"), @ApiResponse(code = 403, message = "forbidden!!!"),
			@ApiResponse(code = 404, message = "not found!!!") })
	@GetMapping(path = "/findByActor/{actor}")
	public ResponseEntity<List<Movie>> findMoviesByActor(@PathVariable String actor) {
		return new ResponseEntity<>(movieService.findMoviesByActor(actor), HttpStatus.OK);
	}


}
