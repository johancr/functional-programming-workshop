package com.kambi.func.examples;

import java.util.function.Function;

@FunctionalInterface
public interface Effect<T> {

    T run();

    default <U> Effect<U> map(Function<T, U> f) {
        return () -> f.apply(run());
    }

    default <U> Effect<U> flatMap(Function<T, Effect<U>> f) {
        return () -> f.apply(run()).run();
    }

    default <U> Effect<U> as(Effect<U> u) {
        return flatMap(ignore -> u);
    }

    default <U> Effect<U> andThen(Effect<U> u) {
        return as(u);
    }
}
