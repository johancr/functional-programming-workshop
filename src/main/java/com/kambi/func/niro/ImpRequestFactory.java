package com.kambi.func.niro;

import java.util.function.Function;

public class ImpRequestFactory {
    public ImpRequest create(EventMapping eventMapping) {
        return new ImpRequest(eventMapping.eventId);
    }

    public Function<EventMapping, ImpRequest> create() {
        return eventMapping -> new ImpRequest(eventMapping.eventId);
    }
}
