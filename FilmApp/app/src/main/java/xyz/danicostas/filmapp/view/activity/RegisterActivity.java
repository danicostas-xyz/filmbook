package xyz.danicostas.filmapp.view.activity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

import xyz.danicostas.filmapp.R;
import xyz.danicostas.filmapp.model.entity.Film;
import xyz.danicostas.filmapp.model.persistence.DaoUsuario;
import xyz.danicostas.filmapp.model.entity.FilmList;
import xyz.danicostas.filmapp.model.entity.User;

public class RegisterActivity extends AppCompatActivity {
    private EditText emailInput, usernameInput, passwordInput;
    private Button registerButton;
    private DaoUsuario daoBBDD;
    private FirebaseAuth mAuth;
    private TextView loginLink;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_register);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.registerActivity), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        emailInput = findViewById(R.id.email);
        usernameInput = findViewById(R.id.username);passwordInput = findViewById(R.id.password);
        registerButton = findViewById(R.id.btRegister);
        daoBBDD = DaoUsuario.getInstance();
        mAuth = FirebaseAuth.getInstance();
        loginLink = findViewById(R.id.login_link);

        loginLink.setOnClickListener(view -> {
            finish();
        });

        registerButton.setOnClickListener(v -> {
            String email = emailInput.getText().toString().trim();
            String username = usernameInput.getText().toString().trim();
            String password = passwordInput.getText().toString().trim();

            if (email.isEmpty()) {
                emailInput.setError(getString(R.string.emailRequired));
                emailInput.requestFocus();
                return;
            }
            if (username.isEmpty()) {
                usernameInput.setError(getString(R.string.userRequired));
                usernameInput.requestFocus();
                return;
            }
            if (password.isEmpty()) {
                passwordInput.setError(getString(R.string.passwordRequired));
                passwordInput.requestFocus();
                return;
            }

            mAuth.createUserWithEmailAndPassword(email, password)
                    .addOnCompleteListener(this, authTask -> {
                        if (authTask.isSuccessful()) {
                            FirebaseUser firebaseUser = mAuth.getCurrentUser();
                            if (firebaseUser != null) {
                                String userId = firebaseUser.getUid();

                                User user = new User();
                                Film film1 = new Film();
                                film1.setId(426063);
                                film1.setOverview("A gothic tale of obsession between a haunted young woman and the terrifying vampire infatuated with her, causing untold horror in its wake.");
                                film1.setOriginalTitle("Nosferatu");
                                film1.setPosterPath("https://image.tmdb.org/t/p/w500/5qGIxdEO841C0tdY8vOdLoRVrr0.jpg");
                                film1.setReleaseDate("2024-12-25");
                                film1.setTitle("Nosferatu");
                                film1.setVoteAverage(6.7);
                                film1.setVoteCount(2002);

                                List<FilmList> filmLists = new ArrayList<>();
                                FilmList list = new FilmList();
                                list.setListName("Favoritas");
                                list.setContent(Arrays.asList(film1, film1, film1, film1, film1, film1, film1, film1));
                                filmLists.add(list);

                                user.setListasDeListas(filmLists);
                                user.setPassword(password);
                                user.setUsername(username);

                                user.setComentarios(new HashMap<>());

                                daoBBDD.createUser(userId, user, task -> {
                                    if (task.isSuccessful()) {
                                        Toast.makeText(RegisterActivity.this, getString(R.string.registerOK), Toast.LENGTH_SHORT).show();

                                        Intent intent = new Intent(RegisterActivity.this, LoginActivity.class);
                                        startActivity(intent);
                                        finish();
                                    } else {
                                        Exception exception = task.getException();
                                        String errorMessage = exception != null ? exception.getMessage() : "Unknown error";
                                        Toast.makeText(RegisterActivity.this, getString(R.string.registerKO) + errorMessage, Toast.LENGTH_SHORT).show();
                                    }
                                });
                            }

                        } else {

                            String errorMessage = authTask.getException() != null ? authTask.getException().getMessage() : "Unknown error";
                            if (errorMessage.contains("The email address is already in use")) {
                                Toast.makeText(RegisterActivity.this, getString(R.string.mailAlreadyInUse), Toast.LENGTH_SHORT).show();
                            } else {
                                Toast.makeText(RegisterActivity.this, getString(R.string.authKO) + errorMessage, Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
        });

    }
}
