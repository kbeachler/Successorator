package edu.ucsd.cse110.successorator.data.db;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;
import java.util.Objects;
import edu.ucsd.cse110.successorator.lib.domain.Task;

@Entity(tableName = "TmrTasks")
public class TTaskEntity {

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
    TTaskEntity(@NonNull String task_name, int sortOrder, boolean marked, String context){
        this.task_name = task_name;
        this.sortOrder = sortOrder;
        this.marked = marked;
        this.context = context;
    }
    public static TTaskEntity fromTask(@NonNull Task Task) {
        var task = new TTaskEntity(Task.task_name(), Task.sortOrder(), Task.getMark(), Task.context());
        task.id = Task.id();
        return task;
    }

    public @NonNull Task toTask() {
        return new Task(id, task_name, sortOrder, marked, context);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        TTaskEntity that = (TTaskEntity) o;
        return sortOrder == that.sortOrder && marked == that.marked && Objects.equals(id, that.id) && Objects.equals(task_name, that.task_name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, task_name, sortOrder, marked);
    }
}