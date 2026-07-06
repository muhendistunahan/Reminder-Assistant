package com.example.term_project.data;

import com.example.term_project.model.Reminder;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Hatırlatıcı verilerini yöneten singleton sınıf (Kapsülleme - OOP).
 */
public class ReminderRepository {

    private static ReminderRepository instance;
    private final List<Reminder> reminders = new ArrayList<>();
    private int nextId = 1;

    private ReminderRepository() {
    }

    public static synchronized ReminderRepository getInstance() {
        if (instance == null) {
            instance = new ReminderRepository();
        }
        return instance;
    }

    public Reminder addReminder(String title, String time) {
        Reminder reminder = new Reminder(nextId++, title, time, true);
        reminders.add(reminder);
        return reminder;
    }

    public void removeReminder(int id) {
        reminders.removeIf(reminder -> reminder.getId() == id);
    }

    public List<Reminder> getReminders() {
        return Collections.unmodifiableList(reminders);
    }

    public boolean isEmpty() {
        return reminders.isEmpty();
    }
}
