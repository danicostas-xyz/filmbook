package xyz.danicostas.filmapp.view.adapter;

import static androidx.core.content.ContextCompat.startActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.util.List;

import xyz.danicostas.filmapp.R;
import xyz.danicostas.filmapp.model.entity.Film;
import xyz.danicostas.filmapp.model.service.ApiFilmService;
import xyz.danicostas.filmapp.view.activity.FilmDetailActivity;

public class FilmListNestedAdapter extends RecyclerView.Adapter<FilmListNestedAdapter.FilmListNestedViewHolder> {
    private List<Film> listOfFilms;
    public FilmListNestedAdapter(List<Film> listOfFilms) {
        this.listOfFilms = listOfFilms;
    }
    public static final String FILM_ID = "Film ID";

    @NonNull
    @Override
    public FilmListNestedViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.film_item, parent, false);
        return new FilmListNestedViewHolder(view);
    }

    public void updateList(List<Film> newList) {
        listOfFilms.clear();
        listOfFilms.addAll(newList);
        notifyDataSetChanged();
    }

    static class FilmListNestedViewHolder extends RecyclerView.ViewHolder {
        ImageView imageView;

        FilmListNestedViewHolder(View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.imageView);
        }
    }

    @Override
    public void onBindViewHolder(@NonNull FilmListNestedViewHolder holder, int position) {
        Film film = listOfFilms.get(position);
        Glide.with(holder.itemView.getContext())
                .load(ApiFilmService.TMDB_API_IMAGE_URL + film.getPosterPath())  // La URL de la imagen
                .into(holder.imageView);

        holder.imageView.setOnClickListener(view -> {
            Intent intent = new Intent(holder.itemView.getContext(), FilmDetailActivity.class);
            intent.putExtra(FILM_ID, film.getId());
            startActivity(holder.itemView.getContext(), intent, Bundle.EMPTY);
        });
    }

    @Override
    public int getItemCount() {
        return listOfFilms.size();
    }

}
