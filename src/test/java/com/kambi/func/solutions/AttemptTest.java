package com.kambi.func.solutions;

import org.junit.Test;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

public class AttemptTest {

    @Test
    public void exception() {

        int result;

        try
        {
            result = 1 / 0;
        }
        catch (ArithmeticException ex)
        {
            result = 0;
        }

        assertThat(result, is(0));
    }

    @Test
    public void exceptions_success() {
        Attempt<Integer> attempt = Attempt.attempt(() -> divide(1, 1));

        assertThat(attempt.isSuccess(), is(true));
    }

    private static int divide(int a, int b) {
        return a / b;
    }

    @Test
    public void exceptions_failure() {
        Attempt<Integer> attempt = Attempt.attempt(() -> divide(1, 0));

        assertThat(attempt.isSuccess(), is(false));
    }
}
