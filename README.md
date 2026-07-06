# Reminder Assistant (Hatırlatıcı Asistan) ⏰📱

An accessible, notification-based daily reminder Android application designed specifically to support disadvantaged individuals, such as elderly users, people with memory difficulties, and anyone needing routine assistance.




## 🌟 Purpose & Project Option

**Selected Option:** Option 1: Accessible Daily Reminder App

Daily routines—such as taking medication, drinking water, and attending appointments—are critical for many disadvantaged individuals. The primary purpose of **Reminder Assistant** is to offer a simple, readable, and notification-based reminder application that provides meaningful support in everyday life. 

The application is built to be both **technically correct** and **socially meaningful**, catering directly to users who struggle with memory, routine management, or complex modern user interfaces.

---

## 🚀 Main Features

### 1. Daily Reminder Management
* **Add Reminders:** Easily enter a reminder title and select an execution time.
* **Scrollable List:** View all scheduled reminders in a clean, scrollable list on the main dashboard.
* **Simple Deletion:** Delete reminders securely using a long-press confirmation dialog to prevent accidental deletion.

### 2. Notification Alerts
* **Scheduled Triggers:** Reminders trigger system notifications at the exact scheduled time.
* **Background Support:** Uses Android's `AlarmManager` and a custom `BroadcastReceiver` to ensure alarms fire even if the app is closed or the device is idle.

