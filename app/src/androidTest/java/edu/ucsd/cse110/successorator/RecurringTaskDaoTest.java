package edu.ucsd.cse110.successorator;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import androidx.room.Room;
import androidx.test.platform.app.InstrumentationRegistry;

import org.junit.Before;
import org.junit.Test;

import edu.ucsd.cse110.successorator.data.db.RecurringTaskDao;
import edu.ucsd.cse110.successorator.data.db.RecurringTaskEntity;
import edu.ucsd.cse110.successorator.data.db.SuccessaratorDatabase;
import edu.ucsd.cse110.successorator.lib.domain.RecurringTask;

public class RecurringTaskDaoTest {
    public SuccessoratorApplication app = (SuccessoratorApplication) InstrumentationRegistry.getInstrumentation().getTargetContext().getApplicationContext();
    public RecurringTaskDao recurringTaskDao;

    @Before
    public void setUp() {

        var database = Room.inMemoryDatabaseBuilder(
                        app,
                        SuccessaratorDatabase.class
                )
                .allowMainThreadQueries()
                .build();

        this.recurringTaskDao = database.RTaskDao();
        recurringTaskDao.clearAll();
    }

    @Test
    public void testInsertAndRetrieveTask() {
        RecurringTask t1 = new RecurringTask(1, "testing1", 2024, 3, 11, "DAILY", "S");
      
        RecurringTaskEntity task = RecurringTaskEntity.fromRTask(t1);
        recurringTaskDao.insert(task);

        RecurringTaskEntity retrievedTask = recurringTaskDao.find(1);

        assertEquals(task.task_name, retrievedTask.task_name);
        assertEquals(task.id, retrievedTask.id);
        assertEquals(task.created_year, retrievedTask.created_year);
        assertEquals(task.created_month, retrievedTask.created_month);
        assertEquals(task.created_day, retrievedTask.created_day);
        assertEquals(task.freq, retrievedTask.freq);
    }

    @Test
    public void testInsertAndRetrieveTaskByFrequency() {
        RecurringTask t1 = new RecurringTask(1, "testing1", 2024, 3, 11, "DAILY", "S");
        RecurringTask t2 = new RecurringTask(2, "testing2", 2024, 3, 11, "WEEKLY", "S");
        RecurringTask t3 = new RecurringTask(3, "testing3", 2024, 3, 11, "MONTHLY", "S");
        RecurringTask t4 = new RecurringTask(4, "testing4", 2024, 3, 11, "YEARLY", "S");

        RecurringTaskEntity task1 = RecurringTaskEntity.fromRTask(t1);
        RecurringTaskEntity task2 = RecurringTaskEntity.fromRTask(t2);
        RecurringTaskEntity task3 = RecurringTaskEntity.fromRTask(t3);
        RecurringTaskEntity task4 = RecurringTaskEntity.fromRTask(t4);
        recurringTaskDao.insert(task1);
        recurringTaskDao.insert(task2);
        recurringTaskDao.insert(task3);
        recurringTaskDao.insert(task4);

        assertTrue(recurringTaskDao.findDaily().contains(task1));
        assertTrue(recurringTaskDao.findWeekly(1).contains(task2));
        assertTrue(recurringTaskDao.findMonthly(1, 2).contains(task3));
        assertTrue(recurringTaskDao.findYearly(3, 11).contains(task4));
    }
}
