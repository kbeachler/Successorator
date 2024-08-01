package edu.ucsd.cse110.successorator.lib.domain;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.Comparator;
import java.util.Objects;

public class Task {
    private final @Nullable Integer id;
    private final @NonNull String task_name;
    private @NonNull int sortOrder;
    private @NonNull boolean marked;
    private @NonNull String context;

    public Task(@Nullable Integer id, @NonNull String task_name, int sortOrder, boolean marked, String context) {
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

    public String context(){return context;}


    public Task withId(int id) {

        return new Task(id,task_name,sortOrder, marked, context);
    }

    public Task withSortOrder(int sortOrder) {
        return new Task(id,task_name,sortOrder, marked, context);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Task task = (Task) o;
        return sortOrder == task.sortOrder && Objects.equals(id, task.id) && Objects.equals(task_name, task.task_name)
                && Objects.equals(context, task.context);
    }


    @Override
    public int hashCode() {
        return Objects.hash(id, task_name, sortOrder, context);
    }

    public static class TaskComparator implements Comparator<Task> {
        @Override
        public int compare(Task task2, Task task1) {
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

