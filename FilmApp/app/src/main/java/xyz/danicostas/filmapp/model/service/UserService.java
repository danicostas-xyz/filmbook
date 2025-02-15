package xyz.danicostas.filmapp.model.service;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.widget.Toast;

import androidx.lifecycle.MutableLiveData;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.List;

import xyz.danicostas.filmapp.model.entity.FilmList;
import xyz.danicostas.filmapp.model.persistence.DaoUser;

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

    public MutableLiveData<List<FilmList>> getUserFilmLists() {
        return dao.getUserFilmLists(authUser.getUid());
    }
}
