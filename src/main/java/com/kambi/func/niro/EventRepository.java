package com.kambi.func.niro;

import com.kambi.func.solutions.Attempt;
import com.kambi.func.solutions.Option;

import java.util.NoSuchElementException;
import java.util.function.Function;

import static com.kambi.func.niro.EventId.BAD_ID;

public class EventRepository {

    public Event findEventOrThrow(EventId id) {
        if (id == BAD_ID) throw new NoSuchElementException("Could not find event with id: " + id);
        else return new Event(id);
    }

    public Event findEventOrNull(EventId id) {
        return id == BAD_ID
                ? null
                : new Event(id);
    }

    public Option<Event> findEventOption(EventId id) {
        return id == BAD_ID
                ? Option.none()
                : Option.some(new Event(id));
    }

    public Attempt<Event> findEventAttempt(EventId id) {
        return id == BAD_ID
                ? Attempt.failure(new NoSuchElementException("Could not find event with id: " + id))
                : Attempt.success(new Event(id));
    }

    public Function<EventId, Event> findEvent() {
        return Event::new;
    }
}
