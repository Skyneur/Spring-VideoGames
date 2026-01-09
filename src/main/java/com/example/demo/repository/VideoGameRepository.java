package com.example.demo.repository;

import com.example.demo.model.VideoGame;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface VideoGameRepository extends JpaRepository<VideoGame, Long> {
    List<VideoGame> findByNameContainingIgnoreCase(String name);
    List<VideoGame> findByCategoriesNameIgnoreCase(String categoryName);
}
