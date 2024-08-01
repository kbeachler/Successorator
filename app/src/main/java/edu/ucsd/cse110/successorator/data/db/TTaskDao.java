package edu.ucsd.cse110.successorator.data.db;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Transaction;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
@Dao
public interface TTaskDao {

    @Query("DELETE FROM tmrtasks")
    void clearAll();
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    Long insert(TTaskEntity Task);

    @Query("SELECT * FROM tmrtasks WHERE id = :id")
    TTaskEntity find(int id);

    @Query("SELECT * from tmrtasks where marked= true")
    List<TTaskEntity> getAllMarked();
    @Query("DELETE from tmrtasks where marked=true")
    void removeMarked();

    @Query("SELECT * FROM tmrtasks WHERE id = :id")
    LiveData<TTaskEntity> findAsLiveData(int id);

    @Query("SELECT * FROM tmrtasks ORDER BY sort_order")
    LiveData<List<TTaskEntity>> findAllAsLiveData();

    @Query("SELECT * FROM tmrtasks ORDER BY sort_order")
    List<TTaskEntity> findAll();

    @Query("UPDATE tmrtasks SET marked = NOT marked WHERE id = :id")
    void toggleMarked(int id);

    @Query("SELECT marked FROM tmrtasks WHERE id = :id")
    boolean getMarked(int id);

    @Query("UPDATE tmrtasks SET sort_order = :sortOrder WHERE id = :id")
    void changeSortOrder(int id, int sortOrder);

    @Query("SELECT COUNT(*) FROM tmrtasks")
    int count();

    @Query("SELECT COUNT(*) FROM tmrtasks")
    int todayCount();

    @Query("SELECT MIN(sort_order) FROM tmrtasks WHERE marked = true")
    int smallestMarked();

    @Query("SELECT MAX(sort_order) FROM tmrtasks WHERE marked = false")
    int largestUnmarked();

    @Query("UPDATE tmrtasks SET id = id")
    void incrementAll();

    @Query("UPDATE tmrtasks SET sort_order = sort_order + 1 WHERE marked = true")
    void incrementAllMarked();

    @Query("SELECT MIN(sort_order) FROM tmrtasks")
    int getMinSortOrder();

    @Query("SELECT MAX(sort_order) FROM tmrtasks")
    int getMaxSortOrder();

    @Query("UPDATE tmrtasks SET sort_order = sort_order + :by " +
            "WHERE sort_order>= :from AND sort_order <=:to")
    void shiftSortOrders(int from, int to, int by);


    @Query("SELECT MAX(sort_order) FROM tmrtasks WHERE marked = true")
    int getLastMarkedSortOrder();

    @Transaction
    default int append(TTaskEntity task) {
        int newSortOrder = getMaxSortOrder();

        int markedSortOrder = smallestMarked();
        if (markedSortOrder != -1) {
            newSortOrder = largestUnmarked()+1;
            shiftSortOrders(largestUnmarked(), getLastMarkedSortOrder()+1,1);
        }
        else {
            shiftSortOrders(getMinSortOrder(), getMaxSortOrder()+1,1);
        }

        TTaskEntity newTask = new TTaskEntity(task.task_name, newSortOrder, task.marked, task.context);
        return Math.toIntExact(insert(newTask));
    }

    @Transaction
    default boolean setMarked(int id) {
        toggleMarked(id);
        return getMarked(id);
    }

    @Transaction
    default Set<String> getAndDeleteMarked(){
        List<TTaskEntity> gam = getAllMarked();
        removeMarked();
        Set<String> ret = new HashSet<>();
        if(gam == null) return ret;
        for (int i = 0; i < gam.size(); i++){
            ret.add(gam.get(i).task_name);
        }
        return ret;

    }
}