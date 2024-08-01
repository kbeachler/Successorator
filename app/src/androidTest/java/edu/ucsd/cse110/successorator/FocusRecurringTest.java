package edu.ucsd.cse110.successorator;

import static androidx.test.espresso.Espresso.onData;
import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.contrib.DrawerActions.open;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withClassName;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.CoreMatchers.anything;

import android.widget.DatePicker;

import androidx.room.Room;
import androidx.test.core.app.ActivityScenario;
import androidx.test.espresso.action.ViewActions;
import androidx.test.espresso.assertion.ViewAssertions;
import androidx.test.espresso.contrib.PickerActions;
import androidx.test.espresso.matcher.ViewMatchers;
import androidx.test.platform.app.InstrumentationRegistry;

import org.hamcrest.Matchers;
import org.junit.Before;
import org.junit.Test;

import java.util.Calendar;

import edu.ucsd.cse110.successorator.data.db.RoomTaskRepository;
import edu.ucsd.cse110.successorator.data.db.SuccessaratorDatabase;
import edu.ucsd.cse110.successorator.lib.domain.TaskRepository;

public class FocusRecurringTest {
    public SuccessoratorApplication app = (SuccessoratorApplication) InstrumentationRegistry.getInstrumentation().getTargetContext().getApplicationContext();
    public TaskRepository taskRepo;

    public int year;
    public int month;
    public int day;

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

        // Get a Calendar instance set to the current date and time
        Calendar calendar = Calendar.getInstance();
        // Step forward 5 days
        calendar.add(Calendar.DAY_OF_MONTH, 5);

        // Extract the year, month, and day from the Calendar instance
        this.year = calendar.get(Calendar.YEAR);
        // Note: Month is 0-based, so add 1 for a 1-based month.
        this.month = calendar.get(Calendar.MONTH) + 1;
        this.day = calendar.get(Calendar.DAY_OF_MONTH);

