package xyz.danicostas.filmapp.viewmodel;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import java.util.List;

import xyz.danicostas.filmapp.model.entity.FilmList;
import xyz.danicostas.filmapp.model.service.GestorUser;

public class FilmListsViewModel extends ViewModel {
    private MutableLiveData<List<FilmList>> listaPeliculasLiveData = new MutableLiveData<>();
    private GestorUser userService;
    private String userId;

    public FilmListsViewModel() {
        userService = GestorUser.getInstance();
    }

    public void setUserId(String userId) {
        this.userId = userId;
        loadUserFilmLists(userId);
    }

    private void loadUserFilmLists(String userId) {
        listaPeliculasLiveData.setValue(userService.getUserFilmLists(userId));
    }

    public LiveData<List<FilmList>> getListaPeliculas() {
        return listaPeliculasLiveData;
    }
}
