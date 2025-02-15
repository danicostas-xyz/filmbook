package xyz.danicostas.filmapp.view.activity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import xyz.danicostas.filmapp.R;
import xyz.danicostas.filmapp.model.service.LoginRegisterService;
import xyz.danicostas.filmapp.model.service.UserService;

public class LoginActivity extends AppCompatActivity {
    private EditText emailInput, passwordInput;
    private TextView registerLink;
    private Button loginButton;
    private LoginRegisterService loginRegisterService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_login);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.loginActivity), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        initViews();
        getInstances();
        setOnClickListeners();
    }

    private void setOnClickListeners() {
        loginButton.setOnClickListener(v -> {
            String email = emailInput.getText().toString().trim();
            String password = passwordInput.getText().toString().trim();

            loginRegisterService.login(LoginActivity.this, email, password);  // Llamar al método de login
        });

        registerLink.setOnClickListener(view -> {
            Intent intent = new Intent(LoginActivity.this, RegisterActivity.class);
            startActivity(intent);
        });
    }

    private void getInstances() {
        loginRegisterService = LoginRegisterService.getInstance();
    }

    private void initViews() {
        emailInput = findViewById(R.id.etEmailLogin);
        passwordInput = findViewById(R.id.etPassLogin);
        loginButton = findViewById(R.id.btLogin);
        registerLink = findViewById(R.id.tvRegisterLinkLogin);
    }
}
