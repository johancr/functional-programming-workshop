package com.kambi.func.niro;

import org.junit.Test;

import java.util.function.Function;

import static com.kambi.func.niro.EventId.GOOD_ID;
import static com.kambi.func.niro.ImpMessageProcessor.send;

public class CompositionTest {

    private final EventRepository eventRepository = new EventRepository();
    private final EventMappingService eventMappingService = new EventMappingService();
    private final ImpRequestFactory impRequestFactory = new ImpRequestFactory();

    @Test
    public void compose() {

        EventId id = GOOD_ID;

        Function<EventId, ImpRequest> createRequest = eventRepository.findEvent()
                .andThen(eventMappingService.processEvent())
                .andThen(impRequestFactory.create());

        send(createRequest.apply(id));
    }
}
