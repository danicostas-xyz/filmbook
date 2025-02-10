package xyz.danicostas.filmapp.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import xyz.danicostas.filmapp.R;
import xyz.danicostas.filmapp.adapter.FilmGridAdapter;
import xyz.danicostas.filmapp.adapter.FilmListAdapter;
import xyz.danicostas.filmapp.model.entity.FilmList;
import xyz.danicostas.filmapp.model.entity.FilmTMDB;
import xyz.danicostas.filmapp.model.entity.MovieResponse;
import xyz.danicostas.filmapp.model.service.ApiFilmService;
import xyz.danicostas.filmapp.model.service.TMDBApiService;

public class FilmGridActivity extends AppCompatActivity {
    private FilmList filmList;
    ProgressDialog mDefaultDialog;
    Context context = this;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_film_list_detail);

        Intent intent = getIntent();
        filmList = (FilmList) intent.getSerializableExtra(FilmListAdapter.FILM_LIST_CONTENT);


        TextView tvFilmListTitle = findViewById(R.id.tvFilmListTitle);
        tvFilmListTitle.setText(filmList.getListName());



    }

    @Override
    protected void onResume() {
        super.onResume();
        obtainTrendingMovies();
    }

    public void obtainTrendingMovies(){
        mostrarEspera();

        TMDBApiService api = ApiFilmService.getInstance().getApi();

        Call<MovieResponse> call = api.getTrendingMovies("50f1837f71c3d1a88f1de905029df6c0");

        call.enqueue(new Callback<MovieResponse>() {

            @Override
            public void onResponse(Call<MovieResponse> call, Response<MovieResponse> response) {
                if (response.isSuccessful()) {
                    Log.d("Success", "Datos traidos del servicio");
                    //Gracias a Gson, me convierte los json a objetos Usuario
                    MovieResponse movieResponse = response.body();
                    Log.d("hey", movieResponse.toString());

                    List<FilmTMDB> lista = movieResponse.getResults();

                    RecyclerView recyclerView = findViewById(R.id.RVFilmGrid);
                    recyclerView.setLayoutManager(new GridLayoutManager(context, 3));
                    recyclerView.setAdapter(new FilmGridAdapter(lista));

                } else {
                    Log.d("Error", response.code() + " " + response.message());
                    return;
                }

                cancelarEspera();
            }

            @Override
            public void onFailure(Call call, Throwable t) {
                Log.d("Error", t.toString());
                cancelarEspera();
            }
        });
    }

    public void mostrarEspera() {
        mDefaultDialog = new ProgressDialog(this);
        // El valor predeterminado es la forma de círculos pequeños
        mDefaultDialog.setProgressStyle (android.app.ProgressDialog.STYLE_SPINNER);
        mDefaultDialog.setMessage("Solicitando datos ...");
        mDefaultDialog.setCanceledOnTouchOutside(false);// Por defecto true
        mDefaultDialog.show();
    }

    public void cancelarEspera(){
        mDefaultDialog.cancel();
    }
}