package ru.yandex.practicum.filmorate.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.model.dto.CatalogDto;
import ru.yandex.practicum.filmorate.service.CatalogService;

import java.util.List;
import java.util.Map;

@RequiredArgsConstructor
@Validated
@RestController
@RequestMapping
public class CatalogController {
    private static final String DIRECTORS_ENDPOINT_PATH = "/directors";
    private static final String DIRECTORS_ID_ENDPOINT_PATH = "/directors/{id}";
    private final CatalogService service;

    @GetMapping("/genres")
    public List<CatalogDto> getGenres() {
        return service.getGenres();
    }

    @GetMapping("/genres/{genre-id}")
    public CatalogDto getGenre(@PathVariable(name = "genre-id") long genreId) {
        return service.getGenre(genreId);
    }

    @GetMapping("/mpa")
    public List<CatalogDto> getAllMpa() {
        return service.getAllMpa();
    }

    @GetMapping("/mpa/{mpa-id}")
    public CatalogDto getMpa(@PathVariable(name = "mpa-id") long mpaId) {
        return service.getMpa(mpaId);
    }

    @GetMapping(DIRECTORS_ENDPOINT_PATH)
    public List<CatalogDto> getDirectors() {
        return service.getDirectors();
    }

    @GetMapping(DIRECTORS_ID_ENDPOINT_PATH)
    public CatalogDto getDirectorById(@PathVariable long id) {
        return service.getDirectorById(id);
    }

    @PostMapping(DIRECTORS_ENDPOINT_PATH)
    public CatalogDto createDirector(@RequestBody CatalogDto dto) {
        return service.createDirector(dto);
    }

    @PutMapping(DIRECTORS_ENDPOINT_PATH)
    public CatalogDto updateDirector(@RequestBody CatalogDto dto) {
        return service.updateDirector(dto);
    }

    @DeleteMapping(DIRECTORS_ID_ENDPOINT_PATH)
    public Map<String, String> deleteDirector(@PathVariable long id) {
        return service.deleteDirector(id);
    }
}
