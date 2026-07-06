package com.example.term_project.activity;

import android.app.TimePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;
import com.example.term_project.R;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;
import java.util.Calendar;
import java.util.Locale;

public class AddReminderActivity extends BaseActivity {

    public static final String EXTRA_REMINDER_TITLE = "reminder_title";
    public static final String EXTRA_REMINDER_TIME = "reminder_time";

    private static final String TAG = "AddReminderActivity";

    private TextInputEditText etReminderTitle;
    private TextInputEditText etReminderTime;
    private MaterialButton btnSaveReminder;

    @Override
    protected String getLogTag() {
        return TAG;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_reminder);

        etReminderTitle = findViewById(R.id.etReminderTitle);
        etReminderTime = findViewById(R.id.etReminderTime);
        btnSaveReminder = findViewById(R.id.btnSaveReminder);

        etReminderTime.setOnClickListener(v -> showTimePicker());

        btnSaveReminder.setOnClickListener(v -> saveReminder());
    }

    private void showTimePicker() {
        Calendar currentTime = Calendar.getInstance();
        int hour = currentTime.get(Calendar.HOUR_OF_DAY);
        int minute = currentTime.get(Calendar.MINUTE);

        TimePickerDialog timePicker = new TimePickerDialog(
                this,
                (view, selectedHour, selectedMinute) -> {
                    String formattedTime = String.format(
                            Locale.getDefault(), "%02d:%02d", selectedHour, selectedMinute);
                    etReminderTime.setText(formattedTime);
                },
                hour,
                minute,
                true
        );
        timePicker.setTitle(R.string.select_time_title);
        timePicker.show();
    }

    private void saveReminder() {
        String title = etReminderTitle.getText() != null
                ? etReminderTitle.getText().toString().trim() : "";
        String time = etReminderTime.getText() != null
                ? etReminderTime.getText().toString().trim() : "";

        if (title.isEmpty() || time.isEmpty()) {
            Toast.makeText(this, R.string.fill_all_fields, Toast.LENGTH_SHORT).show();
            return;
        }

        Intent returnIntent = new Intent();
        returnIntent.putExtra(EXTRA_REMINDER_TITLE, title);
        returnIntent.putExtra(EXTRA_REMINDER_TIME, time);
        setResult(RESULT_OK, returnIntent);
        finish();
    }
}
