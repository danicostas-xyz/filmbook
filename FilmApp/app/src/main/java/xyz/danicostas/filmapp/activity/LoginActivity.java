package xyz.danicostas.filmapp.activity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;

import xyz.danicostas.filmapp.R;

public class LoginActivity extends AppCompatActivity {
    private EditText emailInput, passwordInput;
    private TextView registerLink;
    private Button loginButton;
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        emailInput = findViewById(R.id.email);
        passwordInput = findViewById(R.id.password);
        loginButton = findViewById(R.id.login);
        registerLink = findViewById(R.id.register_link);
        mAuth = FirebaseAuth.getInstance();

        loginButton.setOnClickListener(v -> {
            String email = emailInput.getText().toString().trim();
            String password = passwordInput.getText().toString().trim();

            if (email.isEmpty()) {
                emailInput.setError(getString(R.string.emailRequired));
                emailInput.requestFocus();
                return;
            }

            if (password.isEmpty()) {
                passwordInput.setError(getString(R.string.passwordRequired));
                passwordInput.requestFocus();
                return;
            }

            mAuth.signInWithEmailAndPassword(email, password)
                    .addOnCompleteListener(this, task -> {
                        if (task.isSuccessful()) {
                            Toast.makeText(LoginActivity.this, R.string.loginOK, Toast.LENGTH_SHORT).show();
                            Intent intent = new Intent(LoginActivity.this, ApplicationActivity.class);
                            startActivity(intent);
                            finish();
                        } else {
                            String errorMessage = task.getException() != null ? task.getException().getMessage() : "Invalid Credentials";
                            Log.e("LoginActivity", "Login failed: " + errorMessage);
                            Toast.makeText(LoginActivity.this, getString(R.string.loginKO) + errorMessage, Toast.LENGTH_SHORT).show();
                        }
                    });
        });

        registerLink.setOnClickListener(view -> {
            Intent intent = new Intent(LoginActivity.this, RegisterActivity.class);
            startActivity(intent);
        });
    }
}
