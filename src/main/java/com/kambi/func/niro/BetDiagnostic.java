package com.kambi.func.niro;

import java.time.Instant;

public class BetDiagnostic {

    public final String ticket;
    public final String state;
    public final String payoutStatus;
    public final int outcome;
    public final Instant createdDate;

    public static BetDiagnostic of(String ticket, int outcome, Instant createdDate) {
        return new BetDiagnostic(ticket, "Unknown", "Unknown", outcome, createdDate);
    }

    public static BetDiagnostic of(String ticket, String state, String payoutStatus, int outcome, Instant createdDate) {
        return new BetDiagnostic(ticket, state, payoutStatus, outcome, createdDate);
    }

    private BetDiagnostic(String ticket, String state, String payoutStatus, int outcome, Instant createdDate) {
        this.ticket = ticket;
        this.state = state;
        this.payoutStatus = payoutStatus;
        this.outcome = outcome;
        this.createdDate = createdDate;
    }

    @Override
    public String toString() {
        return "BetDiagnostic{" +
                "ticket='" + ticket + '\'' +
                ", state='" + state + '\'' +
                ", payoutStatus='" + payoutStatus + '\'' +
                ", outcome=" + outcome +
                ", createdDate=" + createdDate +
                '}';
    }
}
