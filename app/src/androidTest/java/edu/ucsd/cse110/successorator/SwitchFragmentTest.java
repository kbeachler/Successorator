package edu.ucsd.cse110.successorator;
import androidx.room.Room;
import androidx.test.core.app.ActivityScenario;
import androidx.test.espresso.action.ViewActions;
import androidx.test.platform.app.InstrumentationRegistry;

import org.junit.Before;
import org.junit.Test;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.matcher.ViewMatchers.*;
import static androidx.test.espresso.assertion.ViewAssertions.*;

import edu.ucsd.cse110.successorator.data.db.RoomTaskRepository;
import edu.ucsd.cse110.successorator.data.db.SuccessaratorDatabase;
import edu.ucsd.cse110.successorator.lib.domain.TaskRepository;

public class SwitchFragmentTest {
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
    }

    @Test
    public void ensureStartOnHome(){
        //Test w/ no tasks
        onView(withId(R.id.enterTask)).check(matches(isDisplayed()));
        onView(withId(R.id.no_goals)).check(matches(isDisplayed()));
    }

    @Test
    public void ensureHomeToTaskList(){
        //Switch from home to task by adding a task
        onView(withId(R.id.action_bar_menu_add_task)).perform(ViewActions.click());
        onView(withId(R.id.taskText)).perform((ViewActions.replaceText("testing")));
        onView(withId(R.id.H)).perform((ViewActions.click()));
        onView(withText("Create")).perform(ViewActions.click());
        onView(withId(R.id.container)).check(matches(isDisplayed()));
        onView(withText("testing")).check(matches(isDisplayed()));
    }

    @Test
    public void ensureTaskListToHome(){
        //Switch from task to home by removing all tasks
        onView(withId(R.id.action_bar_menu_add_task)).perform(ViewActions.click());
        onView(withId(R.id.taskText)).perform((ViewActions.replaceText("testing")));
        onView(withId(R.id.H)).perform((ViewActions.click()));
        onView(withText("Create")).perform(ViewActions.click());
        onView(withId(R.id.container)).check(matches(isDisplayed()));
        onView(withText("testing")).check(matches(isDisplayed()));

        onView(withText("testing")).perform((ViewActions.click()));
        onView(withId(R.id.action_bar_menu_fast_forward)).perform(ViewActions.click());
        onView(withId(R.id.enterTask)).check(matches(isDisplayed()));
        onView(withId(R.id.no_goals)).check(matches(isDisplayed()));
    }
}
