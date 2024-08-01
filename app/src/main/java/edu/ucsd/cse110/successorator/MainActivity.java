package edu.ucsd.cse110.successorator;
import android.os.Bundle;

import android.view.Menu;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.lifecycle.ViewModelProvider;

import com.google.android.material.navigation.NavigationView;

import java.time.LocalDateTime;
import java.util.Objects;

import edu.ucsd.cse110.successorator.data.db.TimeKeep;
import edu.ucsd.cse110.successorator.databinding.ActivityMainBinding;
import edu.ucsd.cse110.successorator.ui.dialog.CreatePendingTaskDialogFragment;
import edu.ucsd.cse110.successorator.ui.dialog.CreateTodayTaskDialogFragment;
import edu.ucsd.cse110.successorator.ui.dialog.CreateTomorrowTaskDialogFragment;

import edu.ucsd.cse110.successorator.ui.dialog.CreateRecurringTaskDialogFragment;
import edu.ucsd.cse110.successorator.ui.home.HomeFragment;
import edu.ucsd.cse110.successorator.ui.tasklist.PendingListFragment;
import edu.ucsd.cse110.successorator.ui.tasklist.RecurringListFragment;
import edu.ucsd.cse110.successorator.ui.tasklist.TaskListFragment;
import edu.ucsd.cse110.successorator.ui.tasklist.TomorrowListFragment;


public class MainActivity extends AppCompatActivity {
    private ActivityMainBinding view;
    private MainViewModel mainViewModel;
    public DrawerLayout drawerLayout;
    public NavigationView navigationView;
    public ActionBarDrawerToggle actionBarDrawerToggle;

    public String focus = "ALL";


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.view = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(view.getRoot());
        mainViewModel = new ViewModelProvider(this).get(MainViewModel.class);

        drawerLayout = view.drawerLayout;
        drawerLayout.setId(R.id.drawer_layout);

        navigationView = view.navView;
        navigationView.setItemIconTintList(null);
        actionBarDrawerToggle = new ActionBarDrawerToggle(this, drawerLayout, R.string.nav_open, R.string.nav_close);

        drawerLayout.addDrawerListener(actionBarDrawerToggle);

        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                var itemId = item.getItemId();
                if (itemId == R.id.home) {
                    focus = "H";
                    actionBarDrawerToggle.getDrawerArrowDrawable().setColor(getResources().getColor(R.color.h, null));
                } else if (itemId == R.id.work) {
                    focus = "W";
                    actionBarDrawerToggle.getDrawerArrowDrawable().setColor(getResources().getColor(R.color.w, null));
                } else if (itemId == R.id.school) {
                    focus = "S";
                    actionBarDrawerToggle.getDrawerArrowDrawable().setColor(getResources().getColor(R.color.s, null));
                } else if (itemId == R.id.errands) {
                    focus = "E";
                    actionBarDrawerToggle.getDrawerArrowDrawable().setColor(getResources().getColor(R.color.e, null));
                } else if (itemId == R.id.cancel) {
                    focus = "ALL";
                    actionBarDrawerToggle.getDrawerArrowDrawable().setColor(getResources().getColor(R.color.white, null));
                }
                mainViewModel.setFocus(focus);

                drawerLayout.closeDrawer(GravityCompat.START);
                return false;
            }
        });

        actionBarDrawerToggle.syncState();

        TimeKeep timeKeep = mainViewModel.getTimeKeeper().getValue();

        view.dateTime.setText("Today, " + timeKeep.getCurrDate());

        launchInitFrag();
    }
    @Override
    protected void onResume() {
        super.onResume();
        TimeKeep timekeeper = mainViewModel.getTimeKeeper().getValue();
        timekeeper.setDateTime(LocalDateTime.now());
        mainViewModel.setTimeKeeper(timekeeper);
        timekeeper = mainViewModel.getTimeKeeper().getValue();
        view.dateTime.setText("Today, " + timekeeper.getCurrDate());
        launchInitFrag();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.action_bar, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        mainViewModel = new ViewModelProvider(this).get(MainViewModel.class);
        TimeKeep timeKeep = mainViewModel.getTimeKeeper().getValue();
        var itemId = item.getItemId();
        String text = view.dateTime.getText().toString();
        if (itemId == R.id.action_bar_menu_add_task) {
            if (text.contains("Recurring")) {
                var dialogFragment = CreateRecurringTaskDialogFragment.newInstance();
                dialogFragment.show(getSupportFragmentManager(), "CreateRecurringTaskDialogFragment");
            }
            else if (text.contains("Pending")) {
                var dialogFragment = CreatePendingTaskDialogFragment.newInstance();
                dialogFragment.show(getSupportFragmentManager(), "CreatePendingTaskDialogFragment");
            }
            else if (text.contains("Today")){
                var dialogFragment = CreateTodayTaskDialogFragment.newInstance();
                dialogFragment.show(getSupportFragmentManager(), "CreateOtherTaskDialogFragment");
            }
            else{
                var dialogFragment = CreateTomorrowTaskDialogFragment.newInstance();
                dialogFragment.show(getSupportFragmentManager(), "CreateOtherTaskDialogFragment");
            }
        }
        else if (itemId == R.id.action_bar_menu_fast_forward){
            timeKeep.forwardDateTime();
            this.view.dateTime.setText("Today, " + timeKeep.getCurrDate());
            mainViewModel.setTimeKeeper(timeKeep);
        }
        if (itemId == R.id.today_text){
            view.dateTime.setText("Today, " + timeKeep.getCurrDate());
            getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.container, new TaskListFragment())
                    .commit();
        }
        else if (itemId == R.id.tomorrow_text){
            view.dateTime.setText(timeKeep.getNextDay());
            getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.container, new TomorrowListFragment())
                    .commit();
        }
        else if (itemId == R.id.pending_text){
            view.dateTime.setText("Pending Tasks");
            getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.container, new PendingListFragment())
                    .commit();
        }
        else if (itemId == R.id.recurring_text){
            view.dateTime.setText("Recurring Tasks");
            getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.container, new RecurringListFragment())
                    .commit();
        }
        if(actionBarDrawerToggle.onOptionsItemSelected(item)){
            return true;
        }
        launchInitFrag();
        return super.onOptionsItemSelected(item);
    }


    public void launchInitFrag(){
        mainViewModel.getTodayNumTasks().observe(num -> {
            String text = view.dateTime.getText().toString();
            if(text.contains("Today") && num == 0){
                getSupportFragmentManager()
                        .beginTransaction()
                        .replace(R.id.container, new HomeFragment())
                        .commit();
            }
            else if(text.contains("Today") && num != 0) {
                getSupportFragmentManager()
                        .beginTransaction()
                        .replace(R.id.container, new TaskListFragment())
                        .commit();
            }
        });
    }
}


