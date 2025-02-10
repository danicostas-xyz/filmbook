package xyz.danicostas.filmapp.model.service;

public class FilmService {
    private static FilmService instance;
    private FilmService(){};
    public static FilmService getInstance() {
        return instance == null ? instance = new FilmService() : instance;
    }
}
