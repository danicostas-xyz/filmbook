package xyz.danicostas.filmapp.activity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.ArrayList;
import java.util.HashMap;

import xyz.danicostas.filmapp.activity.LoginActivity;
import xyz.danicostas.filmapp.R;
import xyz.danicostas.filmapp.dao.DaoUsuario;
import xyz.danicostas.filmapp.model.entity.FilmList;
import xyz.danicostas.filmapp.model.entity.User;

public class RegisterActivity extends AppCompatActivity {
    private EditText emailInput, usernameInput, passwordInput;
    private Button registerButton;
    private DaoUsuario daoBBDD;
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);


        emailInput = findViewById(R.id.email);
        usernameInput = findViewById(R.id.username);passwordInput = findViewById(R.id.password);
        registerButton = findViewById(R.id.register);
        daoBBDD = new DaoUsuario();
        mAuth = FirebaseAuth.getInstance();

        registerButton.setOnClickListener(v -> {
            String email = emailInput.getText().toString().trim();
            String username = usernameInput.getText().toString().trim();
            String password = passwordInput.getText().toString().trim();

            if (email.isEmpty()) {
                emailInput.setError("El email es obligatorio");
                emailInput.requestFocus();
                return;
            }
            if (username.isEmpty()) {
                usernameInput.setError("El username es obligatorio");
                usernameInput.requestFocus();
                return;
            }
            if (password.isEmpty()) {
                passwordInput.setError("La contraseña es obligatoria");
                passwordInput.requestFocus();
                return;
            }


            mAuth.createUserWithEmailAndPassword(email, password)
                    .addOnCompleteListener(this, authTask -> {
                        if (authTask.isSuccessful()) {
                            FirebaseUser firebaseUser = mAuth.getCurrentUser();
                            if (firebaseUser != null) {
                                String userId = firebaseUser.getUid();

                                FilmList filmList = new FilmList();
                                filmList.setContent(new ArrayList<>());

                                ArrayList<FilmList> filmLists = new ArrayList<>();
                                filmLists.add(filmList);

                                User newUser = new User(email, password, null, null);
                                newUser.setEmail(email);
                                newUser.setListaPeliculas(filmLists);
                                newUser.setComentarios(new HashMap<>());

                                daoBBDD.createUser(userId, newUser, task -> {
                                    if (task.isSuccessful()) {
                                        Toast.makeText(RegisterActivity.this, "¡Registro exitoso!", Toast.LENGTH_SHORT).show();

                                        Intent intent = new Intent(RegisterActivity.this, LoginActivity.class);
                                        startActivity(intent);
                                        finish();
                                    } else {
                                        Exception exception = task.getException();
                                        String errorMessage = exception != null ? exception.getMessage() : "Unknown error";
                                        Toast.makeText(RegisterActivity.this, "Fallo en el registro en Firestore: " + errorMessage, Toast.LENGTH_SHORT).show();
                                    }
                                });
                            }

                        } else {

                            String errorMessage = authTask.getException() != null ? authTask.getException().getMessage() : "Unknown error";
                            if (errorMessage.contains("The email address is already in use")) {
                                Toast.makeText(RegisterActivity.this, "El correo ya está en uso", Toast.LENGTH_SHORT).show();
                            } else {
                                Toast.makeText(RegisterActivity.this, "Fallo en la autenticación: " + errorMessage, Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
        });

    }
}