### 3. Daily Support Messages (Internet Integration)
* **Motivational Quotes:** Dynamically fetches an inspirational quote from the [Quotable API](https://api.quotable.io/random) at launch.
* **Safe JSON Parsing:** Automatically parses the remote JSON response and displays the motivational message directly on the home screen.

### 4. Accessible User Interface (UI)
* **Large Touch Targets:** Buttons are designed with a minimum height of `70dp` to allow easy clicking.
* **High Readability:** Text sizes range between `18sp` and `28sp` for enhanced visibility.
* **Clear Layouts:** Includes custom section labels and a prominent ImageView logo.
* **Modern Style:** Styled using Material Design 3 components and custom theme palettes.

### 5. Navigation & Intents
* **Explicit Navigation:** Seamless navigation between three key screens (Activities) using explicit intents.
* **Interactive Menus:** Options menu to access the About screen and accessibility details.
* **Implicit Web Integration:** Opens a WHO accessibility webpage in the browser via implicit intents.
* **Share Functionality:** Share reminder details with others using Android's native sharing sheet (`ACTION_SEND`).

### 6. Visual Enhancements
* **Dynamic Theme:** Built with a custom cohesive color scheme.
* **Smooth Transitions:** A fade-in alpha animation applied to the main content layout.
* **Card Layouts:** Reminders are presented in polished, card-style container items.

---

## 🏗️ Technical Structure & Architecture

Below is the architectural package structure of the project under the root package `com.example.term_project`:

```text
com.example.term_project
├── activity/
│   ├── BaseActivity.java              # Base activity for lifecycle logging
│   ├── MainActivity.java              # Main dashboard with reminder list & daily quote
│   ├── AddReminderActivity.java       # Form to configure and add reminders
│   └── AboutActivity.java             # About screen with project information
├── adapter/
│   └── ReminderAdapter.java           # RecyclerView adapter for rendering reminder cards
├── data/
│   └── ReminderRepository.java        # Local in-memory repository for temporary storage
├── model/
│   └── Reminder.java                  # Reminder data class
├── network/
│   ├── NetworkManager.java            # Network logic for executing background HTTP requests
│   └── QuoteParser.java               # JSON parsing utility for the quote response
├── receiver/
│   └── ReminderAlarmReceiver.java     # BroadcastReceiver to intercept alarm broadcasts
└── util/
    ├── ReminderScheduler.java         # AlarmManager integration for scheduling alarms
    └── ReminderNotificationHelper.java# Helper to construct and show system notifications
```

### Screens (Activities)

| Activity | Role |
| :--- | :--- |
| `MainActivity` | Launcher screen. Displays the daily quote, the scrollable list of reminders, the "Add" navigation button, and the options menu. |
| `AddReminderActivity` | Form allowing the user to input a reminder title and select a time using a native `TimePickerDialog`. |
| `AboutActivity` | Information page displaying the app's purpose, features, and course/student details. |

---

## 📋 Course Requirement Mapping

This project maps directly to the CMPE2004 Advanced Programming syllabus requirements:

| Course Requirement | Implementation Details |
| :--- | :--- |
| **UI Components** | Custom usage of `TextView`, `EditText`, `Button`, `ImageView`, `ScrollView`, `RecyclerView`, and `Menu`. |
| **Multiple Screens** | Three distinct, well-structured Activities (`MainActivity`, `AddReminderActivity`, `AboutActivity`). |
| **Activity Lifecycle** | `BaseActivity` logs lifecycle hooks: `onCreate`, `onStart`, `onResume`, `onPause`, `onStop`, `onDestroy`. |
| **Explicit Intents** | Navigating between `MainActivity` → `AddReminderActivity` and `MainActivity` → `AboutActivity`. |
| **Implicit Intents** | Opening the web browser (`ACTION_VIEW`) to a WHO accessibility resource, and sharing details (`ACTION_SEND`). |
| **Object-Oriented Programming (OOP)** | Clean usage of classes, encapsulation, inheritance, interfaces, and exception handling. |
| **Internet / HTTP** | `NetworkManager` utilizing `HttpURLConnection` and parsing external JSON data from the quote API. |
| **Notification / Broadcast** | Scheduling alarms via `AlarmManager`, catching them with `ReminderAlarmReceiver`, and posting with `NotificationCompat`. |
| **Styles & Themes** | Structured `styles.xml` and `themes.xml` conforming to Material Design 3 guidelines. |
| **Animation** | `fade_in.xml` alpha animation on content load. |

---

## 🛠️ Key Technologies

* **Programming Language:** Java 11
* **IDE:** Android Studio
* **Minimum SDK:** 24 (Android 7.0 Nougat)
* **Target SDK:** 36 (Android 16)
* **Core Libraries:** AndroidX AppCompat, Material Components, RecyclerView

---

## 🧩 Challenges & Solutions

### 1. Running Network Requests Without Blocking the UI
* **Challenge:** Android prevents network calls (like fetching the daily quote) from running on the main UI thread to prevent Application Not Responding (ANR) issues.
* **Solution:** `NetworkManager` delegates HTTP calls to a background thread using an `ExecutorService` and posts the final UI updates back safely using a main-thread `Handler`.

### 2. Scheduling Reminders Reliably
* **Challenge:** Android's power-saving features (like Doze Mode) put apps to sleep and delay standard alarms.
* **Solution:** `ReminderScheduler` schedules notifications using `AlarmManager.setExactAndAllowWhileIdle()` combined with a `PendingIntent` aimed at `ReminderAlarmReceiver`.

### 3. Notification Permission on Android 13+
* **Challenge:** Android 13 (API level 33) and above require runtime permission (`POST_NOTIFICATIONS`) before showing notifications.
* **Solution:** `MainActivity` checks and requests permissions gracefully at launch using the modern `ActivityResultLauncher` API.

### 4. Parsing Internet Data Safely
* **Challenge:** External JSON APIs can fail, timeout, or return malformed data, leading to application crashes.
* **Solution:** `QuoteParser` handles parsing inside structured `try-catch` blocks and falls back to a pre-defined local message if the parse fails.

### 5. Accessible Design for Target Users
* **Challenge:** Disadvantaged users may find complex, cluttered layouts difficult to read and navigate.
* **Solution:** The user interface features large touch targets, high-contrast typography, clear color-coding, a simple menu structure, and minimal steps for creating a reminder.

---

## 🔮 Future Enhancements

* **Persistent Storage:** Integrating `SharedPreferences` or a local SQLite database using **Room** to save reminders across device reboots.
* **Repeating Alarms:** Implementing custom frequency rules (e.g., daily, weekly, custom intervals) for medications.
* **Localization:** Translating the app into Turkish and other languages to expand accessibility.
* **Accessibility Settings:** Adding in-app options to adjust font scale and toggle high-contrast mode dynamically.

---

## 📂 Presentation Outline (Week 14)

1. **Problem Definition:** Why daily reminders matter for disadvantaged individuals.
2. **Target User Group:** Elderly users, people with memory difficulties, and individuals needing daily routine support.
3. **Main Features Demo:** Walkthrough of adding a reminder → list update → notification trigger → daily quote fetching.
4. **Screen Walkthrough:** Demonstrating layout and design of Main, Add Reminder, and About screens.
5. **Technical Explanation:** In-depth review of Activities, intents, OOP patterns, HTTP queries, and the BroadcastReceiver lifecycle.
6. **Q&A**
