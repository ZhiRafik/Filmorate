package ru.yandex.practicum.filmorate.dal;

import ru.yandex.practicum.filmorate.model.Event;

import java.util.List;

public interface EventDao {
    Long insert(long id, String eventType, String operation, long eventId);

    List<Event> getEvents(long userId);
}
