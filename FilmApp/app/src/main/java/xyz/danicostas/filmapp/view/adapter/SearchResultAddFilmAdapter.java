package xyz.danicostas.filmapp.view.adapter;

import static androidx.core.content.ContextCompat.startActivity;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.util.List;

import xyz.danicostas.filmapp.R;
import xyz.danicostas.filmapp.model.entity.Film;
import xyz.danicostas.filmapp.model.service.ApiFilmService;
import xyz.danicostas.filmapp.view.activity.FilmDetailActivity;

public class SearchResultAddFilmAdapter extends RecyclerView.Adapter<SearchResultAddFilmAdapter.SearchResultViewHolder> {
    private List<Film> listOfFIlms;
    public SearchResultAddFilmAdapter(List<Film> listOfFIlms) {
        this.listOfFIlms = listOfFIlms;
    }
    public static final String FILM_ID = "Film ID";

    @NonNull
    @Override
    public SearchResultViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.search_result_add_film, parent, false);
        return new SearchResultViewHolder(view);
    }

    static class SearchResultViewHolder extends RecyclerView.ViewHolder {
        CheckBox cbAddFilm;
        TextView tvSearchAddFilm;
        ImageView imgViewSearchFilmPosterAddFilm;
        LinearLayout linearLayoutAddNewFilm;
        SearchResultViewHolder(View itemView) {
            super(itemView);
            cbAddFilm = itemView.findViewById(R.id.cbAddFilm);
            tvSearchAddFilm = itemView.findViewById(R.id.tvSearchAddFilm);
            imgViewSearchFilmPosterAddFilm = itemView.findViewById(R.id.imgViewSearchFilmPosterAddFilm);
            linearLayoutAddNewFilm = itemView.findViewById(R.id.linearLayoutAddNewFilm);
        }
    }

    public void updateList(List<Film> newList) {
        listOfFIlms.clear();
        listOfFIlms.addAll(newList);
        notifyDataSetChanged();
    }

    @Override
    public void onBindViewHolder(@NonNull SearchResultViewHolder holder, int position) {
        Film film = listOfFIlms.get(position);

        holder.tvSearchAddFilm.setText(film.getTitle());
        String posterPath = film.getPosterPath();
        String imageUrl = ApiFilmService.TMDB_API_IMAGE_URL + posterPath;

        Glide.with(holder.itemView.getContext())
                .load(imageUrl)
                .into(holder.imgViewSearchFilmPosterAddFilm);

        holder.linearLayoutAddNewFilm.setOnClickListener(view -> {
            Intent intent = new Intent(holder.itemView.getContext(), FilmDetailActivity.class);
            intent.putExtra(FILM_ID, film.getId());
            startActivity(holder.itemView.getContext(), intent, Bundle.EMPTY);
        });
    }

    @Override
    public int getItemCount() {
        return listOfFIlms.size();
    }

}
