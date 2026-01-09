package com.example.demo.controller;

import com.example.demo.model.Category;
import com.example.demo.model.VideoGame;
import com.example.demo.repository.CategoryRepository;
import com.example.demo.repository.VideoGameRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/videogames")
public class VideoGameController {

    private final VideoGameRepository videoGameRepository;
    private final CategoryRepository categoryRepository;

    public VideoGameController(VideoGameRepository videoGameRepository, CategoryRepository categoryRepository) {
        this.videoGameRepository = videoGameRepository;
        this.categoryRepository = categoryRepository;
    }

    @GetMapping
    public List<VideoGame> getAll() {
        return videoGameRepository.findAll();
    }

    @GetMapping("/{id}")
    public ResponseEntity<VideoGame> getById(@PathVariable Long id) {
        return videoGameRepository.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<VideoGame> create(@RequestBody VideoGame vg) {
        // ensure categories exist or are created
        if (vg.getCategories() != null) {
            vg.getCategories().forEach(c -> {
                categoryRepository.findByNameIgnoreCase(c.getName())
                        .ifPresentOrElse(existing -> c.setId(existing.getId()), () -> {});
            });
        }
        VideoGame saved = videoGameRepository.save(vg);
        return ResponseEntity.created(URI.create("/api/videogames/" + saved.getId())).body(saved);
    }

    @PutMapping("/{id}")
    public ResponseEntity<VideoGame> update(@PathVariable Long id, @RequestBody VideoGame vg) {
        Optional<VideoGame> maybe = videoGameRepository.findById(id);
        if (maybe.isEmpty()) return ResponseEntity.notFound().build();
        VideoGame exist = maybe.get();
        exist.setName(vg.getName());
        exist.setDescription(vg.getDescription());
        exist.setReleaseDate(vg.getReleaseDate());
        exist.setImage(vg.getImage());
        exist.setCategories(vg.getCategories());
        VideoGame saved = videoGameRepository.save(exist);
        return ResponseEntity.ok(saved);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        if (!videoGameRepository.existsById(id)) return ResponseEntity.notFound().build();
        videoGameRepository.deleteById(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/search")
    public List<VideoGame> searchByName(@RequestParam(required = false) String name) {
        if (name == null || name.isBlank()) return videoGameRepository.findAll();
        return videoGameRepository.findByNameContainingIgnoreCase(name);
    }

    @GetMapping("/search/category")
    public List<VideoGame> searchByCategory(@RequestParam String name) {
        return videoGameRepository.findByCategoriesNameIgnoreCase(name);
    }
}
