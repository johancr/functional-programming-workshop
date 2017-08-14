package com.kambi.func.solutions;

import org.junit.Test;

import java.util.function.Function;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

public class ValidationTest {

    @Test
    public void success() {
        Validation<String, String> v = Validation.success("a");

        assertThat(v.fold(ignore -> true, ignore -> false), is(true));
    }

    @Test
    public void failure() {
        Validation<String, String> v = Validation.failure("a");

        assertThat(v.fold(ignore -> true, ignore -> false), is(false));
    }

    @Test
    public void combiner() {
        Validation<String, String> s = Validation.success("a");
        Validation<String, String> f = Validation.failure("b");

        Validation<String, String> sf = Validation.map2(s, f, sa -> fa -> null);
        assertThat(sf.fold(ignore -> true, ignore -> false), is(false));
    }

    @Test
    public void sequence() {
        Validation<String, String> s1 = Validation.success("a");
        Validation<String, String> s2 = Validation.failure("b");
        Validation<String, String> s3 = Validation.failure("c");

        Validation<String, List<String>> seq = Validation.sequence(List.list(s1, s2, s3));
        List<String> v = seq.fold(value -> value, failure -> failure);

        assertThat(v.size(), is(2));
    }

    @Test
    public void ap() {
        Validation<String, Function<Integer, Integer>> fa = Validation.success(x -> x + 1);
        Validation<String, Integer> a = Validation.success(1);
        Validation<String, Integer> b = a.ap(fa);

        String x = b.fold(String::valueOf, j -> "");

        assertThat(x, is("2"));
    }

    @Test
    public void apap() {

        Validation<String, Integer> a1 = Validation.success(1);
        Validation<String, Integer> a2 = Validation.success(1);
        Validation<String, Integer> b = a2.ap(a1.ap((Validation.<String, Function<Integer, Function<Integer, Integer>>>success(x -> y -> x + y + 1))));

        String x = b.fold(String::valueOf, j -> "");

        assertThat(x, is("3"));
    }
}