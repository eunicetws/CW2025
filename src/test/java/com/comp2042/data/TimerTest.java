package com.comp2042.data;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
class TimerTest {

    @Test
    void testPauseStopsCountdown() throws Exception {
        Timer.setTimer(1); // 60 seconds

        Timer.start();
        Thread.sleep(2000); // allow 2 second to tick
        int before = Timer.getRemainingSeconds();
        Timer.pause(true);  // pause timer
        Thread.sleep(2000);
        int after = Timer.getRemainingSeconds();

        assertEquals(before, after, "Timer should not count while paused");

        Timer.pause(false);  // resume timer
        Thread.sleep(2000); // allow 2 seconds to tick
        int after2 = Timer.getRemainingSeconds();

        assertEquals(before - 2, after2, "Timer should count down by 2 seconds after resuming");

    }

}