        onView(withId(R.id.action_bar_menu_drop)).perform(ViewActions.click());
        onView(withText("Recurring")).perform(ViewActions.click());
    }

    @Test
    public void testHFocus() { //Test Task in H Context View
        //Given
        onView(withId(R.id.action_bar_menu_add_task)).perform(ViewActions.click());
        onView(withId(R.id.taskText)).perform((ViewActions.replaceText("testing1")));
        onView(withId(R.id.H)).perform((ViewActions.click()));
        onView(withText("Pick Time")).perform(ViewActions.click());
        onView(withClassName(Matchers.equalTo(DatePicker.class.getName()))).perform(PickerActions.setDate(year, month, day));
        onView(withText("OK")).perform(ViewActions.click());
        onView(withText("Create")).perform(ViewActions.click());
        onView(withId(R.id.task_list)).check(matches(isDisplayed()));
        onView(withText("testing1")).check(matches(isDisplayed()));

        onView(withId(R.id.action_bar_menu_add_task)).perform(ViewActions.click());
        onView(withId(R.id.taskText)).perform((ViewActions.replaceText("testing2")));
        onView(withId(R.id.W)).perform((ViewActions.click()));
        onView(withText("Pick Time")).perform(ViewActions.click());
        onView(withClassName(Matchers.equalTo(DatePicker.class.getName()))).perform(PickerActions.setDate(year, month, day));
        onView(withText("OK")).perform(ViewActions.click());
        onView(withText("Create")).perform(ViewActions.click());
        onView(withId(R.id.task_list)).check(matches(isDisplayed()));
        onView(withText("testing2")).check(matches(isDisplayed()));

        //When
        onView(withId(R.id.drawer_layout))
                .perform(open());
        onView(withId(R.id.home))
                .perform(ViewActions.click());

        //Then
        onData(anything())
                .inAdapterView(withId(R.id.task_list))
                .atPosition(0)
                .onChildView(ViewMatchers.withText("testing1"))
                .check(ViewAssertions.matches(isDisplayed()));
    }

    @Test
    public void testEFocus() { //Test Task in E Context View
        //Given
        onView(withId(R.id.action_bar_menu_add_task)).perform(ViewActions.click());
        onView(withId(R.id.taskText)).perform((ViewActions.replaceText("testing1")));
        onView(withId(R.id.E)).perform((ViewActions.click()));
        onView(withText("Pick Time")).perform(ViewActions.click());
        onView(withClassName(Matchers.equalTo(DatePicker.class.getName()))).perform(PickerActions.setDate(year, month, day));
        onView(withText("OK")).perform(ViewActions.click());
        onView(withText("Create")).perform(ViewActions.click());
        onView(withId(R.id.task_list)).check(matches(isDisplayed()));
        onView(withText("testing1")).check(matches(isDisplayed()));

        onView(withId(R.id.action_bar_menu_add_task)).perform(ViewActions.click());
        onView(withId(R.id.taskText)).perform((ViewActions.replaceText("testing2")));
        onView(withId(R.id.W)).perform((ViewActions.click()));
        onView(withText("Pick Time")).perform(ViewActions.click());
        onView(withClassName(Matchers.equalTo(DatePicker.class.getName()))).perform(PickerActions.setDate(year, month, day));
        onView(withText("OK")).perform(ViewActions.click());
        onView(withText("Create")).perform(ViewActions.click());
        onView(withId(R.id.task_list)).check(matches(isDisplayed()));
        onView(withText("testing2")).check(matches(isDisplayed()));

        //When
        onView(withId(R.id.drawer_layout))
                .perform(open());
        onView(withId(R.id.errands))
                .perform(ViewActions.click());

        //Then
        onData(anything())
                .inAdapterView(withId(R.id.task_list))
                .atPosition(0)
                .onChildView(ViewMatchers.withText("testing1"))
                .check(ViewAssertions.matches(isDisplayed()));
    }

    @Test
    public void testWFocus() { //Test Task in W Context View
        //Given
        onView(withId(R.id.action_bar_menu_add_task)).perform(ViewActions.click());
        onView(withId(R.id.taskText)).perform((ViewActions.replaceText("testing1")));
        onView(withId(R.id.W)).perform((ViewActions.click()));
        onView(withText("Pick Time")).perform(ViewActions.click());
        onView(withClassName(Matchers.equalTo(DatePicker.class.getName()))).perform(PickerActions.setDate(year, month, day));
        onView(withText("OK")).perform(ViewActions.click());
        onView(withText("Create")).perform(ViewActions.click());
        onView(withId(R.id.task_list)).check(matches(isDisplayed()));
        onView(withText("testing1")).check(matches(isDisplayed()));

        onView(withId(R.id.action_bar_menu_add_task)).perform(ViewActions.click());
        onView(withId(R.id.taskText)).perform((ViewActions.replaceText("testing2")));
        onView(withId(R.id.H)).perform((ViewActions.click()));
        onView(withText("Pick Time")).perform(ViewActions.click());
        onView(withClassName(Matchers.equalTo(DatePicker.class.getName()))).perform(PickerActions.setDate(year, month, day));
        onView(withText("OK")).perform(ViewActions.click());
        onView(withText("Create")).perform(ViewActions.click());
        onView(withId(R.id.task_list)).check(matches(isDisplayed()));
        onView(withText("testing2")).check(matches(isDisplayed()));

        //When
        onView(withId(R.id.drawer_layout))
                .perform(open());
        onView(withId(R.id.work))
                .perform(ViewActions.click());

        //Then
        onData(anything())
                .inAdapterView(withId(R.id.task_list))
                .atPosition(0)
                .onChildView(ViewMatchers.withText("testing1"))
                .check(ViewAssertions.matches(isDisplayed()));
    }

    @Test
    public void testSFocus() { //Test Task in S Context View
        //Given
        onView(withId(R.id.action_bar_menu_add_task)).perform(ViewActions.click());
        onView(withId(R.id.taskText)).perform((ViewActions.replaceText("testing1")));
        onView(withId(R.id.S)).perform((ViewActions.click()));
        onView(withText("Pick Time")).perform(ViewActions.click());
        onView(withClassName(Matchers.equalTo(DatePicker.class.getName()))).perform(PickerActions.setDate(year, month, day));
        onView(withText("OK")).perform(ViewActions.click());
        onView(withText("Create")).perform(ViewActions.click());
        onView(withId(R.id.task_list)).check(matches(isDisplayed()));
        onView(withText("testing1")).check(matches(isDisplayed()));

        onView(withId(R.id.action_bar_menu_add_task)).perform(ViewActions.click());
        onView(withId(R.id.taskText)).perform((ViewActions.replaceText("testing2")));
        onView(withId(R.id.H)).perform((ViewActions.click()));
        onView(withText("Pick Time")).perform(ViewActions.click());
        onView(withClassName(Matchers.equalTo(DatePicker.class.getName()))).perform(PickerActions.setDate(year, month, day));
        onView(withText("OK")).perform(ViewActions.click());
        onView(withText("Create")).perform(ViewActions.click());
        onView(withId(R.id.task_list)).check(matches(isDisplayed()));
        onView(withText("testing2")).check(matches(isDisplayed()));

        //When
        onView(withId(R.id.drawer_layout))
                .perform(open());
        onView(withId(R.id.school))
                .perform(ViewActions.click());

        //Then
        onData(anything())
                .inAdapterView(withId(R.id.task_list))
                .atPosition(0)
                .onChildView(ViewMatchers.withText("testing1"))
                .check(ViewAssertions.matches(isDisplayed()));
    }

    @Test
    public void testFocusToCancel() { //Test Task in S Context View then ALl after Cancel
        //Given
        onView(withId(R.id.action_bar_menu_add_task)).perform(ViewActions.click());
        onView(withId(R.id.taskText)).perform((ViewActions.replaceText("testing1")));
        onView(withId(R.id.S)).perform((ViewActions.click()));
        onView(withText("Pick Time")).perform(ViewActions.click());
        onView(withClassName(Matchers.equalTo(DatePicker.class.getName()))).perform(PickerActions.setDate(year, month, day));
        onView(withText("OK")).perform(ViewActions.click());
        onView(withText("Create")).perform(ViewActions.click());
        onView(withId(R.id.task_list)).check(matches(isDisplayed()));
        onView(withText("testing1")).check(matches(isDisplayed()));

        onView(withId(R.id.action_bar_menu_add_task)).perform(ViewActions.click());
        onView(withId(R.id.taskText)).perform((ViewActions.replaceText("testing2")));
        onView(withId(R.id.H)).perform((ViewActions.click()));
        onView(withText("Pick Time")).perform(ViewActions.click());
        onView(withClassName(Matchers.equalTo(DatePicker.class.getName()))).perform(PickerActions.setDate(year, month, day));
        onView(withText("OK")).perform(ViewActions.click());
        onView(withText("Create")).perform(ViewActions.click());
        onView(withId(R.id.task_list)).check(matches(isDisplayed()));
        onView(withText("testing2")).check(matches(isDisplayed()));

        //When
        onView(withId(R.id.drawer_layout))
                .perform(open());
        onView(withId(R.id.school))
                .perform(ViewActions.click());

        //Then
        onData(anything())
                .inAdapterView(withId(R.id.task_list))
                .atPosition(0)
                .onChildView(ViewMatchers.withText("testing1"))
                .check(ViewAssertions.matches(isDisplayed()));

        //When
        onView(withId(R.id.drawer_layout))
                .perform(open());
        onView(withId(R.id.cancel))
                .perform(ViewActions.click());

        //Then
        onData(anything())
                .inAdapterView(withId(R.id.task_list))
                .atPosition(1)
                .onChildView(ViewMatchers.withText("testing2"))
                .check(ViewAssertions.matches(isDisplayed()));
    }
}
