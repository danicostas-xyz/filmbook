package xyz.danicostas.filmapp.view.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import xyz.danicostas.filmapp.R;
import xyz.danicostas.filmapp.model.Message;
import xyz.danicostas.filmapp.view.adapter.FriendListAdapter;
import xyz.danicostas.filmapp.view.adapter.MessageAdapter;
import xyz.danicostas.filmapp.view.fragment.FriendsFragment;

public class FriendActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private MessageAdapter messageAdapter;
    private EditText messageInput;
    private Button sendButton;
    private String friendName;
    private String friendProfileUrl;
    private static final String TAG = "FriendActivity";
    private List<Message> messageList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_friend);
        
        TextView tvFriendName = findViewById(R.id.tvFriendNameDetail);
        friendName = getIntent().getStringExtra(FriendListAdapter.FRIEND_NAME);
        friendProfileUrl = getIntent().getStringExtra(FriendListAdapter.FRIEND_PROFILE_URL);
        tvFriendName.setText(friendName);
        
        ImageView ivFriendProfile = findViewById(R.id.ivFriendProfile);
        
        try {
            if (friendProfileUrl != null && !friendProfileUrl.isEmpty()) {
                if (friendProfileUrl.matches("\\d+")) {
                    int resourceId = Integer.parseInt(friendProfileUrl);
                    ivFriendProfile.setImageResource(resourceId);
                } else {
                    Glide.with(this)
                        .load(friendProfileUrl)
                        .placeholder(R.drawable.default_avatar_vector)
                        .error(R.drawable.default_avatar_vector)
                        .centerCrop()
                        .into(ivFriendProfile);
                }
            }
        } catch (Exception e) {
            Log.e(TAG,e.getMessage());
            ivFriendProfile.setImageResource(R.drawable.default_avatar_vector);
        }
        
        ImageButton buttonBack = findViewById(R.id.buttonBack);
        buttonBack.setOnClickListener(v -> {
            saveMessages(); 

            finish();
        });

        // Load mensajes  
        loadMessages();
        

        recyclerView = findViewById(R.id.recyclerViewMessages);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        layoutManager.setStackFromEnd(true); // scroll hacia abajo
        recyclerView.setLayoutManager(layoutManager);
        messageAdapter = new MessageAdapter();
        recyclerView.setAdapter(messageAdapter);
        

        for (Message message : messageList) {
            messageAdapter.addMessage(message);
        }

        messageInput = findViewById(R.id.etMessageInput);
        sendButton = findViewById(R.id.btnSendMessage);
        
        sendButton.setOnClickListener(v -> sendMessage());
    }
    
    @Override
    protected void onPause() {
        super.onPause();
        saveMessages(); 
    }
    
    private void sendMessage() {
        String messageText = messageInput.getText().toString().trim();
        if (!messageText.isEmpty()) {
            // añadir mensajes
            Message sentMessage = new Message(messageText, Message.TYPE_SENT);
            messageAdapter.addMessage(sentMessage);
            messageList.add(sentMessage);
            
            messageInput.setText("");
            
            scrollToBottom();
            saveMessages();
        }
    }
    
    private void scrollToBottom() {
        if (messageAdapter.getItemCount() > 0) {
            recyclerView.smoothScrollToPosition(messageAdapter.getItemCount() - 1);
        }
    }
    
    private void saveMessages() {
        SharedPreferences sharedPreferences = getSharedPreferences("ChatMessages", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        
        Gson gson = new Gson();
        String json = gson.toJson(messageList);
        
        String key = "messages_" + friendName.replaceAll("\\s+", "_").toLowerCase();
        editor.putString(key, json);
        editor.apply();
  
    }
    
    private void loadMessages() {
        SharedPreferences sharedPreferences = getSharedPreferences("ChatMessages", Context.MODE_PRIVATE);
        
        String key = "messages_" + friendName.replaceAll("\\s+", "_").toLowerCase();
        String json = sharedPreferences.getString(key, null);
        
        if (json != null) {
            Gson gson = new Gson();
            Type type = new TypeToken<ArrayList<Message>>(){}.getType();
            messageList = gson.fromJson(json, type);
            
            if (messageList == null) {
                messageList = new ArrayList<>();
            }
            
            Log.d(TAG, "Loaded " + messageList.size() + " messages for " + friendName);
        } else {
            Log.d(TAG, "No mensajes guardados para " + friendName);
            messageList = new ArrayList<>();
        }
    }
}
