package edu.ucsd.cse110.successorator.lib.domain;

import static org.junit.Assert.assertNotEquals;

import junit.framework.TestCase;

public class PendingTaskTest extends TestCase {

    public void testWithId() {
        var task = new PendingTask(1, "mow the lawn", 0, false, "H");
        var expected = new PendingTask(42, "mow the lawn", 0, false, "H");
        var actual = task.withId(42);
        assertEquals(expected, actual);
    }

    public void testTestEquals() {
        var t1 = new PendingTask(5, "testing", 2005, false, "H");
        var t2 = new PendingTask(6, "testing", 2006, false, "H");
        var t3 = new PendingTask(5, "testing", 2005, false, "H");

        assertEquals(t1, t3);
        assertNotEquals(t1, t2);
    }
}