package com.example.term_project.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import com.example.term_project.util.ReminderNotificationHelper;

/**
 * AlarmManager tarafından tetiklenen BroadcastReceiver (Gereksinim 7).
 */
public class ReminderAlarmReceiver extends BroadcastReceiver {

    public static final String EXTRA_REMINDER_ID = "extra_reminder_id";
    public static final String EXTRA_REMINDER_TITLE = "extra_reminder_title";
    public static final String EXTRA_REMINDER_TIME = "extra_reminder_time";

    @Override
    public void onReceive(Context context, Intent intent) {
        if (intent == null) {
            return;
        }

        String title = intent.getStringExtra(EXTRA_REMINDER_TITLE);
        String time = intent.getStringExtra(EXTRA_REMINDER_TIME);

        if (title != null && time != null) {
            ReminderNotificationHelper.showReminderNotification(context, title, time);
        }
    }
}
