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

public class FilmListNestedAdapter extends RecyclerView.Adapter<FilmListNestedAdapter.FilmListNestedViewHolder> {
    private List<Film> peliculas;
    public FilmListNestedAdapter(List<Film> peliculas) {
        this.peliculas = peliculas;
    }

    @NonNull
    @Override
    public FilmListNestedViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.film_item, parent, false);
        return new FilmListNestedViewHolder(view);
    }

    static class FilmListNestedViewHolder extends RecyclerView.ViewHolder {
        TextView txtTitulo, txtDirector;

        FilmListNestedViewHolder(View itemView) {
            super(itemView);
            /* txtTitulo = itemView.findViewById(R.id.txtTitulo);
            txtDirector = itemView.findViewById(R.id.txtDirector);*/
        }
    }

    @Override
    public void onBindViewHolder(@NonNull FilmListNestedViewHolder holder, int position) {
        Film pelicula = peliculas.get(position);


        /* holder.txtTitulo.setText(pelicula.getTitulo());
        holder.txtDirector.setText("Dirigida por: " + pelicula.getDirector());*/
    }

    @Override
    public int getItemCount() {
        return peliculas.size();
    }

}
