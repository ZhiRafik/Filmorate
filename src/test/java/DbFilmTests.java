import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.ContextConfiguration;
import ru.yandex.practicum.Main;
import ru.yandex.practicum.filmorate.dal.FilmRepository;
import ru.yandex.practicum.filmorate.dal.mappers.FilmRowMapper;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.storage.FilmDbStorage;
import ru.yandex.practicum.filmorate.storage.UserDbStorage;
import ru.yandex.practicum.filmorate.model.User;

import java.time.Duration;
import java.time.LocalDate;
import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

@JdbcTest
@AutoConfigureTestDatabase(replace= AutoConfigureTestDatabase.Replace.NONE)
@ContextConfiguration(classes = Main.class)
@RequiredArgsConstructor(onConstructor_ = @Autowired)
@Import({FilmDbStorage.class, UserDbStorage.class, Film.class, User.class,
        FilmRepository.class, FilmRowMapper.class})
class DbFilmTests {

    private final FilmDbStorage filmStorage;
    private final UserDbStorage userStorage;
    private Film film1;
    private Film film2;
    private User user1;


    @BeforeEach
    void setUp() {
        film1 = new Film();
        film2 = new Film();
        user1 = new User();
        film1.setDescription("wonderful film");
        film1.setDuration(Duration.ofMinutes(120));
        film1.setName("Duna");
        film1.setReleaseDate(LocalDate.of(2021, 1,1));
        film2.setDescription("normal film");
        film2.setDuration(Duration.ofMinutes(90));
        film2.setName("Oma");
        film2.setReleaseDate(LocalDate.of(2022, 2,2));
        filmStorage.addFilm(film1);
        filmStorage.addFilm(film2);
        user1.setBirthday(LocalDate.of(2005, 11, 10));
        user1.setName("Aleks");
        user1.setLogin("Masterpanda");
        user1.setEmail("myfavouritemail@gmail.com");
        userStorage.addUser(user1);
    }

    @Test
    public void getAll() {
        ArrayList<Film> films = new ArrayList<>(filmStorage.getAll());
        String filmsNames = films.get(0).getName() + " " + films.get(1).getName();
        assertEquals("Duna Oma", filmsNames);
    }

    @Test
    public void updateFilm() {
        Film film = film1;
        film.setName("Duna2");
        ArrayList<Film> films = new ArrayList<>(filmStorage.getAll());
        String name = films.get(0).getName();
        assertEquals("Duna2", name);
    }

    @Test
    public void containsFilm() {
        assertTrue(filmStorage.containsFilm(film1));
    }

    @Test
    public void addLike() {
        filmStorage.addLike(user1.getId(), film1.getId());
        boolean flag = filmStorage.checkLike(user1.getId(), film1.getId());
        assertTrue(flag);
    }

    @Test
    public void removeLike() {
        filmStorage.addLike(user1.getId(), film1.getId());
        filmStorage.removeLike(user1.getId(), film1.getId());
        boolean flag = filmStorage.checkLike(user1.getId(), film1.getId());
        assertFalse(flag);
    }

    @Test
    public void getFilm() {
        Film film = filmStorage.getFilm(film1.getId()).orElse(null);
        assertNotNull(film);
    }
    @Test
    public void getMostPopularFilms() {
        User user2 = new User();
        user2.setBirthday(LocalDate.of(2006, 2, 12));
        user2.setName("Dasha");
        user2.setLogin("Masterhorse");
        user2.setEmail("anotheremail@gmail.com");
        userStorage.addUser(user2);
        filmStorage.addLike(user1.getId(), film1.getId());
        filmStorage.addLike(user2.getId(), film1.getId());
        filmStorage.addLike(user1.getId(), film1.getId());
        ArrayList<Film> mostPopularFilms = new ArrayList<>(filmStorage.getMostPopularFilms(1));
        String mostPopularFilmName = mostPopularFilms.get(0).getName();
        assertEquals(film1.getName(), mostPopularFilmName);
    }
}