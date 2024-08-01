package edu.ucsd.cse110.successorator.data.db;

import android.util.Log;

import androidx.lifecycle.Transformations;

import java.util.Calendar;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

import edu.ucsd.cse110.successorator.lib.domain.PendingTask;
import edu.ucsd.cse110.successorator.lib.domain.RecurringTask;
import edu.ucsd.cse110.successorator.lib.domain.Task;
import edu.ucsd.cse110.successorator.lib.domain.TaskRepository;
import edu.ucsd.cse110.successorator.lib.util.Subject;
import edu.ucsd.cse110.successorator.util.LiveDataSubjectAdapter;

public class RoomTaskRepository implements TaskRepository {
    private static final AtomicInteger counter = new AtomicInteger(0);
    private final TaskDao taskDao;
    private final RecurringTaskDao rTaskDao;
    private final TTaskDao tTaskDao;
    private final PendingTaskDao pTaskDao;

    public RoomTaskRepository(TaskDao taskDao) {
        this.taskDao = taskDao;
        rTaskDao = null;
        tTaskDao = null;
        pTaskDao = null;
    }

    public RoomTaskRepository(TaskDao taskDao, RecurringTaskDao rTaskDao, TTaskDao tTaskDao, PendingTaskDao pTaskDao) {
        this.taskDao = taskDao;
        this.rTaskDao = rTaskDao;
        this.tTaskDao = tTaskDao;
        this.pTaskDao = pTaskDao;
    }

    @Override
    public Subject<Task> find(int id) {
        var entityLiveData = taskDao.findAsLiveData(id);
        var TaskLiveData = Transformations.map(entityLiveData, TaskEntity::toTask);
        return new LiveDataSubjectAdapter<>(TaskLiveData);
    }

    @Override
    public Subject<List<Task>> findAll() {
        var entitiesLiveData = taskDao.findAllAsLiveData();
        var tasksLiveData = Transformations.map(entitiesLiveData, entities -> {
            return entities.stream()
                    .map(TaskEntity::toTask)
                    .collect(Collectors.toList());
        });
        return new LiveDataSubjectAdapter<>(tasksLiveData);
    }

    @Override
    public Subject<List<Task>> findAllT() {
        var entitiesLiveData = tTaskDao.findAllAsLiveData();
        var tasksLiveData = Transformations.map(entitiesLiveData, entities -> {
            return entities.stream()
                    .map(TTaskEntity::toTask)
                    .collect(Collectors.toList());
        });
        return new LiveDataSubjectAdapter<>(tasksLiveData);
    }

    @Override
    public Subject<List<PendingTask>> findAllP() {
        var entitiesLiveData = pTaskDao.findAllAsLiveData();
        var tasksLiveData = Transformations.map(entitiesLiveData, entities -> {
            return entities.stream()
                    .map(PendingTaskEntity::toPTask)
                    .collect(Collectors.toList());
        });
        return new LiveDataSubjectAdapter<>(tasksLiveData);
    }

    @Override
    public Subject<List<RecurringTask>> findAllR() {
        var entitiesLiveData = rTaskDao.findAllAsLiveData();
        var tasksLiveData = Transformations.map(entitiesLiveData, entities -> {
            return entities.stream()
                    .map(RecurringTaskEntity::toRTask)
                    .collect(Collectors.toList());
        });
        return new LiveDataSubjectAdapter<>(tasksLiveData);
    }

    @Override
    public void append(Task task) {
        int newSortOrder = taskDao.smallestMarked();
        taskDao.incrementAllMarked();
        taskDao.append(TaskEntity.fromTask(task.withSortOrder(newSortOrder)));
    }

    @Override
    public void appendT(Task task) {
        int newSortOrder = tTaskDao.smallestMarked();
        tTaskDao.incrementAllMarked();
        tTaskDao.append(TTaskEntity.fromTask(task.withSortOrder(newSortOrder)));
    }

    @Override
    public void appendP(Task task) {
        int newSortOrder = pTaskDao.smallestMarked();
        pTaskDao.incrementAllMarked();
        pTaskDao.append(PendingTaskEntity.fromTask(task.withSortOrder(newSortOrder)));
    }

    @Override
    public void mark(int id) {
        boolean isMarked = taskDao.getMarked(id);
        int newSortOrder = 0;

        if (!isMarked){
           newSortOrder = Math.max(taskDao.largestUnmarked()+1, taskDao.smallestMarked());
           taskDao.incrementAllMarked();
        }
        else{
            newSortOrder = taskDao.getMinSortOrder() - 1;
        }
        taskDao.toggleMarked(id);
        taskDao.changeSortOrder(id, newSortOrder);
    }

    @Override
    public void delete(int id){
        pTaskDao.delete(id);
    }

    public void finish(int id) {
        toTodayMark(id);
    }

    @Override
    public void deleteR(int id){
        rTaskDao.delete(id);
    }

    @Override
    public void markT(int id) {
        boolean isMarked = tTaskDao.getMarked(id);

        int newSortOrder = 0;

        if (!isMarked){
            newSortOrder = Math.max(tTaskDao.largestUnmarked()+1, tTaskDao.smallestMarked());
            tTaskDao.incrementAllMarked();
        }
        else{
            newSortOrder = tTaskDao.getMinSortOrder() - 1;
        }

        tTaskDao.toggleMarked(id);
        tTaskDao.changeSortOrder(id, newSortOrder);
    }

