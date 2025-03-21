package ru.yandex.practicum.filmorate.dal.base;

import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;

import java.util.List;

@RequiredArgsConstructor
public abstract class BaseDao<T> {

    protected final JdbcTemplate jdbc;
    protected final RowMapper<T> mapper;

    protected Long insert(String query, Object... params) {
        return JdbcUtil.insert(jdbc, query, params);
    }

    protected void update(String query, Object... params) {
        JdbcUtil.update(jdbc, query, params);
    }

    protected boolean remove(String query, Object... params) {
        return JdbcUtil.remove(jdbc, query, params);
    }

    protected T findOne(String query, Object... params) {
        return JdbcUtil.findOne(jdbc, mapper, query, params);
    }

    protected List<T> findMany(String query, Object... params) {
        return JdbcUtil.findMany(jdbc, mapper, query, params);
    }
}
