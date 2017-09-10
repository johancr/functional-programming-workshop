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

    public static <T, U> Function<Attempt<T>, Attempt<U>> lift(Function<T, U> f) {
        return x -> x.map(f);
    }

    public abstract <U> Attempt<U> map(Function<T, U> f);

    public static <T, T2, T3> Attempt<T3> map2(Attempt<T> a, Attempt<T2> b, Function<T, Function<T2, T3>> f) {
        return b.ap(a.ap(success(v1 -> v2 -> f.apply(v1).apply(v2))));
    }

    public abstract <U> Attempt<U> ap(Attempt<Function<T, U>> f);

    public static <T, T2, T3, T4> Attempt<T4> map3(Attempt<T> a, Attempt<T2> b, Attempt<T3> c,
                                                   Function<T, Function<T2, Function<T3, T4>>> f) {
        return c.ap(b.ap(a.ap(success(v1 -> v2 -> v3 -> f.apply(v1).apply(v2).apply(v3)))));
    }

    public static <T, T2, T3, T4, T5> Attempt<T5> map4(Attempt<T> a, Attempt<T2> b, Attempt<T3> c, Attempt<T4> d,
                                                       Function<T, Function<T2, Function<T3, Function<T4, T5>>>> f) {
        return d.ap(c.ap(b.ap(a.ap(success(v1 -> v2 -> v3 -> v4 -> f.apply(v1).apply(v2).apply(v3).apply(v4))))));
    }

    public abstract <U> Attempt<U> flatMap(Function<T, Attempt<U>> f);

    public abstract boolean isSuccess();

    public abstract <U> U fold(Function<T, U> fSuccess, Function<Exception, U> fFailure);

    public abstract Attempt<T> mapFailure(String message);

    public abstract void forEach(SideEffect<T> onSuccess, SideEffect<String> onFailure);

    public abstract void forEachOrThrow(SideEffect<T> onSuccess);

    private static class Success<T> extends Attempt<T> {
        private final T value;

        private Success(T value) {
            this.value = value;
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
        public <U> Attempt<U> ap(Attempt<Function<T, U>> f) {
            return f.fold(fs -> success(fs.apply(value)),
                    Attempt::failure);
        }

        @Override
        public boolean isSuccess() {
            return true;
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
        public <U> Attempt<U> ap(Attempt<Function<T, U>> f) {
            return failure(message, exception);
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
