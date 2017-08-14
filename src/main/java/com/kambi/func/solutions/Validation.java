package com.kambi.func.solutions;

import java.util.function.Function;


public abstract class Validation<E, T> {

    public abstract <U> U fold(Function<T, U> onSuccess, Function<List<E>, U> onFailure);

    public static <E, T> Validation<E, T> success(T value) {
        return new Success<>(value);
    }

    public static <E, T> Validation<E, T> failure(E failure) {
        return failure(List.list(failure));
    }

    public static <E, T> Validation<E, T> failure(List<E> failures) {
        return new Failure<>(failures);
    }

    public static <T, E> Validation<E, List<T>> sequence(List<Validation<E, T>> validations) {

        return validations.foldLeft(
                Validation.success(List.list()),
                x -> y -> Validation.map2(x, y, a -> a::append));
    }

    public static <T, T2, E, U> Validation<E, U> map2(Validation<E, T> v1,
                                                      Validation<E, T2> v2,
                                                      Function<T, Function<T2, U>> f) {

        return v1.fold(
                value1 ->
                        v2.fold(value2 -> success(f.apply(value1).apply(value2)),
                                failure2 -> failure(failure2)),
                failure1 ->
                        v2.fold(value2 -> failure(failure1),
                                failure2 -> failure(failure1.concat(failure2))));
    }

    public <U> Validation<E, U> ap(Validation<E, Function<T, U>> f) {
        return Validation.map2(this, f, x -> y -> y.apply(x));
    }

    private static class Success<E, T> extends Validation<E, T> {

        private final T value;

        public Success(T value) {
            this.value = value;
        }

        @Override
        public <U> U fold(Function<T, U> onSuccess, Function<List<E>, U> onFailure) {
            return onSuccess.apply(value);
        }
    }

    private static class Failure<E, T> extends Validation<E, T> {

        private final List<E> failures;

        private Failure(List<E> failures) {
            this.failures = failures;
        }

        @Override
        public <U> U fold(Function<T, U> onSuccess, Function<List<E>, U> onFailure) {
            return onFailure.apply(failures);
        }
    }
}