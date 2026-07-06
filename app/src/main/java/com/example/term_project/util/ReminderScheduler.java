package com.example.term_project.util;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import com.example.term_project.model.Reminder;
import com.example.term_project.receiver.ReminderAlarmReceiver;
import java.util.Calendar;

public final class ReminderScheduler {

    private ReminderScheduler() {
    }

    public static void scheduleReminder(Context context, Reminder reminder) {
        try {
            String[] parts = reminder.getTime().split(":");
            int hour = Integer.parseInt(parts[0]);
            int minute = Integer.parseInt(parts[1]);

            Calendar calendar = Calendar.getInstance();
            calendar.set(Calendar.HOUR_OF_DAY, hour);
            calendar.set(Calendar.MINUTE, minute);
            calendar.set(Calendar.SECOND, 0);
            calendar.set(Calendar.MILLISECOND, 0);

            if (calendar.getTimeInMillis() <= System.currentTimeMillis()) {
                calendar.add(Calendar.DAY_OF_YEAR, 1);
            }

            Intent intent = new Intent(context, ReminderAlarmReceiver.class);
            intent.putExtra(ReminderAlarmReceiver.EXTRA_REMINDER_ID, reminder.getId());
            intent.putExtra(ReminderAlarmReceiver.EXTRA_REMINDER_TITLE, reminder.getTitle());
            intent.putExtra(ReminderAlarmReceiver.EXTRA_REMINDER_TIME, reminder.getTime());

            PendingIntent pendingIntent = PendingIntent.getBroadcast(
                    context,
                    reminder.getId(),
                    intent,
                    PendingIntent.FLAG_UPDATE_CURRENT | PendingIntent.FLAG_IMMUTABLE
            );

            AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
            if (alarmManager != null) {
                alarmManager.setExactAndAllowWhileIdle(
                        AlarmManager.RTC_WAKEUP,
                        calendar.getTimeInMillis(),
                        pendingIntent
                );
            }
        } catch (Exception e) {
            throw new IllegalArgumentException("Geçersiz saat formatı: " + reminder.getTime(), e);
        }
    }

    public static void cancelReminder(Context context, int reminderId) {
        Intent intent = new Intent(context, ReminderAlarmReceiver.class);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(
                context,
                reminderId,
                intent,
                PendingIntent.FLAG_UPDATE_CURRENT | PendingIntent.FLAG_IMMUTABLE
        );

        AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        if (alarmManager != null) {
            alarmManager.cancel(pendingIntent);
        }
    }
}
