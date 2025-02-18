package xyz.danicostas.filmapp.view.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import xyz.danicostas.filmapp.R;
import xyz.danicostas.filmapp.view.adapter.FriendListAdapter;

public class FriendActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_friend);
        TextView tvFriendName = findViewById(R.id.tvFriendNameDetail);
        String friendName = getIntent().getStringExtra(FriendListAdapter.FRIEND_NAME);
        tvFriendName.setText(friendName);
        Button buttonback = findViewById(R.id.buttonBack);
        buttonback.setOnClickListener(v -> {
            Intent intent = new Intent(this, ApplicationActivity.class);
            startActivity(intent);
        });
    }
}
