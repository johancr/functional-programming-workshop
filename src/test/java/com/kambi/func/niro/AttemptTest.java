package com.kambi.func.niro;

import com.kambi.func.solutions.Attempt;
import org.junit.Test;

import java.time.Instant;
import java.util.function.Function;

import static com.kambi.func.niro.BetDiagnosticRepository.*;
import static com.kambi.func.niro.EventId.*;
import static com.kambi.func.solutions.Attempt.*;

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
    public void handlingAttempt_retake() {
        EventId id = GOOD_ID;

        eventRepository.findEventAttempt(id)
                .flatMap(eventMappingService::processEventAttempt)
                .map(impRequestFactory::create)
                .forEach(ImpMessageProcessor::send,
                        System.err::println);
    }

    @Test
    public void betDiagnostic_no_map2() {

        String ticket = NO_DATE_TICKET;

        Attempt<BetDiagnostic> betDiagnostic =
                findOutcome(ticket).flatMap(outcome ->
                        findCreatedDate(ticket).map(createdDate ->
                                BetDiagnostic.of(ticket, outcome, createdDate)));

        betDiagnostic.forEach(System.out::println,
                System.err::println);
    }

    @Test
    public void betDiagnostic_map2() {

        String ticket = NO_DATE_TICKET;

        Attempt<BetDiagnostic> betDiagnostic =
                map2(findOutcome(ticket), findCreatedDate(ticket),
                        createBetDiagnostic(ticket));

        betDiagnostic.forEach(System.out::println,
                System.err::println);
    }

    private Function<Integer, Function<Instant, BetDiagnostic>> createBetDiagnostic(String ticket) {
        return outcome -> createdDate -> BetDiagnostic.of(ticket, outcome, createdDate);
    }

    @Test
    public void betDiagnostic_no_map4() {

        String ticket = GOOD_TICKET;

        Attempt<BetDiagnostic> betDiagnostic =
                findState(ticket).flatMap(state ->
                        findPayoutStatus(ticket).flatMap(payoutStatus ->
                                findOutcome(ticket).flatMap(outcome ->
                                        findCreatedDate(ticket)
                                                .map(createdDate ->
                                                        BetDiagnostic.of(ticket, state, payoutStatus, outcome, createdDate)))));

        betDiagnostic.forEach(System.out::println,
                System.err::println);
    }

    @Test
    public void betDiagnostic_map4() {

        String ticket = GOOD_TICKET;

        Attempt<BetDiagnostic> betDiagnostic =
                map4(findState(ticket),
                        findPayoutStatus(ticket),
                        findOutcome(ticket),
                        findCreatedDate(ticket),
                        createCompleteBetDiagnostic(ticket));

        betDiagnostic.forEach(System.out::println,
                System.err::println);
    }

    private Function<String, Function<String, Function<Integer, Function<Instant, BetDiagnostic>>>> createCompleteBetDiagnostic(String ticket) {
        return state -> payoutStatus -> outcome -> createdDate ->
                BetDiagnostic.of(ticket, state, payoutStatus, outcome, createdDate);
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
}
