package xyz.danicostas.filmapp.view.activity;

import static xyz.danicostas.filmapp.view.adapter.SearchResultAdapter.FILM_ID;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import xyz.danicostas.filmapp.R;
import xyz.danicostas.filmapp.model.entity.ApiResponseFilmDetailsById;
import xyz.danicostas.filmapp.model.entity.ApiResponseKeywordsByFilmId;
import xyz.danicostas.filmapp.model.entity.ApiResponseSearchFilmByTitle;
import xyz.danicostas.filmapp.model.entity.Film;
import xyz.danicostas.filmapp.model.entity.KeywordOrGenres;
import xyz.danicostas.filmapp.model.service.ApiFilmService;
import xyz.danicostas.filmapp.model.service.TMDBApiService;
import xyz.danicostas.filmapp.view.adapter.FilmGridAdapter;
import xyz.danicostas.filmapp.view.adapter.KeywordsGenresAdapter;

public class FilmDetailActivity extends AppCompatActivity {

    private String filmId;

    private TextView tvFilmTitle, tvRatingFilmDetail, tvOriginalTitleB, tvDirectorB, tvReleaseDateB,
            tvSinopsisB;
    private RecyclerView rvGenres, rvKeywords, rvSimilarFilms;
    private ImageView ivFilmDetailCover;
    private TMDBApiService api;
    private final Context context = this;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_film_detail);
        initViews();
        Intent intent = getIntent();
        filmId = intent.getStringExtra(FILM_ID);
        obtainFilmDetails(filmId);
        obtainKeywords(filmId);
        obtainSimilarFilms(filmId);
    }

    private void initViews() {
        tvFilmTitle = findViewById(R.id.tvFilmDetailTitle);
        tvRatingFilmDetail = findViewById(R.id.tvRatingFilmDetail);
        tvOriginalTitleB = findViewById(R.id.tvOriginalTitleB);
        tvDirectorB = findViewById(R.id.tvDirectorB);
        tvReleaseDateB = findViewById(R.id.tvReleaseDateB);
        tvSinopsisB = findViewById(R.id.tvSinopsisB);
        rvGenres = findViewById(R.id.rvGenres);
        rvKeywords = findViewById(R.id.rvKeywords);
        rvSimilarFilms = findViewById(R.id.rvSimilarFilms);
        ivFilmDetailCover = findViewById(R.id.ivFilmDetailCover);
    }

    private void getInstances() {
        api = ApiFilmService.getInstance().getApi();
    }

    private void obtainFilmDetails(String filmId) {
        Call<ApiResponseFilmDetailsById> call = api.getDetailsByFilmId(ApiFilmService.API_KEY, filmId);
        call.enqueue(new Callback<ApiResponseFilmDetailsById>() {

            @Override
            public void onResponse(Call<ApiResponseFilmDetailsById> call,
                                   Response<ApiResponseFilmDetailsById> response) {
                if (response.isSuccessful()) {
                    Log.d("Success", "Datos traidos del servicio");
                    ApiResponseFilmDetailsById apiResponse = response.body();
                    assert apiResponse != null;
                    Log.d("MOVIES BY TITLE", apiResponse.toString());
                    tvFilmTitle.setText(apiResponse.title);
                    tvOriginalTitleB.setText(apiResponse.originalTitle);
                    tvRatingFilmDetail.setText(String.valueOf(apiResponse.voteAverage));
                    tvDirectorB.setText(apiResponse.id);
                    tvReleaseDateB.setText(apiResponse.releaseDate);
                    tvSinopsisB.setText(apiResponse.overview);
                    tvOriginalTitleB.setText(apiResponse.originalTitle);

                    String posterPath = apiResponse.posterPath;
                    String imageUrl = ApiFilmService.TMDB_API_IMAGE_URL + posterPath;

                    Glide.with(context)
                            .load(imageUrl)
                            .into(ivFilmDetailCover);

                    List<KeywordOrGenres> listOfGenres = apiResponse.genres;
                    List<KeywordOrGenres> limitedKeywords =
                            listOfGenres.size() > 3 ? listOfGenres.subList(0, 3) : listOfGenres;
                    rvGenres.setLayoutManager(new LinearLayoutManager(context));
                    rvGenres.setAdapter(new KeywordsGenresAdapter(limitedKeywords));

                } else {
                    Log.d("Error", response.code() + " " + response.message());
                    return;
                }
            }
            @Override
            public void onFailure(Call call, Throwable t) {
                Log.d("Error", t.toString());
            }

        });
    }

    private void obtainKeywords(String filmId) {
        Call<ApiResponseKeywordsByFilmId> call = api.getKeywordsByFilmId(ApiFilmService.API_KEY, filmId);
        call.enqueue(new Callback<ApiResponseKeywordsByFilmId>() {

            @Override
            public void onResponse(Call<ApiResponseKeywordsByFilmId> call,
                                   Response<ApiResponseKeywordsByFilmId> response) {
                if (response.isSuccessful()) {
                    Log.d("Success", "Datos traidos del servicio");
                    ApiResponseKeywordsByFilmId apiResponse = response.body();
                    assert apiResponse != null;
                    Log.d("MOVIES BY TITLE", apiResponse.toString());
                    List<KeywordOrGenres> listOfKeywords = apiResponse.keywords;

                    List<KeywordOrGenres> limitedKeywords =
                            listOfKeywords.size() > 9 ? listOfKeywords.subList(0, 9) : listOfKeywords;
                    rvKeywords.setLayoutManager(new LinearLayoutManager(context));
                    rvKeywords.setAdapter(new KeywordsGenresAdapter(limitedKeywords));

                } else {
                    Log.d("Error", response.code() + " " + response.message());
                    return;
                }
            }
            @Override
            public void onFailure(Call call, Throwable t) {
                Log.d("Error", t.toString());
            }

        });
    }

    private void obtainSimilarFilms(String filmId) {

        Call<ApiResponseSearchFilmByTitle> call = api.getSimilarMoviesByFilmId(ApiFilmService.API_KEY, filmId);
        call.enqueue(new Callback<ApiResponseSearchFilmByTitle>() {

            @Override
            public void onResponse(Call<ApiResponseSearchFilmByTitle> call,
                                   Response<ApiResponseSearchFilmByTitle> response) {
                if (response.isSuccessful()) {
                    Log.d("Success", "Datos traidos del servicio");
                    ApiResponseSearchFilmByTitle apiResponse = response.body();
                    Log.d("hey", apiResponse.toString());
                    List<Film> listOfSimilarFilms = apiResponse.getResults();
                    rvSimilarFilms.setLayoutManager(new GridLayoutManager(context, 3));
                    rvSimilarFilms.setAdapter(new FilmGridAdapter(listOfSimilarFilms));
                } else {
                    Log.d("Error", response.code() + " " + response.message());
                    return;
                }
            }
            @Override
            public void onFailure(Call call, Throwable t) {
                Log.d("Error", t.toString());
            }
        });

    }
}