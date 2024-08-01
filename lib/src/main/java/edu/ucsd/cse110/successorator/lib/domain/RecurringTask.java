package edu.ucsd.cse110.successorator.lib.domain;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.Comparator;
import java.util.Objects;

public class RecurringTask {
    private final @Nullable Integer id;
    private final @NonNull String task_name;
    private int created_year;
    private int created_month;
    private int created_day;
    private @NonNull String freq;

    private String context;



    public RecurringTask(@Nullable Integer id, @NonNull String task_name,
                         int created_year, int created_month, int created_day,
                         @NonNull String freq, @NonNull String context) {

        this.id = id;
        this.task_name = task_name;
        this.created_year = created_year;
        this.created_month = created_month;
        this.created_day = created_day;
        this.freq = freq;
        this.context = context;

    }

    public @Nullable Integer id() {
        return id;
    }
    public @NonNull String task_name() {
        return task_name;
    }

    public int created_year() {
        return created_year;
    }

    public int created_month() {
        return created_month;
    }

    public int created_day() {
        return created_day;
    }

    public String freq() {
        return freq;
    }

    public String context() {
        return context;
    }

    public RecurringTask withId(int id) {
        return new RecurringTask(id,task_name,
                created_year, created_month, created_day, freq, context);
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        RecurringTask rtask = (RecurringTask) o;
        return this.created_year == rtask.created_year &&
                this.created_month == rtask.created_month &&
                this.created_day == rtask.created_day &&
                Objects.equals(this.freq, rtask.freq) &&
                Objects.equals(this.context, rtask.context);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, task_name, created_year, created_month,
                created_day, freq);
    }

    public static class RecurringTaskComparator implements Comparator<RecurringTask> {
        @Override
        public int compare(RecurringTask task1, RecurringTask task2) {

            int yearComparison = Integer.compare(task1.created_year(), task2.created_year());
            if (yearComparison != 0) {
                return yearComparison;
            }

            // If creation years are the same, compare the creation months
            int monthComparison = Integer.compare(task1.created_month(), task2.created_month());
            if (monthComparison != 0) {
                return monthComparison;
            }

            // If creation months are the same, compare the creation days
            return Integer.compare(task1.created_day(), task2.created_day());
        }
    }
}
