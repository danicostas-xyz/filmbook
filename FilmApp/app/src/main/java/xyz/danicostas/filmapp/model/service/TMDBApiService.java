package xyz.danicostas.filmapp.model.service;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;
import xyz.danicostas.filmapp.model.entity.ApiResponseKeywordsByFilmId;
import xyz.danicostas.filmapp.model.entity.ApiResponseFilmDetailsById;
import xyz.danicostas.filmapp.model.entity.ApiResponseSearchFilmByTitle;

/**
 * Interfaz de servicio para interactuar con la API de The Movie Database (TMDb) mediante Retrofit.
 * <p>
 * Esta interfaz define los endpoints disponibles para obtener información sobre películas,
 * tendencias, palabras clave y películas similares.
 * </p>
 */
public interface TMDBApiService {

    /**
     * Obtiene las películas y series en tendencia de la última semana.
     *
     * @param apiKey   Clave de API necesaria para autenticar la solicitud.
     * @param language Idioma en el que se devolverán los resultados (ejemplo: "es" o "en-US").
     * @return Un {@link Call} que devuelve una respuesta con la lista de películas y series en tendencia.
     */
    @GET("trending/all/week")
    Call<ApiResponseSearchFilmByTitle> getTrendingMovies(
            @Query("api_key") String apiKey,
            @Query("language") String language);

    /**
     * Busca una película por su título en la base de datos de TMDb.
     *
     * @param apiKey   Clave de API necesaria para autenticar la solicitud.
     * @param query    Título de la película a buscar.
     * @param language Idioma en el que se devolverán los resultados.
     * @return Un {@link Call} que devuelve una respuesta con las películas encontradas.
     */
    @GET("search/movie")
    Call<ApiResponseSearchFilmByTitle> getMovieByTitle(
            @Query("api_key") String apiKey,
            @Query("query") String query,
            @Query("language") String language);

    /**
     * Obtiene una lista de películas similares a la película especificada por su ID.
     *
     * @param movieId  ID de la película en TMDb.
     * @param apiKey   Clave de API necesaria para autenticar la solicitud.
     * @param language Idioma en el que se devolverán los resultados.
     * @return Un {@link Call} que devuelve una respuesta con la lista de películas similares.
     */
    @GET("movie/{movie_id}/similar")
    Call<ApiResponseSearchFilmByTitle> getSimilarMoviesByFilmId(
            @Path("movie_id") int movieId,
            @Query("api_key") String apiKey,
            @Query("language") String language);

    /**
     * Obtiene las palabras clave asociadas a una película en TMDb.
     *
     * @param movieId  ID de la película en TMDb.
     * @param apiKey   Clave de API necesaria para autenticar la solicitud.
     * @param language Idioma en el que se devolverán los resultados.
     * @return Un {@link Call} que devuelve una respuesta con las palabras clave de la película.
     */
    @GET("movie/{movie_id}/keywords")
    Call<ApiResponseKeywordsByFilmId> getKeywordsByFilmId(
            @Path("movie_id") int movieId,
            @Query("api_key") String apiKey,
            @Query("language") String language);

    /**
     * Obtiene los detalles completos de una película específica a partir de su ID.
     *
     * @param movieId  ID de la película en TMDb.
     * @param apiKey   Clave de API necesaria para autenticar la solicitud.
     * @param language Idioma en el que se devolverán los resultados.
     * @return Un {@link Call} que devuelve una respuesta con los detalles de la película.
     */
    @GET("movie/{movie_id}")
    Call<ApiResponseFilmDetailsById> getDetailsByFilmId(
            @Path("movie_id") int movieId,
            @Query("api_key") String apiKey,
            @Query("language") String language);
}
