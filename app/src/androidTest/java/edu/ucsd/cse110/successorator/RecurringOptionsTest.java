package edu.ucsd.cse110.successorator;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.assertion.ViewAssertions.doesNotExist;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withClassName;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;

import android.os.SystemClock;
import android.widget.DatePicker;

import androidx.room.Room;
import androidx.test.core.app.ActivityScenario;
import androidx.test.espresso.action.ViewActions;
import androidx.test.espresso.contrib.PickerActions;
import androidx.test.platform.app.InstrumentationRegistry;

import org.hamcrest.Matchers;
import org.junit.Before;
import org.junit.Test;

import java.util.Calendar;

import edu.ucsd.cse110.successorator.data.db.RoomTaskRepository;
import edu.ucsd.cse110.successorator.data.db.SuccessaratorDatabase;
import edu.ucsd.cse110.successorator.lib.domain.TaskRepository;

public class RecurringOptionsTest {
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
    public void testScheduleGivenDate() { //Test Task in H Context View
        //Given
        onView(withId(R.id.action_bar_menu_add_task)).perform(ViewActions.click());
        onView(withId(R.id.taskText)).perform((ViewActions.replaceText("testing1")));
        onView(withId(R.id.E)).perform(ViewActions.click());
        onView(withText("Pick Time")).perform(ViewActions.click());

        // When
        onView(withClassName(Matchers.equalTo(DatePicker.class.getName()))).perform(PickerActions.setDate(year, month, day));
        onView(withText("OK")).perform(ViewActions.click());
        onView(withText("Create")).perform(ViewActions.click());

        // Then
        onView(withId(R.id.action_bar_menu_drop)).perform(ViewActions.click());
        onView(withText("Today")).perform(ViewActions.click());
        for (int i = 0; i < 5; i++) onView(withId(R.id.action_bar_menu_fast_forward)).perform(ViewActions.click());
        SystemClock.sleep(1000);
        onView(withText("testing1")).check(matches(isDisplayed()));
    }

    @Test
    public void deleteRecurringTest() {
        // Given
        onView(withId(R.id.action_bar_menu_add_task)).perform(ViewActions.click());
        onView(withId(R.id.taskText)).perform((ViewActions.replaceText("testing1")));
        onView(withId(R.id.E)).perform(ViewActions.click());
        onView(withText("Pick Time")).perform(ViewActions.click());
        onView(withClassName(Matchers.equalTo(DatePicker.class.getName()))).perform(PickerActions.setDate(year, month, day));
        onView(withText("OK")).perform(ViewActions.click());
        onView(withText("Create")).perform(ViewActions.click());

        onView(withId(R.id.action_bar_menu_add_task)).perform(ViewActions.click());
        onView(withId(R.id.taskText)).perform((ViewActions.replaceText("testing2")));
        onView(withId(R.id.E)).perform(ViewActions.click());
        onView(withText("Pick Time")).perform(ViewActions.click());
        onView(withClassName(Matchers.equalTo(DatePicker.class.getName()))).perform(PickerActions.setDate(year, month, day));
        onView(withText("OK")).perform(ViewActions.click());
        onView(withText("Create")).perform(ViewActions.click());

        // When
        onView(withText("testing2")).perform(ViewActions.longClick());
        onView(withText("Delete task")).perform(ViewActions.click());

        // Then
        onView(withText("testing1")).check(matches(isDisplayed()));
        onView(withText("testing2")).check(doesNotExist());
    }
}
