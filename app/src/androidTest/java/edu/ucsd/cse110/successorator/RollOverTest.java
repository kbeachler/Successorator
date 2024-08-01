package edu.ucsd.cse110.successorator;

import androidx.room.Room;
import androidx.test.core.app.ActivityScenario;
import androidx.test.espresso.UiController;
import androidx.test.espresso.ViewAction;
import androidx.test.espresso.ViewInteraction;
import androidx.test.espresso.action.ViewActions;
import androidx.test.espresso.assertion.ViewAssertions;
import androidx.test.platform.app.InstrumentationRegistry;

import org.hamcrest.Matcher;
import org.junit.Before;
import org.junit.Test;

import static androidx.test.espresso.Espresso.onData;
import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.matcher.ViewMatchers.*;
import static androidx.test.espresso.assertion.ViewAssertions.*;
import static org.hamcrest.CoreMatchers.anything;
import static org.junit.Assert.assertNotEquals;


import android.view.View;
import android.widget.TextView;
import androidx.test.espresso.matcher.ViewMatchers;

import java.util.List;

import edu.ucsd.cse110.successorator.data.db.RoomTaskRepository;
import edu.ucsd.cse110.successorator.data.db.SuccessaratorDatabase;
import edu.ucsd.cse110.successorator.lib.domain.TaskRepository;

public class RollOverTest {
    public static String getText(ViewInteraction matcher) {
        final String[] text = new String[1];
        matcher.perform(new ViewAction() {
            @Override
            public Matcher<View> getConstraints() {
                return ViewMatchers.isAssignableFrom(TextView.class);
            }

            @Override
            public String getDescription() {
                return "Text of the view";
            }

            @Override
            public void perform(UiController uiController, View view) {
                TextView tv = (TextView) view;
                text[0] = tv.getText().toString();
            }
        });
        return text[0];
    }
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
    public void ensureDateChange(){
        String text1 = getText(onView(withId(R.id.date_time)));
        onView(withId(R.id.action_bar_menu_fast_forward)).perform(ViewActions.click());
        String text2 = getText(onView(withId(R.id.date_time)));
        assertNotEquals(text1, text2);
    }

    @Test
    public void ensureMarkedTaskDeletion(){
        onView(withId(R.id.action_bar_menu_add_task)).perform(ViewActions.click());
        onView(withId(R.id.taskText)).perform((ViewActions.replaceText("testing1")));
        onView(withId(R.id.H)).perform((ViewActions.click()));
        onView(withText("Create")).perform(ViewActions.click());
        onView(withId(R.id.task_list)).check(matches(isDisplayed()));
        onView(withText("testing1")).check(matches(isDisplayed()));

        onView(withText("testing1")).perform((ViewActions.click()));

        onView(withId(R.id.action_bar_menu_fast_forward)).perform(ViewActions.click());

        onView(withText("testing1")).check(doesNotExist());
    }

    @Test
    public void ensureMarkedTasksDeletion(){
        onView(withId(R.id.action_bar_menu_add_task)).perform(ViewActions.click());
        onView(withId(R.id.taskText)).perform((ViewActions.replaceText("testing1")));
        onView(withId(R.id.H)).perform((ViewActions.click()));
        onView(withText("Create")).perform(ViewActions.click());
        onView(withId(R.id.task_list)).check(matches(isDisplayed()));
        onView(withText("testing1")).check(matches(isDisplayed()));

        onView(withId(R.id.action_bar_menu_add_task)).perform(ViewActions.click());
        onView(withId(R.id.taskText)).perform((ViewActions.replaceText("testing2")));
        onView(withId(R.id.H)).perform((ViewActions.click()));
        onView(withText("Create")).perform(ViewActions.click());
        onView(withId(R.id.task_list)).check(matches(isDisplayed()));
        onView(withText("testing2")).check(matches(isDisplayed()));

        onView(withText("testing1")).perform((ViewActions.click()));
        onView(withText("testing2")).perform((ViewActions.click()));

        onView(withId(R.id.action_bar_menu_fast_forward)).perform(ViewActions.click());

        onView(withText("testing1")).check(doesNotExist());
        onView(withText("testing2")).check(doesNotExist());
    }

    @Test
    public void ensureUnmarkedTaskRollover(){
        onView(withId(R.id.action_bar_menu_add_task)).perform(ViewActions.click());
        onView(withId(R.id.taskText)).perform((ViewActions.replaceText("testing1")));
        onView(withId(R.id.H)).perform((ViewActions.click()));
        onView(withText("Create")).perform(ViewActions.click());
        onView(withId(R.id.task_list)).check(matches(isDisplayed()));
        onView(withText("testing1")).check(matches(isDisplayed()));

        onView(withId(R.id.action_bar_menu_fast_forward)).perform(ViewActions.click());

        onView(withText("testing1")).check(matches(isDisplayed()));
    }

