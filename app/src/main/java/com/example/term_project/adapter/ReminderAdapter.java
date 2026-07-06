package com.example.term_project.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.example.term_project.R;
import com.example.term_project.model.Reminder;
import java.util.ArrayList;
import java.util.List;

public class ReminderAdapter extends RecyclerView.Adapter<ReminderAdapter.ReminderViewHolder> {

    public interface ReminderActionListener {
        void onReminderLongClick(Reminder reminder);
    }

    private final List<Reminder> reminders = new ArrayList<>();
    private final ReminderActionListener listener;

    public ReminderAdapter(ReminderActionListener listener) {
        this.listener = listener;
    }

    public void setReminders(List<Reminder> newReminders) {
        reminders.clear();
        reminders.addAll(newReminders);
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ReminderViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_reminder, parent, false);
        return new ReminderViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ReminderViewHolder holder, int position) {
        holder.bind(reminders.get(position));
    }

    @Override
    public int getItemCount() {
        return reminders.size();
    }

    class ReminderViewHolder extends RecyclerView.ViewHolder {

        private final TextView tvReminderTime;
        private final TextView tvReminderTitle;

        ReminderViewHolder(@NonNull View itemView) {
            super(itemView);
            tvReminderTime = itemView.findViewById(R.id.tvReminderTime);
            tvReminderTitle = itemView.findViewById(R.id.tvReminderTitle);
        }

        void bind(Reminder reminder) {
            tvReminderTime.setText("⏰ " + reminder.getTime());
            tvReminderTitle.setText(reminder.getTitle());

            itemView.setOnLongClickListener(v -> {
                if (listener != null) {
                    listener.onReminderLongClick(reminder);
                }
                return true;
            });
        }
    }
}
