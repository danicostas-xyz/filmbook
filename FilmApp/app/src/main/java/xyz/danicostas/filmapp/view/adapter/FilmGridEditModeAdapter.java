package xyz.danicostas.filmapp.view.adapter;

import static androidx.core.content.ContextCompat.startActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.util.List;

import xyz.danicostas.filmapp.R;
import xyz.danicostas.filmapp.model.entity.Film;
import xyz.danicostas.filmapp.model.service.ApiFilmService;
import xyz.danicostas.filmapp.view.activity.FilmDetailActivity;

public class FilmGridEditModeAdapter extends RecyclerView.Adapter<FilmGridEditModeAdapter.FilmGridViewHolder> {
    private List<Film> listOfFilms;
    public FilmGridEditModeAdapter(List<Film> listOfFilms) {
        this.listOfFilms = listOfFilms;
    }
    public static final String FILM_ID = "Film ID";

    @NonNull
    @Override
    public FilmGridViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.film_item_edit_mode, parent, false);
        return new FilmGridViewHolder(view);
    }

    public void removeFilm (int position) {
        this.listOfFilms.remove(position);
        notifyDataSetChanged();
    }

    static class FilmGridViewHolder extends RecyclerView.ViewHolder {
        ImageView imageView;
        FrameLayout btDeleteFilm;
        FilmGridViewHolder(View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.imageView);
            btDeleteFilm = itemView.findViewById(R.id.btDeleteFilm);
        }
    }

    @Override
    public void onBindViewHolder(@NonNull FilmGridViewHolder holder, int position) {
        Film film = listOfFilms.get(position);
        // URL de la imagen
        String posterPath = film.getPosterPath();
        String imageUrl = ApiFilmService.TMDB_API_IMAGE_URL + posterPath;

        // Usar Glide para cargar la imagen
        Glide.with(holder.itemView.getContext())
                .load(imageUrl)  // La URL de la imagen
                .into(holder.imageView);
        holder.imageView.setOnClickListener(view -> {
            Intent intent = new Intent(holder.itemView.getContext(), FilmDetailActivity.class);
            intent.putExtra(FILM_ID, film.getId());
            startActivity(holder.itemView.getContext(), intent, Bundle.EMPTY);
        });

        holder.btDeleteFilm.setOnClickListener(v -> {
            removeFilm(position);
        });
    }

    @Override
    public int getItemCount() {
        return listOfFilms.size();
    }

}
