package ru.yandex.practicum.filmorate.dal.base;

import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import ru.yandex.practicum.filmorate.exception.InternalServerException;

import java.sql.PreparedStatement;
import java.sql.Statement;
import java.util.List;

public class JdbcUtil {

    public static Long insert(JdbcTemplate jdbc, String query, Object... params) {
        KeyHolder keyHolder = new GeneratedKeyHolder();
        jdbc.update(connection -> {
            PreparedStatement ps = connection.prepareStatement(query, Statement.RETURN_GENERATED_KEYS);
            for (int idx = 0; idx < params.length; idx++) {
                ps.setObject(idx + 1, params[idx]);
            }
            return ps;
        }, keyHolder);
        Long id = keyHolder.getKeyAs(Long.class);
        if (id == null) {
            throw new InternalServerException("Не удалось сохранить данные");
        }
        return id;
    }

    public static void update(JdbcTemplate jdbc, String query, Object... params) {
        jdbc.update(query, params);
    }

    public static boolean remove(JdbcTemplate jdbc, String query, Object... params) {
        int rowsDeleted = jdbc.update(query, params);
        return rowsDeleted > 0;
    }

    public static <T> T findOne(JdbcTemplate jdbc, RowMapper<T> mapper, String query, Object... params) {
        try {
            return jdbc.queryForObject(query, mapper, params);
        } catch (EmptyResultDataAccessException e) {
            return null;
        }
    }

    public static <T> List<T> findMany(JdbcTemplate jdbc, RowMapper<T> mapper, String query, Object... params) {
        return jdbc.query(query, mapper, params);
    }
}
