package xyz.danicostas.filmapp.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import xyz.danicostas.filmapp.R;
import xyz.danicostas.filmapp.model.entity.Film;

public class FilmGridAdapter extends RecyclerView.Adapter<FilmGridAdapter.FilmGridViewHolder> {
    private List<Film> peliculas;

    public FilmGridAdapter(List<Film> peliculas) {
        this.peliculas = peliculas;
    }

    @NonNull
    @Override
    public FilmGridViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.film_item_grid, parent, false);
        return new FilmGridViewHolder(view);
    }

    static class FilmGridViewHolder extends RecyclerView.ViewHolder {
        TextView txtTitulo, txtDirector;

        FilmGridViewHolder(View itemView) {
            super(itemView);
            /* txtTitulo = itemView.findViewById(R.id.txtTitulo);
            txtDirector = itemView.findViewById(R.id.txtDirector);*/
        }
    }

    @Override
    public void onBindViewHolder(@NonNull FilmGridViewHolder holder, int position) {
        Film pelicula = peliculas.get(position);
        /* holder.txtTitulo.setText(pelicula.getTitulo());
        holder.txtDirector.setText("Dirigida por: " + pelicula.getDirector());*/
    }

    @Override
    public int getItemCount() {
        return peliculas.size();
    }


}
