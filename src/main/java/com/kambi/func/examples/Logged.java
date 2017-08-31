package com.kambi.func.examples;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.function.Function;

public class Logged<T> {

    private final T value;
    private final List<String> log;

    public static <T> Logged<T> log(T value, String message) {
        return new Logged<>(value, Collections.singletonList(message));
    }

    private Logged(T value, List<String> log) {
        this.value = value;
        this.log = log;
    }

    public T getValue() {
        return value;
    }

    public List<String> getLog() {
        return log;
    }

    public <U> Logged<U> flatMap(Function<T, Logged<U>> f) {
        Logged<U> result = f.apply(value);

        List<String> updatedLog = new ArrayList<>(log);
        updatedLog.addAll(result.getLog());

        return new Logged<>(result.getValue(), updatedLog);
    }
}
