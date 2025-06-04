package xyz.danicostas.filmapp.model;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

/**
 * Clase modelo para representar mensajes en el chat.
 * - Tipos de mensaje: enviado (TYPE_SENT) y recibido (TYPE_RECEIVED)
 * - Almacena texto, timestamp y tipo de mensaje
 * - Formato hora  (ej: 3:45 PM)
 */
public class Message {
    public static final int TYPE_SENT = 0;     // Mensaje enviado
    public static final int TYPE_RECEIVED = 1;  // Mensaje recibido

    private String text;
    private long timestamp;
    private int type;


    public Message(String text, int type) {
        this.text = text;
        this.type = type;
        this.timestamp = System.currentTimeMillis();
    }

    /**
     * @return Contenido del mensaje
     */
    public String getText() {
        return text;
    }

    /**
     * @return Tipo de mensaje (enviado/recibido)
     */
    public int getType() {
        return type;
    }

    /**
     * Formatea el timestamp a hora legible
     * @return (ej: 3:45 PM)
     */
    public String getFormattedTime() {
        SimpleDateFormat sdf = new SimpleDateFormat("h:mm a", Locale.getDefault());
        return sdf.format(new Date(timestamp));
    }
}