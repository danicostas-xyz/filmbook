package xyz.danicostas.filmapp.model.service;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.widget.Toast;

import androidx.lifecycle.MutableLiveData;

import com.google.firebase.auth.FirebaseAuth;

import java.util.List;

import xyz.danicostas.filmapp.R;
import xyz.danicostas.filmapp.model.entity.FilmList;
import xyz.danicostas.filmapp.model.persistence.DaoUser;
import xyz.danicostas.filmapp.view.activity.ApplicationActivity;

public class UserService {
    private static UserService instance;
    private FirebaseAuth mAuth;
    private DaoUser dao;

    private UserService() {
        mAuth = FirebaseAuth.getInstance();
        dao = DaoUser.getInstance();
    }

    public static UserService getInstance() {
        return instance == null ? instance = new UserService() : instance;
    }

    public void login(Context context, String email, String password) {

        if (email.isEmpty()) {
            Toast.makeText(context, context.getString(R.string.emailRequired), Toast.LENGTH_SHORT).show();
            return;
        }

        if (password.isEmpty()) {
            Toast.makeText(context, context.getString(R.string.passwordRequired), Toast.LENGTH_SHORT).show();
            return;
        }

        mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        Log.d("GestorUser", "Login successful.");
                        Toast.makeText(context, R.string.loginOK, Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(context, ApplicationActivity.class);
                        context.startActivity(intent);
                    } else {
                        String errorMessage = task.getException() != null ? task.getException().getMessage() : "Invalid Credentials";
                        Log.e("GestorUser", "Login failed: " + errorMessage);
                        Toast.makeText(context, context.getString(R.string.loginKO) + errorMessage, Toast.LENGTH_SHORT).show();
                    }
                });
    }

    public MutableLiveData<List<FilmList>> getUserFilmLists() {
        return dao.getUserFilmLists(mAuth.getCurrentUser().getUid());
    }
}
