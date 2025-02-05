package xyz.danicostas.filmapp;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;

import xyz.danicostas.filmapp.model.entity.User;

public class RegisterActivity extends AppCompatActivity {
    private EditText usernameInput, passwordInput;
    private Button registerButton;
    private DaoBBDD daoBBDD;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        usernameInput = findViewById(R.id.username);
        passwordInput = findViewById(R.id.password);
        registerButton = findViewById(R.id.register);
        daoBBDD = new DaoBBDD();

        registerButton.setOnClickListener(v -> {
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

            // Crear un nuevo usuario
            User newUser = new User(username, password);

            // Usamos el DAO para registrar al usuario
            daoBBDD.createUser(newUser, new OnCompleteListener<DocumentReference>() {
                @Override
                public void onComplete(Task<DocumentReference> task) {
                    if (task.isSuccessful()) {
                        Toast.makeText(RegisterActivity.this, "Registration Successful!", Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(RegisterActivity.this, LoginActivity.class);
                        startActivity(intent);
                    } else {
                        Toast.makeText(RegisterActivity.this, "Registration Failed: " + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                    }
                }
            });
        });
    }
}
