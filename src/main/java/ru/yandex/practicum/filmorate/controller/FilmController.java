package ru.yandex.practicum.filmorate.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.model.dto.FilmDto;
import ru.yandex.practicum.filmorate.service.FilmService;
import ru.yandex.practicum.filmorate.validation.group.ValidationGroup;

import java.util.Collection;
import java.util.List;
import java.util.Map;

@RequiredArgsConstructor
@Validated
@RestController
@RequestMapping("/films")
public class FilmController {

    private final FilmService service;

    @PostMapping
    @Validated(ValidationGroup.OnCreate.class)
    public FilmDto addFilm(@Valid @RequestBody FilmDto dto) {
        return service.addFilm(dto);
    }

    @PutMapping
    @Validated(ValidationGroup.OnUpdate.class)
    public FilmDto updateFilm(@Valid @RequestBody FilmDto dto) {
        return service.updateFilm(dto);
    }

    @GetMapping
    public Collection<FilmDto> getFilms() {
        return service.getFilms();
    }

    @GetMapping("/{film-id}")
    public FilmDto getFilm(@PathVariable(name = "film-id") long filmId) {
        return service.getFilm(filmId);
    }

    @PutMapping("/{film-id}/like/{user-id}")
    public void like(@PathVariable(name = "film-id") long filmId, @PathVariable(name = "user-id") long userId) {
        service.like(filmId, userId);
    }

    @DeleteMapping("/{film-id}/like/{user-id}")
    public void removeLike(@PathVariable(name = "film-id") long filmId, @PathVariable(name = "user-id") long userId) {
        service.removeLike(filmId, userId);
    }

    @DeleteMapping("/{id}")
    public Map<String, String> deleteFilm(@PathVariable long id) {
        return service.deleteFilm(id);
    }

    @GetMapping("/popular")
    public Collection<FilmDto> findPopularFilms(
            @RequestParam(defaultValue = "10") Long count,
            @RequestParam(required = false) Long genreId,
            @RequestParam(required = false) Long year
    ) {
        return service.findPopularFilms(count, genreId, year);
    }

    @GetMapping("/director/{director-id}")
    public List<FilmDto> findFilmWithDirectorsOnLikes(
            @PathVariable(value = "director-id") Long directorId,
            @RequestParam String sortBy
    ) {
        return service.findFilmWithDirectors(directorId, sortBy);
    }

    @GetMapping("/search")
    public Collection<FilmDto> searchFilms(@RequestParam String query, @RequestParam List<String> by) {
        return service.searchFilms(query, by);
    }

    @GetMapping("/common")
    public Collection<FilmDto> searchCommonFilms(@RequestParam Long userId, @RequestParam Long friendId) {
        return service.searchCommonFilms(userId, friendId);
    }
}