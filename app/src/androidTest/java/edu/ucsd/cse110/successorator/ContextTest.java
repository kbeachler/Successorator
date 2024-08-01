package edu.ucsd.cse110.successorator;

import static androidx.test.espresso.Espresso.onData;
import static androidx.test.espresso.Espresso.onView;
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

public class ContextTest {
    public SuccessoratorApplication app = (SuccessoratorApplication) InstrumentationRegistry.getInstrumentation().getTargetContext().getApplicationContext();
    public TaskRepository taskRepo;

    @Before
    public void setUp() {
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
    public void testHContext() { //Test w/ one added task
        //Given
        onView(withId(R.id.action_bar_menu_add_task)).perform(ViewActions.click());
        onView(withId(R.id.taskText)).perform((ViewActions.replaceText("testing1")));

        //When
        onView(withId(R.id.H)).perform((ViewActions.click()));
        onView(withText("Create")).perform(ViewActions.click());
        onView(withId(R.id.task_list)).check(matches(isDisplayed()));
        onView(withText("testing1")).check(matches(isDisplayed()));

        //Then
        onData(anything())
                .inAdapterView(withId(R.id.task_list))
                .atPosition(0)
                .onChildView(ViewMatchers.withText("testing1"))
                .check(ViewAssertions.matches(isDisplayed()));
        }

    @Test
    public void testEContext() { //Test w/ one added task
        //Given
        onView(withId(R.id.action_bar_menu_add_task)).perform(ViewActions.click());
        onView(withId(R.id.taskText)).perform((ViewActions.replaceText("testing1")));

        //When
        onView(withId(R.id.E)).perform((ViewActions.click()));
        onView(withText("Create")).perform(ViewActions.click());
        onView(withId(R.id.task_list)).check(matches(isDisplayed()));
        onView(withText("testing1")).check(matches(isDisplayed()));

        //Then
        onData(anything())
                .inAdapterView(withId(R.id.task_list))
                .atPosition(0)
                .onChildView(ViewMatchers.withText("testing1"))
                .check(ViewAssertions.matches(isDisplayed()));
    }

    @Test
    public void testSContext() { //Test w/ one added task
        //Given
        onView(withId(R.id.action_bar_menu_add_task)).perform(ViewActions.click());
        onView(withId(R.id.taskText)).perform((ViewActions.replaceText("testing1")));

        //When
        onView(withId(R.id.S)).perform((ViewActions.click()));
        onView(withText("Create")).perform(ViewActions.click());
        onView(withId(R.id.task_list)).check(matches(isDisplayed()));
        onView(withText("testing1")).check(matches(isDisplayed()));

        //Then
        onData(anything())
                .inAdapterView(withId(R.id.task_list))
                .atPosition(0)
                .onChildView(ViewMatchers.withText("testing1"))
                .check(ViewAssertions.matches(isDisplayed()));
    }

    @Test
    public void testWContext() { //Test w/ one added task
        //Given
        onView(withId(R.id.action_bar_menu_add_task)).perform(ViewActions.click());
        onView(withId(R.id.taskText)).perform((ViewActions.replaceText("testing1")));

        //When
        onView(withId(R.id.W)).perform((ViewActions.click()));
        onView(withText("Create")).perform(ViewActions.click());
        onView(withId(R.id.task_list)).check(matches(isDisplayed()));
        onView(withText("testing1")).check(matches(isDisplayed()));

        //Then
        onData(anything())
                .inAdapterView(withId(R.id.task_list))
                .atPosition(0)
                .onChildView(ViewMatchers.withText("testing1"))
                .check(ViewAssertions.matches(isDisplayed()));
    }

    @Test
    public void testAllContexts() { //Test w/ one added task
        //Given
        onView(withId(R.id.action_bar_menu_add_task)).perform(ViewActions.click());
        onView(withId(R.id.taskText)).perform((ViewActions.replaceText("testing1")));

        //When
        onView(withId(R.id.W)).perform((ViewActions.click()));
        onView(withText("Create")).perform(ViewActions.click());
        onView(withId(R.id.task_list)).check(matches(isDisplayed()));
        onView(withText("testing1")).check(matches(isDisplayed()));

        onView(withId(R.id.action_bar_menu_add_task)).perform(ViewActions.click());
        onView(withId(R.id.taskText)).perform((ViewActions.replaceText("testing2")));
        onView(withId(R.id.E)).perform((ViewActions.click()));
        onView(withText("Create")).perform(ViewActions.click());
        onView(withId(R.id.task_list)).check(matches(isDisplayed()));
        onView(withText("testing2")).check(matches(isDisplayed()));

        onView(withId(R.id.action_bar_menu_add_task)).perform(ViewActions.click());
        onView(withId(R.id.taskText)).perform((ViewActions.replaceText("testing3")));
        onView(withId(R.id.H)).perform((ViewActions.click()));
        onView(withText("Create")).perform(ViewActions.click());
        onView(withId(R.id.task_list)).check(matches(isDisplayed()));
        onView(withText("testing3")).check(matches(isDisplayed()));

        onView(withId(R.id.action_bar_menu_add_task)).perform(ViewActions.click());
        onView(withId(R.id.taskText)).perform((ViewActions.replaceText("testing4")));
        onView(withId(R.id.S)).perform((ViewActions.click()));
        onView(withText("Create")).perform(ViewActions.click());
        onView(withId(R.id.task_list)).check(matches(isDisplayed()));
        onView(withText("testing4")).check(matches(isDisplayed()));

        //Then
        List<String> tasks = List.of("testing3", "testing1", "testing4", "testing2");
        for(int i = 0; i <= 3; i++){
            onData(anything())
                    .inAdapterView(withId(R.id.task_list))
                    .atPosition(i)
                    .onChildView(ViewMatchers.withText(tasks.get(i)))
                    .check(ViewAssertions.matches(isDisplayed()));
        }
    }
}

