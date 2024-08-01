package edu.ucsd.cse110.successorator;

import static org.junit.Assert.assertEquals;

import androidx.room.Room;
import androidx.test.platform.app.InstrumentationRegistry;

import org.junit.Before;
import org.junit.Test;

import edu.ucsd.cse110.successorator.data.db.SuccessaratorDatabase;
import edu.ucsd.cse110.successorator.data.db.PendingTaskDao;
import edu.ucsd.cse110.successorator.data.db.PendingTaskEntity;
import edu.ucsd.cse110.successorator.lib.domain.Task;

public class PendingTaskDaoTest {
    public SuccessoratorApplication app = (SuccessoratorApplication) InstrumentationRegistry.getInstrumentation().getTargetContext().getApplicationContext();
    public PendingTaskDao pendingTaskDao;

    @Before
    public void setUp() {

        var database = Room.inMemoryDatabaseBuilder(
                        app,
                        SuccessaratorDatabase.class
                )
                .allowMainThreadQueries()
                .build();

        this.pendingTaskDao = database.pTaskDao();
        pendingTaskDao.clearAll();
    }

    @Test
    public void testInsertAndRetrieveTask() {
        Task t1 = new Task(1, "testing1", 0, false, "S");
      
        PendingTaskEntity task = PendingTaskEntity.fromTask(t1);
        pendingTaskDao.insert(task);

        PendingTaskEntity retrievedTask = pendingTaskDao.find(1);

        assertEquals(task.task_name, retrievedTask.task_name);
        assertEquals(task.id, retrievedTask.id);
        assertEquals(task.sortOrder, retrievedTask.sortOrder);
        assertEquals(task.marked, retrievedTask.marked);
    }

    @Test
    public void testGetMinSortOrder() {
        PendingTaskEntity task1 = PendingTaskEntity.fromTask(new Task(1, "Task 1", 2, false, "S"));
        PendingTaskEntity task2 = PendingTaskEntity.fromTask(new Task(2, "Task 2", 3, true, "S"));
        PendingTaskEntity task3 = PendingTaskEntity.fromTask(new Task(3, "Task 3", 1, true, "S"));

        pendingTaskDao.insert(task1);
        pendingTaskDao.insert(task2);
        pendingTaskDao.insert(task3);

        int minSortOrder = pendingTaskDao.getMinSortOrder();

        assertEquals(1, minSortOrder);
    }

    @Test
    public void testGetMaxSortOrder() {
        PendingTaskEntity task1 = PendingTaskEntity.fromTask(new Task(1, "Task 1", 2, false, "S"));
        PendingTaskEntity task2 = PendingTaskEntity.fromTask(new Task(2, "Task 2", 3, true, "S"));
        PendingTaskEntity task3 = PendingTaskEntity.fromTask(new Task(3, "Task 3", 1, true, "S"));

        pendingTaskDao.insert(task1);
        pendingTaskDao.insert(task2);
        pendingTaskDao.insert(task3);

        int maxSortOrder = pendingTaskDao.getMaxSortOrder();

        assertEquals(3, maxSortOrder);
    }

    @Test
    public void testSmallestMarked() {
        PendingTaskEntity task1 = PendingTaskEntity.fromTask(new Task(1, "Task 1", 2, false, "S"));
        PendingTaskEntity task2 = PendingTaskEntity.fromTask(new Task(2, "Task 2", 3, true, "S"));
        PendingTaskEntity task3 = PendingTaskEntity.fromTask(new Task(3, "Task 3", 1, false, "S"));

        pendingTaskDao.insert(task1);
        pendingTaskDao.insert(task2);
        pendingTaskDao.insert(task3);

        int smallestMarked = pendingTaskDao.smallestMarked();

        assertEquals(3, smallestMarked);
    }

