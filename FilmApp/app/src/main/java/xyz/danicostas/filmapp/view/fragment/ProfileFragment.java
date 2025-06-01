package xyz.danicostas.filmapp.view.fragment;

import static androidx.core.content.ContextCompat.startActivity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
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
import xyz.danicostas.filmapp.model.entity.Film;
import xyz.danicostas.filmapp.view.activity.FilmDetailActivity;
import xyz.danicostas.filmapp.view.adapter.FilmListAdapter;
import xyz.danicostas.filmapp.viewmodel.FilmListsViewModel;

public class ProfileFragment extends Fragment {

    private RecyclerView recyclerView;
    private FilmListAdapter adapter;
    private FilmListsViewModel viewModel;
    private ImageButton btAddNewList;
    ActivityResultLauncher<Intent> registerActivityLauncher;


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
        setActivityForResultLauncher();
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
        }, getParentFragmentManager(), film -> {
            Intent intent = new Intent(getContext(), FilmDetailActivity.class);
            intent.putExtra("Film ID", film.getId());
            registerActivityLauncher.launch(intent);
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

    @Override
    public void onResume() {
        super.onResume();
        viewModel.refreshLists();
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

    private void setActivityForResultLauncher() {
        registerActivityLauncher =
                registerForActivityResult(new ActivityResultContracts.StartActivityForResult(),
                        result -> {
                            if (result.getResultCode() == Activity.RESULT_OK && result.getData() != null) {
                                Film filmFromResult = (Film) result.getData().getSerializableExtra("Film");
                                Fragment fragment = new NewReviewFragment();
                                Bundle args = new Bundle();
                                args.putSerializable("Film", filmFromResult);
                                fragment.setArguments(args);
                                FragmentTransaction transaction = getParentFragmentManager().beginTransaction();
                                transaction.replace(R.id.fragment_container, fragment);
                                transaction.addToBackStack(null);
                                transaction.commit();
                            }
                        });
    }
}