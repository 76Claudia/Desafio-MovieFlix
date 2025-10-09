package com.devsuperior.movieflix.services;


import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.devsuperior.movieflix.dto.MovieCardDTO;
import com.devsuperior.movieflix.entities.Movie;
import com.devsuperior.movieflix.repositories.MovieRepository;
import com.devsuperior.movieflix.services.exceptions.ResourceNotFoundException;

@Service
public class MovieService {
	
	@Autowired
	private MovieRepository repository;
	
	@Transactional(readOnly = true)
	public List<MovieCardDTO> findAll(){
		List<Movie> list = repository.findAll();
		return list.stream().map(x -> new MovieCardDTO(x)).toList();	
	}
	
	@Transactional(readOnly = true)
	public MovieCardDTO findById(Long id){
		Optional<Movie> obj = repository.findById(id);
		Movie entity = obj.orElseThrow(() -> new ResourceNotFoundException("Entity not found"));
		return new MovieCardDTO(entity);
	}
	
	@Transactional
	public MovieCardDTO insert (MovieCardDTO dto) {
		Movie entity = new Movie();
		entity.setTitle(dto.getTitle());
		entity.setYear(dto.getYear());
		entity.setImgUrl(dto.getImgUrl());
		entity = repository.save(entity);
		return new MovieCardDTO(entity);
	
	}

}
