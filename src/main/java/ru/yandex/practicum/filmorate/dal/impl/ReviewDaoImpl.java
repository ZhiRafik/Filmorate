package ru.yandex.practicum.filmorate.dal.impl;

import org.springframework.context.annotation.Primary;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.dal.ReviewDao;
import ru.yandex.practicum.filmorate.dal.base.BaseDao;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.Review;

import java.util.List;
import java.util.Optional;

import static ru.yandex.practicum.filmorate.dal.impl.query.ReviewQuery.*;

@Primary
@Component
public class ReviewDaoImpl extends BaseDao<Review> implements ReviewDao {

    public ReviewDaoImpl(JdbcTemplate jdbc, RowMapper<Review> mapper) {
        super(jdbc, mapper);
    }

    @Override
    public Long insert(Review review) {
        return insert(INSERT_REVIEW_QUERY,
                review.getContent(),
                review.getIsPositive(),
                review.getUserId(),
                review.getFilmId()
        );
    }

    @Override
    public Long update(Review review) {
        update(UPDATE_REVIEW_QUERY,
                review.getContent(),
                review.getIsPositive(),
                review.getUserId(),
                review.getFilmId(),
                review.getReviewId());
        return review.getReviewId();
    }

    @Override
    public boolean delete(long id) {
        return remove(DELETE_REVIEW_QUERY, id);
    }

    @Override
    public Optional<Review> getReview(long id) {
        return Optional.ofNullable(findOne(QUERY_FIND_REVIEW_BY_ID, id));
    }

    @Override
    public List<Review> getReviews(long id, long count) {
        return findMany(FIND_REVIEW_BY_ID_AND_COUNT_QUERY, id, count);
    }

    @Override
    public List<Review> getReviews(long count) {
        return findMany(FIND_REVIEW_BY_COUNT_QUERY, count);
    }

    @Override
    public void createLike(long reviewId, long userId) {
        insert(INSERT_MARK_REVIEW_QUERY, reviewId, userId, 1);
    }

    @Override
    public void createDislike(long reviewId, long userId) {
        insert(INSERT_MARK_REVIEW_QUERY, reviewId, userId, -1);
    }

    @Override
    public void deleteMark(long reviewId, long userId) {
        remove(DELETE_MARK_REVIEW_QUERY, reviewId, userId);
    }

    @Override
    public void checkReviewExistOrThrow(long reviewId) {
        int count = jdbc.queryForObject(COUNT_REVIEW_QUERY, Integer.class, reviewId);
        if (count == 0) {
            throw new NotFoundException("Фильм с таким идентификатором не существует");
        }
    }
}
