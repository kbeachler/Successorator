package edu.ucsd.cse110.successorator.data.db;
import androidx.room.Database;
import androidx.room.RoomDatabase;
@Database(entities = {TaskEntity.class, RecurringTaskEntity.class, TTaskEntity.class, PendingTaskEntity.class}, version = 2)

public abstract class SuccessaratorDatabase extends RoomDatabase{
    public abstract TaskDao taskDao();
    public abstract RecurringTaskDao RTaskDao();
    public abstract TTaskDao tTaskDao();
    public abstract PendingTaskDao pTaskDao();
}
