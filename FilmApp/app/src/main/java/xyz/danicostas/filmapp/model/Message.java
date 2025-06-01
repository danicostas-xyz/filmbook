package xyz.danicostas.filmapp.model;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class Message {
    public static final int TYPE_SENT = 0;
    public static final int TYPE_RECEIVED = 1;

    private String text;
    private long timestamp;
    private int type;

    public Message(String text, int type) {
        this.text = text;
        this.type = type;
        this.timestamp = System.currentTimeMillis();
    }

    public String getText() {
        return text;
    }

    public int getType() {
        return type;
    }

    public String getFormattedTime() {
        SimpleDateFormat sdf = new SimpleDateFormat("h:mm a", Locale.getDefault());
        return sdf.format(new Date(timestamp));
    }
} 