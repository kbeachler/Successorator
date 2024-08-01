package edu.ucsd.cse110.successorator;

import androidx.test.platform.app.InstrumentationRegistry;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import androidx.room.Room;

import edu.ucsd.cse110.successorator.data.db.SuccessaratorDatabase;
import edu.ucsd.cse110.successorator.data.db.TTaskDao;
import edu.ucsd.cse110.successorator.data.db.TTaskEntity;
import edu.ucsd.cse110.successorator.lib.domain.Task;

public class TTaskDaoTest {
    public SuccessoratorApplication app = (SuccessoratorApplication) InstrumentationRegistry.getInstrumentation().getTargetContext().getApplicationContext();
    public TTaskDao tTaskDao;

    @Before
    public void setUp() {

        var database = Room.inMemoryDatabaseBuilder(
                        app,
                        SuccessaratorDatabase.class
                )
                .allowMainThreadQueries()
                .build();

        this.tTaskDao = database.tTaskDao();
        tTaskDao.clearAll();
    }

    @Test
    public void testInsertAndRetrieveTask() {
        Task t1 = new Task(1, "testing1", 0, false, "S");

        TTaskEntity task = TTaskEntity.fromTask(t1);
        tTaskDao.insert(task);

        TTaskEntity retrievedTask = tTaskDao.find(1);

        assertEquals(task.task_name, retrievedTask.task_name);
        assertEquals(task.id, retrievedTask.id);
        assertEquals(task.sortOrder, retrievedTask.sortOrder);
        assertEquals(task.marked, retrievedTask.marked);
    }

    @Test
    public void testGetMinSortOrder() {
        TTaskEntity task1 = TTaskEntity.fromTask(new Task(1, "Task 1", 2, false, "S"));
        TTaskEntity task2 = TTaskEntity.fromTask(new Task(2, "Task 2", 3, true, "S"));
        TTaskEntity task3 = TTaskEntity.fromTask(new Task(3, "Task 3", 1, true, "S"));

        tTaskDao.insert(task1);
        tTaskDao.insert(task2);
        tTaskDao.insert(task3);

        int minSortOrder = tTaskDao.getMinSortOrder();

        assertEquals(1, minSortOrder);
    }

    @Test
    public void testGetMaxSortOrder() {
        TTaskEntity task1 = TTaskEntity.fromTask(new Task(1, "Task 1", 2, false, "S"));
        TTaskEntity task2 = TTaskEntity.fromTask(new Task(2, "Task 2", 3, true, "S"));
        TTaskEntity task3 = TTaskEntity.fromTask(new Task(3, "Task 3", 1, true, "S"));

        tTaskDao.insert(task1);
        tTaskDao.insert(task2);
        tTaskDao.insert(task3);

        int maxSortOrder = tTaskDao.getMaxSortOrder();

        assertEquals(3, maxSortOrder);
    }

    @Test
    public void testSmallestMarked() {
        TTaskEntity task1 = TTaskEntity.fromTask(new Task(1, "Task 1", 2, false, "S"));
        TTaskEntity task2 = TTaskEntity.fromTask(new Task(2, "Task 2", 3, true, "S"));
        TTaskEntity task3 = TTaskEntity.fromTask(new Task(3, "Task 3", 1, false, "S"));
      
        tTaskDao.insert(task1);
        tTaskDao.insert(task2);
        tTaskDao.insert(task3);

        int smallestMarked = tTaskDao.smallestMarked();

        assertEquals(3, smallestMarked);
    }

    @Test
    public void testLargestUnmarked() {
        TTaskEntity task1 = TTaskEntity.fromTask(new Task(1, "Task 1", 2, false, "S"));
        TTaskEntity task2 = TTaskEntity.fromTask(new Task(2, "Task 2", 3, true, "S"));
        TTaskEntity task3 = TTaskEntity.fromTask(new Task(3, "Task 3", 1, false, "S"));

        tTaskDao.insert(task1);
        tTaskDao.insert(task2);
        tTaskDao.insert(task3);

        int largestUnmarked = tTaskDao.largestUnmarked();

        assertEquals(2, largestUnmarked);
    }


