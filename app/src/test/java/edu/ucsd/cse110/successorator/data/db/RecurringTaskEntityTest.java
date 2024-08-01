package edu.ucsd.cse110.successorator.data.db;

import static org.junit.Assert.*;

import org.junit.Test;

import edu.ucsd.cse110.successorator.lib.domain.RecurringTask;

public class RecurringTaskEntityTest {
    @Test
    public void fromRTask() {
        var actual = RecurringTaskEntity.fromRTask(new RecurringTask(5, "testing", 2005, 5, 5, "WEEKLY", "H"));
        var expected = new RecurringTaskEntity("testing", 2005, 5, 5, "WEEKLY", "H");
        assertEquals(expected, actual);
    }

    @Test
    public void toRTask() {
        RecurringTaskEntity testREntity = new RecurringTaskEntity("testing", 2005, 5, 5, "WEEKLY", "H");
        var expected = new RecurringTask(5, "testing", 2005, 5, 5, "WEEKLY", "H");
        var actual = testREntity.toRTask();
        assertEquals(expected, actual);
    }

    @Test
    public void DayOfWeek() {
        RecurringTaskEntity testREntity = new RecurringTaskEntity("testing", 2024, 3, 7, "WEEKLY", "H");
        var expected = 4 % 7;
        var actual = testREntity.day_of_week;
        assertEquals(expected, actual);
    }

    @Test
    public void WeekOfMonth() {
        RecurringTaskEntity testREntity = new RecurringTaskEntity("testing", 2024, 3, 7, "WEEKLY", "H");
        var expected = (7 - 1) / 7 + 1;
        var actual = testREntity.week_of_month;
        assertEquals(expected, actual);
    }

    @Test
    public void testEquals() {
        RecurringTaskEntity testEntity1 = new RecurringTaskEntity("testing", 2005, 5,5, "WEEKLY", "H");
        RecurringTaskEntity testEntity2 = new RecurringTaskEntity("testing1", 2006,6, 6, "WEEKLY", "H");
        RecurringTaskEntity testEntity3 = new RecurringTaskEntity("testing", 2005,5, 5, "WEEKLY", "H");

        assertEquals(testEntity1, testEntity3);
        assertNotEquals(testEntity1, testEntity2);
    }

}