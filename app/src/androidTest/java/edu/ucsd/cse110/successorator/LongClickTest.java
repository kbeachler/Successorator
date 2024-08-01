package edu.ucsd.cse110.successorator;

import static androidx.test.espresso.Espresso.onData;
import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.assertion.ViewAssertions.doesNotExist;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;

import static org.hamcrest.CoreMatchers.anything;

import androidx.room.Room;
import androidx.test.core.app.ActivityScenario;
import androidx.test.espresso.action.ViewActions;
import androidx.test.espresso.assertion.ViewAssertions;
import androidx.test.espresso.matcher.ViewMatchers;
import androidx.test.platform.app.InstrumentationRegistry;

import org.junit.Before;
import org.junit.Test;

import java.util.List;

import edu.ucsd.cse110.successorator.data.db.RoomTaskRepository;
import edu.ucsd.cse110.successorator.data.db.SuccessaratorDatabase;
import edu.ucsd.cse110.successorator.lib.domain.TaskRepository;

public class LongClickTest {
    public SuccessoratorApplication app = (SuccessoratorApplication) InstrumentationRegistry.getInstrumentation().getTargetContext().getApplicationContext();
    public TaskRepository taskRepo;

    @Before
    public void setUp(){
        var database = Room.inMemoryDatabaseBuilder(
                        app,
                        SuccessaratorDatabase.class
                )
                .allowMainThreadQueries()
                .build();
        this.taskRepo = new RoomTaskRepository(database.taskDao(), database.RTaskDao(), database.tTaskDao(), database.pTaskDao());
        taskRepo.clearAllTest();
        app.setDataSource(taskRepo);
        ActivityScenario.launch(MainActivity.class);

        // Before each test setup
        onView(withId(R.id.action_bar_menu_drop)).perform(ViewActions.click());
        onView(withText("Pending")).perform(ViewActions.click());
        onView(withId(R.id.action_bar_menu_add_task)).perform(ViewActions.click());
        onView(withId(R.id.taskText)).perform((ViewActions.replaceText("testing1")));
        onView(withId(R.id.H)).perform((ViewActions.click()));
        onView(withText("Create")).perform(ViewActions.click());
        onView(withId(R.id.action_bar_menu_add_task)).perform(ViewActions.click());
        onView(withId(R.id.taskText)).perform((ViewActions.replaceText("testing2")));
        onView(withId(R.id.W)).perform((ViewActions.click()));
        onView(withText("Create")).perform(ViewActions.click());
        onView(withId(R.id.action_bar_menu_add_task)).perform(ViewActions.click());
        onView(withId(R.id.taskText)).perform((ViewActions.replaceText("testing3")));
        onView(withId(R.id.S)).perform((ViewActions.click()));
        onView(withText("Create")).perform(ViewActions.click());
        onView(withId(R.id.action_bar_menu_add_task)).perform(ViewActions.click());
        onView(withId(R.id.taskText)).perform((ViewActions.replaceText("testing4")));
        onView(withId(R.id.E)).perform((ViewActions.click()));
        onView(withText("Create")).perform(ViewActions.click());
    }


    @Test
    public void moveToTodayTest() {
        // Given

        // When
        onView(withText("testing1")).perform(ViewActions.longClick());
        onView(withText("Move to today")).perform(ViewActions.click());

        // Then
        onView(withId(R.id.action_bar_menu_drop)).perform(ViewActions.click());
        onView(withText("Today")).perform(ViewActions.click());
        onView(withId(R.id.task_list)).check(matches(isDisplayed()));
        onView(withText("testing1")).check(matches(isDisplayed()));
    }

    @Test
    public void moveToTomorrowTest() {
        // Given

        // When
        onView(withText("testing2")).perform(ViewActions.longClick());
        onView(withText("Move to tomorrow")).perform(ViewActions.click());

        // Then
        onView(withId(R.id.action_bar_menu_drop)).perform(ViewActions.click());
        onView(withText("Tomorrow")).perform(ViewActions.click());
        onView(withId(R.id.task_list)).check(matches(isDisplayed()));
        onView(withText("testing2")).check(matches(isDisplayed()));
    }

    @Test
    public void markTaskTest() {
        // Given

        // When
        onView(withText("testing3")).perform(ViewActions.longClick());
        onView(withText("Mark as completed")).perform(ViewActions.click());

        // Then
        onView(withId(R.id.action_bar_menu_drop)).perform(ViewActions.click());
        onView(withText("Today")).perform(ViewActions.click());
        onView(withId(R.id.task_list)).check(matches(isDisplayed()));
        onView(withText("testing3")).check(matches(isDisplayed()));
    }

    @Test
    public void deleteTaskTest() {
        // Given

        // When
        onView(withText("testing3")).perform(ViewActions.longClick());
        onView(withText("Delete task")).perform(ViewActions.click());

        // Then
        List<String> tasks = List.of("testing1", "testing2", "testing4");
        for(int i = 0; i <= 2; i++){
            onData(anything())
                    .inAdapterView(withId(R.id.task_list))
                    .atPosition(i)
                    .onChildView(ViewMatchers.withText(tasks.get(i)))
                    .check(ViewAssertions.matches(isDisplayed()));
        }
        onView(withText("testing3")).check(doesNotExist());
    }
}
