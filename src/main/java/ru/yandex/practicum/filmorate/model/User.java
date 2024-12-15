package ru.yandex.practicum.filmorate.model;

import java.time.LocalDate;
import jakarta.validation.constraints.Email;
import lombok.Data;

@Data
public class User {
    long id;
    @Email
    String email;
    String login;
    String name;
    LocalDate birthday;
}