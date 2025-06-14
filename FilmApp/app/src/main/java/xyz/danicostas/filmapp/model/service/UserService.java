package xyz.danicostas.filmapp.model.service;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.widget.Toast;

import androidx.lifecycle.MutableLiveData;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

import xyz.danicostas.filmapp.model.entity.Film;
import xyz.danicostas.filmapp.model.entity.FilmList;
import xyz.danicostas.filmapp.model.entity.Review;
import xyz.danicostas.filmapp.model.entity.User;
import xyz.danicostas.filmapp.model.interfaces.OnFilmCheckListener;
import xyz.danicostas.filmapp.model.interfaces.OnUserDataCallback;
import xyz.danicostas.filmapp.model.persistence.DaoUser;
import xyz.danicostas.filmapp.view.activity.ApplicationActivity;

public class UserService {
    private static UserService instance;
    private FirebaseAuth mAuth;
    private DaoUser dao;
    private FirebaseUser authUser;

    private UserService() {
        mAuth = FirebaseAuth.getInstance();
        dao = DaoUser.getInstance();
        authUser = mAuth.getCurrentUser();
    }

    public static UserService getInstance() {
        return instance == null ? instance = new UserService() : instance;
    }

    /**
     * Retrieves user data from the database using the provided user ID.
     * If the user data is successfully retrieved, the method logs the user data and
     * updates the current user session. If there is a failure in retrieving the data,
     * no action is taken.
     *
     * @param userId the ID of the user whose data is to be retrieved.
     */

    public void getUserData(String userId){
        dao.getUserData(userId, new OnUserDataCallback() {
            @Override
            public void onSuccess(User u) {
                if (u != null) {
                    Log.d("ON-SUCCESS-GET-USER-DATA-USER-SERVICE", u.toString());
                    UserSession.getInstance().setUser(u.getName(), u.getId(),
                            u.getEmail());
                    UserSession.getInstance().setUsername(u.getUsername());
                }
                Log.d("USER-SESSION", UserSession.getInstance().toString());
            }

            @Override
            public void onFailure(String errorMessage) {}
        });
    }

    public MutableLiveData<List<FilmList>> getUserFilmLists() {
        return dao.getUserFilmLists(mAuth.getCurrentUser().getUid());
    }

    public void clearUserSessionData() {
        UserSession.getInstance().clearUserData();
    }

    public void addNewList(String filmListTitle, String userId, Runnable onComplete) {
        dao.addNewList(filmListTitle, userId, onComplete);
    }

    public void addFilmToList(Film film, String userId, FilmList filmList) {
        dao.addFilmToList(film, userId, filmList);
    }

    public void removeFilmFromList(Film film, String userId, FilmList filmList) {
        dao.removeFilmFromList(film, userId, filmList);
    }

    public void checkIfFilmIsInList(Film film, FilmList filmList, String userId, OnFilmCheckListener onFilmCheckListener) {
        dao.checkIfFilmIsInList(film, filmList,userId,onFilmCheckListener);
    }

    public MutableLiveData<FilmList> getFilmList(String userId, String listName) {
        return dao.getFilmList(userId, listName);
    }

    public void getReviewList(String userId, Consumer<List<Review>> callback) {
        dao.getReviewList(userId, callback);
    }

    public void addReview (Review review, String userId, Runnable callback){
        dao.addReview(review, userId, callback);
    }

    public void searchUsers(String query, Consumer<List<User>> callback) {
        dao.searchUsers(query, callback);
    }

    public void addFriend(User friend, Runnable onSuccess, Consumer<String> onError) {
        if (authUser != null) {
            dao.addFriend(authUser.getUid(), friend, onSuccess, onError);
        } else {
            onError.accept("No hay usuario autenticado");
        }
    }

    public void getFriends(Consumer<List<User>> callback) {
        if (authUser != null) {
            dao.getFriends(authUser.getUid(), callback);
        } else {
            callback.accept(new ArrayList<>());
        }
    }

    public void updateList(FilmList list, FilmList newList, Consumer<FilmList> callback) {
        dao.updateList(list, newList, callback);
    }

    public void deleteList(FilmList filmList, Runnable callback) {
        dao.deleteList(filmList, callback);
    }
}
