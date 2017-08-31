package com.kambi.func.examples;

import org.junit.Test;

import java.util.Arrays;
import java.util.logging.Logger;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

public class LoggedTest {

    private final static Logger log = Logger.getLogger(LoggedTest.class.getName());

    @Test
    public void log() {
        int result = computation();

        assertThat(result, is(1));
    }

    private static int computation() {
        log.info("computing...");
        return 1;
    }

    @Test
    public void log_separated() {

        Logged result = Logged.log(pureComputation(), "computing...")
                .flatMap(value -> Logged.log(value * 2, "doubling..."));

        assertThat(result.getValue(), is(2));
        assertThat(result.getLog(), is(Arrays.asList("computing...", "doubling...")));
    }

    private static int pureComputation() {
        return 1;
    }
}