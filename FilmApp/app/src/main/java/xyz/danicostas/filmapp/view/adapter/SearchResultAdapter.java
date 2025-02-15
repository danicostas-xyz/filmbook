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
import xyz.danicostas.filmapp.model.entity.FilmList;

public class SearchResultAdapter extends RecyclerView.Adapter<SearchResultAdapter.SearchResultViewHolder> {
    private List<Film> peliculas;
    public SearchResultAdapter(List<Film> peliculas) {
        this.peliculas = peliculas;
    }

    @NonNull
    @Override
    public SearchResultViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.search_result, parent, false);
        return new SearchResultViewHolder(view);
    }

    static class SearchResultViewHolder extends RecyclerView.ViewHolder {
        TextView tVsearchTitle, tVsearchRating, tVsearchContent;
        ImageView iVsearchFilmPoster;
        SearchResultViewHolder(View itemView) {
            super(itemView);
            tVsearchTitle = itemView.findViewById(R.id.TVsearchTitle);
            tVsearchRating = itemView.findViewById(R.id.TVsearchRating);
            tVsearchContent = itemView.findViewById(R.id.TVsearchContent);
            iVsearchFilmPoster = itemView.findViewById(R.id.IVsearchFilmPoster);
        }
    }

    public void updateList(List<Film> newList) {
        peliculas.clear();
        peliculas.addAll(newList);
        notifyDataSetChanged();
    }

    @Override
    public void onBindViewHolder(@NonNull SearchResultViewHolder holder, int position) {
        Film pelicula = peliculas.get(position);

        holder.tVsearchTitle.setText(pelicula.getTitle());
        holder.tVsearchRating.setText(String.valueOf(pelicula.getVoteAverage()));
        holder.tVsearchContent.setText(pelicula.getOverview());

        String posterPath = pelicula.getPosterPath();
        String imageUrl = "https://image.tmdb.org/t/p/w500" + posterPath;

        Glide.with(holder.itemView.getContext())
                .load(imageUrl)
                .into(holder.iVsearchFilmPoster);
    }

    @Override
    public int getItemCount() {
        return peliculas.size();
    }

}
