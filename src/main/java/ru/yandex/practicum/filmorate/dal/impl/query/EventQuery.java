package ru.yandex.practicum.filmorate.dal.impl.query;

public class EventQuery {

    public static final String INSERT_EVENT_QUERY = """
            INSERT INTO events (timestamp, user_id, event_type, operation, entity_id) VALUES (?, ?, ?, ?, ?)
            """;

    public static final String FIND_EVENT_QUERY = """
            SELECT id, timestamp, user_id, event_type, operation, entity_id FROM events WHERE user_id = ?
            """;
}
