package ru.yandex.practicum.filmorate.dal.impl.query;

public class CatalogQuery {

    public static final String GET_GENRES_QUERY = "SELECT * FROM genres";
    public static final String GET_GENRE_QUERY = "SELECT * FROM genres WHERE id = ?";
    public static final String GET_COUNT_GENRES = "SELECT COUNT(*) FROM genres WHERE id IN (:genreIds)";

    public static final String GET_ALL_MPA_QUERY = "SELECT * FROM mpa";
    public static final String GET_MPA_QUERY = "SELECT * FROM mpa WHERE id = ?";
    public static final String GET_COUNT_MPA = "SELECT COUNT(*) FROM mpa WHERE id = ?";

    public static final String GET_DIRECTORS_QUERY = "SELECT * FROM directors";
    public static final String GET_DIRECTOR_QUERY = "SELECT * FROM directors WHERE id = ?";
    public static final String INSERT_DIRECTOR_QUERY = "INSERT INTO directors (name) VALUES (?)";
    public static final String UPDATE_DIRECTOR_QUERY = "UPDATE directors SET name = ? WHERE id = ?";
    public static final String DELETE_DIRECTOR_QUERY = "DELETE FROM directors WHERE id = ?";
    public static final String GET_COUNT_DIRECTORS = "SELECT COUNT(*) FROM directors WHERE id = ?";
}
