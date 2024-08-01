package edu.ucsd.cse110.successorator.data.db;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;
import java.util.Objects;
import edu.ucsd.cse110.successorator.lib.domain.PendingTask;
import edu.ucsd.cse110.successorator.lib.domain.Task;

@Entity(tableName = "PendingTasks")
public class PendingTaskEntity {

    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id")
    public Integer id = null;

    @ColumnInfo(name = "task_name")
    public String task_name;

    @ColumnInfo(name = "sort_order")
    public int sortOrder;

    @ColumnInfo(name = "marked")
    public boolean marked;

    @ColumnInfo(name = "context")
    public String context;

    PendingTaskEntity(@NonNull String task_name, int sortOrder, boolean marked, String context){
        this.task_name = task_name;
        this.sortOrder = sortOrder;
        this.marked = marked;
        this.context = context;
    }
    public static PendingTaskEntity fromTask(@NonNull Task Task) {
        var task = new PendingTaskEntity(Task.task_name(), Task.sortOrder(), Task.getMark(), Task.context());
        task.id = Task.id();
        return task;
    }

    public @NonNull PendingTask toPTask() {
        return new PendingTask(id, task_name, sortOrder, marked, context);
    }
    public @NonNull Task toTask() {
        return new Task(id, task_name, sortOrder, marked, context);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        PendingTaskEntity that = (PendingTaskEntity) o;
        return sortOrder == that.sortOrder && Objects.equals(id, that.id) && Objects.equals(task_name, that.task_name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, task_name, sortOrder, context);
    }
}