    @Test
    public void testLargestUnmarked() {
        PendingTaskEntity task1 = PendingTaskEntity.fromTask(new Task(1, "Task 1", 2, false, "S"));
        PendingTaskEntity task2 = PendingTaskEntity.fromTask(new Task(2, "Task 2", 3, true, "S"));
        PendingTaskEntity task3 = PendingTaskEntity.fromTask(new Task(3, "Task 3", 1, false, "S"));

        pendingTaskDao.insert(task1);
        pendingTaskDao.insert(task2);
        pendingTaskDao.insert(task3);

        int largestUnmarked = pendingTaskDao.largestUnmarked();

        assertEquals(2, largestUnmarked);
    }


    @Test
    public void testChangeSortOrder() {
        PendingTaskEntity task1 = PendingTaskEntity.fromTask(new Task(1, "Task 1", 1, false, "S"));

        pendingTaskDao.insert(task1);

        int newSortOrder = 5;
        pendingTaskDao.changeSortOrder(task1.id, newSortOrder);

        PendingTaskEntity retrievedTask = pendingTaskDao.find(task1.id);
        assertEquals(newSortOrder, retrievedTask.sortOrder);
    }

    @Test
    public void testIncrementAllMarked() {
        PendingTaskEntity task1 = PendingTaskEntity.fromTask(new Task(1, "Task 1", 1, true, "S"));
        PendingTaskEntity task2 = PendingTaskEntity.fromTask(new Task(2, "Task 2", 2, true, "S"));
        PendingTaskEntity task3 = PendingTaskEntity.fromTask(new Task(3, "Task 3", 3, false, "S"));

        pendingTaskDao.insert(task1);
        pendingTaskDao.insert(task2);
        pendingTaskDao.insert(task3);

        pendingTaskDao.incrementAllMarked();

        PendingTaskEntity retrievedTask1 = pendingTaskDao.find(task1.id);
        PendingTaskEntity retrievedTask2 = pendingTaskDao.find(task2.id);

        assertEquals(task1.sortOrder + 1, retrievedTask1.sortOrder);
        assertEquals(task2.sortOrder + 1, retrievedTask2.sortOrder);
    }

    @Test
    public void testShiftSortOrders() {
        PendingTaskEntity task1 = PendingTaskEntity.fromTask(new Task(1, "Task 1", 1, true, "S"));
        PendingTaskEntity task2 = PendingTaskEntity.fromTask(new Task(2, "Task 2", 2, true, "S"));
        PendingTaskEntity task3 = PendingTaskEntity.fromTask(new Task(3, "Task 3", 3, true, "S"));

        pendingTaskDao.insert(task1);
        pendingTaskDao.insert(task2);
        pendingTaskDao.insert(task3);

        int from = 1;
        int to = 3;
        int by = 2;

        pendingTaskDao.shiftSortOrders(from, to, by);

        PendingTaskEntity retrievedTask1 = pendingTaskDao.find(task1.id);
        PendingTaskEntity retrievedTask2 = pendingTaskDao.find(task2.id);
        PendingTaskEntity retrievedTask3 = pendingTaskDao.find(task3.id);

        assertEquals(task1.sortOrder + by, retrievedTask1.sortOrder);
        assertEquals(task2.sortOrder + by, retrievedTask2.sortOrder);
        assertEquals(task3.sortOrder + by, retrievedTask3.sortOrder);
    }

    @Test
    public void testAppend() {
        PendingTaskEntity task1 = PendingTaskEntity.fromTask(new Task(1, "Task 1", 1, false, "S"));
        PendingTaskEntity task2 = PendingTaskEntity.fromTask(new Task(2, "Task 2", 2, false, "S"));
        PendingTaskEntity task3 = PendingTaskEntity.fromTask(new Task(3, "Task 3", 3, true, "S"));

        pendingTaskDao.insert(task1);
        pendingTaskDao.insert(task2);
        pendingTaskDao.insert(task3);

        PendingTaskEntity newTask = PendingTaskEntity.fromTask(new Task(4, "Task 4", 0, false, "S"));
      
        pendingTaskDao.append(newTask);

        PendingTaskEntity appendedTask = pendingTaskDao.find(4);
        assertEquals(3, appendedTask.sortOrder);
    }

}
