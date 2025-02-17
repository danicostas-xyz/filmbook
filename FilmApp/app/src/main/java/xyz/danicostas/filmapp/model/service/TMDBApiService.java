package xyz.danicostas.filmapp.model.service;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;
import xyz.danicostas.filmapp.model.entity.ApiResponseKeywordsByFilmId;
import xyz.danicostas.filmapp.model.entity.ApiResponseFilmDetailsById;
import xyz.danicostas.filmapp.model.entity.ApiResponseSearchFilmByTitle;

public interface TMDBApiService {

    // https://api.themoviedb.org/3
    // La Apì Key en TMDBApi no va en la cabecera, si no como QueryParam en la url

    @GET("trending/all/week")
    Call<ApiResponseSearchFilmByTitle> getTrendingMovies(
            @Query("api_key") String apiKey,
            @Query("language") String language);

    // https://api.themoviedb.org/3/search/movie?query=blue%20velvet&include_adult=false&language=es&page=1' \

    @GET("search/movie")
    Call<ApiResponseSearchFilmByTitle> getMovieByTitle(
            @Query("api_key") String apiKey,
            @Query ("query") String query,
            @Query("language") String language);

    @GET("movie/{movie_id}/similar")
    Call<ApiResponseSearchFilmByTitle> getSimilarMoviesByFilmId(
            @Path("movie_id") int movieId,
            @Query("api_key") String apiKey,
            @Query("language") String language);

    @GET("movie/{movie_id}/keywords")
    Call<ApiResponseKeywordsByFilmId> getKeywordsByFilmId(
            @Path("movie_id") int movieId,
            @Query("api_key") String apiKey,
            @Query("language") String language);

    @GET("movie/{movie_id}")
    Call<ApiResponseFilmDetailsById> getDetailsByFilmId(
            @Path("movie_id") int movieId,
            @Query("api_key") String apiKey,
            @Query("language") String language);

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