package ru.yandex.practicum.filmorate.model;

import java.time.LocalDate;
import java.util.Set;
import lombok.Data;

@Data
public class User {
    long id;
    String email;
    String login;
    String name;
    LocalDate birthday;
    Set<Long> friendsIds;
}