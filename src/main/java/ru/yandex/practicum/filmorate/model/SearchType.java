package ru.yandex.practicum.filmorate.model;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;

import java.util.Arrays;

@Getter
public enum SearchType {

    DIRECTOR("director"),
    TITLE("title"),
    NONE("");

    @Setter(value = AccessLevel.PACKAGE)
    private String value;

    SearchType(String value) {
        this.value = value;
    }

    public static SearchType fromValue(String value) {
        return Arrays.stream(values())
                .filter(type -> type.value.equals(value))
                .findFirst()
                .orElseGet(() -> {
                    SearchType result = NONE;
                    result.setValue(value);
                    return result;
                });
    }
}
