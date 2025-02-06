package xyz.danicostas.filmapp.activity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import xyz.danicostas.filmapp.DatabaseHelper;
import xyz.danicostas.filmapp.R;

public class LoginActivity extends AppCompatActivity {
    private EditText usernameInput, passwordInput;
    private TextView registerLink;
    private Button loginButton;
    private DatabaseHelper dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);


        usernameInput = findViewById(R.id.username);
        passwordInput = findViewById(R.id.password);
        loginButton = findViewById(R.id.login);
        registerLink = findViewById(R.id.register_link);
        dbHelper = new DatabaseHelper(this);


        loginButton.setOnClickListener(v -> {
            String username = usernameInput.getText().toString().trim();
            String password = passwordInput.getText().toString().trim();


            if (username.isEmpty()) {
                usernameInput.setError("Username is required");
                usernameInput.requestFocus();
                return;
            }
            if (password.isEmpty()) {
                passwordInput.setError("Password is required");
                passwordInput.requestFocus();
                return;
            }

            if (dbHelper.checkUser(username, password)) {
                Toast.makeText(LoginActivity.this, "Login Successful!", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(LoginActivity.this, ApplicationActivity.class);
                intent.putExtra("K_Usuario", username);
                intent.putExtra("K_Password", password);
                dbHelper.close();
                startActivity(intent);

            } else {
                Toast.makeText(LoginActivity.this, "Invalid Credentials", Toast.LENGTH_SHORT).show();
            }
        });


        registerLink.setOnClickListener(v -> {
            Intent intent = new Intent(LoginActivity.this, RegisterActivity.class);
            startActivity(intent);
        });
    }
}
