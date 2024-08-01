package edu.ucsd.cse110.successorator.data.db;

import static org.junit.Assert.*;
import org.junit.Test;

import edu.ucsd.cse110.successorator.lib.domain.Task;

public class TaskEntityTest {
    @Test
    public void testFromTask() {
        var actual = TaskEntity.fromTask(new Task(5,"testing", 0, false, "H"));
        var expected = new TaskEntity("testing", 0, false, "H");
        expected.id = 5;
        assertEquals(expected, actual);
    }

    @Test
    public void testToTask() {
        TaskEntity testEntity = new TaskEntity("testing", 0, false, "H");
        var expected = new Task(null, "testing", 0, false, "H");
        var actual = testEntity.toTask();
        assertEquals(expected, actual);
    }

    @Test
    public void testEquals() {
        TaskEntity testEntity1 = new TaskEntity("testing", 0, false, "H");
        TaskEntity testEntity2 = new TaskEntity("testing", 0, false, "H");
        TaskEntity testEntity3 = new TaskEntity("testing", 0, true, "H");

        assertEquals(testEntity1, testEntity2);
        assertNotEquals(testEntity1, testEntity3);
    }
}