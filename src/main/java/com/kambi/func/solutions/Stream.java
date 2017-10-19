package com.kambi.func.solutions;

import java.util.function.Function;
import java.util.function.Supplier;

import static com.kambi.func.solutions.List.list;

public abstract class Stream<T> {

    @SafeVarargs
    public static <T> Stream<T> of(T... args) {
        return of(list(args));
    }

    public static <T> Stream<T> of(List<T> list) {
        return list.isEmpty()
                ? Stream.empty()
                : cons(list.head(), () -> of(list.tail()));
    }

    public static <T> Stream<T> repeat(T value) {
        return cons(value, () -> repeat(value));
    }

    public static <T> Stream<T> iterate(T z, Function<T, T> f) {
        return cons(z, () -> iterate(f.apply(z), f));
    }

    public static <T> Stream<T> empty() {
        return new Empty<>();
    }

    public static <T> Stream<T> cons(T value, Supplier<Stream<T>> supplier) {
        return new Cons<>(value, supplier);
    }

    public abstract T head();

    public abstract Stream<T> tail();

    public abstract Stream<T> drop(int n);

    public abstract Stream<T> take(int n);

    public abstract List<T> toList();

    public abstract <U> Stream<U> map(Function<T, U> f);

    public abstract Stream<T> filter(Function<T, Boolean> p);

    public abstract Option<T> find(Function<T, Boolean> p);

    public abstract T foldRight(T z, Function<T, Function<T, T>> f);

    private static class Cons<T> extends Stream<T> {

        private final T head;
        private final Supplier<Stream<T>> tail;

        private Cons(T head, Supplier<Stream<T>> tail) {
            this.head = head;
            this.tail = tail;
        }

        @Override
        public T head() {
            return head;
        }

        @Override
        public Stream<T> tail() {
            return tail.get();
        }

        @Override
        public Stream<T> drop(int n) {
            return n > 0
                    ? tail().drop(n - 1)
                    : this;
        }

        @Override
        public Stream<T> take(int n) {
            return n < 1
                    ? empty()
                    : cons(head, () -> tail().take(n - 1));
        }

        @Override
        public List<T> toList() {
            return toList(list(head));
        }

        @Override
        public <U> Stream<U> map(Function<T, U> f) {
            return cons(f.apply(head), () -> tail().map(f));
        }

        @Override
        public Stream<T> filter(Function<T, Boolean> p) {
            return p.apply(head)
                    ? cons(head, () -> tail().filter(p))
                    : tail().filter(p);
        }

        @Override
        public Option<T> find(Function<T, Boolean> p) {
            return p.apply(head)
                    ? Option.some(head)
                    : tail().find(p);
        }

        @Override
        public T foldRight(T z, Function<T, Function<T, T>> f) {
            return tail().foldRight(f.apply(head).apply(z), f);
        }

        private List<T> toList(List<T> acc) {
            return acc.concat(tail().toList());
        }
    }

    private static class Empty<T> extends Stream<T> {

        @Override
        public T head() {
            throw new IllegalStateException("head() called on empty stream");
        }

        @Override
        public Stream<T> tail() {
            throw new IllegalStateException("tail() called on empty stream");
        }

        @Override
        public Stream<T> drop(int n) {
            return this;
        }

        @Override
        public Stream<T> take(int n) {
            throw new IllegalStateException("take() called on empty stream");
        }

        @Override
        public List<T> toList() {
            return list();
        }

        @Override
        public <U> Stream<U> map(Function<T, U> f) {
            return empty();
        }

        @Override
        public Stream<T> filter(Function<T, Boolean> p) {
            return empty();
        }

        @Override
        public Option<T> find(Function<T, Boolean> p) {
            return Option.none();
        }

        @Override
        public T foldRight(T z, Function<T, Function<T, T>> f) {
            return z;
        }
    }
}
