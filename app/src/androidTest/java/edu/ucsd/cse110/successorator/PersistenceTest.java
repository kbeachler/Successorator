package edu.ucsd.cse110.successorator;
import androidx.test.core.app.ActivityScenario;
import androidx.test.espresso.action.ViewActions;
import androidx.test.espresso.assertion.ViewAssertions;
import androidx.test.platform.app.InstrumentationRegistry;
import org.junit.Before;
import org.junit.Test;
import static androidx.test.espresso.Espresso.onData;
import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.matcher.ViewMatchers.*;
import static org.hamcrest.CoreMatchers.anything;
import androidx.room.Room;
import androidx.test.espresso.matcher.ViewMatchers;
import java.util.List;
import edu.ucsd.cse110.successorator.data.db.RoomTaskRepository;
import edu.ucsd.cse110.successorator.data.db.SuccessaratorDatabase;
import edu.ucsd.cse110.successorator.lib.domain.TaskRepository;
public class PersistenceTest {
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
    }
    @Test
    public void persistenceClosingAppTest() {
        //Test closing and opening app

        //Given
        ActivityScenario<MainActivity> scenario = ActivityScenario.launch(MainActivity.class);
        onView(withId(R.id.action_bar_menu_add_task)).perform(ViewActions.click());
        onView(withId(R.id.taskText)).perform((ViewActions.replaceText("testing1")));
        onView(withId(R.id.S)).perform((ViewActions.click()));
        onView(withText("Create")).perform(ViewActions.click());
        onView(withId(R.id.action_bar_menu_add_task)).perform(ViewActions.click());
        onView(withId(R.id.taskText)).perform((ViewActions.replaceText("testing2")));
        onView(withId(R.id.H)).perform((ViewActions.click()));
        onView(withText("Create")).perform(ViewActions.click());
        onView(withText("testing1")).perform((ViewActions.click()));

        //When
        scenario.close();

        //Then
        scenario = ActivityScenario.launch(MainActivity.class);
        List<String> tasks = List.of("testing2", "testing1");
        for(int i = 0; i < 2; i++){
            onData(anything())
                    .inAdapterView(withId(R.id.task_list))
                    .atPosition(i)
                    .onChildView(ViewMatchers.withText(tasks.get(i)))
                    .check(ViewAssertions.matches(isDisplayed()));
        }
    }
}