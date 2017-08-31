package com.kambi.func.examples;

import java.util.function.Function;
import java.util.function.Supplier;

public abstract class Attempt<T> {

    public static <T> Attempt<T> attempt(Supplier<T> operation) {

        try {
            return Attempt.success(operation.get());
        } catch (Exception ex) {
            return Attempt.failure(ex);
        }
    }

    public abstract boolean isSuccess();
    public abstract <U> Attempt<U> map(Function<T, U> f);
    public abstract <U> U fold(Function<T, U> fSuccess, Function<Exception, U> fFailure);

    public static <T> Attempt<T> success(T value) {
                                                return new Success<>(value);
                                                                            }

    public static <T> Attempt<T> failure(Exception ex) {
                                                     return new Failure<>(ex);
                                                                              }

    private static class Success<T> extends Attempt<T> {
        private final T value;

        private Success(T value) {
                               this.value = value;
                                                  }

        @Override
        public boolean isSuccess() {
                                 return true;
                                             }

        @Override
        public <U> Attempt<U> map(Function<T, U> f) {
            return success(f.apply(value));
        }

        @Override
        public <U> U fold(Function<T, U> fSuccess, Function<Exception, U> fFailure) {
            return fSuccess.apply(value);
        }
    }

    private static class Failure<T> extends Attempt<T> {
        private final Exception exception;

        private Failure(Exception exception) {
                                           this.exception = exception;
                                                                      }

        @Override
        public boolean isSuccess() {
                                 return false;
                                              }

        @Override
        public <U> Attempt<U> map(Function<T, U> f) {
            return failure(exception);
        }

        @Override
        public <U> U fold(Function<T, U> fSuccess, Function<Exception, U> fFailure) {
            return fFailure.apply(exception);
        }
    }
}
