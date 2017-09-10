package com.kambi.func.examples;

import com.kambi.func.solutions.List;
import com.kambi.func.solutions.Option;

import java.util.function.Function;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Logged<T> {

    private final T value;
    private final List<String> log;

    private Logged(T value, List<String> log) {
        this.value = value;
        this.log = log;
    }

    public static <T> Logged<T> log(T value, String message) {
        return new Logged<>(value, List.list(message));
    }

    public static <T> Logged<T> log(T value, Function<T, Option<String>> message) {
        return new Logged<>(value, message.apply(value)
                .map(List::list)
                .getOrElse(List.list()));
    }

    public static <T> Logged<T> log(T value, Option<String> message) {
        return log(value, v -> message);
    }

    public static <T> Logged<T> log(T value) {
        return log(value, Option.none());
    }

    public <U> Logged<U> flatMap(Function<T, Logged<U>> f) {
        Logged<U> result = f.apply(value);

        return new Logged<>(result.getValue(), log.concat(result.getLog()));
    }

    public <U> Logged<U> as(U u) {
        return new Logged<>(u, log);
    }

    public T getValue() {
        return value;
    }

    public List<String> getLog() {
        return log;
    }

    public T run(Logger logger) {
        log.forEach(l -> logger.log(Level.INFO, l));
        return value;
    }
}
