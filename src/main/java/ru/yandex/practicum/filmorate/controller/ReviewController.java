package ru.yandex.practicum.filmorate.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.model.Review;
import ru.yandex.practicum.filmorate.service.ReviewService;

import java.util.List;
import java.util.Map;

@RequiredArgsConstructor
@RestController
@RequestMapping("/reviews")
public class ReviewController {
    private static final String ID_ENDPOINT_PATH = "/{id}";
    private static final String LIKE_ENDPOINT_PATH = "/{id}/like/{user-id}";
    private static final String DISLIKE_ENDPOINT_PATH = "/{id}/dislike/{user-id}";
    private final ReviewService service;

    @PostMapping
    public Review create(@Valid @RequestBody Review review) {
        return service.createReview(review);
    }

    @PutMapping
    public Review update(@Valid @RequestBody Review review) {
        return service.updateReview(review);
    }

    @GetMapping
    public List<Review> findSpecificCountOnFilmId(@RequestParam(required = false) Long filmId,
                                                  @RequestParam(defaultValue = "10") Long count) {
        return service.getReviews(filmId, count);
    }

    @DeleteMapping(ID_ENDPOINT_PATH)
    public Map<String, String> delete(@PathVariable Long id) {
        return service.deleteReview(id);
    }

    @GetMapping(ID_ENDPOINT_PATH)
    public Review findById(@PathVariable Long id) {
        return service.getReview(id);
    }

    @PutMapping(LIKE_ENDPOINT_PATH)
    public void createLike(@PathVariable Long id, @PathVariable(value = "user-id") Long userId) {
        service.createLike(id, userId);
    }

    @PutMapping(DISLIKE_ENDPOINT_PATH)
    public void createDislike(@PathVariable Long id, @PathVariable(value = "user-id") Long userId) {
        service.createDislike(id, userId);
    }

    @DeleteMapping(LIKE_ENDPOINT_PATH)
    public void deleteLike(@PathVariable Long id, @PathVariable(value = "user-id") Long userId) {
        service.deleteReviewMark(id, userId);
    }

    @DeleteMapping(DISLIKE_ENDPOINT_PATH)
    public void deleteDislike(@PathVariable Long id, @PathVariable(value = "user-id") Long userId) {
        service.deleteReviewMark(id, userId);
    }
}
