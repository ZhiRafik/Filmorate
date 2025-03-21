package ru.yandex.practicum.filmorate.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.model.Event;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.service.UserService;
import ru.yandex.practicum.filmorate.validation.group.ValidationGroup;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Set;

@RequiredArgsConstructor
@Validated
@RestController
@RequestMapping("/users")
public class UserController {

    private final UserService service;

    @PostMapping
    @Validated(ValidationGroup.OnCreate.class)
    public User createUser(@Valid @RequestBody User user) {
        return service.createUser(user);
    }

    @PutMapping
    @Validated(ValidationGroup.OnUpdate.class)
    public User updateUser(@Valid @RequestBody User user) {
        return service.updateUser(user);
    }

    @GetMapping
    public Collection<User> getUsers() {
        return service.getUsers();
    }

    @DeleteMapping("/{id}")
    public Map<String, String> delete(@PathVariable long id) {
        return service.deleteUser(id);
    }

    @GetMapping("/{id}")
    public User getUser(@PathVariable long id) {
        return service.getUser(id);
    }

    @PutMapping("/{user-id}/friends/{friend-id}")
    public void addFriend(@PathVariable(name = "user-id") long userId, @PathVariable(name = "friend-id") long friendId) {
        service.addFriend(userId, friendId);
    }

    @DeleteMapping("/{user-id}/friends/{friend-id}")
    public void removeFriend(@PathVariable(name = "user-id") long userId, @PathVariable(name = "friend-id") long friendId) {
        service.removeFriend(userId, friendId);
    }

    @GetMapping("/{user-id}/friends")
    public Set<User> getFriends(@PathVariable(name = "user-id") long userId) {
        return service.getFriends(userId);
    }

    @GetMapping("/{user-id}/friends/common/{other-user-id}")
    public Set<User> getCommonFriends(@PathVariable(name = "user-id") long userId,
                                      @PathVariable(name = "other-user-id") long otherUserId) {
        return service.getCommonFriends(userId, otherUserId);
    }

    @GetMapping("/{id}/recommendations")
    public Collection<Film> getRecommendations(@PathVariable(name = "id") long userId) {
        return service.getRecommendations(userId);
    }

    @GetMapping("/{id}/likes")
    public Collection<Long> getLikes(@PathVariable(name = "id") long userId) {
        return service.getLikeFilms(userId);
    }

    @GetMapping("/{id}/feed")
    public List<Event> getEvent(@PathVariable long id) {
        return service.getEvents(id);
    }
}
