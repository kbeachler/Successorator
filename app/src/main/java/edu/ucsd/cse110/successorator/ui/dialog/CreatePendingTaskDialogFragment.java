package edu.ucsd.cse110.successorator.ui.dialog;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
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
import edu.ucsd.cse110.successorator.databinding.FragmentDialogCreateTaskBinding;
import edu.ucsd.cse110.successorator.databinding.ListItemTaskBinding;
import edu.ucsd.cse110.successorator.lib.domain.Task;

public class CreatePendingTaskDialogFragment extends DialogFragment {
    private FragmentDialogCreateTaskBinding view;
    private MainViewModel activityModel;
    private String context;
    private List<Integer> ids = new ArrayList<>();

    CreatePendingTaskDialogFragment() {
    }

    @NonNull
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState){
        this.view = FragmentDialogCreateTaskBinding.inflate(getLayoutInflater());

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
            activityModel.appendP(item);
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
    public static CreatePendingTaskDialogFragment newInstance(){
        var fragment = new CreatePendingTaskDialogFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }
}
