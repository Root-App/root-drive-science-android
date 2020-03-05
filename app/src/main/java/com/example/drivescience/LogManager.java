package com.example.drivescience;

import android.widget.TextView;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class LogManager {

    private TextView eventLog;

    public LogManager(TextView eventLog) {
        this.eventLog = eventLog;
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
}
