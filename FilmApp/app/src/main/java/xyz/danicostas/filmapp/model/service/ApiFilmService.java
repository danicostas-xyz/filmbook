package xyz.danicostas.filmapp.model.service;

import com.google.gson.GsonBuilder;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ApiFilmService {

    public static ApiFilmService instance;
    private TMDBApiService api;

    private ApiFilmService() {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://api.themoviedb.org/3")
                .addConverterFactory(GsonConverterFactory.create(
                        new GsonBuilder().serializeNulls().create()
                )).build();

        api = retrofit.create(TMDBApiService.class);
    }

    public static ApiFilmService getInstance() {
        return instance == null ? instance = new ApiFilmService() : instance;
    }
}
