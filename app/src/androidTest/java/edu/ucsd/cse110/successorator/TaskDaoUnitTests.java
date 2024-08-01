package edu.ucsd.cse110.successorator;
import androidx.test.platform.app.InstrumentationRegistry;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import androidx.room.Room;

import edu.ucsd.cse110.successorator.data.db.SuccessaratorDatabase;
import edu.ucsd.cse110.successorator.data.db.TaskDao;
import edu.ucsd.cse110.successorator.data.db.TaskEntity;
import edu.ucsd.cse110.successorator.lib.domain.Task;

public class TaskDaoUnitTests {
    public SuccessoratorApplication app = (SuccessoratorApplication) InstrumentationRegistry.getInstrumentation().getTargetContext().getApplicationContext();
    public TaskDao taskDao;

    @Before
    public void setUp(){

        var database = Room.inMemoryDatabaseBuilder(
                        app,
                        SuccessaratorDatabase.class
                )
                .allowMainThreadQueries()
                .build();

        this.taskDao = database.taskDao();
        taskDao.clearAll();
    }


    @Test
    public void testInsertAndRetrieveTask() {
        Task t1 = new Task(1, "testing1", 0, false, "H");
        TaskEntity task = TaskEntity.fromTask(t1);
        taskDao.insert(task);

        TaskEntity retrievedTask = taskDao.find(1);

        assertEquals(task.task_name, retrievedTask.task_name);
        assertEquals(task.id, retrievedTask.id);
        assertEquals(task.sortOrder, retrievedTask.sortOrder);
        assertEquals(task.marked, retrievedTask.marked);
        assertEquals(task.context, retrievedTask.context);
    }

    @Test
    public void testGetMinSortOrder() {
        TaskEntity task1 = TaskEntity.fromTask(new Task(1, "Task 1", 2, false, "H"));
        TaskEntity task2 = TaskEntity.fromTask(new Task(2, "Task 2", 3, true, "H"));
        TaskEntity task3 = TaskEntity.fromTask(new Task(3, "Task 3", 1, true,"H"));

        taskDao.insert(task1);
        taskDao.insert(task2);
        taskDao.insert(task3);

        int minSortOrder = taskDao.getMinSortOrder();

        assertEquals(1, minSortOrder);
    }

    @Test
    public void testGetMaxSortOrder() {
        TaskEntity task1 = TaskEntity.fromTask(new Task(1, "Task 1", 2, false, "H"));
        TaskEntity task2 = TaskEntity.fromTask(new Task(2, "Task 2", 3, true, "H"));
        TaskEntity task3 = TaskEntity.fromTask(new Task(3, "Task 3", 1, true, "H"));

        taskDao.insert(task1);
        taskDao.insert(task2);
        taskDao.insert(task3);

        int maxSortOrder = taskDao.getMaxSortOrder();

        assertEquals(3, maxSortOrder);
    }

    @Test
    public void testSmallestMarked() {
        TaskEntity task1 = TaskEntity.fromTask(new Task(1, "Task 1", 2, false, "H"));
        TaskEntity task2 = TaskEntity.fromTask(new Task(2, "Task 2", 3, true, "H"));
        TaskEntity task3 = TaskEntity.fromTask(new Task(3, "Task 3", 1, false, "H"));

        taskDao.insert(task1);
        taskDao.insert(task2);
        taskDao.insert(task3);

        int smallestMarked = taskDao.smallestMarked();

        assertEquals(3, smallestMarked);
    }

    @Test
    public void testLargestUnmarked() {
        TaskEntity task1 = TaskEntity.fromTask(new Task(1, "Task 1", 2, false, "H"));
        TaskEntity task2 = TaskEntity.fromTask(new Task(2, "Task 2", 3, true, "H"));
        TaskEntity task3 = TaskEntity.fromTask(new Task(3, "Task 3", 1, false, "H"));

        taskDao.insert(task1);
        taskDao.insert(task2);
        taskDao.insert(task3);

        int largestUnmarked = taskDao.largestUnmarked();

        assertEquals(2, largestUnmarked);
    }


