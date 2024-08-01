package edu.ucsd.cse110.successorator.ui.dialog;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import java.util.Calendar;

import edu.ucsd.cse110.successorator.R;

public class DatePickerDialogFragment extends DialogFragment{
    private CreateRecurringTaskDialogFragment listener;
    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        final Calendar c = Calendar.getInstance();
        int year = c.get(Calendar.YEAR);
        int month = c.get(Calendar.MONTH);
        int day = c.get(Calendar.DAY_OF_MONTH);
        // Create a new instance of DatePickerDialog and return it.
        return new DatePickerDialog(requireContext(), R.style.DialogTheme, listener, year, month, day);
    }
    public static DatePickerDialogFragment newInstance(){
        var fragment = new DatePickerDialogFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }
    public void setListener(CreateRecurringTaskDialogFragment createRecurringTaskDialogFragment){
        listener = createRecurringTaskDialogFragment;
    }
}
