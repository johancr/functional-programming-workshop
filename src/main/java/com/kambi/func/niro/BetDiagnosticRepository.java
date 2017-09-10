package com.kambi.func.niro;

import com.kambi.func.solutions.Attempt;

import java.time.Instant;

public class BetDiagnosticRepository {

    public static final String GOOD_TICKET = "a good ticket";
    public static final String NO_OUTCOME_TICKET = "ticket without outcome";
    public static final String NO_DATE_TICKET = "ticket without created date";
    public static final String NO_PAYOUT_STATUS_TICKET = "ticket without payout status";
    public static final String NO_STATE_TICKET = "ticket without state";

    public static Attempt<Integer> findOutcome(String ticket) {
        return ticket.equals(NO_OUTCOME_TICKET)
                ? Attempt.failure(new RuntimeException("Could not find outcome for ticket"))
                : Attempt.success(1);
    }

    public static Attempt<Instant> findCreatedDate(String ticket) {
        return ticket.equals(NO_DATE_TICKET)
                ? Attempt.failure(new RuntimeException("Could not find created date for ticket"))
                : Attempt.success(Instant.now());
    }

    public static Attempt<String> findPayoutStatus(String ticket) {
        return ticket.equals(NO_PAYOUT_STATUS_TICKET)
                ? Attempt.failure(new RuntimeException("Could not find payout status for ticket"))
                : Attempt.success("COMPLETE");
    }

    public static Attempt<String> findState(String ticket) {
        return ticket.equals(NO_STATE_TICKET)
                ? Attempt.failure(new RuntimeException("Could not find state for ticket"))
                : Attempt.success("PENDING");
    }
}
