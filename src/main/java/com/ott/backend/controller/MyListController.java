package com.ott.backend.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import com.ott.backend.dto.MyListRequest;
import com.ott.backend.entity.MyList;
import com.ott.backend.entity.User;
import com.ott.backend.repository.MyListRepository;
import com.ott.backend.repository.UserRepository;
import com.ott.backend.security.JwtUtil;

import jakarta.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("/api/my-list")
public class MyListController {

    @Autowired
    private MyListRepository myListRepository;

    @Autowired
    private UserRepository userRepository;

    @PostMapping
    public MyList addToMyList(
            @RequestBody MyListRequest request,
            HttpServletRequest httpRequest
    ) {

        System.out.println("➡️ ADD TO MY LIST CALLED");

        String authHeader = httpRequest.getHeader("Authorization");
        System.out.println("Auth Header: " + authHeader);

        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            throw new RuntimeException("Missing Authorization header");
        }

        String token = authHeader.substring(7);
        String email = JwtUtil.extractEmail(token);
        System.out.println("Extracted Email: " + email);

        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found for email: " + email));

        System.out.println("User ID: " + user.getId());

        if (request.getMovieId() == null) {
            throw new RuntimeException("movieId is NULL");
        }

        System.out.println("Movie ID: " + request.getMovieId());

        return myListRepository
                .findByUserIdAndMovieId(user.getId(), request.getMovieId())
                .orElseGet(() -> {
                    MyList myList = new MyList();
                    myList.setUserId(user.getId());
                    myList.setMovieId(request.getMovieId());
                    return myListRepository.save(myList);
                });
    }

    @GetMapping
    public List<MyList> getMyList(HttpServletRequest request) {

        String authHeader = request.getHeader("Authorization");
        String token = authHeader.substring(7);
        String email = JwtUtil.extractEmail(token);

        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));

        return myListRepository.findByUserId(user.getId());
    }
}
