package xyz.danicostas.filmapp.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

import java.util.Arrays;
import java.util.List;

import xyz.danicostas.filmapp.R;
import xyz.danicostas.filmapp.adapter.FilmGridAdapter;
import xyz.danicostas.filmapp.adapter.FilmListAdapter;
import xyz.danicostas.filmapp.model.entity.Film;
import xyz.danicostas.filmapp.model.entity.FilmList;

public class FilmGridActivity extends AppCompatActivity {

    private FilmList filmList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_film_list_detail);

        Intent intent = getIntent();
        filmList = (FilmList) intent.getSerializableExtra(FilmListAdapter.FILM_LIST_CONTENT);


        TextView tvFilmListTitle = findViewById(R.id.tvFilmListTitle);
        tvFilmListTitle.setText(filmList.getListName());

        RecyclerView recyclerView = findViewById(R.id.RVFilmGrid);
        recyclerView.setLayoutManager(new GridLayoutManager(this, 3));
        FilmGridAdapter adapter = new FilmGridAdapter(filmList.getContent());
        recyclerView.setAdapter(adapter);

    }
}