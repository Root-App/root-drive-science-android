package com.example.drivescience;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.text.Layout;
import android.text.method.ScrollingMovementMethod;
import android.widget.TextView;
import android.widget.Toast;

import androidx.room.Room;

import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.List;

public class LogManager {
    private AppDatabase db;
    private ClipboardManager clipboard;
    private TextView eventLog;

    public LogManager(Context context, TextView eventLog) {
        this.eventLog = eventLog;
        this.eventLog.setMovementMethod(new ScrollingMovementMethod());
        this.clipboard = (ClipboardManager) context.getSystemService(Context.CLIPBOARD_SERVICE);
        this.db = Room.databaseBuilder(context, AppDatabase.class, "log-messages").allowMainThreadQueries().build();

        List<LogMessage> existingMessages = db.logMessageDao().getAll();
        for (LogMessage log : existingMessages)
        {
            displayLog(log);
        }
    }

    private LogMessage storeLog(String string)
    {
        Date date = new Date();
        LogMessage logMessage = new LogMessage();
        logMessage.date = date;
        logMessage.message = string;
        db.logMessageDao().insert(logMessage);
        return logMessage;
    }

    private void displayLog(LogMessage logMessage)
    {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd LLL HH:mm:ss");
        String dateText = logMessage.date.toString();
        eventLog.setText(String.format("%s%s: %s\n", eventLog.getText(), dateText, logMessage.message));

        final Layout layout = eventLog.getLayout();
        if(layout != null){
            int scrollDelta = layout.getLineBottom(eventLog.getLineCount() - 1) - eventLog.getScrollY() - eventLog.getHeight();
            if(scrollDelta > 0) {
                eventLog.scrollBy(0, scrollDelta);
            }
        }
    }

    public void addToLog(String string) {
        LogMessage log = storeLog(string);
        displayLog(log);
    }

    public void clearLog() {
        eventLog.setText("");
        db.logMessageDao().deleteAll();
    }

    public void copyLogToClipboard(Context context) {
        ClipData logText = ClipData.newPlainText("Trip Tracker log", eventLog.getText());
        clipboard.setPrimaryClip(logText);
        Toast.makeText(context, "Copied!", Toast.LENGTH_SHORT).show();
    }
}
