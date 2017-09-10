package com.kambi.func.niro;

import org.junit.Test;

import static com.kambi.func.niro.EventId.GOOD_ID;
import static com.kambi.func.niro.ImpMessageProcessor.send;
import static com.kambi.func.niro.NiroLog.logEventError;
import static com.kambi.func.niro.NiroLog.logEventMappingError;

public class NullTest {

    private final EventRepository eventRepository = new EventRepository();
    private final EventMappingService eventMappingService = new EventMappingService();
    private final ImpRequestFactory impRequestFactory = new ImpRequestFactory();

    @Test
    public void handlingNull() {
        EventId id = GOOD_ID;

        Event event = eventRepository.findEventOrNull(id);

        if (event != null)
        {
            EventMapping eventMapping = eventMappingService.processEventOrNull(event);

            if (eventMapping != null)
            {
                ImpRequest request = impRequestFactory.create(eventMapping);
                send(request);
            }
            else
            {
                logEventMappingError(event);
            }
        }
        else
        {
            logEventError(id);
        }
    }
}
