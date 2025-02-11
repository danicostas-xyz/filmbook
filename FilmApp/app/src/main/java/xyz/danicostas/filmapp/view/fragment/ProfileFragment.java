package xyz.danicostas.filmapp.view.fragment;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import xyz.danicostas.filmapp.R;
import xyz.danicostas.filmapp.view.adapter.FilmListAdapter;
import xyz.danicostas.filmapp.model.entity.Film;
import xyz.danicostas.filmapp.model.entity.FilmList;
import xyz.danicostas.filmapp.viewmodel.FilmListsViewModel;

public class ProfileFragment extends Fragment {

    private RecyclerView recyclerView;
    private FilmListAdapter adapter;
    private FilmListsViewModel viewModel;

    public ProfileFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_profile, container, false);

        /*List<Film> listaPeliculas = Arrays.asList(
                new Film(),new Film(),new Film(),new Film(), new Film(), new Film(),new Film(), new Film(), new Film(), new Film(), new Film(), new Film(), new Film(),new Film(), new Film()
        );
        List<FilmList> listaDeListas = Arrays.asList(
                new FilmList(), new FilmList(),new FilmList(),new FilmList(),new FilmList(),new FilmList()
        );

        listaDeListas.get(0).setContent(listaPeliculas);
        listaDeListas.get(0).setListName("Favoritas");
        listaDeListas.get(1).setContent(listaPeliculas);
        listaDeListas.get(1).setListName("Anime");
        listaDeListas.get(2).setContent(listaPeliculas);
        listaDeListas.get(2).setListName("Pendientes");
        listaDeListas.get(3).setContent(listaPeliculas);
        listaDeListas.get(3).setListName("Watch List");
        listaDeListas.get(4).setContent(listaPeliculas);
        listaDeListas.get(4).setListName("Sci-Fi");
        listaDeListas.get(5).setContent(listaPeliculas);
        listaDeListas.get(5).setListName("Españolas");*/

        recyclerView = view.findViewById(R.id.RVFilmList);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext(), RecyclerView.VERTICAL, false));
        adapter = new FilmListAdapter(new ArrayList<>());
        recyclerView.setAdapter(adapter);

        viewModel = new ViewModelProvider(this).get(FilmListsViewModel.class);

        // Observar cambios en las listas
        viewModel.getListaPeliculas().observe(getViewLifecycleOwner(), peliculas -> {
            adapter.updateList(peliculas); // Método que actualiza la lista en el adapter
        });


        return view;
    }
}