package com.kambi.func.niro;

import org.junit.Test;

import static com.kambi.func.niro.EventId.NOT_MAPPABLE_ID;

public class OptionTest {

    private final EventRepository eventRepository = new EventRepository();
    private final EventMappingService eventMappingService = new EventMappingService();
    private final ImpRequestFactory impRequestFactory = new ImpRequestFactory();

    @Test
    public void handlingOption() {
        EventId id = NOT_MAPPABLE_ID;

        eventRepository.findEventOption(id)
                .flatMap(eventMappingService::processEventOrNone)
                .map(impRequestFactory::create)
                .ifPresent(ImpMessageProcessor::send);
    }
}
