package com.kambi.func.examples;

public class Tuple<T, T2> {

    public final T _1;
    public final T2 _2;

    private Tuple(T t, T2 t2) {
        _1 = t;
        _2 = t2;
    }

    public static <T, T2> Tuple<T, T2> of(T t, T2 t2) {
        return new Tuple<>(t, t2);
    }
}
