package com.kambi.func.solutions;


import org.junit.Test;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

public class OptionTest {

    @Test
    public void map() {

        Option<Integer> some = Option.some(1);
        Option<Integer> none = Option.none();

        Option<Integer> somePlusOne = some.map(x -> x + 1);
        Option<Integer> nonePlusOne = none.map(x -> x + 1);

        assertThat(somePlusOne.getOrElse(0), is(2));
        assertThat(nonePlusOne.getOrElse(0), is(0));
    }

    @Test
    public void flatMap() {

        Option<Integer> some = Option.some(1);
        Option<Integer> fmappedSome = some.flatMap(x -> x > 0 ? Option.some(x + 1) : Option.none());
        Option<Integer> fmappedNone = some.flatMap(x -> x < 0 ? Option.some(x + 1) : Option.none());

        assertThat(fmappedSome.getOrElse(0), is(2));
        assertThat(fmappedNone.getOrElse(0), is(0));
    }
}
