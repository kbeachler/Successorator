package edu.ucsd.cse110.successorator;
import android.app.Application;
import androidx.room.Room;
import java.time.LocalDateTime;
import edu.ucsd.cse110.successorator.data.db.RoomTaskRepository;
import edu.ucsd.cse110.successorator.data.db.SuccessaratorDatabase;
import edu.ucsd.cse110.successorator.data.db.TimeKeep;
import edu.ucsd.cse110.successorator.lib.domain.TaskRepository;

public class SuccessoratorApplication extends Application {
    private TaskRepository taskRepository;
    private TimeKeep timeKeeper;
    @Override
    public void onCreate() {
        super.onCreate();
        var database = Room.databaseBuilder(
                        getApplicationContext(),
                        SuccessaratorDatabase.class,
                        "tasks-database"
                )
                .allowMainThreadQueries()
                .build();

        this.taskRepository = new RoomTaskRepository(database.taskDao(), database.RTaskDao(), database.tTaskDao(), database.pTaskDao());

        this.timeKeeper = new TimeKeep();
        timeKeeper.setDateTime(LocalDateTime.now());

        removeMarked();
    }

    public TaskRepository getTaskRepository() {
        return taskRepository;
    }

    public TimeKeep getTimeKeeper() {
        return timeKeeper;
    }

    public void setDataSource(TaskRepository taskRepository){
        this.taskRepository = taskRepository;
    }

    public void removeMarked(){
        var sharedPreferences = getSharedPreferences("successorator", MODE_PRIVATE);
        var lastVisit = sharedPreferences.getInt("lastStartTime", -1);
        boolean newDay = lastVisit != -1 && lastVisit != timeKeeper.getDateTime().getValue().getDayOfMonth() && (timeKeeper.getDateTime().getValue().getHour() >= 0);
        if(newDay) {
            taskRepository.newDay(timeKeeper.getCurrentYear(), timeKeeper.getCurrentMonth(), timeKeeper.getCurrentDayOfMonth());
        }
        sharedPreferences.edit()
                .putInt("lastStartTime", timeKeeper.getDateTime().getValue().getDayOfMonth())
                .apply();
    }

}
