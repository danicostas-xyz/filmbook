package xyz.danicostas.filmapp.model.service;

import android.graphics.Movie;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;
import retrofit2.http.Query;
import xyz.danicostas.filmapp.model.entity.MovieResponse;

public interface TMDBApiService {

    // https://api.themoviedb.org/3

    @GET("trending/all/week")
    Call<MovieResponse> getTrendingMovies(@Query("api_key") String apiKey);


    /*@Headers({
            "Authorization: Bearer c2013086d51347da56494c501d63f7c14f51b908a43b9c0ec0145cfab4b901cd"
    })
    @GET("users/{id}")
    Call<Usuario> getUsuarioPorId(@Path("id") String id);

    //Para modificar el API rest "GoRest" hay que logarse en la página y generar
    //un token de autenticación, luego hay que incluirlo en la cabecera, dentro
    //del parametros Authorization
    @Headers({
            "Authorization: Bearer c2013086d51347da56494c501d63f7c14f51b908a43b9c0ec0145cfab4b901cd"
    })
    @POST("users")
    Call<Usuario> crearUsuario(@Body Usuario usuario);

    @Headers({
            "Authorization: Bearer c2013086d51347da56494c501d63f7c14f51b908a43b9c0ec0145cfab4b901cd"
    })
    @PUT("users/{id}")
    Call<Void> modificarUsuario(@Path("id") String id, @Body Usuario usuario);

    @Headers({
            "Authorization: Bearer c2013086d51347da56494c501d63f7c14f51b908a43b9c0ec0145cfab4b901cd"
    })
    @DELETE("users/{id}")
    Call<Void> borrarUsuario(@Path("id") String id);*/
}