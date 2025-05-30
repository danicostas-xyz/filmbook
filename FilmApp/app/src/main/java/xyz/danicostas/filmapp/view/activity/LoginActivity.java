package xyz.danicostas.filmapp.view.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;

import xyz.danicostas.filmapp.R;
import xyz.danicostas.filmapp.model.service.LoginRegisterService;

public class LoginActivity extends AppCompatActivity {
    private static final int REGISTER_REQUEST_CODE = 1001; // Unique request code
    private EditText emailInput, passwordInput;
    private TextView registerLink;
    private Button loginButton;
    private LoginRegisterService loginRegisterService;
    private final Context CONTEXT = this;

    // Intent for result method
    /**
     * This method will set an email in the emailInput if the RESULT from the RegisterActivity is
     * RESULT_OK, it also will return with the email that will be set in the input.
     */
    private final ActivityResultLauncher<Intent> registerActivityLauncher =
            registerForActivityResult(new ActivityResultContracts.StartActivityForResult(),
                    result -> {
                        if (result.getResultCode() == RESULT_OK && result.getData() != null) {
                            String registeredEmail = result.getData().getStringExtra("REGISTERED_EMAIL");
                            if (registeredEmail != null) {
                                emailInput.setText(registeredEmail);
                                Toast.makeText(CONTEXT, "Email auto-completado desde el registro", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        initViews();
        getInstances();
        setOnClickListeners();
    }

    private void setOnClickListeners() {
        /**
         * When login button is pressed, it will verify none of the inputs are empty,
         * after that, the method from loginRegisterService will happen using the CONTEXT(this),
         * the email text and the password
         */
        loginButton.setOnClickListener(v -> {
            String email = emailInput.getText().toString().trim();
            String password = passwordInput.getText().toString().trim();

            if (email.isEmpty()) {
                Toast.makeText(CONTEXT, CONTEXT.getString(R.string.emailRequired), Toast.LENGTH_SHORT)
                        .show();
                return;
            }

            if (password.isEmpty()) {
                Toast.makeText(CONTEXT, CONTEXT.getString(R.string.passwordRequired), Toast.LENGTH_SHORT)
                        .show();
                return;
            }

            loginRegisterService.login(
                    CONTEXT,
                    email,
                    password
            );
        });
        /**
         * When this button is pressed, it will redirect the user to the RegisterActivity,
         * it also will save the email there and bring it to the LoginActivity once the
         * register is completed.
         */
        registerLink.setOnClickListener(view -> {
            Intent intent = new Intent(LoginActivity.this, RegisterActivity.class);
            registerActivityLauncher.launch(intent);
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
