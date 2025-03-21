package ru.yandex.practicum.filmorate.dal;

import ru.yandex.practicum.filmorate.model.Review;

import java.util.List;
import java.util.Optional;

public interface ReviewDao {
    Long insert(Review review);

    Long update(Review review);

    boolean delete(long id);

    Optional<Review> getReview(long id);

    List<Review> getReviews(long id, long count);

    List<Review> getReviews(long count);

    void checkReviewExistOrThrow(long reviewId);

    void createLike(long reviewId, long userId);

    void createDislike(long reviewId, long userId);

    void deleteMark(long reviewId, long userId);
}
