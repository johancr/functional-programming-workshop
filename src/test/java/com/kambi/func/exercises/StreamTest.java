package com.kambi.func.exercises;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

public class StreamTest {

    @Rule
    public ExpectedException thrown = ExpectedException.none();

    /*
    @Test
    public void empty() {
        Stream<Integer> s = Stream.of();

        thrown.expect(IllegalStateException.class);
        s.head();
    }

    /*
    @Test
    public void singleItem() {
        Stream<Integer> s = Stream.of(1);

        assertThat(s.head(), is(1));
    }

    @Test
    public void multipleItems() {
        Stream<Integer> s = Stream.of(1, 2, 3);

        assertThat(s.head(), is(1));
        assertThat(s.tail().head(), is(2));
        assertThat(s.tail().tail().head(), is(3));
    }

    @Test
    public void repeat() {
        Stream<Integer> s = Stream.repeat(1);

        assertThat(s.head(), is(1));
        assertThat(s.tail().head(), is(1));
        assertThat(s.tail().tail().head(), is(1));
    }

    @Test
    public void iterate() {
        Stream<Integer> s = Stream.iterate(1, x -> x + 1);

        assertThat(s.head(), is(1));
        assertThat(s.tail().head(), is(2));
        assertThat(s.tail().tail().head(), is(3));
    }

    @Test
    public void drop() {
        Stream<Integer> s = Stream.iterate(1, x -> x + 1).drop(2);

        assertThat(s.head(), is(3));
        assertThat(s.tail().head(), is(4));
        assertThat(s.tail().tail().head(), is(5));
    }

    @Test
    public void take() {
        Stream<Integer> s = Stream.iterate(1, x -> x + 1).take(2);

        assertThat(s.toList().toString(), is(List.list(1, 2).toString()));
    }

    @Test
    public void map() {
        Stream<Integer> s = Stream.of(1, 2).map(x -> x + 1);

        assertThat(s.head(), is(2));
        assertThat(s.tail().head(), is(3));
    }

    @Test
    public void filter() {
        Stream<Integer> s = Stream.of(1, 2).filter(x -> x == 2);

        assertThat(s.head(), is(2));
    }

    @Test
    public void find() {
        Stream<Integer> s = Stream.of(1, 2);

        assertThat(s.find(x -> x == 2).isPresent(), is(true));
        assertThat(s.find(x -> x == 3).isPresent(), is(false));
    }

    @Test
    public void lazy() {
        Stream<Integer> s = Stream.iterate(0, x -> {
            throw new RuntimeException("Boom!");
        });

        assertThat(s.take(1).head(), is(0));
        assertThat(s.take(2).filter(x -> x == 0).map(x -> x + 1).head(), is(1));

        thrown.expect(RuntimeException.class);
        assertThat(s.take(2).filter(x -> x == 0).map(x -> x + 1).tail().head(), is(1));
    }

    @Test
    public void foldRight() {
        Stream<Integer> s = Stream.repeat(1);

        assertThat(s.take(3).foldRight(0, x -> acc -> x + acc), is(3));
    }
    //*/
}
