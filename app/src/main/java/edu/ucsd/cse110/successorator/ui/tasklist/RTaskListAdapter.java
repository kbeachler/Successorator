package edu.ucsd.cse110.successorator.ui.tasklist;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.PopupMenu;

import androidx.annotation.NonNull;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.function.Consumer;

import edu.ucsd.cse110.successorator.R;
import edu.ucsd.cse110.successorator.databinding.ListItemTaskBinding;
import edu.ucsd.cse110.successorator.lib.domain.RecurringTask;
import edu.ucsd.cse110.successorator.lib.domain.Task;

public class RTaskListAdapter extends ArrayAdapter<RecurringTask> {
    Consumer<Integer> delete;

    public RTaskListAdapter(
            Context context,
            List<RecurringTask> rtasks,
            Consumer<Integer> delete
    ) {
        super(context, 0, new ArrayList<>(rtasks));
        this.delete = delete;
    }

    @NonNull
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // Get the task for this position.
        var task = getItem(position);
        assert task != null;

        // Check if a view is being reused...
        ListItemTaskBinding binding;
        if (convertView != null) {
            // if so, bind to it
            binding = ListItemTaskBinding.bind(convertView);
        } else {
            // otherwise inflate a new view from our layout XML.
            var layoutInflater = LayoutInflater.from(getContext());
            binding = ListItemTaskBinding.inflate(layoutInflater, parent, false);
        }

        binding.taskText.setText(task.task_name());
        setColor(task, binding);
        LocalDate date = LocalDate.of(task.created_year(), task.created_month(), task.created_day());
        String info = "";
        if (task.freq().equals("DAILY")) {
            info = "daily";
        } else if (task.freq().equals("WEEKLY")) {
            info = "weekly on " + date.format(DateTimeFormatter.ofPattern("EEEE", Locale.US));
        } else if (task.freq().equals("MONTHLY")) {
            LocalDate firstDayOfMonth = date.withDayOfMonth(1);
            // Iterate through the days of the month
            for (int i = 1; i <= firstDayOfMonth.lengthOfMonth(); i++) {
                LocalDate currentDate = firstDayOfMonth.withDayOfMonth(i);
                if (currentDate.getDayOfWeek().equals(date.getDayOfWeek()) && currentDate.getDayOfMonth() == date.getDayOfMonth()) {
                    int num = (i + 7 - 1) / 7;
                    if(num == 1){
                        info = "monthly on " + num + "st " + date.format(DateTimeFormatter.ofPattern("EEEE", Locale.US));
                    } else if(num == 2){
                        info = "monthly on " + num + "nd " + date.format(DateTimeFormatter.ofPattern("EEEE", Locale.US));
                    } else if(num == 3){
                        info = "monthly on " + num + "rd " + date.format(DateTimeFormatter.ofPattern("EEEE", Locale.US));
                    } else {
                        info = "monthly on " + num + "th " + date.format(DateTimeFormatter.ofPattern("EEEE", Locale.US));
                    }
                }
            }

        } else if (task.freq().equals("YEARLY")) {
            info = "annually on " + date.format(DateTimeFormatter.ofPattern("M/d", Locale.US));
        }
        binding.freq.setText(info);


        binding.taskDeleteButton.setOnLongClickListener(v -> {
            var id = task.id();
            assert id!=null;
            PopupMenu menu = new PopupMenu(getContext(), binding.taskText);
            menu.inflate(R.menu.recurring_context_menu);
            menu.setOnMenuItemClickListener(menuItem -> {
                 if(menuItem.getItemId() == R.id.recurring_delete) {
                    delete.accept(id);
                }
                return false;
            });
            menu.show();
            return false;
        });

        return binding.getRoot();
    }
    public void setColor(RecurringTask task, ListItemTaskBinding binding){
        if(task.context().equals("H")){
            binding.context.setBackground(getContext().getResources().getDrawable(R.drawable.ic_rounded_textview_h, null));
            binding.context.setText("H");
        } else if(task.context().equals("S")){
            binding.context.setBackground(getContext().getResources().getDrawable(R.drawable.ic_rounded_textview_s, null));
            binding.context.setText("S");
        } else if(task.context().equals("W")){
            binding.context.setBackground(getContext().getResources().getDrawable(R.drawable.ic_rounded_textview_w, null));
            binding.context.setText("W");
        } else if(task.context().equals("E")){
            binding.context.setBackground(getContext().getResources().getDrawable(R.drawable.ic_rounded_textview_e, null));
            binding.context.setText("E");
        }
    }

    @Override
    public long getItemId(int position) {
        var task = getItem(position);
        assert task != null;

        var id = task.id();
        assert id != null;

        return id;
    }
}
