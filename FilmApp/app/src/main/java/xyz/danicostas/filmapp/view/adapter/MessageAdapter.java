package xyz.danicostas.filmapp.view.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

import xyz.danicostas.filmapp.R;
import xyz.danicostas.filmapp.model.Message;

/**
 * Adaptador para mostrar mensajes en el RecyclerView del chat.
 * Maneja dos tipos de mensajes:
 * - Mensajes enviados : Mostrados a la derecha
 * - Mensajes recibidos : Mostrados a la izquierda
 */
public class MessageAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    // Lista de mensajes del chat
    private List<Message> messageList;

    public MessageAdapter() {
        messageList = new ArrayList<>();
    }

    /**
     * Determina el tipo de vista basado en el tipo de mensaje
     * (enviado o recibido)
     */
    @Override
    public int getItemViewType(int position) {
        return messageList.get(position).getType();
    }

    /**
     * Crea las vistas para los mensajes.
     * Usa layouts diferentes según el tipo:
     * - item_message_sent.xml para mensajes enviados
     * - item_message_received.xml para mensajes recibidos
     */
    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if (viewType == Message.TYPE_SENT) {
            View view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.item_message_sent, parent, false);
            return new SentMessageHolder(view);
        } else {
            View view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.item_message_received, parent, false);
            return new ReceivedMessageHolder(view);
        }
    }

    /**
     * Vincula los datos del mensaje con la vista correspondiente
     */
    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        Message message = messageList.get(position);

        if (getItemViewType(position) == Message.TYPE_SENT) {
            ((SentMessageHolder) holder).bind(message);
        } else {
            ((ReceivedMessageHolder) holder).bind(message);
        }
    }

    @Override
    public int getItemCount() {
        return messageList.size();
    }

    /**
     * Añade un nuevo mensaje al final de la lista
     * y notifica al RecyclerView para actualizarse
     */
    public void addMessage(Message message) {
        messageList.add(message);
        notifyItemInserted(messageList.size() - 1);
    }

    /**
     * Elimina el último mensaje de la lista el "escribiendo..."
     */
    public void removeLastMessage() {
        if (!messageList.isEmpty()) {
            int lastIndex = messageList.size() - 1;
            messageList.remove(lastIndex);
            notifyItemRemoved(lastIndex);
        }
    }

    public void clear() {
        messageList.clear();
        notifyDataSetChanged();
    }

    /**
     * ViewHolder para mensajes enviados
     * Maneja la vista con el texto y hora del mensaje
     */
    static class SentMessageHolder extends RecyclerView.ViewHolder {
        TextView messageText;    // Contenido del mensaje
        TextView timeText;       // Hora de envío

        SentMessageHolder(View itemView) {
            super(itemView);
            messageText = itemView.findViewById(R.id.tvSentMessage);
            timeText = itemView.findViewById(R.id.tvMessageTime);
        }

        void bind(Message message) {
            messageText.setText(message.getText());
            timeText.setText(message.getFormattedTime());
        }
    }

    /**
     * ViewHolder para mensajes recibidos
     * igual que el SentMessageHolder pero con diferente layout
     */
    static class ReceivedMessageHolder extends RecyclerView.ViewHolder {
        TextView messageText;    // Contenido del mensaje
        TextView timeText;       // Hora de recepción

        ReceivedMessageHolder(View itemView) {
            super(itemView);
            messageText = itemView.findViewById(R.id.tvReceivedMessage);
            timeText = itemView.findViewById(R.id.tvMessageTime);
        }

        void bind(Message message) {
            messageText.setText(message.getText());
            timeText.setText(message.getFormattedTime());
        }
    }
}