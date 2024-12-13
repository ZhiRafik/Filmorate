package ru.yandex.practicum.filmorate.model;

import java.time.LocalDate;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.PastOrPresent;
import lombok.Data;

@Data
public class User {
    long id;
    @NotBlank
    String email;
    @NotBlank
    String login;
    String name;
    @NotNull @PastOrPresent
    LocalDate birthday;
}