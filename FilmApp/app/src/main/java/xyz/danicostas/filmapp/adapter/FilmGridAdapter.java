package xyz.danicostas.filmapp.adapter;

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
import xyz.danicostas.filmapp.model.entity.FilmTMDB;
import xyz.danicostas.filmapp.model.entity.MovieResponse;

public class FilmGridAdapter extends RecyclerView.Adapter<FilmGridAdapter.FilmGridViewHolder> {
    private List<MovieResponse.FilmTMDB> peliculas;
    public FilmGridAdapter(List<MovieResponse.FilmTMDB> peliculas) {
        this.peliculas = peliculas;
    }

    @NonNull
    @Override
    public FilmGridViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.film_item, parent, false);
        return new FilmGridViewHolder(view);
    }

    static class FilmGridViewHolder extends RecyclerView.ViewHolder {
        TextView txtTitulo, txtDirector;
        ImageView imageView;
        FilmGridViewHolder(View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.imageView);
            /* txtTitulo = itemView.findViewById(R.id.txtTitulo);
            txtDirector = itemView.findViewById(R.id.txtDirector);*/
        }
    }

    @Override
    public void onBindViewHolder(@NonNull FilmGridViewHolder holder, int position) {
        MovieResponse.FilmTMDB pelicula = peliculas.get(position);
        // URL de la imagen
        String posterPath = pelicula.getPosterPath();
        String imageUrl = "https://image.tmdb.org/t/p/w500" + posterPath;

        // Usar Glide para cargar la imagen
        Glide.with(holder.itemView.getContext())
                .load(imageUrl)  // La URL de la imagen
                .into(holder.imageView);
        /* holder.txtTitulo.setText(pelicula.getTitulo());
        holder.txtDirector.setText("Dirigida por: " + pelicula.getDirector());*/
    }

    @Override
    public int getItemCount() {
        return peliculas.size();
    }


}
