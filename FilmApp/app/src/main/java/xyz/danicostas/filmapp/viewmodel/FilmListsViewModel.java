package xyz.danicostas.filmapp.viewmodel;

import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.google.firebase.auth.FirebaseAuth;

import java.util.List;

import xyz.danicostas.filmapp.model.entity.FilmList;
import xyz.danicostas.filmapp.model.persistence.DaoUsuario;
import xyz.danicostas.filmapp.model.service.GestorUser;

public class FilmListsViewModel extends ViewModel {
    private MutableLiveData<List<FilmList>> listaPeliculasLiveData = new MutableLiveData<>();
    private GestorUser userService;


    public FilmListsViewModel() {
        userService = GestorUser.getInstance();
        loadUserFilmLists();
    }

    private void loadUserFilmLists() {
        listaPeliculasLiveData = userService.getUserFilmLists();
    }

    public LiveData<List<FilmList>> getListaPeliculas() {
        return listaPeliculasLiveData;
    }
}

