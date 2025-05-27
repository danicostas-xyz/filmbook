package xyz.danicostas.filmapp.view.fragment;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.LinearSnapHelper;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageButton;

import java.util.ArrayList;

import xyz.danicostas.filmapp.R;
import xyz.danicostas.filmapp.view.adapter.FilmListAdapter;
import xyz.danicostas.filmapp.viewmodel.FilmListsViewModel;

public class ProfileFragment extends Fragment {

    private RecyclerView recyclerView;
    private FilmListAdapter adapter;
    private FilmListsViewModel viewModel;
    private ImageButton btAddNewList;

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

        recyclerView = view.findViewById(R.id.rvFilmList);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext(), RecyclerView.VERTICAL, false));
        recyclerView.setHasFixedSize(true);
        adapter = new FilmListAdapter(new ArrayList<>(), list -> {
            Fragment fragment = new AddFilmFragment();

            Bundle args = new Bundle();
            args.putSerializable("FilmList", list);
            fragment.setArguments(args);

            requireActivity().getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.fragment_container, fragment)
                    .addToBackStack(null)
                    .commit();
        });

       // adapter = new FilmListAdapter(new ArrayList<>());
        recyclerView.setAdapter(adapter);
        btAddNewList = view.findViewById(R.id.btAddNewList);

        viewModel = new ViewModelProvider(this).get(FilmListsViewModel.class);

        // Observar cambios en las listas
        viewModel.getListaPeliculas().observe(getViewLifecycleOwner(), peliculas -> {
            adapter.updateList(peliculas); // Método que actualiza la lista en el adapter
        });
        setClickListeners();
        return view;
    }

    public void setClickListeners() {
        btAddNewList.setOnClickListener(v -> {
            Log.d("Botón", "Se está pulsando el botón");
            FragmentTransaction transaction = getParentFragmentManager().beginTransaction();
            transaction.replace(R.id.fragment_container, new NewListFragment());
            transaction.addToBackStack(null);
            transaction.commit();
        });
    }
}