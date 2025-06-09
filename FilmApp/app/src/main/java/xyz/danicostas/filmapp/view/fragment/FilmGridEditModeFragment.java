package xyz.danicostas.filmapp.view.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

import xyz.danicostas.filmapp.R;
import xyz.danicostas.filmapp.model.entity.Film;
import xyz.danicostas.filmapp.model.entity.FilmList;
import xyz.danicostas.filmapp.model.service.UserService;
import xyz.danicostas.filmapp.model.service.UserSession;
import xyz.danicostas.filmapp.view.adapter.FilmGridAdapter;
import xyz.danicostas.filmapp.view.adapter.FilmGridEditModeAdapter;

public class FilmGridEditModeFragment extends Fragment {

    private FilmList filmList;
    private FilmList originalCopy;
    private RecyclerView recyclerView;
    private EditText etEditarNombreLista;
    private final UserService service = UserService.getInstance();
    private final UserSession session = UserSession.getInstance();
    private Button btGuardarCambios;
    private Button btCancelarCambios;

    public FilmGridEditModeFragment() {
        // Required empty public constructor
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.activity_film_grid_edit_mode, container, false);

        initViews(view);
        initData();
        setListeners();

        return view;
    }

    private void initViews(View view) {
        etEditarNombreLista = view.findViewById(R.id.etEditarNombreLista);
        recyclerView = view.findViewById(R.id.RVFilmGrid);
        btGuardarCambios = view.findViewById(R.id.btGuardarCambios);
        btCancelarCambios = view.findViewById(R.id.btCancelarCambios);

    }

    private void initData() {
        if (getArguments() != null) {
            filmList = (FilmList) getArguments().getSerializable("FilmList");
            etEditarNombreLista.setText(filmList.getListName());
            originalCopy = new FilmList();
            ArrayList<Film> listaCopia = new ArrayList<Film>(filmList.getContent());
            originalCopy.setContent(listaCopia);
            originalCopy.setListName(filmList.getListName());
            recyclerView.setLayoutManager(new GridLayoutManager(requireContext(), 3));
            recyclerView.setHasFixedSize(true);
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        recyclerView.setAdapter(new FilmGridEditModeAdapter(filmList.getContent()));

    }

    private void setListeners() {
        btGuardarCambios.setOnClickListener(v -> {
            String newTitle = etEditarNombreLista.getText().toString();
            filmList.setListName(newTitle);
            service.updateList(originalCopy, filmList, (fl) -> {
                FilmGridFragment fragment = new FilmGridFragment();
                Bundle args = new Bundle();
                args.putSerializable("FilmList", fl);
                fragment.setArguments(args);
                requireActivity().getSupportFragmentManager()
                        .beginTransaction()
                        .replace(R.id.fragment_container, fragment)
                        .commit();
            });
        });

        btCancelarCambios.setOnClickListener(v -> {
            Fragment fragment = new FilmGridFragment();
            Bundle args = new Bundle();
            args.putSerializable("FilmList", originalCopy);
            fragment.setArguments(args);

            requireActivity().getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.fragment_container, fragment)
                    .commit();

        });
    }
}
