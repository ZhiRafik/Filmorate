package ru.yandex.practicum.filmorate.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.dal.EventDao;
import ru.yandex.practicum.filmorate.dal.FilmDao;
import ru.yandex.practicum.filmorate.dal.ReviewDao;
import ru.yandex.practicum.filmorate.dal.UserDao;
import ru.yandex.practicum.filmorate.model.Review;

import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static ru.yandex.practicum.filmorate.exception.NotFoundException.notFoundException;

@Service
@RequiredArgsConstructor
public class ReviewService {
    private static final String REVIEW_ERROR_MESSAGE = "Отзыв с идентификатором {0} - не найден";
    private static final String USER_ERROR_MESSAGE = "Пользователь с идентификатором: {0} - не существует.";

    private final ReviewDao reviewDao;
    private final FilmDao filmDao;
    private final UserDao userDao;
    private final EventDao eventDao;

    public Review createReview(Review review) {
        checkUserAndFilm(review);
        Long id = reviewDao.insert(review);
        review.setReviewId(id);
        eventDao.insert(review.getUserId(), "REVIEW", "ADD", review.getReviewId());
        return review;
    }

    public Review updateReview(Review newReview) {
        checkUserAndFilm(newReview);
        reviewDao.checkReviewExistOrThrow(newReview.getReviewId());
        Review oldReview = reviewDao.getReview(newReview.getReviewId()).orElseThrow(
                notFoundException(REVIEW_ERROR_MESSAGE, newReview.getReviewId()));
        Review review = update(oldReview, newReview);
        reviewDao.update(review);
        eventDao.insert(review.getUserId(), "REVIEW", "UPDATE", review.getReviewId());
        return review;
    }

    public Map<String, String> deleteReview(long id) {
        long userId = reviewDao.getReview(id).orElseThrow(notFoundException(USER_ERROR_MESSAGE, id)).getUserId();
        reviewDao.checkReviewExistOrThrow(id);
        reviewDao.delete(id);
        eventDao.insert(userId, "REVIEW", "REMOVE", id);
        return Map.of("Удаление отзыва.", String.format("Отзыв с идентификатором %d - удален.", id));
    }

    public Review getReview(long id) {
        return reviewDao.getReview(id).orElseThrow(notFoundException(REVIEW_ERROR_MESSAGE, id));
    }

    public List<Review> getReviews(Long id, Long count) {
        return getReviewsWithCheck(id, count);
    }

    public void createLike(long id, long userId) {
        reviewDao.deleteMark(id, userId);
        reviewDao.createLike(id, userId);
    }

    public void createDislike(long id, long userId) {
        reviewDao.deleteMark(id, userId);
        reviewDao.createDislike(id, userId);
    }

    public void deleteReviewMark(long id, long userId) {
        reviewDao.deleteMark(id, userId);
    }

    private void checkUserAndFilm(Review review) {
        filmDao.checkFilmExistOrThrow(review.getFilmId());
        userDao.checkUserExistsOrThrow(review.getUserId());
    }

    private List<Review> getReviewsWithCheck(Long id, Long count) {
        if (id == null && count == 10L) {
            return reviewSortedOnUseful(reviewDao.getReviews(count));
        } else if (id == null) {
            return reviewsSortedOnReviewId(reviewDao.getReviews(count));
        } else
            return reviewDao.getReviews(id, count);
    }

    private List<Review> reviewSortedOnUseful(List<Review> reviews) {
        return reviews.stream().sorted(Comparator.comparing(Review::getUseful).reversed()).collect(Collectors.toList());
    }

    private List<Review> reviewsSortedOnReviewId(List<Review> reviews) {
        return reviews.stream().sorted(Comparator.comparing(Review::getReviewId)).collect(Collectors.toList());
    }

    private Review update(Review oldReview, Review newReview) {
        return Review.builder()
                .reviewId(newReview.getReviewId())
                .content(newReview.getContent() == null ? oldReview.getContent() : newReview.getContent())
                .isPositive(newReview.getIsPositive() == null ? oldReview.getIsPositive() : newReview.getIsPositive())
                .userId(oldReview.getUserId())
                .filmId(oldReview.getFilmId())
                .useful(oldReview.getUseful())
                .build();
    }
}
