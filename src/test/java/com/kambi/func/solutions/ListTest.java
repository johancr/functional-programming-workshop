package com.kambi.func.solutions;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import java.util.function.Function;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

public class ListTest {

    @Rule
    public ExpectedException thrown = ExpectedException.none();

    @Test
    public void foldLeft() {

        List<Integer> list = List.list(1, 2, 3);

        int sum = list.foldLeft(0, acc -> head -> acc + head);

        assertThat(sum, is(6));
    }

    @Test
    public void map() {
        List<Integer> list = List.list(1, 2, 3);

        List<Integer> plusOne = list.map(x -> x + 1);

        assertThat(plusOne.toString(), is("2, 3, 4, EMPTY"));
    }

    @Test
    public void flatMap() {
        List<Integer> numbers = List.list(1, 2, 3);
        Function<Integer, List<Integer>> f = x -> List.list(x, x + 1);

        List<Integer> moreNumbers = numbers.flatMap(f);

        assertThat(moreNumbers.toString(), is("1, 2, 2, 3, 3, 4, EMPTY"));
    }

}
