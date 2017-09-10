package com.kambi.func.niro;

public class NiroLog {

    public static void logEventMappingError(Event event) {
        System.err.println("Could not create event mapping for event with id: " + event.id);
    }

    public static void logEventError(EventId id) {
        System.err.println("Wanted to process event with id: " + id + " but it was not found");
    }

    public static void logSendingRequests() {
        System.out.println("Sending imp request");
    }

    public static void logProcessingEvent(Event event) {
        System.out.println("Processing event with id: " + event.id);
    }
}
