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
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.util.List;

import xyz.danicostas.filmapp.R;
import xyz.danicostas.filmapp.model.entity.Film;
import xyz.danicostas.filmapp.view.activity.FilmDetailActivity;

public class SearchResultAdapter extends RecyclerView.Adapter<SearchResultAdapter.SearchResultViewHolder> {
    private List<Film> listOfFIlms;
    public SearchResultAdapter(List<Film> listOfFIlms) {
        this.listOfFIlms = listOfFIlms;
    }
    public static final String FILM_ID = "Film ID";

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
            tVsearchTitle = itemView.findViewById(R.id.tvSearchTitle);
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
        holder.tVsearchRating.setText(String.valueOf(film.getVoteAverage()));
        holder.tVsearchContent.setText(film.getOverview());

        String posterPath = film.getPosterPath();
        String imageUrl = "https://image.tmdb.org/t/p/w500" + posterPath;

        Glide.with(holder.itemView.getContext())
                .load(imageUrl)
                .into(holder.iVsearchFilmPoster);

        holder.cardViewSearch.setOnClickListener(view -> {
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
