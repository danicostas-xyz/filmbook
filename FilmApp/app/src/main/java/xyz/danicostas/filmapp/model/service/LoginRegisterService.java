package xyz.danicostas.filmapp.model.service;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

import xyz.danicostas.filmapp.R;
import xyz.danicostas.filmapp.model.entity.Film;
import xyz.danicostas.filmapp.model.entity.FilmList;
import xyz.danicostas.filmapp.model.entity.Review;
import xyz.danicostas.filmapp.model.entity.User;
import xyz.danicostas.filmapp.model.persistence.DaoUser;
import xyz.danicostas.filmapp.view.activity.ApplicationActivity;
import xyz.danicostas.filmapp.view.activity.LoginActivity;

public class LoginRegisterService {
    private FirebaseAuth mAuth;
    private DaoUser daoUser;
    private UserService userService;
    private static LoginRegisterService instance;
    private LoginRegisterService(){
        mAuth = FirebaseAuth.getInstance();
        daoUser = DaoUser.getInstance();
        userService = UserService.getInstance();
    };

    public static LoginRegisterService getInstance(){
        return instance == null ? instance = new LoginRegisterService() : instance;
    }

    public void login(Context context, String email, String password) {

        mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        Log.d("GestorUser", "Login successful.");
                        Toast.makeText(context, R.string.loginOK, Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(context, ApplicationActivity.class);
                        context.startActivity(intent);
                    } else {
                        String errorMessage = task.getException() != null ? task.getException()
                                .getMessage() : "Invalid Credentials";
                        Log.e("GestorUser", "Login failed: " + errorMessage);
                        Toast.makeText(context, context.getString(R.string.loginKO) + errorMessage,
                                Toast.LENGTH_SHORT).show();
                    }
                });

        userService.getUserData();

    }

    public void register(Context context, String email, String username, String password) {
        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(authTask -> {

                    if (authTask.isSuccessful()) {
                        FirebaseUser firebaseUser = mAuth.getCurrentUser();

                        if (firebaseUser != null) {
                            // TODO INSTANCIAR NOMBRE
                            UserSession.getInstance().setUser("Name", firebaseUser.getUid(),
                                    username, email);

                            User user = new User();
                            List<FilmList> filmLists = mockListOfLists();
                            user.setId(UserSession.getInstance().getUserId());
                            user.setListasDeListas(filmLists);
                            user.setUsername(UserSession.getInstance().getUsername());
                            user.setListOfReviews(new ArrayList<Review>());
                            user.setEmail(UserSession.getInstance().getEmail());
                            user.setName(UserSession.getInstance().getName());
                            user.setListaAmigos(new ArrayList<User>());

                            daoUser.createUser(user.getId(), user, task -> {
                                if (task.isSuccessful()) {
                                    Toast.makeText(context, context.getString(R.string.registerOK), Toast.LENGTH_SHORT)
                                            .show();

                                    Intent intent = new Intent(context, LoginActivity.class);
                                    context.startActivity(intent);
                                    if (context instanceof Activity) {
                                        ((Activity) context).finish();
                                    }

                                } else {
                                    Exception exception = task.getException();
                                    String errorMessage = exception != null ? exception.getMessage()
                                            : "Unknown error";
                                    Toast.makeText(context, context.getString(R.string.registerKO)
                                            + errorMessage, Toast.LENGTH_SHORT).show();
                                }
                            });
                        }

                    } else {

                        String errorMessage = authTask.getException() != null
                                ? authTask.getException().getMessage()
                                : "Unknown error";
                        if (errorMessage.contains("The email address is already in use")) {
                            Toast.makeText(context, context.getString(R.string.mailAlreadyInUse), Toast.LENGTH_SHORT)
                                    .show();
                        } else {
                            Toast.makeText(context, context.getString(R.string.authKO) + errorMessage,
                                    Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }

    @NonNull
    private List<FilmList> mockListOfLists() {
        Film film1 = new Film();
        film1.setId(426063);
        film1.setOverview("A gothic tale of obsession between a haunted young woman and the " +
                "terrifying vampire infatuated with her, causing untold horror in its wake.");
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
        return filmLists;
    }
}
