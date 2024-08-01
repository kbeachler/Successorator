package edu.ucsd.cse110.successorator.lib.domain;
import edu.ucsd.cse110.successorator.lib.util.Subject;
import java.util.List;
public interface TaskRepository {
    Subject<Task> find(int id);

    Subject<List<Task>> findAll();

    Subject<List<Task>> findAllT();
    Subject<List<PendingTask>> findAllP();

    Subject<List<RecurringTask>> findAllR();

    void append(Task task);

    void appendT(Task task);

    void appendP(Task task);

    void mark(int id);
    void delete(int id);

    void toToday(int id);

    void toTomorrow(int id);

    void finish(int id);

    void deleteR(int id);

    void markT(int id);

    int getNumTasks();
    int getTodayNumTasks();

    void clearAll();

    void clearAllTest();

    void appendRecurring(Task task, RecurringTask RTask, int year, int month, int day);

    void newDay(int year, int month, int day);

    void notifyFocusChanged();

}
