package xyz.danicostas.filmapp.view.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import xyz.danicostas.filmapp.R;
import xyz.danicostas.filmapp.model.entity.ApiResponseKeywordsByFilmId;
import xyz.danicostas.filmapp.model.entity.Film;
import xyz.danicostas.filmapp.model.entity.KeywordOrGenres;

public class KeywordsGenresAdapter extends RecyclerView.Adapter<KeywordsGenresAdapter.KeywordsGenresViewHolder> {
    private List<KeywordOrGenres> listOfWords;
    public KeywordsGenresAdapter(List<KeywordOrGenres> listOfWords) {
        this.listOfWords = listOfWords;
    }
    public static final String KEYWORD_GENRE = "Keyword or Genre";

    @NonNull
    @Override
    public KeywordsGenresViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.keywords_genres, parent, false);
        return new KeywordsGenresViewHolder(view);
    }

    static class KeywordsGenresViewHolder extends RecyclerView.ViewHolder {
        TextView tvKeywordsGenres;

        KeywordsGenresViewHolder(View itemView) {
            super(itemView);
            tvKeywordsGenres = itemView.findViewById(R.id.tvKeywordGenre);
        }
    }

    public void updateList(List<KeywordOrGenres> newList) {
        listOfWords.clear();
        listOfWords.addAll(newList);
        notifyDataSetChanged();
    }

    @Override
    public void onBindViewHolder(@NonNull KeywordsGenresViewHolder holder, int position) {
        String keywordGenre = listOfWords.get(position).getName();
        holder.tvKeywordsGenres.setText(keywordGenre.toLowerCase());
        // TODO (Abrir un intent cuando se pulse el Keyword o Genre para hacer una búsqueda nueva)
        // Ahora mismo solo hay una clase que agrupa a keyword y genre, pero supongo que tendré
        // que separarlas en 2 porque si no luego no puedo saber desde aquí si es keyword o genre
        // para poder hacer la llamada pertintente a la api.
    }

    @Override
    public int getItemCount() {
        return listOfWords.size();
    }

}
