package ru.yandex.practicum.filmorate.dal.mappers;

import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.enums.MPA;

import java.sql.ResultSet;
import java.sql.SQLException;

@Component("mpaRowMapper")
public class MpaRowMapper implements RowMapper<MPA> {

    @Override
    public MPA mapRow(ResultSet rs, int rowNum) throws SQLException {
        String genreName = rs.getString("name").toUpperCase();
        return MPA.valueOf(genreName); // конвертируем строку в enum
    }
}
