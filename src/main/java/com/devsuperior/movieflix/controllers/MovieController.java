package com.devsuperior.movieflix.controllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.devsuperior.movieflix.dto.MovieCardDTO;
import com.devsuperior.movieflix.services.MovieService;

@RestController
@RequestMapping(value = "/movies")
public class MovieController {

	@Autowired
	private MovieService service;
	
	@GetMapping
	public ResponseEntity<List<MovieCardDTO>> findAll(){
		List<MovieCardDTO> list = service.findAll();
		return ResponseEntity.ok().body(list);
		
	}
	
	public ResponseEntity<MovieCardDTO> findById(@PathVariable Long id){
		MovieCardDTO dto = service.findById(id);
		return ResponseEntity.ok().body(dto);
	}
}
