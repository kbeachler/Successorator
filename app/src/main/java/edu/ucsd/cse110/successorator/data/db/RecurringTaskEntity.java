package edu.ucsd.cse110.successorator.data.db;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.util.Objects;

import edu.ucsd.cse110.successorator.lib.domain.RecurringTask;


@Entity(tableName = "RecurringTasks")
public class RecurringTaskEntity {
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id")
    public Integer id = null;

    @ColumnInfo(name = "task_name")
    public String task_name;

    @ColumnInfo(name = "created_year")
    public int created_year;

    @ColumnInfo(name = "created_month")
    public int created_month;

    @ColumnInfo(name = "created_day")
    public int created_day;

    @ColumnInfo(name = "day_of_week")
    public int day_of_week;

    @ColumnInfo(name = "week_of_month")
    public int week_of_month;

    @ColumnInfo(name = "frequency")
    public String freq;

    @ColumnInfo(name = "context")
    public String context;

    RecurringTaskEntity(@NonNull String task_name, int created_year, int created_month,
                        int created_day, String freq, String context){
        this.task_name = task_name;
        this.created_year = created_year;
        this.created_month = created_month;
        this.created_day = created_day;
        this.freq = freq;
        this.context = context;

        LocalDate date = LocalDate.of(created_year, created_month, created_day);

        DayOfWeek dayOfWeek = date.getDayOfWeek();
        this.day_of_week = dayOfWeek.getValue() % 7;
        this.week_of_month = (created_day - 1) / 7 + 1;
    }

    public static RecurringTaskEntity fromRTask(@NonNull RecurringTask rTask) {
        var rTaskEntity = new RecurringTaskEntity(rTask.task_name(), rTask.created_year(),
                rTask.created_month(), rTask.created_day(), rTask.freq(), rTask.context());
        rTaskEntity.id = rTask.id();
        return rTaskEntity;
    }

    public @NonNull RecurringTask toRTask() {
        return new RecurringTask(id, task_name,
                created_year, created_month, created_day, freq, context);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        RecurringTaskEntity that = (RecurringTaskEntity) o;
        return created_year == that.created_year && created_month == that.created_month
                && created_day == that.created_day && Objects.equals(freq, that.freq)
                && Objects.equals(task_name, that.task_name)
                && Objects.equals(context, that.context);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, task_name, created_year, created_month,
                created_day, freq, context);
    }
}
