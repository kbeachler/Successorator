package edu.ucsd.cse110.successorator.data.db;

import static org.junit.Assert.*;
import org.junit.Test;

import edu.ucsd.cse110.successorator.lib.domain.PendingTask;
import edu.ucsd.cse110.successorator.lib.domain.Task;

public class PendingTaskEntityTest {
    @Test
    public void testFromPTask() {
        var actual = PendingTaskEntity.fromTask(new Task(5,"testing", 0, false, "H"));
        var expected = new PendingTaskEntity("testing", 0, false, "H");
        expected.id = 5;
        assertEquals(expected, actual);
    }

    @Test
    public void testToPTask() {
        PendingTaskEntity testEntity = new PendingTaskEntity("testing", 0, false, "H");
        var expected = new PendingTask(null, "testing", 0, false, "H");
        var actual = testEntity.toPTask();
        assertEquals(expected, actual);
    }

    @Test
    public void testEquals() {
        PendingTaskEntity testEntity1 = new PendingTaskEntity("testing", 0, false, "H");
        PendingTaskEntity testEntity2 = new PendingTaskEntity("testing", 0, false, "H");
        PendingTaskEntity testEntity3 = new PendingTaskEntity("testing2", 0, false, "H");

        assertEquals(testEntity1, testEntity2);
        assertNotEquals(testEntity1, testEntity3);
    }
}