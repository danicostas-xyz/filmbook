package xyz.danicostas.filmapp.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;

import java.util.Arrays;
import java.util.List;

import xyz.danicostas.filmapp.R;
import xyz.danicostas.filmapp.adapter.FilmGridAdapter;
import xyz.danicostas.filmapp.model.entity.Film;

public class FilmGridActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_film_list_detail);

        List<Film> listaPeliculas = Arrays.asList(
                new Film(),new Film(),new Film(),new Film(), new Film(), new Film(),new Film(), new Film(), new Film(), new Film(), new Film(), new Film(), new Film(),new Film(), new Film()
        );

        RecyclerView recyclerView = findViewById(R.id.RVFilmGrid);
        recyclerView.setLayoutManager(new GridLayoutManager(this, 3));
        FilmGridAdapter adapter = new FilmGridAdapter(listaPeliculas);
        recyclerView.setAdapter(adapter);

    }
}