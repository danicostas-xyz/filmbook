package xyz.danicostas.filmapp.model.service;

import com.google.gson.GsonBuilder;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Servicio singleton para interactuar con la API de The Movie Database (TMDb).
 *
 * Proporciona una instancia única de {@link TMDBApiService} para realizar peticiones HTTP
 * a la API de TMDb. Utiliza Retrofit para la gestión de las solicitudes y respuestas.
 */
public class ApiFilmService {

    /**
     * URL base de la API de TMDb.
     */
    public final static String TMDB_API_BASE_URL = "https://api.themoviedb.org/3/";

    /**
     * URL base para obtener imágenes de TMDb con un tamaño de 500px de ancho.
     */
    public final static String TMDB_API_IMAGE_URL = "https://image.tmdb.org/t/p/w500";

    /**
     * Clave de API para autenticarse en TMDb.
     */
    public final static String API_KEY = "50f1837f71c3d1a88f1de905029df6c0";

    /**
     * Código de idioma para las respuestas en español.
     */
    public final static String LANG_SPANISH = "es";

    /**
     * Código de idioma para las respuestas en inglés (EE.UU.).
     */
    public final static String LANG_ENGLISH = "en-US";

    /**
     * Instancia única de {@link ApiFilmService} para garantizar un acceso centralizado al servicio.
     */
    private static ApiFilmService instance;

    /**
     * Obtiene la instancia única de {@link ApiFilmService}.
     * Si no existe, se crea una nueva instancia.
     *
     * @return La instancia única de {@link ApiFilmService}.
     */
    public static ApiFilmService getInstance() {
        return instance == null ? instance = new ApiFilmService() : instance;
    }

    /**
     * Interfaz de servicio de Retrofit para interactuar con la API de TMDb.
     */
    private TMDBApiService api;

    /**
     * Constructor privado que configura Retrofit y la instancia de {@link TMDBApiService}.
     */
    private ApiFilmService() {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(TMDB_API_BASE_URL)
                .addConverterFactory(GsonConverterFactory.create(
                        new GsonBuilder().serializeNulls().create()
                )).build();

        api = retrofit.create(TMDBApiService.class);
    }

    /**
     * Obtiene la instancia del servicio de la API de TMDb.
     *
     * @return Una instancia de {@link TMDBApiService} para realizar peticiones a la API.
     */
    public TMDBApiService getApi(){
        return api;
    }
}
