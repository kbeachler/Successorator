package edu.ucsd.cse110.successorator.data.db;
import androidx.lifecycle.LiveData;
import androidx.room.Insert;
import androidx.room.Dao;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Transaction;

import java.util.List;
@Dao
public interface TaskDao {

    @Query("DELETE FROM tasks")
    void clearAll();
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    Long insert(TaskEntity Task);

    @Query("SELECT * FROM tasks WHERE id = :id")
    TaskEntity find(int id);

    @Query("DELETE from tasks where marked=true")
    void removeMarked();

    @Query("SELECT * FROM tasks WHERE id = :id")
    LiveData<TaskEntity> findAsLiveData(int id);

    @Query("SELECT * FROM tasks ORDER BY sort_order")
    LiveData<List<TaskEntity>> findAllAsLiveData();

    @Query("SELECT * FROM tasks WHERE context = :focus ORDER BY sort_order ")
    LiveData<List<TaskEntity>> findAllAsLiveData(String focus);

    @Query("SELECT * FROM tasks ORDER BY sort_order")
    List<TaskEntity> findAll();

    @Query("UPDATE tasks SET marked = NOT marked WHERE id = :id")
    void toggleMarked(int id);

    @Query("SELECT marked FROM tasks WHERE id = :id")
    boolean getMarked(int id);

    @Query("UPDATE tasks SET sort_order = :sortOrder WHERE id = :id")
    void changeSortOrder(int id, int sortOrder);

    @Query("SELECT COUNT(*) FROM tasks")
    int count();

    @Query("SELECT COUNT(*) FROM tasks")
    int todayCount();

    @Query("SELECT MIN(sort_order) FROM tasks WHERE marked = true")
    int smallestMarked();

    @Query("SELECT MAX(sort_order) FROM tasks WHERE marked = false")
    int largestUnmarked();

    @Query("UPDATE tasks SET sort_order = sort_order + 1 WHERE marked = true")
    void incrementAllMarked();

    @Query("UPDATE tasks SET id = id")
    void incrementAll();

    @Query("SELECT MIN(sort_order) FROM tasks")
    int getMinSortOrder();

    @Query("SELECT MAX(sort_order) FROM tasks")
    int getMaxSortOrder();

    @Query("UPDATE tasks SET sort_order = sort_order + :by " +
            "WHERE sort_order>= :from AND sort_order <=:to")
    void shiftSortOrders(int from, int to, int by);


    @Query("SELECT MAX(sort_order) FROM tasks WHERE marked = true")
    int getLastMarkedSortOrder();

    @Query("SELECT * FROM tasks WHERE task_name = :name")
    TaskEntity findByName(String name);

    @Transaction
    default int append(TaskEntity task) {
        int newSortOrder = getMaxSortOrder();

        int markedSortOrder = smallestMarked();
        if (markedSortOrder != -1) {
            newSortOrder = largestUnmarked()+1;
            shiftSortOrders(largestUnmarked(), getLastMarkedSortOrder()+1,1);
        }
        else {
            shiftSortOrders(getMinSortOrder(), getMaxSortOrder()+1,1);
        }

        TaskEntity newTask = new TaskEntity(task.task_name, newSortOrder,
                task.marked, task.context);
        return Math.toIntExact(insert(newTask));
    }

    @Transaction
    default boolean setMarked(int id) {
        toggleMarked(id);
        return getMarked(id);
    }
}
