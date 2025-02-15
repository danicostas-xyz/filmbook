package xyz.danicostas.filmapp.view.adapter;

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

    public void updateList(List<Film> newList) {
        peliculas.clear();
        peliculas.addAll(newList);
        notifyDataSetChanged();
    }

    static class FilmListNestedViewHolder extends RecyclerView.ViewHolder {
        TextView txtTitulo, txtDirector;
        ImageView imageView;

        FilmListNestedViewHolder(View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.imageView);
            /* txtTitulo = itemView.findViewById(R.id.txtTitulo);
            txtDirector = itemView.findViewById(R.id.txtDirector);*/
        }
    }

    @Override
    public void onBindViewHolder(@NonNull FilmListNestedViewHolder holder, int position) {
        Film pelicula = peliculas.get(position);
        Glide.with(holder.itemView.getContext())
                .load(pelicula.getPosterPath())  // La URL de la imagen
                .into(holder.imageView);

        /* holder.txtTitulo.setText(pelicula.getTitulo());
        holder.txtDirector.setText("Dirigida por: " + pelicula.getDirector());*/
    }

    @Override
    public int getItemCount() {
        return peliculas.size();
    }

}
