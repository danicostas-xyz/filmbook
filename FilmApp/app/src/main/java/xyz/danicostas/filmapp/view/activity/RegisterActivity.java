package xyz.danicostas.filmapp.view.activity;

import android.content.Context;
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

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

import xyz.danicostas.filmapp.R;
import xyz.danicostas.filmapp.model.entity.Film;
import xyz.danicostas.filmapp.model.persistence.DaoUser;
import xyz.danicostas.filmapp.model.entity.FilmList;
import xyz.danicostas.filmapp.model.entity.User;
import xyz.danicostas.filmapp.model.service.LoginRegisterService;
import xyz.danicostas.filmapp.model.service.UserService;

public class RegisterActivity extends AppCompatActivity {
    private EditText emailInput, usernameInput, passwordInput;
    private Button registerButton;
    private LoginRegisterService loginRegisterService;
    private DaoUser daoBBDD;
    private FirebaseAuth mAuth;
    private TextView loginLink;
    private Context context;

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

        initViews();
        getInstances();
        setOnClickListeners();

    }

    private void setOnClickListeners() {
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

            loginRegisterService.register(context, email, username, password);
        });
    }

    private void getInstances() {
        daoBBDD = DaoUser.getInstance();
        loginRegisterService = LoginRegisterService.getInstance();
        mAuth = FirebaseAuth.getInstance();
        context = RegisterActivity.this;
    }

    private void initViews() {
        emailInput = findViewById(R.id.etEmailLogin);
        usernameInput = findViewById(R.id.username);
        passwordInput = findViewById(R.id.etPassLogin);
        registerButton = findViewById(R.id.btRegister);
        loginLink = findViewById(R.id.login_link);
    }
}
