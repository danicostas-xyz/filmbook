package xyz.danicostas.filmapp.view.adapter;

import static androidx.core.content.ContextCompat.startActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import xyz.danicostas.filmapp.R;
import xyz.danicostas.filmapp.model.service.GestorUser;
import xyz.danicostas.filmapp.view.activity.FilmGridActivity;
import xyz.danicostas.filmapp.model.entity.Film;
import xyz.danicostas.filmapp.model.entity.FilmList;

public class FilmListAdapter extends RecyclerView.Adapter<FilmListAdapter.FilmListViewHolder> {
    private List<FilmList> filmLists;
    public static final String FILM_LIST_NAME = "Film List Name";
    public static final String FILM_LIST_CONTENT = "Film List Content";

    public FilmListAdapter(List<FilmList> filmLists) {
        this.filmLists = filmLists;
    }

    @NonNull
    @Override
    public FilmListViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.film_list, parent, false);
        return new FilmListViewHolder(view);
    }

    public void updateList(List<FilmList> peliculas) {
        filmLists.clear();
        filmLists.addAll(peliculas);
        notifyDataSetChanged();
    }

    static class FilmListViewHolder extends RecyclerView.ViewHolder {
        TextView txtTitulo, txtDirector;
        TextView tvListTitle, tvVerTodo;
        ConstraintLayout layout;
        RecyclerView nestedList;
        FilmListNestedAdapter adapter;

        FilmListViewHolder(View itemView) {
            super(itemView);

            tvListTitle = itemView.findViewById(R.id.tvListName);
            layout = itemView.findViewById(R.id.FilmListLayout);
            tvVerTodo = itemView.findViewById(R.id.tvVerTodo);
            /*
             * Aquí creamos las películas de cada una de las listas de películas que se crean en este adapter
             * Lista de Listas de películas > cada lista tiene una lista de películas
             */
            nestedList = itemView.findViewById(R.id.RVNestedList);
            nestedList.setLayoutManager(new LinearLayoutManager(itemView.getContext(), RecyclerView.HORIZONTAL, false));
            adapter = new FilmListNestedAdapter(new ArrayList<>());
            nestedList.setAdapter(adapter);
        }

        public void setFilms(List<Film> films) {
            adapter.updateList(films);
        }
    }

    @Override
    public void onBindViewHolder(@NonNull FilmListViewHolder holder, int position) {
        FilmList listaDeListas = filmLists.get(position);
        holder.tvListTitle.setText(listaDeListas.getListName());
        holder.setFilms(listaDeListas.getContent());
        holder.tvVerTodo.setOnClickListener(view -> {
            Intent intent = new Intent(holder.itemView.getContext(), FilmGridActivity.class);
            intent.putExtra(FILM_LIST_NAME, holder.tvListTitle.getText());
            intent.putExtra(FILM_LIST_CONTENT, listaDeListas);
            startActivity(holder.itemView.getContext(), intent, Bundle.EMPTY);
        });
    }

    @Override
    public int getItemCount() {
        return filmLists.size();
    }


}
