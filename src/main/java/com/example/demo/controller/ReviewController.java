package com.example.demo.controller;

import com.example.demo.model.Review;
import com.example.demo.model.VideoGame;
import com.example.demo.repository.ReviewRepository;
import com.example.demo.repository.VideoGameRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/reviews")
public class ReviewController {

    private final ReviewRepository reviewRepository;
    private final VideoGameRepository videoGameRepository;

    public ReviewController(ReviewRepository reviewRepository, VideoGameRepository videoGameRepository) {
        this.reviewRepository = reviewRepository;
        this.videoGameRepository = videoGameRepository;
    }

    @GetMapping
    public List<Review> all() { return reviewRepository.findAll(); }

    @GetMapping("/{id}")
    public ResponseEntity<Review> get(@PathVariable Long id) {
        return reviewRepository.findById(id).map(ResponseEntity::ok).orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<?> create(@RequestBody Review r, @RequestParam Long videoGameId) {
        Optional<VideoGame> vg = videoGameRepository.findById(videoGameId);
        if (vg.isEmpty()) return ResponseEntity.badRequest().body("Video game not found");
        r.setVideoGame(vg.get());
        r.setDate(LocalDateTime.now());
        Review saved = reviewRepository.save(r);
        return ResponseEntity.created(URI.create("/api/reviews/" + saved.getId())).body(saved);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Review> update(@PathVariable Long id, @RequestBody Review r) {
        Optional<Review> maybe = reviewRepository.findById(id);
        if (maybe.isEmpty()) return ResponseEntity.notFound().build();
        Review exist = maybe.get();
        exist.setAuthorName(r.getAuthorName());
        exist.setRating(r.getRating());
        exist.setComment(r.getComment());
        Review saved = reviewRepository.save(exist);
        return ResponseEntity.ok(saved);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        if (!reviewRepository.existsById(id)) return ResponseEntity.notFound().build();
        reviewRepository.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}
