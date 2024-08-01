package edu.ucsd.cse110.successorator.ui.tasklist;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import androidx.annotation.NonNull;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

import edu.ucsd.cse110.successorator.databinding.ListItemTaskBinding;
import edu.ucsd.cse110.successorator.lib.domain.Task;
import edu.ucsd.cse110.successorator.R;
public class TaskListAdapter extends ArrayAdapter<Task> {
    Consumer<Integer> onMarkClick;

    public TaskListAdapter(
            Context context,
            List<Task> tasks,
            Consumer<Integer> onMarkClick
    ) {

        super(context, 0, new ArrayList<>(tasks));
        this.onMarkClick = onMarkClick;
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
        setColor(task, binding);
        binding.taskText.setText(task.task_name());
        if (task.getMark()) {
            binding.taskText.setPaintFlags(binding.taskText.getPaintFlags() | android.graphics.Paint.STRIKE_THRU_TEXT_FLAG);
            binding.context.setBackground(getContext().getResources().getDrawable(R.drawable.ic_rounded_textview_default, null));

        }
        else {
            binding.taskText.setPaintFlags(binding.taskText.getPaintFlags() & ~android.graphics.Paint.STRIKE_THRU_TEXT_FLAG);
            setColor(task, binding);
        }

        binding.taskDeleteButton.setOnClickListener(v -> {
            var id = task.id();
            assert id!=null;
            onMarkClick.accept(id);
        });

        return binding.getRoot();
    }

    public void setColor(Task task, ListItemTaskBinding binding){
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