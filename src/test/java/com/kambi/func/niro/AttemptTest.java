package com.kambi.func.niro;

import com.kambi.func.solutions.Attempt;
import org.junit.Test;

import static com.kambi.func.niro.EventId.*;
import static com.kambi.func.solutions.Attempt.attempt;
import static com.kambi.func.solutions.Attempt.lift;

public class AttemptTest {

    private final EventRepository eventRepository = new EventRepository();
    private final EventMappingService eventMappingService = new EventMappingService();
    private final ImpRequestFactory impRequestFactory = new ImpRequestFactory();

    @Test
    public void handlingAttempt() {
        EventId id = BAD_ID;

        attempt(() -> eventRepository.findEventOrThrow(id))
                .flatMap(event -> attempt(() -> eventMappingService.processEventOrThrow(event)))
                .map(impRequestFactory::create)
                .forEach(ImpMessageProcessor::send,
                        System.err::println);
    }

    @Test
    public void handlingAttempt_lift() {
        EventId id = NOT_MAPPABLE_ID;

        lift(eventRepository::findEventOrThrow)
                .andThen(lift(eventMappingService::processEventOrThrow))
                .andThen(lift(impRequestFactory::create))
                .apply(Attempt.success(id))
                .forEach(ImpMessageProcessor::send,
                        System.err::println);
    }


    @Test
    public void handlingAttempt_retake() {
        EventId id = GOOD_ID;

        eventRepository.findEventAttempt(id)
                .flatMap(eventMappingService::processEventAttempt)
                .map(impRequestFactory::create)
                .forEach(ImpMessageProcessor::send,
                        System.err::println);
    }

}
