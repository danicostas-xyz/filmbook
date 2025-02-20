package xyz.danicostas.filmapp.view.adapter;

import static androidx.core.content.ContextCompat.startActivity;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
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

import xyz.danicostas.filmapp.R;
import xyz.danicostas.filmapp.model.entity.Review;
import xyz.danicostas.filmapp.model.service.ApiFilmService;
import xyz.danicostas.filmapp.view.activity.FilmDetailActivity;

public class ReviewAdapter extends RecyclerView.Adapter<ReviewAdapter.SearchResultViewHolder> {
    private List<Review> listOfReviews;
    public ReviewAdapter(List<Review> listOfFIlms) {
        this.listOfReviews = listOfFIlms;
    }
    public static final String FILM_ID = "Film ID";

    @NonNull
    @Override
    public SearchResultViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.review, parent,
                false);
        return new SearchResultViewHolder(view);
    }

    static class SearchResultViewHolder extends RecyclerView.ViewHolder {
        TextView tvFilmTitle, tvFilmRating, tvReviewDescription, tvReviewTitle;
        ImageView imgVSearchFilmPoster;
        CardView cardViewSearch;
        SearchResultViewHolder(View itemView) {
            super(itemView);
            tvFilmTitle = itemView.findViewById(R.id.tvReviewFilmTitle);
            tvFilmRating = itemView.findViewById(R.id.tvReviewRating);
            tvReviewDescription = itemView.findViewById(R.id.tvReviewContent);
            imgVSearchFilmPoster = itemView.findViewById(R.id.imgViewReviewFilmPoster);
            cardViewSearch = itemView.findViewById(R.id.cardViewSearch);
            tvReviewTitle = itemView.findViewById(R.id.tvReviewTitle);
        }
    }

    public void updateList(List<Review> newList) {
        listOfReviews.clear();
        listOfReviews.addAll(newList);
        notifyDataSetChanged();
    }

    @Override
    public void onBindViewHolder(@NonNull SearchResultViewHolder holder, int position) {
        Review review = listOfReviews.get(position);

        holder.tvFilmTitle.setText(review.getFilmTitle());
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            holder.tvFilmTitle.setAutoSizeTextTypeUniformWithConfiguration(
                    18,
                    32,
                    4, TypedValue.COMPLEX_UNIT_SP
            );
        }
        holder.tvFilmRating.setText(String.valueOf(
                Math.round(review.getVoteAverage() * 10.0) / 10.0));
        holder.tvReviewDescription.setText(review.getOverview());
        holder.tvReviewTitle.setText(review.getTitle());
        String posterPath = review.getPosterPath();
        String imageUrl = ApiFilmService.TMDB_API_IMAGE_URL + posterPath;

        Glide.with(holder.itemView.getContext())
                .load(imageUrl)
                .into(holder.imgVSearchFilmPoster);

        holder.cardViewSearch.setOnClickListener(view -> {
            Intent intent = new Intent(holder.itemView.getContext(), FilmDetailActivity.class);
            intent.putExtra(FILM_ID, review.getFilmId());
            Log.i("FILM_ID_FROM_REVIEW_ADAPTER", "FilmID: " + review.getFilmId());
            startActivity(holder.itemView.getContext(), intent, Bundle.EMPTY);
        });  // TODO Podemos poner un intent al detalle de la review
    }

    @Override
    public int getItemCount() {
        return listOfReviews.size();
    }

}
