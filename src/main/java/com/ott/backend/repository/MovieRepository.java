package com.ott.backend.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.ott.backend.entity.Movie;

@Repository
public interface MovieRepository extends JpaRepository<Movie, Long> {
}
