package xyz.danicostas.filmapp.view.adapter;

import static androidx.core.content.ContextCompat.startActivity;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.util.List;
import java.util.function.Consumer;

import xyz.danicostas.filmapp.R;
import xyz.danicostas.filmapp.model.entity.Film;
import xyz.danicostas.filmapp.model.service.ApiFilmService;
import xyz.danicostas.filmapp.view.activity.FilmDetailActivity;

public class SearchResultAdapter extends RecyclerView.Adapter<SearchResultAdapter.SearchResultViewHolder> {
    private List<Film> listOfFIlms;
    public static final String FILM_ID = "Film ID";
    public Consumer<Film> callback;
    public SearchResultAdapter(List<Film> listOfFIlms, Consumer<Film> callback) {
        this.listOfFIlms = listOfFIlms;
        this.callback = callback;
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
        CardView cardViewSearch;
        SearchResultViewHolder(View itemView) {
            super(itemView);
            tVsearchTitle = itemView.findViewById(R.id.tvFilmDetailTitle);
            tVsearchRating = itemView.findViewById(R.id.tvSearchRating);
            tVsearchContent = itemView.findViewById(R.id.tvSearchContent);
            iVsearchFilmPoster = itemView.findViewById(R.id.imgViewSearchFilmPoster);
            cardViewSearch = itemView.findViewById(R.id.cardViewSearch);
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

        holder.tVsearchTitle.setText(film.getTitle());
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            holder.tVsearchTitle.setAutoSizeTextTypeUniformWithConfiguration(
                    14,
                    32,
                    4, TypedValue.COMPLEX_UNIT_SP
            );
        }
        holder.tVsearchRating.setText(String.valueOf(
                Math.round(film.getVoteAverage() * 10.0) / 10.0));
        holder.tVsearchContent.setText(film.getOverview());

        String posterPath = film.getPosterPath();
        String imageUrl = ApiFilmService.TMDB_API_IMAGE_URL + posterPath;

        Glide.with(holder.itemView.getContext())
                .load(imageUrl)
                .into(holder.iVsearchFilmPoster);

        if (callback != null) {
            // Si viene desde Review -> Se ejecuta callback que acepta Film y devuelve a NewReviewFragment
            holder.cardViewSearch.setOnClickListener(view -> {
                callback.accept(film);
            });
        } else {
            // Si no viene desde Review -> Se envía a FilmDetailActivity
            holder.cardViewSearch.setOnClickListener(view -> {
                Intent intent = new Intent(holder.itemView.getContext(), FilmDetailActivity.class);
                intent.putExtra(FILM_ID, film.getId());
                startActivity(holder.itemView.getContext(), intent, Bundle.EMPTY);
            });
        }
    }

    @Override
    public int getItemCount() {
        return listOfFIlms.size();
    }

}
