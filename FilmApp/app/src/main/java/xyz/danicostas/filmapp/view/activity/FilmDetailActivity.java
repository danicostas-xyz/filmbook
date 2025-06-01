package xyz.danicostas.filmapp.view.activity;

import static xyz.danicostas.filmapp.view.adapter.SearchResultAdapter.FILM_ID;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.util.TypedValue;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.google.android.flexbox.AlignItems;
import com.google.android.flexbox.FlexDirection;
import com.google.android.flexbox.FlexWrap;
import com.google.android.flexbox.FlexboxLayoutManager;
import com.google.android.flexbox.JustifyContent;

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
import xyz.danicostas.filmapp.view.loader.CustomAlertDialog;

public class FilmDetailActivity extends AppCompatActivity {

    private int filmId;

    private TextView tvFilmTitle, tvRatingFilmDetail, tvOriginalTitleB, tvDirectorB, tvReleaseDateB,
            tvSinopsisB;
    private RecyclerView rvGenres, rvKeywords, rvSimilarFilms;
    private ImageView ivFilmDetailCover;
    private TMDBApiService api;
    private Film film;
    private Button btAddToDiary;
    private final Context context = this;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        CustomAlertDialog progressDialog = new CustomAlertDialog(context, "Cargando");
        progressDialog.show();
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_film_detail);
        initViews();
        getInstances();
        Intent intent = getIntent();
        filmId = intent.getIntExtra(FILM_ID,0);
        obtainFilmDetails(filmId);
        obtainKeywords(filmId);
        obtainSimilarFilms(filmId);
        progressDialog.dismiss(1000L);
        setListeners();
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
        btAddToDiary = findViewById(R.id.btAddToDiary);

    }

    private void getInstances() {
        api = ApiFilmService.getInstance().getApi();
    }

    private void obtainFilmDetails(int filmId) {
        Call<ApiResponseFilmDetailsById> call = api.getDetailsByFilmId(filmId,
                ApiFilmService.API_KEY, ApiFilmService.LANG_SPANISH);
        call.enqueue(new Callback<ApiResponseFilmDetailsById>() {

            @Override
            public void onResponse(Call<ApiResponseFilmDetailsById> call,
                                   Response<ApiResponseFilmDetailsById> response) {
                if (response.isSuccessful()) {
                    Log.d("Success", "OBTAINFILMDETAILS");
                    ApiResponseFilmDetailsById apiResponse = response.body();
                    assert apiResponse != null;
                    Log.d("OBTAINFILMDETAILS", apiResponse.toString());
                    film = new Film();
                    film.setId(apiResponse.getId());
                    film.setTitle(apiResponse.getTitle());
                    film.setAdult(apiResponse.isAdult());
                    film.setBackdropPath(apiResponse.getBackdropPath());
                    film.setOverview(apiResponse.getOverview());
                    film.setPopularity(apiResponse.getPopularity());
                    film.setPosterPath(apiResponse.getPosterPath());
                    film.setOriginalLanguage(apiResponse.getOriginalLanguage());
                    film.setReleaseDate(apiResponse.getReleaseDate());
                    film.setVoteAverage(apiResponse.getVoteAverage());
                    film.setVoteCount(apiResponse.getVoteCount());
                    film.setOriginalTitle(apiResponse.getOriginalTitle());

                    tvFilmTitle.setText(apiResponse.getTitle());
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                        tvFilmTitle.setAutoSizeTextTypeUniformWithConfiguration(
                                18,
                                32,
                                4, TypedValue.COMPLEX_UNIT_SP
                                );
                    }
                    tvOriginalTitleB.setText(apiResponse.getOriginalTitle());
                    tvRatingFilmDetail.setText(String.valueOf(
                            Math.round(apiResponse.getVoteAverage() * 10.0) / 10.0));
                    tvDirectorB.setText(String.valueOf(apiResponse.getId()));
                    tvReleaseDateB.setText(apiResponse.getReleaseDate());
                    tvSinopsisB.setText(apiResponse.getOverview());
                    tvOriginalTitleB.setText(apiResponse.getOriginalTitle());

                    String posterPath = apiResponse.getPosterPath();
                    String imageUrl = ApiFilmService.TMDB_API_IMAGE_URL + posterPath;

                    Glide.with(context)
                            .load(imageUrl)
                            .into(ivFilmDetailCover);

                    List<KeywordOrGenres> listOfGenres = apiResponse.getGenres();
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

    private void obtainKeywords(int filmId) {
        Call<ApiResponseKeywordsByFilmId> call = api.getKeywordsByFilmId(filmId,
                ApiFilmService.API_KEY, ApiFilmService.LANG_SPANISH);
        call.enqueue(new Callback<ApiResponseKeywordsByFilmId>() {

            @Override
            public void onResponse(Call<ApiResponseKeywordsByFilmId> call,
                                   Response<ApiResponseKeywordsByFilmId> response) {
                if (response.isSuccessful()) {
                    Log.d("Success", "OBTAINKEYOWRDS");
                    ApiResponseKeywordsByFilmId apiResponse = response.body();
                    assert apiResponse != null;
                    Log.d("OBTAINKEYOWRDS", apiResponse.toString());
                    List<KeywordOrGenres> listOfKeywords = apiResponse.getKeywords();

                    List<KeywordOrGenres> limitedKeywords =
                            listOfKeywords.size() > 9 ? listOfKeywords.subList(0, 9) : listOfKeywords;

                    FlexboxLayoutManager flex = new FlexboxLayoutManager(context);
                    flex.setFlexWrap(FlexWrap.WRAP);
                    flex.setFlexDirection(FlexDirection.ROW);
                    flex.setJustifyContent(JustifyContent.FLEX_START);
                    rvKeywords.setLayoutManager(flex);
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

    private void obtainSimilarFilms(int filmId) {

        Call<ApiResponseSearchFilmByTitle> call = api.getSimilarMoviesByFilmId(filmId,
                ApiFilmService.API_KEY, ApiFilmService.LANG_SPANISH);
        call.enqueue(new Callback<ApiResponseSearchFilmByTitle>() {

            @Override
            public void onResponse(Call<ApiResponseSearchFilmByTitle> call,
                                   Response<ApiResponseSearchFilmByTitle> response) {
                if (response.isSuccessful()) {
                    Log.d("Success", "SIMILARFILMS");
                    ApiResponseSearchFilmByTitle apiResponse = response.body();
                    Log.d("SIMILARFILMS", apiResponse.toString());
                    List<Film> listOfSimilarFilms = apiResponse.getResults();
                    List<Film> limitedFilms =
                            listOfSimilarFilms.size() > 9 ? listOfSimilarFilms.subList(0, 9) : listOfSimilarFilms;
                    rvSimilarFilms.setLayoutManager(new GridLayoutManager(context, 3));
                    rvSimilarFilms.setAdapter(new FilmGridAdapter(limitedFilms));
                    rvSimilarFilms.setHasFixedSize(true);
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

    private void setListeners() {
        btAddToDiary.setOnClickListener(v -> {
            Intent resultIntent = new Intent();
            resultIntent.putExtra("Film", film);
            setResult(RESULT_OK, resultIntent);
            finish();
        });
    }
}