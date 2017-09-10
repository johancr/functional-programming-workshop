package com.kambi.func.niro;

import com.kambi.func.solutions.Attempt;
import com.kambi.func.solutions.Option;

import java.util.function.Function;

import static com.kambi.func.niro.EventId.NOT_MAPPABLE_ID;
import static com.kambi.func.niro.NiroLog.logProcessingEvent;

public class EventMappingService {

    public EventMapping processEventOrNull(Event event) {
        logProcessingEvent(event);

        return event.id == NOT_MAPPABLE_ID
                ? null
                : new EventMapping(event.id);
    }

    public EventMapping processEventOrThrow(Event event) {
        logProcessingEvent(event);

        if (event.id == NOT_MAPPABLE_ID) throw new RuntimeException("Could not map event with id: " + event.id);
        else return new EventMapping(event.id);
    }

    public Option<EventMapping> processEventOrNone(Event event) {
        logProcessingEvent(event);

        return event.id == NOT_MAPPABLE_ID
                ? Option.none()
                : Option.some(new EventMapping(event.id));
    }

    public Attempt<EventMapping> processEventAttempt(Event event) {
        logProcessingEvent(event);

        return event.id == NOT_MAPPABLE_ID
                ? Attempt.failure(new RuntimeException("Could not map event with id: " + event.id))
                : Attempt.success(new EventMapping(event.id));
    }

    public Function<Event, EventMapping> processEvent() {
        return event ->
        {
            logProcessingEvent(event);
            return new EventMapping(event.id);
        };
    }
}
