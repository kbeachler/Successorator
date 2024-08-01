package edu.ucsd.cse110.successorator.data.db;
import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.room.Insert;
import androidx.room.Dao;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Transaction;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
@Dao
public interface RecurringTaskDao {
    @Query("DELETE FROM recurringtasks")
    void clearAll();
    @Query("DELETE FROM recurringtasks WHERE id= :id")
    void delete(int id);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    Long insert(RecurringTaskEntity RTask);

    @Query("SELECT * FROM recurringtasks WHERE id = :id")
    RecurringTaskEntity find(int id);

    @Query("SELECT * FROM recurringtasks WHERE id = :id")
    LiveData<RecurringTaskEntity> findAsLiveData(int id);

    @Query("SELECT * FROM recurringtasks")
    LiveData<List<RecurringTaskEntity>> findAllAsLiveData();

    @Query("UPDATE recurringtasks SET id = id")
    void incrementAll();

    @Query("SELECT * FROM recurringtasks WHERE frequency = 'DAILY'")
    List<RecurringTaskEntity> findDaily();

    @Query("SELECT * FROM recurringtasks WHERE frequency = 'WEEKLY' AND day_of_week = :dayOfWeek")
    List<RecurringTaskEntity> findWeekly(int dayOfWeek);

    @Query("SELECT * FROM recurringtasks WHERE frequency = 'MONTHLY' AND day_of_week = :dayOfWeek" +
            " AND week_of_month = :weekOfMonth")
    List<RecurringTaskEntity> findMonthly(int dayOfWeek, int weekOfMonth);

    @Query("SELECT * FROM recurringtasks WHERE frequency = 'YEARLY' AND created_day = :day" +
            " AND created_month = :month")
    List<RecurringTaskEntity> findYearly(int month, int day);

    @Transaction
    default List<RecurringTaskEntity> getAllTasks(int year, int month, int day) {
        ArrayList<RecurringTaskEntity> collect = new ArrayList<>();
        LocalDate date = LocalDate.of(year, month, day);

        DayOfWeek dayOfWeek = date.getDayOfWeek();
        int day_of_week = dayOfWeek.getValue() % 7;
        int week_of_month = (day - 1) / 7 + 1;

        List<RecurringTaskEntity> daily = findDaily();
        List<RecurringTaskEntity> weekly = findWeekly(day_of_week);
        List<RecurringTaskEntity> monthly = findMonthly(day_of_week, week_of_month);
        List<RecurringTaskEntity> yearly = findYearly(month, day);

        collect.addAll(daily);
        collect.addAll(weekly);
        collect.addAll(monthly);
        collect.addAll(yearly);

        return collect;
    }
}
