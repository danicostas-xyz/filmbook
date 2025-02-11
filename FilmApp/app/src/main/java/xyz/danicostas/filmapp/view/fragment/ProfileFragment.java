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

import xyz.danicostas.filmapp.R;
import xyz.danicostas.filmapp.view.adapter.FilmListAdapter;
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

        recyclerView = view.findViewById(R.id.RVFilmList);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext(), RecyclerView.VERTICAL, false));
        adapter = new FilmListAdapter(new ArrayList<>());
        recyclerView.setAdapter(adapter);

        viewModel = new ViewModelProvider(this).get(FilmListsViewModel.class);

        // Observar cambios en las listas
        viewModel.getListaPeliculas().observe(getViewLifecycleOwner(), peliculas -> {
            adapter.updateList(peliculas); // Método que actualiza la lista en el adapter
        });

        viewModel.loadUserFilmLists();

        return view;
    }
}
