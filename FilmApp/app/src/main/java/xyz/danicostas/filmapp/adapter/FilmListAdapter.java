package xyz.danicostas.filmapp.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.Arrays;
import java.util.List;

import xyz.danicostas.filmapp.R;
import xyz.danicostas.filmapp.model.entity.Film;
import xyz.danicostas.filmapp.model.entity.FilmList;

public class FilmListAdapter extends RecyclerView.Adapter<FilmListAdapter.FilmListViewHolder> {
    private List<FilmList> filmLists;

    public FilmListAdapter(List<FilmList> filmLists) {
        this.filmLists = filmLists;
    }

    @NonNull
    @Override
    public FilmListViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.film_list, parent, false);
        return new FilmListViewHolder(view);
    }

    static class FilmListViewHolder extends RecyclerView.ViewHolder {
        TextView txtTitulo, txtDirector;

        FilmListViewHolder(View itemView) {
            super(itemView);
            Context context = itemView.getContext();

            List<Film> listaPeliculas = Arrays.asList(
                    new Film(),new Film(),new Film(),new Film(), new Film(), new Film(),new Film(), new Film(), new Film(), new Film(), new Film(), new Film(), new Film(),new Film(), new Film()
            );
            RecyclerView nestedList = itemView.findViewById(R.id.RVNestedList);
            nestedList.setLayoutManager(new LinearLayoutManager(context, RecyclerView.HORIZONTAL, false));
            FilmListNestedAdapter adapter = new FilmListNestedAdapter(listaPeliculas);
            nestedList.setAdapter(adapter);
            /* txtTitulo = itemView.findViewById(R.id.txtTitulo);
            txtDirector = itemView.findViewById(R.id.txtDirector);*/
        }
    }

    @Override
    public void onBindViewHolder(@NonNull FilmListViewHolder holder, int position) {
        FilmList listaDeListas = filmLists.get(position);

        /* holder.txtTitulo.setText(pelicula.getTitulo());
        holder.txtDirector.setText("Dirigida por: " + pelicula.getDirector());*/
    }

    @Override
    public int getItemCount() {
        return filmLists.size();
    }


}
