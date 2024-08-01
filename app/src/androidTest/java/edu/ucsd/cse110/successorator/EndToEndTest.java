package edu.ucsd.cse110.successorator;

import static androidx.test.espresso.Espresso.onData;
import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.pressBack;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.contrib.DrawerActions.open;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.isRoot;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.CoreMatchers.anything;

import android.os.SystemClock;

import androidx.room.Room;
import androidx.test.core.app.ActivityScenario;
import androidx.test.espresso.action.ViewActions;
import androidx.test.espresso.assertion.ViewAssertions;
import androidx.test.espresso.matcher.ViewMatchers;
import androidx.test.platform.app.InstrumentationRegistry;

import org.junit.Before;
import org.junit.Test;

import edu.ucsd.cse110.successorator.data.db.RoomTaskRepository;
import edu.ucsd.cse110.successorator.data.db.SuccessaratorDatabase;
import edu.ucsd.cse110.successorator.lib.domain.TaskRepository;

public class EndToEndTest {
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
    public void endToEnd(){
        //Given
        onView(withId(R.id.action_bar_menu_add_task)).perform(ViewActions.click());
        onView(withId(R.id.taskText)).perform((ViewActions.replaceText("finish revising paper")));

        //When
        onView(withId(R.id.S)).perform((ViewActions.click()));
        onView(withText("Create")).perform(ViewActions.click());
        onView(withId(R.id.task_list)).check(matches(isDisplayed()));
        onView(withText("finish revising paper")).check(matches(isDisplayed()));

        //Then
        onData(anything())
                .inAdapterView(withId(R.id.task_list))
                .atPosition(0)
                .onChildView(ViewMatchers.withText("finish revising paper"))
                .check(ViewAssertions.matches(isDisplayed()));

        //Add Tomorrow Task
        //Given
        onView(withId(R.id.action_bar_menu_drop)).perform(ViewActions.click());
        onView(withText("Tomorrow")).perform(ViewActions.click());
        onView(withId(R.id.action_bar_menu_add_task)).perform(ViewActions.click());
        onView(withId(R.id.taskText)).perform((ViewActions.replaceText("turn in paper")));

        //When
        onView(withId(R.id.S)).perform((ViewActions.click()));
        onView(withText("Create")).perform(ViewActions.click());
        onView(withId(R.id.task_list)).check(matches(isDisplayed()));
        onView(withText("turn in paper")).check(matches(isDisplayed()));

        //Then
        onData(anything())
                .inAdapterView(withId(R.id.task_list))
                .atPosition(0)
                .onChildView(ViewMatchers.withText("turn in paper"))
                .check(ViewAssertions.matches(isDisplayed()));

        //Add Pending Task
        //Given
        onView(withId(R.id.action_bar_menu_drop)).perform(ViewActions.click());
        onView(withText("Pending")).perform(ViewActions.click());
        onView(withId(R.id.action_bar_menu_add_task)).perform(ViewActions.click());
        onView(withId(R.id.taskText)).perform((ViewActions.replaceText("research plane ticket")));

        //When
        onView(withId(R.id.W)).perform((ViewActions.click()));
        onView(withText("Create")).perform(ViewActions.click());
        onView(withId(R.id.task_list)).check(matches(isDisplayed()));
        onView(withText("research plane ticket")).check(matches(isDisplayed()));

        //Then
        onData(anything())
                .inAdapterView(withId(R.id.task_list))
                .atPosition(0)
                .onChildView(ViewMatchers.withText("research plane ticket"))
                .check(ViewAssertions.matches(isDisplayed()));

        onView(withText("research plane ticket")).perform(ViewActions.longClick());
        SystemClock.sleep(1000);
        onView(isRoot()).perform(pressBack());
        SystemClock.sleep(1000);

        //Add Recurring Task
        //Given
        onView(withId(R.id.action_bar_menu_drop)).perform(ViewActions.click());
        onView(withText("Recurring")).perform(ViewActions.click());
        onView(withId(R.id.action_bar_menu_add_task)).perform(ViewActions.click());
        onView(withId(R.id.taskText)).perform((ViewActions.replaceText("take out trash")));

        //When
        onView(withId(R.id.E)).perform((ViewActions.click()));
        onView(withId(R.id.monthlyButton)).perform((ViewActions.click()));
        onView(withText("Pick Time")).perform(ViewActions.click());
        onView(withText("OK")).perform(ViewActions.click());
        onView(withText("Create")).perform(ViewActions.click());
        onView(withId(R.id.task_list)).check(matches(isDisplayed()));
        onView(withText("take out trash")).check(matches(isDisplayed()));

        //Then
        onData(anything())
                .inAdapterView(withId(R.id.task_list))
                .atPosition(0)
                .onChildView(ViewMatchers.withText("take out trash"))
                .check(ViewAssertions.matches(isDisplayed()));

        //Add Category Tasks
        //Given
        onView(withId(R.id.action_bar_menu_drop)).perform(ViewActions.click());
        onView(withText("Today")).perform(ViewActions.click());

        onView(withId(R.id.action_bar_menu_add_task)).perform(ViewActions.click());
        onView(withId(R.id.taskText)).perform((ViewActions.replaceText("caffe calabria - coffee")));
        onView(withId(R.id.E)).perform((ViewActions.click()));
        onView(withText("Create")).perform(ViewActions.click());

        onView(withId(R.id.action_bar_menu_add_task)).perform(ViewActions.click());
        onView(withId(R.id.taskText)).perform((ViewActions.replaceText("cook dinner")));
        onView(withId(R.id.H)).perform((ViewActions.click()));
        onView(withText("Create")).perform(ViewActions.click());

        onView(withId(R.id.action_bar_menu_add_task)).perform(ViewActions.click());
        onView(withId(R.id.taskText)).perform((ViewActions.replaceText("clean out boxes")));
        onView(withId(R.id.H)).perform((ViewActions.click()));
        onView(withText("Create")).perform(ViewActions.click());

        // When
        onView(withId(R.id.drawer_layout))
                .perform(open());
        onView(withId(R.id.school))
                .perform(ViewActions.click());

        //Then
        onData(anything())
                .inAdapterView(withId(R.id.task_list))
                .atPosition(0)
                .onChildView(ViewMatchers.withText("finish revising paper"))
                .check(ViewAssertions.matches(isDisplayed()));
    }
}
