package com.kambi.func.solutions;

import com.kambi.func.examples.SideEffect;

import java.util.function.Function;
import java.util.function.Supplier;

public abstract class Attempt<T> {

    public static <T> Attempt<T> attempt(Supplier<T> operation) {

        try
        {
            return Attempt.success(operation.get());
        }
        catch (Exception ex)
        {
            return Attempt.failure(ex);
        }
    }

    public static <T> Attempt<T> success(T value) {
        return new Success<>(value);
    }

    public static <T> Attempt<T> failure(Exception ex) {
        return new Failure<>(ex.getMessage(), ex);
    }

    public static <T> Attempt<T> failure(String message, Exception ex) {
        return new Failure<>(message, ex);
    }

    public abstract boolean isSuccess();

    public abstract <U> Attempt<U> map(Function<T, U> f);

    public abstract <U> Attempt<U> flatMap(Function<T, Attempt<U>> f);

    public abstract <U> U fold(Function<T, U> fSuccess, Function<Exception, U> fFailure);

    public abstract Attempt<T> mapFailure(String message);

    public abstract void forEach(SideEffect<T> onSuccess, SideEffect<String> onFailure);

    public abstract void forEachOrThrow(SideEffect<T> onSuccess);

    public static <T, U> Function<Attempt<T>, Attempt<U>> lift(Function<T, U> f) {
        return x -> x.map(f);
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
            try
            {
                return success(f.apply(value));
            }
            catch (Exception ex)
            {
                return failure(ex);
            }
        }

        @Override
        public <U> Attempt<U> flatMap(Function<T, Attempt<U>> f) {
            return f.apply(value);
        }

        @Override
        public <U> U fold(Function<T, U> fSuccess, Function<Exception, U> fFailure) {
            return fSuccess.apply(value);
        }

        @Override
        public Attempt<T> mapFailure(String message) {
            return this;
        }

        @Override
        public void forEach(SideEffect<T> onSuccess, SideEffect<String> onFailure) {
            onSuccess.run(value);
        }

        @Override
        public void forEachOrThrow(SideEffect<T> onSuccess) {
            onSuccess.run(value);
        }
    }

    private static class Failure<T> extends Attempt<T> {
        private final String message;
        private final Exception exception;

        private Failure(String message, Exception exception) {
            this.message = message;
            this.exception = exception;
        }

        @Override
        public boolean isSuccess() {
            return false;
        }

        @Override
        public <U> Attempt<U> map(Function<T, U> f) {
            return failure(message, exception);
        }

        @Override
        public <U> Attempt<U> flatMap(Function<T, Attempt<U>> f) {
            return failure(message, exception);
        }

        @Override
        public <U> U fold(Function<T, U> fSuccess, Function<Exception, U> fFailure) {
            return fFailure.apply(exception);
        }

        @Override
        public Attempt<T> mapFailure(String message) {
            return failure(message, exception);
        }

        @Override
        public void forEach(SideEffect<T> onSuccess, SideEffect<String> onFailure) {
            onFailure.run(message);
        }

        @Override
        public void forEachOrThrow(SideEffect<T> onSuccess) {
            throw new IllegalStateException(exception);
        }
    }
}
