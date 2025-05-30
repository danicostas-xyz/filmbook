package xyz.danicostas.filmapp.viewmodel;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import java.util.ArrayList;
import java.util.List;

import xyz.danicostas.filmapp.model.entity.FilmList;
import xyz.danicostas.filmapp.model.service.UserService;

public class FilmListsViewModel extends ViewModel {
    private static MutableLiveData<List<FilmList>> listaPeliculasLiveData = new MutableLiveData<>();
    private UserService userService;

    public FilmListsViewModel() {
        userService = UserService.getInstance();
        loadUserFilmLists();
    }

    private void loadUserFilmLists() {
        userService.getUserFilmLists().observeForever(listaPeliculasLiveData::setValue);
    }

    public LiveData<List<FilmList>> getListaPeliculas() {
        return listaPeliculasLiveData;
    }

    public static void notifyUserLoggedOut() {
        listaPeliculasLiveData.setValue(new ArrayList<>()); // Borra las listas
    }

    public void refreshLists() {
        loadUserFilmLists();
    }
}

