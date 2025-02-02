package ru.yandex.practicum.filmorate.dal.mappers;

import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.Film;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.Duration;

@Component("filmRowMapper")
public class FilmRowMapper implements RowMapper<Film> {
    @Override
    public Film mapRow(ResultSet resultSet, int rowNum) throws SQLException {
        Film film = new Film();
        film.setId(resultSet.getLong("film_id"));
        film.setName(resultSet.getString("name"));
        film.setDescription(resultSet.getString("description"));
        film.setDuration(Duration.ofMinutes(resultSet.getInt("duration")));
        film.setGenreId(resultSet.getInt("genre_id"));
        film.setMpaId(resultSet.getInt("mpa_id"));
        Timestamp realeaseDate = resultSet.getTimestamp("release_date");
        film.setReleaseDate(realeaseDate.toLocalDateTime().toLocalDate());
        return film;
    }
}