    @Test
    public void testToggleMarked() {
        TaskEntity task1 = TaskEntity.fromTask(new Task(1, "Task 1", 1, false, "H"));
        taskDao.insert(task1);

        taskDao.toggleMarked(task1.id);

        assertEquals(true, taskDao.getMarked(task1.id));
    }

    @Test
    public void testChangeSortOrder() {
        TaskEntity task1 = TaskEntity.fromTask(new Task(1, "Task 1", 1, false, "H"));
        taskDao.insert(task1);

        int newSortOrder = 5;
        taskDao.changeSortOrder(task1.id, newSortOrder);

        TaskEntity retrievedTask = taskDao.find(task1.id);
        assertEquals(newSortOrder, retrievedTask.sortOrder);
    }

    @Test
    public void testIncrementAllMarked() {
        TaskEntity task1 = TaskEntity.fromTask(new Task(1, "Task 1", 1, true, "H"));
        TaskEntity task2 = TaskEntity.fromTask(new Task(2, "Task 2", 2, true, "H"));
        TaskEntity task3 = TaskEntity.fromTask(new Task(3, "Task 3", 3, false, "H"));

        taskDao.insert(task1);
        taskDao.insert(task2);
        taskDao.insert(task3);

        taskDao.incrementAllMarked();

        TaskEntity retrievedTask1 = taskDao.find(task1.id);
        TaskEntity retrievedTask2 = taskDao.find(task2.id);

        assertEquals(task1.sortOrder + 1, retrievedTask1.sortOrder);
        assertEquals(task2.sortOrder + 1, retrievedTask2.sortOrder);
    }

    @Test
    public void testShiftSortOrders() {
        TaskEntity task1 = TaskEntity.fromTask(new Task(1, "Task 1", 1, true, "H"));
        TaskEntity task2 = TaskEntity.fromTask(new Task(2, "Task 2", 2, true, "H"));
        TaskEntity task3 = TaskEntity.fromTask(new Task(3, "Task 3", 3, true, "H"));

        taskDao.insert(task1);
        taskDao.insert(task2);
        taskDao.insert(task3);

        int from = 1;
        int to = 3;
        int by = 2;

        taskDao.shiftSortOrders(from, to, by);

        TaskEntity retrievedTask1 = taskDao.find(task1.id);
        TaskEntity retrievedTask2 = taskDao.find(task2.id);
        TaskEntity retrievedTask3 = taskDao.find(task3.id);

        assertEquals(task1.sortOrder + by, retrievedTask1.sortOrder);
        assertEquals(task2.sortOrder + by, retrievedTask2.sortOrder);
        assertEquals(task3.sortOrder + by, retrievedTask3.sortOrder);
    }

    @Test
    public void testAppend() {
        TaskEntity task1 = TaskEntity.fromTask(new Task(1, "Task 1", 1, false, "H"));
        TaskEntity task2 = TaskEntity.fromTask(new Task(2, "Task 2", 2, false, "H"));
        TaskEntity task3 = TaskEntity.fromTask(new Task(3, "Task 3", 3, true, "H"));

        taskDao.insert(task1);
        taskDao.insert(task2);
        taskDao.insert(task3);

        TaskEntity newTask = TaskEntity.fromTask(new Task(4, "Task 4", 0, false, "H"));
        taskDao.append(newTask);

        TaskEntity appendedTask = taskDao.find(4);
        assertEquals(3, appendedTask.sortOrder);
    }


    @Test
    public void testSetMarked() {
        TaskEntity task2 = TaskEntity.fromTask(new Task(2, "Task 2", 2, false, "H"));

        taskDao.insert(task2);

        taskDao.setMarked(2);

        boolean markedStatus = taskDao.getMarked(2);
        assertTrue(markedStatus);
    }

}