    public void toTodayMark(int id){
        List<PendingTaskEntity> pend = pTaskDao.findAll();
        List<TaskEntity> cur = taskDao.findAll();
        HashSet<String> exists = new HashSet<>();

        for (int i = 0; i < cur.size(); i++){
            Task t = cur.get(i).toTask();
            exists.add(t.task_name());

        }

        for (int i = 0; i < pend.size(); i++){
            Task t = pend.get(i).toTask();
            if (t.id().equals(id)){
                if (!exists.contains(t.task_name()))taskDao.append(TaskEntity.fromTask(t));
            }
        }
        String name = pTaskDao.find(id).task_name;
        mark(taskDao.findByName(name).id);
        pTaskDao.delete(id);
    }
    public void toToday(int id){
        List<PendingTaskEntity> pend = pTaskDao.findAll();
        List<TaskEntity> cur = taskDao.findAll();
        HashSet<String> exists = new HashSet<>();

        for (int i = 0; i < cur.size(); i++){
            Task t = cur.get(i).toTask();
            exists.add(t.task_name());
        }

        for (int i = 0; i < pend.size(); i++){
            Task t = pend.get(i).toTask();
            if (t.id().equals(id)){
                if (!exists.contains(t.task_name()))taskDao.append(TaskEntity.fromTask(t));
            }
        }
        pTaskDao.delete(id);
    }

    public void toTomorrow(int id){
        List<PendingTaskEntity> pend = pTaskDao.findAll();
        List<TTaskEntity> cur = tTaskDao.findAll();
        HashSet<String> exists = new HashSet<>();

        for (int i = 0; i < cur.size(); i++){
            Task t = cur.get(i).toTask();
            exists.add(t.task_name());
        }
        for (int i = 0; i < pend.size(); i++){
            Task t = pend.get(i).toTask();
            if (t.id().equals(id)){
                if (!exists.contains(t.task_name()))tTaskDao.append(TTaskEntity.fromTask(t));
            }
        }
        pTaskDao.delete(id);
    }
    @Override
    public int getNumTasks() {
        return taskDao.count();
    }

    @Override
    public int getTodayNumTasks() {
        return taskDao.todayCount();
    }

    @Override
    public void clearAll(){
        taskDao.clearAll();
    }

    public void clearAllTest(){
        taskDao.clearAll();
        tTaskDao.clearAll();
        rTaskDao.clearAll();
        pTaskDao.clearAll();
    }

    public void appendRecurring(Task task, RecurringTask RTask, int year, int month, int day) {
        rTaskDao.insert(RecurringTaskEntity.fromRTask(RTask));
        repopulateToday(year, month, day, new HashSet<String>());
        repopulateTmr(year, month, day);
    }

    @Override
    public void newDay(int year, int month, int day) {
        taskDao.removeMarked();
        Set<String> hs  = tTaskDao.getAndDeleteMarked();
        repopulateToday(year, month, day, hs);
        moveTomorrowToToday(year, month, day);
        repopulateTmr(year, month, day);
    }

    @Override
    public void notifyFocusChanged(){
        taskDao.incrementAll();
        tTaskDao.incrementAll();
        pTaskDao.incrementAll();
        rTaskDao.incrementAll();
    }

    public void repopulateToday(int year, int month, int day, Set<String> hs){

        List<TaskEntity> cur = taskDao.findAll();

        HashSet<String> exists = new HashSet<>();

        for (int i = 0; i < cur.size(); i++){
            Task t = cur.get(i).toTask();
            exists.add(t.task_name());
        }

        List<RecurringTaskEntity> rec = rTaskDao.getAllTasks(year, month, day);

        for (int i = 0; i < rec.size(); i++){
            RecurringTask rt = rec.get(i).toRTask();

            Task nt = new Task(rt.id(), rt.task_name(), 0, false, rt.context());

            int todayIndex = 400 * year + (month-1) * 32 + day;
            int taskIndex = 400 * rt.created_year() + (rt.created_month()-1) * 32 + rt.created_day();
            if (todayIndex >= taskIndex && !exists.contains(rt.task_name())
                    && !hs.contains(rt.task_name())) append(nt);
        }
    }

    public void repopulateTmr(int year, int month, int day){

        List<TTaskEntity> tmr = tTaskDao.findAll();

        HashSet<String> existsT = new HashSet<>();

        for (int i = 0; i < tmr.size(); i++){
            Task t = tmr.get(i).toTask();
            existsT.add(t.task_name());
        }

        Calendar calendar = Calendar.getInstance();
        calendar.set(year, month-1, day);

        // Add one day to the current date
        calendar.add(Calendar.DAY_OF_MONTH, 1);

        int nextYear = calendar.get(Calendar.YEAR);
        int nextMonth = calendar.get(Calendar.MONTH)+1;
        int nextDay = calendar.get(Calendar.DAY_OF_MONTH);

        List<RecurringTaskEntity> recTmr = rTaskDao.getAllTasks(nextYear, nextMonth, nextDay);

        for (int i = 0; i < recTmr.size(); i++){
            RecurringTask rt = recTmr.get(i).toRTask();

            Task nt = new Task(rt.id(), rt.task_name(), 0, false, rt.context());

            int todayIndex = 400 * year + (month-1) * 32 + day + 1;
            int taskIndex = 400 * rt.created_year() + (rt.created_month()-1) * 32 + rt.created_day();
            if (todayIndex >= taskIndex && !existsT.contains(rt.task_name())) appendT(nt);
        }
    }

    public void moveTomorrowToToday(int year, int month, int day){

        List<TTaskEntity> tmr = tTaskDao.findAll();
        List<TaskEntity> cur = taskDao.findAll();

        HashSet<String> exists = new HashSet<>();

        for (int i = 0; i < cur.size(); i++){
            Task t = cur.get(i).toTask();
            exists.add(t.task_name());
        }

        for (int i = 0; i < tmr.size(); i++){
            Task t = tmr.get(i).toTask();
            if (!exists.contains(t.task_name()))taskDao.append(TaskEntity.fromTask(t));
        }

        tTaskDao.clearAll();

    }


}


