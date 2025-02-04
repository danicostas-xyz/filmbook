package xyz.danicostas.filmapp;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import xyz.danicostas.filmapp.model.entity.User;

public class LoginActivity extends AppCompatActivity {
    private EditText usernameInput, passwordInput;
    private TextView registerLink;
    private Button loginButton;
    private DaoBBDD daoBBDD;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        usernameInput = findViewById(R.id.username);
        passwordInput = findViewById(R.id.password);
        loginButton = findViewById(R.id.login);
        registerLink = findViewById(R.id.register_link);
        daoBBDD = new DaoBBDD();

        loginButton.setOnClickListener(v -> {
            String username = usernameInput.getText().toString().trim();
            String password = passwordInput.getText().toString().trim();

            // validaciones
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

            //
            // Esta parte es rarisima ;-;
            //
            daoBBDD.getUserByUsername(username, task -> {
                if (task.isSuccessful()) {
                    QuerySnapshot querySnapshot = task.getResult();
                    if (!querySnapshot.isEmpty()) {
                        User user = querySnapshot.getDocuments().get(0).toObject(User.class);
                        if (user != null && user.getPassword().equals(password)) {
                            // Inicio de sesión exitoso
                            Toast.makeText(LoginActivity.this, "Login Successful!", Toast.LENGTH_SHORT).show();
                            Intent intent = new Intent(LoginActivity.this, Application.class);
                            intent.putExtra("K_Usuario", username);
                            startActivity(intent);
                        } else {
                            Toast.makeText(LoginActivity.this, "Invalid Credentials", Toast.LENGTH_SHORT).show();
                        }
                    } else {
                        Toast.makeText(LoginActivity.this, "User not found", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(LoginActivity.this, "Error: " + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                }
            });


        });
        registerLink = findViewById(R.id.register_link);
        registerLink.setOnClickListener(view -> {
            Intent intent = new Intent(LoginActivity.this, RegisterActivity.class);
            startActivity(intent);
        });
    }
}
