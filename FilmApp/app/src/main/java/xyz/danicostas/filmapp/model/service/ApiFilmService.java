package xyz.danicostas.filmapp.model.service;

import com.google.gson.GsonBuilder;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ApiFilmService {

    public final static String TMDB_API_BASE_URL = "https://api.themoviedb.org/3/";
    public final static String TMDB_API_IMAGE_URL = "https://image.tmdb.org/t/p/w500";
    public final static String API_KEY = "50f1837f71c3d1a88f1de905029df6c0";
    private static ApiFilmService instance;
    public static ApiFilmService getInstance() {
        return instance == null ? instance = new ApiFilmService() : instance;
    }

    private TMDBApiService api;

    private ApiFilmService() {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(TMDB_API_BASE_URL)
                .addConverterFactory(GsonConverterFactory.create(
                        new GsonBuilder().serializeNulls().create()
                )).build();

        api = retrofit.create(TMDBApiService.class);
    }

    public TMDBApiService getApi(){
        return api;
    }
}
