package edu.ucsd.cse110.successorator.lib.domain;

import static org.junit.Assert.*;
import org.junit.Test;

public class TaskTest {
    @Test
    public void testWithId() {
        var task = new Task(1, "mow the lawn", 0, false, "H");
        var expected = new Task(42, "mow the lawn", 0, false, "H");
        var actual = task.withId(42);
        assertEquals(expected, actual);
    }

    @Test
    public void testGetters() {
        var task = new Task(1, "mow the lawn", 0, false, "H");
        assertEquals(Integer.valueOf(1), task.id());
        assertEquals("mow the lawn", task.task_name());
        assertEquals(0, task.sortOrder());
    }

    @Test
    public void testWithSortOrder() {
        var task = new Task(1, "mow the lawn", 0, false, "H");
        var expected = new Task(1, "mow the lawn", 42, false, "H");
        var actual = task.withSortOrder(42);
        assertEquals(expected, actual);
    }

    @Test
    public void testEquals() {
        var task1 = new Task(1, "mow the lawn", 0, false, "H");
        var task2 = new Task(1, "mow the lawn", 0, false, "H");
        var task3 = new Task(2, "mow the lawn", 0, false, "H");
        assertEquals(task1, task2);
        assertNotEquals(task1, task3);
    }
}