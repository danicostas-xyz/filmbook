package xyz.danicostas.filmapp.view.adapter;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

import xyz.danicostas.filmapp.R;
import xyz.danicostas.filmapp.model.interfaces.OnAddFilmClickListener;
import xyz.danicostas.filmapp.model.entity.Film;
import xyz.danicostas.filmapp.model.entity.FilmList;
import xyz.danicostas.filmapp.view.fragment.FilmGridFragment;

public class FilmListAdapter extends RecyclerView.Adapter<FilmListAdapter.FilmListViewHolder> {
    private List<FilmList> listOfFilmLists;
    public static final String FILM_LIST_NAME = "Film List Name";
    public static final String FILM_LIST_CONTENT = "Film List Content";
    private OnAddFilmClickListener listener;
    private FragmentManager fragmentManager;

    public FilmListAdapter(List<FilmList> listOfFilmLists, OnAddFilmClickListener listener, FragmentManager fragmentManager) {
        this.listOfFilmLists = listOfFilmLists;
        this.listener = listener;
        this.fragmentManager = fragmentManager;
    }

    @NonNull
    @Override
    public FilmListViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.film_list, parent, false);
        return new FilmListViewHolder(view);
    }

    public void updateList(List<FilmList> newList) {
        listOfFilmLists.clear();
        listOfFilmLists.addAll(newList);
        notifyDataSetChanged();
    }

    static class FilmListViewHolder extends RecyclerView.ViewHolder {
        TextView tvListTitle, tvVerTodo;
        ConstraintLayout layout;
        RecyclerView nestedList;
        FilmListNestedAdapter adapter;
        ImageButton btAddNewFilmToListFromProfile;

        FilmListViewHolder(View itemView) {
            super(itemView);

            tvListTitle = itemView.findViewById(R.id.tvListName);
            layout = itemView.findViewById(R.id.FilmListLayout);
            tvVerTodo = itemView.findViewById(R.id.tvVerTodo);
            btAddNewFilmToListFromProfile = itemView.findViewById(R.id.btAddNewFilmToListFromProfile);
            /*
             * Aquí creamos las películas de cada una de las listas de películas que se crean en este adapter
             * Lista de Listas de películas > cada lista tiene una lista de películas
             */
            nestedList = itemView.findViewById(R.id.rclViewNestedList);
            nestedList.setLayoutManager(new LinearLayoutManager(itemView.getContext(), RecyclerView.HORIZONTAL, false));
            nestedList.setHasFixedSize(true);
            adapter = new FilmListNestedAdapter(new ArrayList<>());
            nestedList.setAdapter(adapter);
        }

        public void setFilms(List<Film> films) {
            adapter.updateList(films);
        }


    }

    @Override
    public void onBindViewHolder(@NonNull FilmListViewHolder holder, int position) {
        FilmList filmList = listOfFilmLists.get(position);
        Log.d("FIRESTORE-DATA///FILM-LIST-ADAPTER", "Lista: " + filmList.toString());
        holder.tvListTitle.setText(filmList.getListName());
        holder.setFilms(filmList.getContent());
        /*holder.tvVerTodo.setOnClickListener(view -> {
            Intent intent = new Intent(holder.itemView.getContext(), FilmGridActivity.class);
            intent.putExtra(FILM_LIST_NAME, holder.tvListTitle.getText());
            intent.putExtra(FILM_LIST_CONTENT, listaDeListas);
            startActivity(holder.itemView.getContext(), intent, Bundle.EMPTY);
        });*/

        holder.tvVerTodo.setOnClickListener(v -> {
            Fragment fragment = new FilmGridFragment();

            Bundle args = new Bundle();
            args.putSerializable("FilmList", filmList);
            fragment.setArguments(args);

            fragmentManager
                    .beginTransaction()
                    .replace(R.id.fragment_container, fragment)
                    .addToBackStack(null)
                    .commit();
        });

        holder.btAddNewFilmToListFromProfile.setOnClickListener(v -> {
            FilmList list = listOfFilmLists.get(position);
            listener.onAddFilmClicked(list);
        });
    }


    @Override
    public int getItemCount() {
        return listOfFilmLists.size();
    }


}
