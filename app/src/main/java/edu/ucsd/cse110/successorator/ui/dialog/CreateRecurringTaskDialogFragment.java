package edu.ucsd.cse110.successorator.ui.dialog;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.View;
import android.widget.DatePicker;
import android.widget.RadioGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import androidx.lifecycle.ViewModelProvider;

import com.google.android.material.chip.Chip;
import com.google.android.material.chip.ChipGroup;

import java.text.DateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

import edu.ucsd.cse110.successorator.MainViewModel;
import edu.ucsd.cse110.successorator.R;
import edu.ucsd.cse110.successorator.databinding.FragmentDialogCreateRecurringTaskBinding;
import edu.ucsd.cse110.successorator.lib.domain.RecurringTask;
import edu.ucsd.cse110.successorator.lib.domain.Task;

public class CreateRecurringTaskDialogFragment extends DialogFragment implements DatePickerDialog.OnDateSetListener {
    private FragmentDialogCreateRecurringTaskBinding view;
    private MainViewModel activityModel;
    private int year;
    private int month;
    private int day;
    private List<Integer> ids  = new ArrayList<>();;
    private String freq;
    private String context;

    CreateRecurringTaskDialogFragment() {
    }

    @NonNull
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState){
        this.view = FragmentDialogCreateRecurringTaskBinding.inflate(getLayoutInflater());
        year = -1;
        month = -1;
        day = -1;
        view.radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                if (checkedId == R.id.dailyButton) {
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
        view.radioGroup.check(R.id.dailyButton);

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
        view.pickTime.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                var datePickerFragment = DatePickerDialogFragment.newInstance();
                datePickerFragment.setListener(CreateRecurringTaskDialogFragment.this);
                datePickerFragment.show(getParentFragmentManager(), "DatePickerDialogFragment");
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
        if(year == -1 && month == -1 && day == -1){
            Toast.makeText(requireActivity(), "Please select a start date", Toast.LENGTH_LONG).show();
            return;
        }
        if(ids.isEmpty()){
            Toast.makeText(requireActivity(), "Please select a context", Toast.LENGTH_LONG).show();
            return;
        }
        if(!task.isEmpty()) {
            var item = new Task(null,task,-1, false, context);
            var r_item = new RecurringTask(null, task, year, month+1, day, freq, context);
            activityModel.appendRecurring(item, r_item);
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
    public static CreateRecurringTaskDialogFragment newInstance(){
        var fragment = new CreateRecurringTaskDialogFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
        Calendar c = Calendar.getInstance();
        c.set(Calendar.YEAR, year);
        c.set(Calendar.MONTH, month);
        c.set(Calendar.DAY_OF_MONTH, dayOfMonth);
        this.year = year;
        this.month = month;
        this.day = dayOfMonth;
        String currentDateString = DateFormat.getDateInstance(DateFormat.FULL).format(c.getTime());
        setCustomDateText(currentDateString);
        setRadioButtons();
    }

    public void setCustomDateText(String currentDateString){
        view.customDate.setText(currentDateString);
    }

    public void setRadioButtons(){
        LocalDate date = LocalDate.of(year, month + 1, day);
        view.weeklyButton.setText("Weekly on " + date.format(DateTimeFormatter.ofPattern("EEEE", Locale.US)));
        LocalDate firstDayOfMonth = date.withDayOfMonth(1);
        // Iterate through the days of the month
        String info = "";
        for (int i = 1; i <= firstDayOfMonth.lengthOfMonth(); i++) {
            LocalDate currentDate = firstDayOfMonth.withDayOfMonth(i);
            if (currentDate.getDayOfWeek().equals(date.getDayOfWeek()) && currentDate.getDayOfMonth() == date.getDayOfMonth()) {
                int num = (i + 7 - 1) / 7;
                if(num == 1){
                    info = "Monthly on " + num + "st " + date.format(DateTimeFormatter.ofPattern("EEEE", Locale.US));
                } else if(num == 2){
                    info = "Monthly on " + num + "nd " + date.format(DateTimeFormatter.ofPattern("EEEE", Locale.US));
                } else if(num == 3){
                    info = "Monthly on " + num + "rd " + date.format(DateTimeFormatter.ofPattern("EEEE", Locale.US));
                } else {
                    info = "Monthly on " + num + "th " + date.format(DateTimeFormatter.ofPattern("EEEE", Locale.US));
                }
            }
        }
        view.monthlyButton.setText(info);
        view.annuallyButton.setText("Annually on " + date.format(DateTimeFormatter.ofPattern("M/d", Locale.US)));
    }
}