    @Test
    public void ensureUnmarkedTasksRollover(){
        onView(withId(R.id.action_bar_menu_add_task)).perform(ViewActions.click());
        onView(withId(R.id.taskText)).perform((ViewActions.replaceText("testing1")));
        onView(withId(R.id.H)).perform((ViewActions.click()));
        onView(withText("Create")).perform(ViewActions.click());
        onView(withId(R.id.task_list)).check(matches(isDisplayed()));
        onView(withText("testing1")).check(matches(isDisplayed()));

        onView(withId(R.id.action_bar_menu_add_task)).perform(ViewActions.click());
        onView(withId(R.id.taskText)).perform((ViewActions.replaceText("testing2")));
        onView(withId(R.id.H)).perform((ViewActions.click()));
        onView(withText("Create")).perform(ViewActions.click());
        onView(withId(R.id.task_list)).check(matches(isDisplayed()));
        onView(withText("testing2")).check(matches(isDisplayed()));

        onView(withId(R.id.action_bar_menu_fast_forward)).perform(ViewActions.click());

        onView(withText("testing1")).check(matches(isDisplayed()));
        onView(withText("testing2")).check(matches(isDisplayed()));
    }

    @Test
    public void ensureCombinedTasks(){
        //ActivityScenario.launch(MainActivity.class);
        onView(withId(R.id.action_bar_menu_add_task)).perform(ViewActions.click());
        onView(withId(R.id.taskText)).perform((ViewActions.replaceText("testing1")));
        onView(withId(R.id.H)).perform((ViewActions.click()));
        onView(withText("Create")).perform(ViewActions.click());
        onView(withId(R.id.task_list)).check(matches(isDisplayed()));
        onView(withText("testing1")).check(matches(isDisplayed()));

        onView(withId(R.id.action_bar_menu_add_task)).perform(ViewActions.click());
        onView(withId(R.id.taskText)).perform((ViewActions.replaceText("testing2")));
        onView(withId(R.id.H)).perform((ViewActions.click()));
        onView(withText("Create")).perform(ViewActions.click());
        onView(withId(R.id.task_list)).check(matches(isDisplayed()));
        onView(withText("testing2")).check(matches(isDisplayed()));

        onView(withId(R.id.action_bar_menu_add_task)).perform(ViewActions.click());
        onView(withId(R.id.taskText)).perform((ViewActions.replaceText("testing3")));
        onView(withId(R.id.H)).perform((ViewActions.click()));
        onView(withText("Create")).perform(ViewActions.click());
        onView(withId(R.id.task_list)).check(matches(isDisplayed()));
        onView(withText("testing3")).check(matches(isDisplayed()));

        onView(withText("testing2")).perform((ViewActions.click()));
        onView(withId(R.id.action_bar_menu_fast_forward)).perform(ViewActions.click());


        onView(withText("testing1")).check(matches(isDisplayed()));
        onView(withText("testing3")).check(matches(isDisplayed()));
        onView(withText("testing2")).check(doesNotExist());

    }

    @Test
    public void ensureRolloverOrder(){
        onView(withId(R.id.action_bar_menu_add_task)).perform(ViewActions.click());
        onView(withId(R.id.taskText)).perform((ViewActions.replaceText("testing1")));
        onView(withId(R.id.H)).perform((ViewActions.click()));
        onView(withText("Create")).perform(ViewActions.click());
        onView(withId(R.id.task_list)).check(matches(isDisplayed()));
        onView(withText("testing1")).check(matches(isDisplayed()));

        onView(withId(R.id.action_bar_menu_add_task)).perform(ViewActions.click());
        onView(withId(R.id.taskText)).perform((ViewActions.replaceText("testing2")));
        onView(withId(R.id.H)).perform((ViewActions.click()));
        onView(withText("Create")).perform(ViewActions.click());
        onView(withId(R.id.task_list)).check(matches(isDisplayed()));
        onView(withText("testing2")).check(matches(isDisplayed()));

        onView(withId(R.id.action_bar_menu_add_task)).perform(ViewActions.click());
        onView(withId(R.id.taskText)).perform((ViewActions.replaceText("testing3")));
        onView(withId(R.id.H)).perform((ViewActions.click()));
        onView(withText("Create")).perform(ViewActions.click());
        onView(withId(R.id.task_list)).check(matches(isDisplayed()));
        onView(withText("testing3")).check(matches(isDisplayed()));

        onView(withText("testing2")).perform((ViewActions.click()));

        onView(withId(R.id.action_bar_menu_fast_forward)).perform(ViewActions.click());

        List<String> expected = List.of("testing1", "testing3");

        for(int i = 0; i < 2; i++){
            onData(anything())
                    .inAdapterView(withId(R.id.task_list))
                    .atPosition(i)
                    .onChildView(ViewMatchers.withText(expected.get(i)))
                    .check(ViewAssertions.matches(isDisplayed()));
        }

        onView(withText("testing2")).check(doesNotExist());
    }
}
