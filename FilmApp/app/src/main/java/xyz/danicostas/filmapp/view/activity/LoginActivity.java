package xyz.danicostas.filmapp.view.activity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import xyz.danicostas.filmapp.R;
import xyz.danicostas.filmapp.model.service.GestorUser;

public class LoginActivity extends AppCompatActivity {
    private EditText emailInput, passwordInput;
    private TextView registerLink;
    private Button loginButton;
    private GestorUser gu;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        emailInput = findViewById(R.id.email);
        passwordInput = findViewById(R.id.password);
        loginButton = findViewById(R.id.login);
        registerLink = findViewById(R.id.register_link);

        gu = new GestorUser();

        loginButton.setOnClickListener(v -> {
            String email = emailInput.getText().toString().trim();
            String password = passwordInput.getText().toString().trim();

                gu.loguearse(LoginActivity.this, email, password);  // Llamar al método de login


        });

        registerLink.setOnClickListener(view -> {
            Intent intent = new Intent(LoginActivity.this, RegisterActivity.class);
            startActivity(intent);
        });
    }
}
