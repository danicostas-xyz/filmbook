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

public class MessageAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    
    private List<Message> messageList;
    
    public MessageAdapter() {
        messageList = new ArrayList<>();
    }
    
    @Override
    public int getItemViewType(int position) {
        return messageList.get(position).getType();
    }
    
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
    
    public void addMessage(Message message) {
        messageList.add(message);
        notifyItemInserted(messageList.size() - 1);
    }
    
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
    
    static class SentMessageHolder extends RecyclerView.ViewHolder {
        TextView messageText, timeText;
        
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
    
    static class ReceivedMessageHolder extends RecyclerView.ViewHolder {
        TextView messageText, timeText;
        
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