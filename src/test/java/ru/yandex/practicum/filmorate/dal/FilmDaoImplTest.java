package ru.yandex.practicum.filmorate.dal;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.context.annotation.Import;
import ru.yandex.practicum.filmorate.dal.impl.FilmDaoImpl;
import ru.yandex.practicum.filmorate.dal.impl.UserDaoImpl;
import ru.yandex.practicum.filmorate.mapper.row.FilmRowMapper;
import ru.yandex.practicum.filmorate.mapper.row.UserRowMapper;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.model.Mpa;
import ru.yandex.practicum.filmorate.model.User;

import java.time.LocalDate;
import java.util.List;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@JdbcTest
@AutoConfigureTestDatabase
@RequiredArgsConstructor(onConstructor_ = @Autowired)
@Import({FilmDaoImpl.class, FilmRowMapper.class, UserDaoImpl.class, UserRowMapper.class})
class FilmDaoImplTest {

    private final FilmDaoImpl filmDao;
    private final UserDaoImpl userDao;

    private Film film1;
    private Film film2;

    @BeforeEach
    public void setUp() {
        film1 = Film.builder()
                .name("Test Film 1")
                .description("Test description 1")
                .releaseDate(LocalDate.of(2010, 7, 16))
                .duration(148L)
                .mpa(Mpa.builder().id(1L).name("Test").build())
                .genres(Set.of(Genre.builder().id(1L).name("Test").build()))
                .build();

        film2 = Film.builder()
                .name("Test Film 2")
                .description("Test description 2")
                .releaseDate(LocalDate.of(2014, 11, 7))
                .duration(169L)
                .mpa(Mpa.builder().id(1L).name("Test").build())
                .genres(Set.of(Genre.builder().id(2L).name("Test").build()))
                .build();

    }

    @Test
    public void testInsertFilm() {
        Long filmId = filmDao.insert(film1);

        assertNotNull(filmId);
        Film savedFilm = filmDao.getFilm(filmId);

        assertNotNull(savedFilm);
        assertEquals(savedFilm.getName(), film1.getName());
    }

    @Test
    public void testGetFilms() {
        filmDao.insert(film1);
        filmDao.insert(film2);

        List<Film> films = filmDao.getFilms();
        assertEquals(2, films.size());
    }

    @Test
    public void testGetFilm() {
        Long filmId = filmDao.insert(film2);
        Film foundFilm = filmDao.getFilm(filmId);

        assertNotNull(foundFilm);
        assertEquals(foundFilm.getName(), film2.getName());
    }

    @Test
    public void testAddAndRemoveLike() {
        Long filmId = filmDao.insert(film1);
        User user = User.builder()
                .email("user1@example.com")
                .login("user1")
                .name("User One")
                .birthday(LocalDate.of(1991, 2, 2))
                .build();
        Long userId = userDao.insert(user);

        filmDao.addLike(filmId, userId);
        Film likedFilm = filmDao.getFilm(filmId);
        assertEquals(1, likedFilm.getLikeCount());

        filmDao.removeLike(filmId, userId);
        likedFilm = filmDao.getFilm(filmId);
        assertEquals(0, likedFilm.getLikeCount());
    }
}