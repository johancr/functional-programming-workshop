package com.kambi.func.solutions;

import com.kambi.func.examples.SideEffect;

import java.util.function.Function;

public abstract class Option<T> {

    public static <T> Option<T> some(T a) {
        return new Some<>(a);
    }

    public static <T> Option<T> none() {
        return new None<>();
    }

    public abstract <U> Option<U> map(Function<T, U> f);

    public abstract T getOrElse(T defaultValue);

    public abstract <U> Option<U> flatMap(Function<T, Option<U>> f);

    public abstract void ifPresent(SideEffect<T> effect);

    public abstract boolean isPresent();

    private static class None<T> extends Option<T> {

        @Override
        public <U> Option<U> map(Function<T, U> f) {
            return none();
        }

        @Override
        public T getOrElse(T defaultValue) {
            return defaultValue;
        }

        @Override
        public <U> Option<U> flatMap(Function<T, Option<U>> f) {
            return none();
        }

        @Override
        public void ifPresent(SideEffect<T> effect) {
        }

        @Override
        public boolean isPresent() {
            return false;
        }
    }

    private static class Some<T> extends Option<T> {

        private final T value;

        public Some(T value) {
            this.value = value;
        }

        @Override
        public <U> Option<U> map(Function<T, U> f) {
            return some(f.apply(value));
        }

        @Override
        public T getOrElse(T defaultValue) {
            return value;
        }

        @Override
        public <U> Option<U> flatMap(Function<T, Option<U>> f) {
            return f.apply(value);
        }

        @Override
        public void ifPresent(SideEffect<T> effect) {
            effect.run(value);
        }

        @Override
        public boolean isPresent() {
            return true;
        }
    }
}