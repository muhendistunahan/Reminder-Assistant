package com.example.term_project.activity;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AlertDialog;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.example.term_project.R;
import com.example.term_project.adapter.ReminderAdapter;
import com.example.term_project.data.ReminderRepository;
import com.example.term_project.model.Reminder;
import com.example.term_project.network.NetworkManager;
import com.example.term_project.network.QuoteParser;
import com.example.term_project.util.ReminderNotificationHelper;
import com.example.term_project.util.ReminderScheduler;
import com.google.android.material.button.MaterialButton;

public class MainActivity extends BaseActivity implements ReminderAdapter.ReminderActionListener {

    private static final String TAG = "MainActivity";
    private static final String QUOTE_API_URL = "https://api.quotable.io/random";
    private static final String ACCESSIBILITY_INFO_URL =
            "https://www.who.int/health-topics/disabilities";

    private TextView tvDailyQuote;
    private TextView tvNoReminders;
    private ImageView ivAppLogo;
    private MaterialButton btnAddNewReminder;
    private RecyclerView rvReminders;
    private NetworkManager networkManager;
    private ReminderRepository reminderRepository;
    private ReminderAdapter reminderAdapter;

    private final ActivityResultLauncher<Intent> reminderActivityResultLauncher =
            registerForActivityResult(
                    new ActivityResultContracts.StartActivityForResult(),
                    result -> {
                        if (result.getResultCode() == RESULT_OK && result.getData() != null) {
                            Intent data = result.getData();
                            String title = data.getStringExtra(AddReminderActivity.EXTRA_REMINDER_TITLE);
                            String time = data.getStringExtra(AddReminderActivity.EXTRA_REMINDER_TIME);

                            if (title != null && time != null) {
                                addReminder(title, time);
                            }
                        }
                    }
            );

    private final ActivityResultLauncher<String> notificationPermissionLauncher =
            registerForActivityResult(
                    new ActivityResultContracts.RequestPermission(),
                    isGranted -> {
                        if (!isGranted) {
                            Toast.makeText(this, R.string.notification_permission_denied, Toast.LENGTH_SHORT).show();
                        }
                    }
            );

    @Override
    protected String getLogTag() {
        return TAG;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        androidx.appcompat.widget.Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        tvDailyQuote = findViewById(R.id.tvDailyQuote);
        tvNoReminders = findViewById(R.id.tvNoReminders);
        ivAppLogo = findViewById(R.id.ivAppLogo);
        btnAddNewReminder = findViewById(R.id.btnAddNewReminder);
        rvReminders = findViewById(R.id.rvReminders);

        networkManager = new NetworkManager();
        reminderRepository = ReminderRepository.getInstance();
        reminderAdapter = new ReminderAdapter(this);

        rvReminders.setLayoutManager(new LinearLayoutManager(this));
        rvReminders.setAdapter(reminderAdapter);

        ReminderNotificationHelper.createNotificationChannel(this);
        requestNotificationPermissionIfNeeded();

        btnAddNewReminder.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, AddReminderActivity.class);
            reminderActivityResultLauncher.launch(intent);
        });

        ivAppLogo.startAnimation(AnimationUtils.loadAnimation(this, R.anim.fade_in));
        tvDailyQuote.startAnimation(AnimationUtils.loadAnimation(this, R.anim.fade_in));

        refreshReminderList();
        loadInternetData();
    }

    private void requestNotificationPermissionIfNeeded() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.POST_NOTIFICATIONS)
                    != PackageManager.PERMISSION_GRANTED) {
                notificationPermissionLauncher.launch(Manifest.permission.POST_NOTIFICATIONS);
            }
        }
    }

    private void addReminder(String title, String time) {
        try {
            Reminder reminder = reminderRepository.addReminder(title, time);
            ReminderScheduler.scheduleReminder(this, reminder);
            refreshReminderList();
            Toast.makeText(this, R.string.reminder_saved, Toast.LENGTH_SHORT).show();
        } catch (IllegalArgumentException e) {
            Toast.makeText(this, R.string.invalid_time_format, Toast.LENGTH_SHORT).show();
            Log.e(TAG, "Hatırlatıcı zamanlama hatası", e);
        }
    }

    private void refreshReminderList() {
        reminderAdapter.setReminders(reminderRepository.getReminders());
        tvNoReminders.setVisibility(reminderRepository.isEmpty() ? TextView.VISIBLE : TextView.GONE);
        rvReminders.setVisibility(reminderRepository.isEmpty() ? RecyclerView.GONE : RecyclerView.VISIBLE);
    }

    @Override
    public void onReminderLongClick(Reminder reminder) {
        showDeleteDialog(reminder);
    }

    private void showDeleteDialog(Reminder reminder) {
        new AlertDialog.Builder(this)
                .setTitle(R.string.delete_reminder_title)
                .setMessage(getString(R.string.delete_reminder_message, reminder.getTitle()))
                .setPositiveButton(R.string.yes_delete, (dialog, which) -> {
                    ReminderScheduler.cancelReminder(this, reminder.getId());
                    reminderRepository.removeReminder(reminder.getId());
                    refreshReminderList();
                    Toast.makeText(this, R.string.reminder_deleted, Toast.LENGTH_SHORT).show();
                })
                .setNeutralButton(R.string.share_reminder, (dialog, which) -> shareReminder(reminder))
                .setNegativeButton(R.string.cancel, null)
                .show();
    }

    private void shareReminder(Reminder reminder) {
        Intent shareIntent = new Intent(Intent.ACTION_SEND);
        shareIntent.setType("text/plain");
        shareIntent.putExtra(Intent.EXTRA_SUBJECT, getString(R.string.share_subject));
        shareIntent.putExtra(Intent.EXTRA_TEXT,
                getString(R.string.share_text, reminder.getTime(), reminder.getTitle()));
        startActivity(Intent.createChooser(shareIntent, getString(R.string.share_chooser_title)));
    }

    private void loadInternetData() {
        tvDailyQuote.setText(R.string.loading_quote);
        networkManager.fetchDailyData(QUOTE_API_URL, new NetworkManager.NetworkCallback() {
            @Override
            public void onSuccess(String result) {
                tvDailyQuote.setText(QuoteParser.parseQuote(result));
            }

            @Override
            public void onError(Exception e) {
                tvDailyQuote.setText(R.string.quote_load_error);
                Log.e(TAG, "İnternet hatası", e);
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int itemId = item.getItemId();
        if (itemId == R.id.menu_about) {
            startActivity(new Intent(this, AboutActivity.class));
            return true;
        }
        if (itemId == R.id.menu_accessibility_info) {
            openAccessibilityInfo();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void openAccessibilityInfo() {
        Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(ACCESSIBILITY_INFO_URL));
        startActivity(browserIntent);
    }
}