    @Test
    public void testToggleMarked() {
        TTaskEntity task1 = TTaskEntity.fromTask(new Task(1, "Task 1", 1, false, "S"));

        tTaskDao.insert(task1);

        tTaskDao.toggleMarked(task1.id);

        assertEquals(true, tTaskDao.getMarked(task1.id));
    }

    @Test
    public void testChangeSortOrder() {
        TTaskEntity task1 = TTaskEntity.fromTask(new Task(1, "Task 1", 1, false, "S"));

        tTaskDao.insert(task1);

        int newSortOrder = 5;
        tTaskDao.changeSortOrder(task1.id, newSortOrder);

        TTaskEntity retrievedTask = tTaskDao.find(task1.id);
        assertEquals(newSortOrder, retrievedTask.sortOrder);
    }

    @Test
    public void testIncrementAllMarked() {
        TTaskEntity task1 = TTaskEntity.fromTask(new Task(1, "Task 1", 1, true, "S"));
        TTaskEntity task2 = TTaskEntity.fromTask(new Task(2, "Task 2", 2, true, "S"));
        TTaskEntity task3 = TTaskEntity.fromTask(new Task(3, "Task 3", 3, false, "S"));

        tTaskDao.insert(task1);
        tTaskDao.insert(task2);
        tTaskDao.insert(task3);

        tTaskDao.incrementAllMarked();

        TTaskEntity retrievedTask1 = tTaskDao.find(task1.id);
        TTaskEntity retrievedTask2 = tTaskDao.find(task2.id);

        assertEquals(task1.sortOrder + 1, retrievedTask1.sortOrder);
        assertEquals(task2.sortOrder + 1, retrievedTask2.sortOrder);
    }

    @Test
    public void testShiftSortOrders() {
        TTaskEntity task1 = TTaskEntity.fromTask(new Task(1, "Task 1", 1, true, "S"));
        TTaskEntity task2 = TTaskEntity.fromTask(new Task(2, "Task 2", 2, true, "S"));
        TTaskEntity task3 = TTaskEntity.fromTask(new Task(3, "Task 3", 3, true, "S"));

        tTaskDao.insert(task1);
        tTaskDao.insert(task2);
        tTaskDao.insert(task3);

        int from = 1;
        int to = 3;
        int by = 2;

        tTaskDao.shiftSortOrders(from, to, by);

        TTaskEntity retrievedTask1 = tTaskDao.find(task1.id);
        TTaskEntity retrievedTask2 = tTaskDao.find(task2.id);
        TTaskEntity retrievedTask3 = tTaskDao.find(task3.id);

        assertEquals(task1.sortOrder + by, retrievedTask1.sortOrder);
        assertEquals(task2.sortOrder + by, retrievedTask2.sortOrder);
        assertEquals(task3.sortOrder + by, retrievedTask3.sortOrder);
    }

    @Test
    public void testAppend() {
        TTaskEntity task1 = TTaskEntity.fromTask(new Task(1, "Task 1", 1, false, "S"));
        TTaskEntity task2 = TTaskEntity.fromTask(new Task(2, "Task 2", 2, false, "S"));
        TTaskEntity task3 = TTaskEntity.fromTask(new Task(3, "Task 3", 3, true, "S"));
      
        tTaskDao.insert(task1);
        tTaskDao.insert(task2);
        tTaskDao.insert(task3);

        TTaskEntity newTask = TTaskEntity.fromTask(new Task(4, "Task 4", 0, false, "S"));

        tTaskDao.append(newTask);

        TTaskEntity appendedTask = tTaskDao.find(4);
        assertEquals(3, appendedTask.sortOrder);
    }


    @Test
    public void testSetMarked() {
        TTaskEntity task2 = TTaskEntity.fromTask(new Task(2, "Task 2", 2, false, "S"));


        tTaskDao.insert(task2);

        tTaskDao.setMarked(2);

        boolean markedStatus = tTaskDao.getMarked(2);
        assertTrue(markedStatus);
    }
}
