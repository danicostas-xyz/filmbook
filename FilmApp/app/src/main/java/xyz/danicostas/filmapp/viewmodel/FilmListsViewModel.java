package xyz.danicostas.filmapp.viewmodel;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import java.util.List;

import xyz.danicostas.filmapp.model.entity.FilmList;
import xyz.danicostas.filmapp.model.service.UserService;

public class FilmListsViewModel extends ViewModel {
    private MutableLiveData<List<FilmList>> listaPeliculasLiveData = new MutableLiveData<>();
    private UserService userService;

    public FilmListsViewModel() {
        userService = UserService.getInstance();
        loadUserFilmLists();
    }

    private void loadUserFilmLists() {
        listaPeliculasLiveData = userService.getUserFilmLists();
    }

    public LiveData<List<FilmList>> getListaPeliculas() {
        return listaPeliculasLiveData;
    }
}

