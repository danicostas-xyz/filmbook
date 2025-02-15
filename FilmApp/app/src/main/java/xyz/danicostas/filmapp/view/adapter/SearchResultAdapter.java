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

public class SearchResultAdapter extends RecyclerView.Adapter<SearchResultAdapter.SearchResultViewHolder> {
    private List<Film> listOfFIlms;
    public SearchResultAdapter(List<Film> listOfFIlms) {
        this.listOfFIlms = listOfFIlms;
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
            tVsearchTitle = itemView.findViewById(R.id.tvSearchTitle);
            tVsearchRating = itemView.findViewById(R.id.tvSearchRating);
            tVsearchContent = itemView.findViewById(R.id.tvSearchContent);
            iVsearchFilmPoster = itemView.findViewById(R.id.imgViewSearchFilmPoster);
        }
    }

    public void updateList(List<Film> newList) {
        listOfFIlms.clear();
        listOfFIlms.addAll(newList);
        notifyDataSetChanged();
    }

    @Override
    public void onBindViewHolder(@NonNull SearchResultViewHolder holder, int position) {
        Film pelicula = listOfFIlms.get(position);

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
        return listOfFIlms.size();
    }

}
