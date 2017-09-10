package com.kambi.func.niro;

import org.junit.Test;

import java.util.NoSuchElementException;

import static com.kambi.func.niro.EventId.BAD_ID;
import static com.kambi.func.niro.ImpMessageProcessor.send;
import static com.kambi.func.niro.NiroLog.logEventError;
import static com.kambi.func.niro.NiroLog.logEventMappingError;

public class ExceptionTest {

    private final EventRepository eventRepository = new EventRepository();
    private final EventMappingService eventMappingService = new EventMappingService();
    private final ImpRequestFactory impRequestFactory = new ImpRequestFactory();

    @Test
    public void handlingException() {
        EventId id = BAD_ID;

        try
        {
            Event event = eventRepository.findEventOrThrow(id);

            try
            {
                EventMapping eventMapping = eventMappingService.processEventOrThrow(event);
                ImpRequest impRequest = impRequestFactory.create(eventMapping);

                send(impRequest);
            }
            catch (RuntimeException ex)
            {
                logEventMappingError(event);
            }
        }
        catch (NoSuchElementException ex)
        {
            logEventError(id);
        }
    }
}
