package com.devsuperior.movieflix.services;


import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.devsuperior.movieflix.dto.MovieDetailsDTO;
import com.devsuperior.movieflix.entities.Genre;
import com.devsuperior.movieflix.entities.Movie;
import com.devsuperior.movieflix.entities.User;
import com.devsuperior.movieflix.repositories.GenreRepository;
import com.devsuperior.movieflix.repositories.MovieRepository;
import com.devsuperior.movieflix.services.exceptions.DatabaseException;
import com.devsuperior.movieflix.services.exceptions.ResourceNotFoundException;

import jakarta.persistence.EntityNotFoundException;

@Service
public class MovieService {
	
	@Autowired
	private MovieRepository repository;
	
	@Autowired
	private GenreRepository genreRepository;
	
	@Autowired
	public AuthService authService;
	
	@Transactional(readOnly = true)
	public Page<MovieDetailsDTO> findAllPaged(Pageable pageable) {
		Page<Movie>	page = repository.findAll(pageable);
		return page.map(x -> new MovieDetailsDTO(x));
		}
	
	@Transactional(readOnly = true)
	public MovieDetailsDTO findById(Long id){
		Optional<Movie> obj = repository.findById(id);
		Movie entity = obj.orElseThrow(() -> new ResourceNotFoundException("Entity not found"));
		return new MovieDetailsDTO(entity);
	}
	@Transactional(readOnly = true) 
	public Page<MovieDetailsDTO> findByGenre(Long id, String name, Pageable pageable) {
		Page<Movie> page = repository.findByGenre(id,  name, pageable);
		return page.map(x-> new MovieDetailsDTO(x));
	}
	
	@Transactional
	public MovieDetailsDTO insert (MovieDetailsDTO dto) {
		Movie entity = new Movie();
		Genre genre = genreRepository.getReferenceById(dto.getGenre().getId());
		User user =authService.authenticated();
		
		entity.setTitle(dto.getTitle());
		entity.setSubTitle(dto.getSubTitle());
		entity.setYear(dto.getYear());
		entity.setImgUrl(dto.getImgUrl());
		entity.setSynopsis(dto.getSynopsis());
		entity.setGenre(new Genre(dto.getId(), null));
		
		
		entity = repository.save(entity);
		return new MovieDetailsDTO(entity);
	
	}
	@Transactional
	public MovieDetailsDTO update(Long id, MovieDetailsDTO dto) {
		try {
			Movie entity = repository.getReferenceById(id);
			entity.setTitle(dto.getTitle());
			entity.setSubTitle(dto.getSubTitle());
			entity.setYear(dto.getYear());
			entity.setImgUrl(dto.getImgUrl());
			entity.setSynopsis(dto.getSynopsis());
			entity.setGenre(new Genre(dto.getId(), null));
			entity = repository.save(entity);
			return new MovieDetailsDTO(entity);	
		}
		catch (EntityNotFoundException e) {
			throw new ResourceNotFoundException("Id not found " + id);
		}
	}
	
	@Transactional
	public void delete(Long id) {
		if (!repository.existsById(id)) {
			throw new ResourceNotFoundException("Recurso não encontrado");
		}
		try {
			repository.deleteById(id);
		}
		catch (DataIntegrityViolationException e) {
			throw new DatabaseException("Falha de integridade referencial");
			
		}
	}

	
}
