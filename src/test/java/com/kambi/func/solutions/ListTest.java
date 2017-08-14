package com.kambi.func.solutions;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

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

}
