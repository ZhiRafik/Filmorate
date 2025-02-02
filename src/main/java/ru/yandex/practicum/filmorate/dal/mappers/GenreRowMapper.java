package ru.yandex.practicum.filmorate.dal.mappers;

import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.enums.Genre;

import java.sql.ResultSet;
import java.sql.SQLException;

@Component("genreRowMapper")
public class GenreRowMapper implements RowMapper<Genre> {

    @Override
    public Genre mapRow(ResultSet rs, int rowNum) throws SQLException {
        String genreName = rs.getString("name").toUpperCase();
        return Genre.valueOf(genreName); // конвертируем строку в enum
    }
}