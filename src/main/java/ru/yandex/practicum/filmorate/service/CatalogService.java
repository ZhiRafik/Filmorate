package ru.yandex.practicum.filmorate.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.dal.CatalogDao;
import ru.yandex.practicum.filmorate.mapper.dto.CatalogMapper;
import ru.yandex.practicum.filmorate.model.dto.CatalogDto;

import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class CatalogService {

    private final CatalogDao dao;

    public List<CatalogDto> getGenres() {
        return CatalogMapper.genresToCatalogs(dao.getGenres());
    }

    public CatalogDto getGenre(long genreId) {
        dao.checkGenresExistOrThrow(List.of(genreId));
        return CatalogMapper.genreToCatalog(dao.getGenre(genreId));
    }

    public List<CatalogDto> getAllMpa() {
        return CatalogMapper.mpaListToCatalogs(dao.getAllMpa());
    }

    public CatalogDto getMpa(long mpaId) {
        dao.checkMpaExistOrThrow(mpaId);
        return CatalogMapper.mpaToCatalog(dao.getMpa(mpaId));
    }

    public List<CatalogDto> getDirectors() {
        return CatalogMapper.directorsToCatalogs(dao.getDirectors());
    }

    public CatalogDto getDirectorById(long directorId) {
        dao.checkDirectorExistOrThrow(directorId);
        return CatalogMapper.directorToCatalog(dao.getDirector(directorId));
    }

    public CatalogDto createDirector(CatalogDto dto) {
        if (dto.getName().isBlank()) {
            throw new IllegalArgumentException("Имя не может быть пустым!");
        }
        long id = dao.createDirector(CatalogMapper.catalogToDirector(dto));
        dto.setId(id);
        return dto;
    }

    public CatalogDto updateDirector(CatalogDto dto) {
        dao.checkDirectorExistOrThrow(dto.getId());
        long id = dao.updateDirector(CatalogMapper.catalogToDirector(dto));
        dto.setId(id);
        return dto;
    }

    public Map<String, String> deleteDirector(long directorId) {
        dao.checkDirectorExistOrThrow(directorId);
        dao.deleteDirectorById(directorId);
        return Map.of("description", String.format("Режиссер с идентификатором: %d - удален", directorId));
    }
}
