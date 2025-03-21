package ru.yandex.practicum.filmorate.model;

import jakarta.validation.constraints.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Data;
import lombok.experimental.FieldDefaults;
import ru.yandex.practicum.filmorate.validation.group.ValidationGroup;

import java.time.LocalDate;

@Data
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class User {

    @Null(groups = ValidationGroup.OnCreate.class)
    @NotNull(groups = ValidationGroup.OnUpdate.class)
    @Positive
    Long id;

    @NotNull(groups = ValidationGroup.OnCreate.class)
    @Pattern(regexp = "\\S+")
    @Email
    String email;

    @NotNull(groups = ValidationGroup.OnCreate.class)
    @Pattern(regexp = "\\S+")
    String login;

    String name;

    @PastOrPresent
    LocalDate birthday;
}
