package com.kambi.func.solutions;

import java.util.function.Function;

public abstract class List<T> {

    public abstract <U> List<U> map(Function<T, U> f);
    public abstract <U> U foldLeft(U identity, Function<U, Function<T, U>> f);

    public static <T> List<T> list() {
        return new Empty<>();
    }

    @SafeVarargs
    public static <T> List<T> list(T... args) {
        List<T> list = List.list();
        for (int i = args.length - 1; i >= 0; i--) {
            list = list.append(args[i]);
        }
        return list;
    }

    public List<T> append(T t) {
        return new Cons<>(t, this);
    }

    private static class Empty<T> extends List<T> {

        @Override
        public <U> List<U> map(Function<T, U> f) {
            return List.list();
        }

        @Override
        public <U> U foldLeft(U identity, Function<U, Function<T, U>> f) {
            return identity;
        }

        @Override
        public String toString() {
            return "EMPTY";
        }
    }

    private static class Cons<T> extends List<T> {

        private final T head;
        private final List<T> tail;

        private Cons(T head, List<T> tail) {
            this.head = head;
            this.tail = tail;
        }

        @Override
        public <U> List<U> map(Function<T, U> f) {
            return new Cons<>(f.apply(head), tail.map(f));
        }

        @Override
        public String toString() {
            return String.format("%s", head + ", " + tail.toString());
        }

        @Override
        public <U> U foldLeft(U identity, Function<U, Function<T, U>> f) {
            return tail.foldLeft(f.apply(identity).apply(head), f);
        }
    }
}
