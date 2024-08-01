package edu.ucsd.cse110.successorator.lib.domain;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.Comparator;
import java.util.Objects;

public class PendingTask {
    private final @Nullable Integer id;
    private final @NonNull String task_name;
    private int sortOrder;
    private boolean marked;
    private String context;

    public PendingTask(@Nullable Integer id, @NonNull String task_name, int sortOrder, boolean marked
    , String context) {
        this.id = id;
        this.task_name = task_name;
        this.sortOrder = sortOrder;
        this.marked = marked;
        this.context = context;
    }
    public @Nullable Integer id() {
        return id;
    }
    public @NonNull String task_name() {
        return task_name;
    }
    public int sortOrder() { return sortOrder; }

    public boolean getMark(){ return marked; }

    public String context(){
        return context;
    }

    public PendingTask withId(int id) {
        return new PendingTask(id,task_name,sortOrder, marked, context);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        PendingTask task = (PendingTask) o;
        return sortOrder == task.sortOrder && Objects.equals(id, task.id)
                && Objects.equals(task_name, task.task_name)
                && Objects.equals(context, task.context);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, task_name, sortOrder, marked, context);
    }

    public static class PendingTaskComparator implements Comparator<PendingTask> {
        @Override
        public int compare(PendingTask task2, PendingTask task1) {
            // Check if both tasks have marked=true
            if (task1.getMark() && !task2.getMark()) {
                return -1; // task1 has higher precedence
            } else if (!task1.getMark() && task2.getMark()) {
                return 1; // task2 has higher precedence
            } else if (task1.getMark() && task2.getMark()) {
                // If both tasks have marked=true, sort them using sortOrder
                if (task1.sortOrder() != task2.sortOrder()) {
                    return Integer.compare(task2.sortOrder(), task1.sortOrder());
                }
            }

            // Compare tasks based on context
            int contextComparison = getContextPrecedence(task1.context()) - getContextPrecedence(task2.context());
            if (contextComparison != 0) {
                return contextComparison;
            }

            // If tasks have the same context, sort them using sortOrder
            return Integer.compare(task2.sortOrder(), task1.sortOrder());
        }
        // Helper method to determine precedence of context
        private int getContextPrecedence(String context) {
            switch (context) {
                case "E":
                    return 1;
                case "S":
                    return 2;
                case "W":
                    return 3;
                case "H":
                    return 4;
                default:
                    return Integer.MAX_VALUE; // Default case for unknown contexts
            }
        }
    }
}
