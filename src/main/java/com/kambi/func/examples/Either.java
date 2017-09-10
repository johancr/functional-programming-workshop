package com.kambi.func.examples;

import java.util.function.Function;

public abstract class Either<L, R> {

    public static <L, R> Either<L, R> left(L value) {
        return new Left<>(value);
    }

    public static <L, R> Either<L, R> right(R value) {
        return new Right<>(value);
    }

    public abstract <T> T fold(Function<L, T> fLeft, Function<R, T> fRight);

    private static class Left<L, R> extends Either<L, R> {

        private final L value;

        public Left(L value) {
            this.value = value;
        }

        @Override
        public <T> T fold(Function<L, T> fLeft, Function<R, T> fRight) {
            return fLeft.apply(value);
        }
    }

    public static class Right<L, R> extends Either<L, R> {

        private final R value;

        public Right(R value) {
            this.value = value;
        }

        @Override
        public <T> T fold(Function<L, T> fLeft, Function<R, T> fRight) {
            return fRight.apply(value);
        }
    }
}
