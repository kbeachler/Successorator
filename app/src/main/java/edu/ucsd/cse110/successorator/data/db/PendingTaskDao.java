package edu.ucsd.cse110.successorator.data.db;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Transaction;

import java.util.List;

@Dao
public interface PendingTaskDao {
    @Query("DELETE FROM pendingtasks")
    void clearAll();

    @Query("DELETE FROM pendingtasks WHERE id=:id")
    void delete(int id);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    Long insert(PendingTaskEntity Task);

    @Query("SELECT * FROM pendingtasks WHERE id = :id")
    PendingTaskEntity find(int id);

    @Query("SELECT * FROM pendingtasks WHERE id = :id")
    LiveData<PendingTaskEntity> findAsLiveData(int id);

    @Query("SELECT * FROM pendingtasks ORDER BY sort_order")
    LiveData<List<PendingTaskEntity>> findAllAsLiveData();

    @Query("SELECT * FROM pendingtasks ORDER BY sort_order")
    List<PendingTaskEntity> findAll();

    @Query("UPDATE pendingtasks SET sort_order = :sortOrder WHERE id = :id")
    void changeSortOrder(int id, int sortOrder);

    @Query("SELECT COUNT(*) FROM pendingtasks")
    int count();

    @Query("SELECT COUNT(*) FROM pendingtasks")
    int todayCount();

    @Query("SELECT MIN(sort_order) FROM pendingtasks WHERE marked = true")
    int smallestMarked();

    @Query("SELECT MAX(sort_order) FROM pendingtasks WHERE marked = false")
    int largestUnmarked();

    @Query("UPDATE pendingtasks SET id = id")
    void incrementAll();

    @Query("UPDATE pendingtasks SET sort_order = sort_order + 1 WHERE marked = true")
    void incrementAllMarked();

    @Query("SELECT MIN(sort_order) FROM pendingtasks")
    int getMinSortOrder();

    @Query("SELECT MAX(sort_order) FROM pendingtasks")
    int getMaxSortOrder();

    @Query("UPDATE pendingtasks SET sort_order = sort_order + :by " +
            "WHERE sort_order>= :from AND sort_order <=:to")
    void shiftSortOrders(int from, int to, int by);

    @Query("SELECT MAX(sort_order) FROM pendingtasks WHERE marked = true")
    int getLastMarkedSortOrder();

    @Transaction
    default int append(PendingTaskEntity task) {
        int newSortOrder = getMaxSortOrder();

        int markedSortOrder = smallestMarked();
        if (markedSortOrder != -1) {
            newSortOrder = largestUnmarked()+1;
            shiftSortOrders(largestUnmarked(), getLastMarkedSortOrder()+1,1);
        }
        else {
            shiftSortOrders(getMinSortOrder(), getMaxSortOrder()+1,1);
        }

        PendingTaskEntity newTask = new PendingTaskEntity(task.task_name, newSortOrder, task.marked, task.context);
        return Math.toIntExact(insert(newTask));
    }
}
