package xyz.danicostas.filmapp.view.activity;

import android.os.Bundle;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import xyz.danicostas.filmapp.R;

public class FriendActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_friend);
        TextView tvFriendName = findViewById(R.id.tvFriendNameDetail);
        String friendName = getIntent().getStringExtra("friendName");
        tvFriendName.setText(friendName);
    }
}
