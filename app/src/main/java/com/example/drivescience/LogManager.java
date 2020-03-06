package com.example.drivescience;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.widget.TextView;
import android.widget.Toast;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class LogManager {

    private ClipboardManager clipboard;
    private TextView eventLog;

    public LogManager(Context context, TextView eventLog) {
        this.eventLog = eventLog;
        this.clipboard = (ClipboardManager) context.getSystemService(Context.CLIPBOARD_SERVICE);
    }

    public void addToLog(String string) {
        LocalDateTime datetime = LocalDateTime.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd LLL HH:mm:ss");
        String dateText = datetime.format(formatter);
        eventLog.setText(String.format("%s%s: %s\n", eventLog.getText(), dateText, string));
    }

    public void clearLog() {
        eventLog.setText("");
    }

    public void copyLogToClipboard(Context context) {
        ClipData logText = ClipData.newPlainText("Trip Tracker log", eventLog.getText());
        clipboard.setPrimaryClip(logText);
        Toast.makeText(context, "Copied!", Toast.LENGTH_SHORT).show();
    }
}
