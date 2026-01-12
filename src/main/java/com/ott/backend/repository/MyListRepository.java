package com.ott.backend.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.ott.backend.entity.MyList;

public interface MyListRepository extends JpaRepository<MyList, Long> {

    List<MyList> findByUserId(Long userId);

    Optional<MyList> findByUserIdAndMovieId(Long userId, Long movieId);
}
