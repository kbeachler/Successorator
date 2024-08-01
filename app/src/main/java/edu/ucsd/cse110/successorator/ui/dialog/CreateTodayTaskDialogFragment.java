package edu.ucsd.cse110.successorator.ui.dialog;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.widget.RadioGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import androidx.lifecycle.ViewModelProvider;

import com.google.android.material.chip.Chip;
import com.google.android.material.chip.ChipGroup;

import java.util.ArrayList;
import java.util.List;

import edu.ucsd.cse110.successorator.MainViewModel;
import edu.ucsd.cse110.successorator.R;
import edu.ucsd.cse110.successorator.data.db.TimeKeep;
import edu.ucsd.cse110.successorator.databinding.FragmentDialogCreateOtherTaskBinding;
import edu.ucsd.cse110.successorator.lib.domain.RecurringTask;
import edu.ucsd.cse110.successorator.lib.domain.Task;
public class CreateTodayTaskDialogFragment extends DialogFragment {
    private FragmentDialogCreateOtherTaskBinding view;
    private MainViewModel activityModel;
    private List<Integer> ids = new ArrayList<>();
    private int year;
    private int month;
    private int day;
    private String freq;
    private String context;

    CreateTodayTaskDialogFragment() {
    }

    @NonNull
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState){
        TimeKeep timeKeep = activityModel.getTimeKeeper().getValue();
        this.day = timeKeep.getCurrentDayOfMonth();
        this.month = timeKeep.getCurrentMonth();
        this.year = timeKeep.getCurrentYear();
        this.view = FragmentDialogCreateOtherTaskBinding.inflate(getLayoutInflater());
        this.view.weeklyButton.setText("Weekly on " + timeKeep.getDayOfWeek());
        this.view.monthlyButton.setText("Monthly on " + timeKeep.getMonthly());
        this.view.annuallyButton.setText("Annually on " + timeKeep.getYearly());

        view.radioGroup.check(R.id.one_timeButton);
        freq = "ONCE";
        view.radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                if (checkedId == R.id.one_timeButton) {
                    freq = "ONCE";
                } else if (checkedId == R.id.dailyButton) {
                    freq = "DAILY";
                } else if (checkedId == R.id.weeklyButton) {
                    freq = "WEEKLY";
                } else if (checkedId == R.id.monthlyButton) {
                    freq = "MONTHLY";
                } else if (checkedId == R.id.annuallyButton) {
                    freq = "YEARLY";
                }
            }
        });

        view.contextGroup.setOnCheckedStateChangeListener(new ChipGroup.OnCheckedStateChangeListener() {
            @Override
            public void onCheckedChanged(@NonNull ChipGroup group, @NonNull List<Integer> checkedIds) {
                ids = new ArrayList<>(checkedIds);
                for (int checkedId : checkedIds) {
                    Chip chip = group.findViewById(checkedId);
                    if(chip != null) {
                        context = chip.getText().toString();
                    }

                }
            }
        });
        return new AlertDialog.Builder(getActivity())
                .setTitle("New Task")
                .setMessage("Please describe your new task.")
                .setView(view.getRoot())
                .setPositiveButton("Create", this::onPositiveButtonClick)
                .setNegativeButton("Cancel", this::onNegativeButtonClick)
                .create();
    }
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        var modelOwner = requireActivity();
        var modelFactory = ViewModelProvider.Factory.from(MainViewModel.initializer);
        var modelProvider = new ViewModelProvider(modelOwner, modelFactory);
        this.activityModel = modelProvider.get(MainViewModel.class);

    }
    private void onPositiveButtonClick(DialogInterface dialog, int which){
        var task = view.taskText.getText().toString();
        if(ids.isEmpty()){
            Toast.makeText(requireActivity(), "Please select a context", Toast.LENGTH_LONG).show();
            return;
        }
        if(!task.isEmpty()) {
            var item = new Task(null,task,-1, false, context);
            if(!freq.equals("ONCE")){
                var r_item = new RecurringTask(null, task, year, month, day, freq, context);
                activityModel.appendRecurring(item, r_item);
            } else {
                activityModel.append(item);
            }
            activityModel.setTodayNumTasks();
        } else {
            Toast.makeText(requireActivity(), "Please enter a task name", Toast.LENGTH_LONG).show();
            return;
        }
        dialog.dismiss();
    }
    private void onNegativeButtonClick(DialogInterface dialog, int which){
        dialog.cancel();
    }
    public static CreateTodayTaskDialogFragment newInstance(){
        var fragment = new CreateTodayTaskDialogFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }
}
