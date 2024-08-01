package edu.ucsd.cse110.successorator.data.db;

import static org.junit.Assert.*;
import org.junit.Test;

import edu.ucsd.cse110.successorator.lib.domain.Task;

public class TTaskEntityTest {
    @Test
    public void testFromTask() {
        var actual = TTaskEntity.fromTask(new Task(5,"testing", 0, false, "H"));
        var expected = new TTaskEntity("testing", 0, false, "H");
        expected.id = 5;
        assertEquals(expected, actual);
    }

    @Test
    public void testToTask() {
        TTaskEntity testEntity = new TTaskEntity("testing", 0, false, "H");
        var expected = new Task(null, "testing", 0, false, "H");
        var actual = testEntity.toTask();
        assertEquals(expected, actual);
    }
    @Test
    public void testEquals() {
        TTaskEntity testEntity1 = new TTaskEntity("testing", 0, false, "H");
        TTaskEntity testEntity2 = new TTaskEntity("testing", 0, false, "H");
        TTaskEntity testEntity3 = new TTaskEntity("testing", 0, true, "H");

        assertEquals(testEntity1, testEntity2);
        assertNotEquals(testEntity1, testEntity3);
    }
}