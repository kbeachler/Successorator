package edu.ucsd.cse110.successorator;

import static androidx.lifecycle.ViewModelProvider.AndroidViewModelFactory.APPLICATION_KEY;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.viewmodel.ViewModelInitializer;

import java.util.List;
import java.util.stream.Collectors;

import edu.ucsd.cse110.successorator.data.db.TimeKeep;
import edu.ucsd.cse110.successorator.lib.domain.PendingTask;
import edu.ucsd.cse110.successorator.lib.domain.RecurringTask;
import edu.ucsd.cse110.successorator.lib.util.MutableSubject;
import edu.ucsd.cse110.successorator.lib.domain.Task;
import edu.ucsd.cse110.successorator.lib.domain.TaskRepository;
import edu.ucsd.cse110.successorator.lib.util.Subject;
import edu.ucsd.cse110.successorator.lib.util.SimpleSubject;
public class MainViewModel extends ViewModel {
    private final TaskRepository taskRepository;
    // UI state
    private final MutableSubject<List<PendingTask>> pendingTasks;
    private final MutableSubject<List<Task>> todayTasks;
    private final MutableSubject<List<Task>> tomorrowTasks;
    private final MutableSubject<List<RecurringTask>> recurringTasks;
    private String focus;
    private final MutableLiveData<TimeKeep> timeKeeper  = new MutableLiveData<>();
    public void setTimeKeeper(TimeKeep timekeeper){
        timeKeeper.setValue(timekeeper);
    }

    public void setFocus(String focus){
        this.focus = focus;
        taskRepository.notifyFocusChanged();
    }
    public LiveData<TimeKeep> getTimeKeeper() {return timeKeeper;}
    private final MutableSubject<Integer> numTasks;
    private final MutableSubject<Integer> TodayNumTasks;

    public static final ViewModelInitializer<MainViewModel> initializer =
            new ViewModelInitializer<>(
                    MainViewModel.class,
                    creationExtras -> {
                        var app = (SuccessoratorApplication) creationExtras.get(APPLICATION_KEY);
                        assert app != null;
                        return new MainViewModel(app.getTaskRepository(), app.getTimeKeeper());
                    });

    public MainViewModel(TaskRepository taskRepository, TimeKeep timekeeper) {
        this.taskRepository = taskRepository;

        // Create the observable subjects.
        this.pendingTasks = new SimpleSubject<>();
        this.todayTasks = new SimpleSubject<>();
        this.tomorrowTasks = new SimpleSubject<>();
        this.recurringTasks = new SimpleSubject<>();
        setFocus("ALL");

        setTimeKeeper(timekeeper);
        getTimeKeeper();
        var currDay = timekeeper.getDay(timekeeper);

        this.numTasks = new SimpleSubject<>();
        this.TodayNumTasks = new SimpleSubject<>();

        numTasks.setValue(taskRepository.getNumTasks());
        TodayNumTasks.setValue(taskRepository.getTodayNumTasks());

        taskRepository.findAll().observe(tasks -> {
            if (tasks == null) return; // not ready yet, ignore
            var newTodayTasks = tasks.stream()
                    .filter(task -> focus.equals("ALL") || task.context().equals(focus))
                    .sorted(new Task.TaskComparator())
                    .collect(Collectors.toList());
            todayTasks.setValue(newTodayTasks);
        });

        taskRepository.findAllP().observe(tasks -> {
            if (tasks == null) return; // not ready yet, ignore
            var newPendingTasks = tasks.stream()
                    .filter(task -> focus.equals("ALL") || task.context().equals(focus))
                    .sorted(new PendingTask.PendingTaskComparator())
                    .collect(Collectors.toList());
            pendingTasks.setValue(newPendingTasks);
        });

        taskRepository.findAllT().observe(tasks -> {
            if (tasks == null) return; // not ready yet, ignore
            var newTomorrowTasks = tasks.stream()
                    .filter(task -> focus.equals("ALL") || task.context().equals(focus))
                    .sorted(new Task.TaskComparator())
                    .collect(Collectors.toList());
            tomorrowTasks.setValue(newTomorrowTasks);
        });

        taskRepository.findAllR().observe(rtasks -> {
            if (rtasks == null) return;
            var newRecurringTasks = rtasks.stream()
                    .filter(task -> focus.equals("ALL") || task.context().equals(focus))
                    .sorted(new RecurringTask.RecurringTaskComparator())
                    .collect(Collectors.toList());
            recurringTasks.setValue(newRecurringTasks);
        });

        timekeeper.getDateTime().observe(date -> {
            if(date.getHour() >= 0 && date.getDayOfMonth() != currDay) {
                taskRepository.newDay(timekeeper.getCurrentYear(), timekeeper.getCurrentMonth(),
                        timekeeper.getCurrentDayOfMonth());
                setTodayNumTasks();
            }
        });
    }
    public Subject<List<PendingTask>> getPendingTasks() {
        return pendingTasks;
    }
    public Subject<List<Task>> getTodayTasks() {
        return todayTasks;
    }
    public Subject<List<Task>> getTomorrowTasks() {
        return tomorrowTasks;
    }
    public Subject<List<RecurringTask>> getRecurringTasks() {
        return recurringTasks;
    }

    public void append(Task task) {
        taskRepository.append(task);
    }
    public void appendT(Task task) {
        taskRepository.appendT(task);
    }

    public void appendP(Task task) {
        taskRepository.appendP(task);
    }

    public void appendRecurring(Task task, RecurringTask rtask) {;
        taskRepository.appendRecurring(task, rtask,
                timeKeeper.getValue().getCurrentYear(),
                timeKeeper.getValue().getCurrentMonth(),
                timeKeeper.getValue().getCurrentDayOfMonth());
    }
    public void mark(int id) {
        taskRepository.mark(id);
    }
  
    public void delete(int id){
        taskRepository.delete(id);
    }

    public void toToday(int id) {
        taskRepository.toToday(id);
    }

    public void toTomorrow(int id) {
        taskRepository.toTomorrow(id);
    }

    public void finish(int id) {
        taskRepository.finish(id);
    }

    public void deleteR(int id){
        taskRepository.deleteR(id);
        taskRepository.newDay(timeKeeper.getValue().getCurrentYear(),
                timeKeeper.getValue().getCurrentMonth(),
                timeKeeper.getValue().getCurrentDayOfMonth());
    }

    public void markT(int id) {
        taskRepository.markT(id);
    }
    public Subject<Integer> getTodayNumTasks(){
        return TodayNumTasks;
    }
    public void setTodayNumTasks(){
        if (TodayNumTasks.getValue() != taskRepository.getTodayNumTasks()) {
            TodayNumTasks.setValue(taskRepository.getTodayNumTasks());
        }
    }
}
