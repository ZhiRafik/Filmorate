package ru.yandex.practicum.filmorate.dal;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.enums.MPA;

import java.util.Collection;
import java.util.Optional;

@Repository
public class MPARepository extends BaseRepository<MPA> {

    private static final String GET_MPAS = "SELECT * FROM mpa";
    private static final String GET_MPA_BY_ID = "SELECT * FROM mpa WHERE id = ?";

    public MPARepository(JdbcTemplate jdbc, RowMapper<MPA> mapper) {
        super(jdbc, mapper);
    }

    public Collection<MPA> getMPAs() {
        return findMany(GET_MPAS);
    }

    public Optional<MPA> getMPA(int id) {
        return findOne(GET_MPA_BY_ID, id);
    }
}