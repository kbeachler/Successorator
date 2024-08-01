package edu.ucsd.cse110.successorator.ui.tasklist;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.PopupMenu;

import androidx.annotation.NonNull;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

import edu.ucsd.cse110.successorator.R;
import edu.ucsd.cse110.successorator.databinding.ListItemTaskBinding;
import edu.ucsd.cse110.successorator.lib.domain.PendingTask;

public class PTaskListAdapter extends ArrayAdapter<PendingTask> {
    Consumer<Integer> delete;
    Consumer<Integer> moveToToday;
    Consumer<Integer> moveToTomorrow;
    Consumer<Integer> finish;
    Consumer<Integer> mark;

    public PTaskListAdapter(
            Context context,
            List<PendingTask> ptasks,
            Consumer<Integer> delete,
            Consumer<Integer> moveToToday,
            Consumer<Integer> moveToTomorrow,
            Consumer<Integer> finish,
            Consumer<Integer> mark
    ) {

        super(context, 0, new ArrayList<>(ptasks));
        this.delete= delete;
        this.moveToToday = moveToToday;
        this.moveToTomorrow = moveToTomorrow;
        this.finish = finish;
        this.mark = mark;
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


        binding.taskDeleteButton.setOnLongClickListener(v -> {
            var id = task.id();
            assert id!=null;
            PopupMenu menu = new PopupMenu(getContext(), binding.taskText);
            menu.inflate(R.menu.pending_context_menu);
            menu.setOnMenuItemClickListener(menuItem -> {
                if(menuItem.getItemId() == R.id.pending_to_today) {
                    moveToToday.accept(id);
                }
                else if(menuItem.getItemId() == R.id.pending_to_tomorrow) {
                    moveToTomorrow.accept(id);

                }
                else if(menuItem.getItemId() == R.id.pending_mark_as_complete) {
                    finish.accept(id);

                }
                else if(menuItem.getItemId() == R.id.pending_delete) {
                    delete.accept(id);
                }

                return false;
            });
            menu.show();
            return false;
        });

        return binding.getRoot();
    }

    @SuppressLint("UseCompatLoadingForDrawables")
    public void setColor(PendingTask task, ListItemTaskBinding binding){
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
