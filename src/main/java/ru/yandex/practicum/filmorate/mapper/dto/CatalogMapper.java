package ru.yandex.practicum.filmorate.mapper.dto;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import ru.yandex.practicum.filmorate.model.Director;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.model.Mpa;
import ru.yandex.practicum.filmorate.model.dto.CatalogDto;

import java.util.Collection;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class CatalogMapper {

    public static List<CatalogDto> genresToCatalogs(Collection<Genre> collection) {
        return collection.stream()
                .map(CatalogMapper::genreToCatalog)
                .collect(Collectors.toList());
    }

    public static Set<Genre> catalogsToGenres(Collection<CatalogDto> collection) {
        if (collection == null || collection.isEmpty()) return Set.of();
        return collection.stream()
                .map(CatalogMapper::catalogToGenre)
                .collect(Collectors.toSet());
    }

    public static Genre catalogToGenre(CatalogDto catalog) {
        return Genre.builder()
                .id(catalog.getId())
                .name(catalog.getName())
                .build();
    }

    public static CatalogDto genreToCatalog(Genre genre) {
        return CatalogDto.builder()
                .id(genre.getId())
                .name(genre.getName())
                .build();
    }

    public static List<CatalogDto> mpaListToCatalogs(Collection<Mpa> collection) {
        return collection.stream()
                .map(CatalogMapper::mpaToCatalog)
                .collect(Collectors.toList());
    }

    public static Mpa catalogToMpa(CatalogDto catalog) {
        return Mpa.builder()
                .id(catalog.getId())
                .name(catalog.getName())
                .build();
    }

    public static CatalogDto mpaToCatalog(Mpa mpa) {
        return CatalogDto.builder()
                .id(mpa.getId())
                .name(mpa.getName())
                .build();
    }

    public static List<CatalogDto> directorsToCatalogs(Collection<Director> collection) {
        return collection.stream()
                .map(CatalogMapper::directorToCatalog)
                .collect(Collectors.toList());
    }

    public static Director catalogToDirector(CatalogDto catalog) {
        return Director.builder()
                .id(catalog.getId())
                .name(catalog.getName())
                .build();
    }

    public static CatalogDto directorToCatalog(Director director) {
        return CatalogDto.builder()
                .id(director.getId())
                .name(director.getName())
                .build();
    }
}
