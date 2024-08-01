package edu.ucsd.cse110.successorator.lib.domain;

import static org.junit.Assert.*;

import org.junit.Test;

public class RecurringTaskTest {
    @Test
    public void testEquals() {
        var t1 = new RecurringTask(5, "testing", 2005, 5, 5, "WEEKLY", "H");
        var t2 = new RecurringTask(6, "testing", 2006, 5, 5, "WEEKLY", "H");
        var t3 = new RecurringTask(5, "testing", 2005, 5, 5, "WEEKLY", "H");

        assertEquals(t1, t3);
        assertNotEquals(t1, t2);
    }
}