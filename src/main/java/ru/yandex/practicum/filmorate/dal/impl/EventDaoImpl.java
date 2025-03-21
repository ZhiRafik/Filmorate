package ru.yandex.practicum.filmorate.dal.impl;

import org.springframework.context.annotation.Primary;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.dal.EventDao;
import ru.yandex.practicum.filmorate.dal.base.BaseDao;
import ru.yandex.practicum.filmorate.model.Event;

import java.time.Instant;
import java.util.List;

import static ru.yandex.practicum.filmorate.dal.impl.query.EventQuery.FIND_EVENT_QUERY;
import static ru.yandex.practicum.filmorate.dal.impl.query.EventQuery.INSERT_EVENT_QUERY;

@Primary
@Component
public class EventDaoImpl extends BaseDao<Event> implements EventDao {

    public EventDaoImpl(JdbcTemplate jdbc, RowMapper<Event> mapper) {
        super(jdbc, mapper);
    }

    @Override
    public Long insert(long id, String eventType, String operation, long eventId) {
        return insert(INSERT_EVENT_QUERY,
                Instant.now().toEpochMilli(),
                id,
                eventType,
                operation,
                eventId);
    }

    @Override
    public List<Event> getEvents(long id) {
        return findMany(FIND_EVENT_QUERY, id);
    }
}
