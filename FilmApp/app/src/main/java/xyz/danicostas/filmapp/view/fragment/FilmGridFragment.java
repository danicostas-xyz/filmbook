package xyz.danicostas.filmapp.view.fragment;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import xyz.danicostas.filmapp.R;
import xyz.danicostas.filmapp.model.entity.FilmList;
import xyz.danicostas.filmapp.model.service.UserService;
import xyz.danicostas.filmapp.model.service.UserSession;
import xyz.danicostas.filmapp.view.adapter.FilmGridAdapter;
import xyz.danicostas.filmapp.view.adapter.FilmListAdapter;
import xyz.danicostas.filmapp.view.fragment.AddFilmFragment;

public class FilmGridFragment extends Fragment {

    private FilmList filmList;
    private RecyclerView recyclerView;
    private TextView tvFilmListTitle;
    private ImageButton btAddNewFilmToListFromGrid;
    private final UserService service = UserService.getInstance();
    private final UserSession session = UserSession.getInstance();

    public FilmGridFragment() {
        // Required empty public constructor
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.activity_film_grid, container, false);

        initViews(view);
        initData();
        setListeners();

        return view;
    }

    private void initViews(View view) {
        tvFilmListTitle = view.findViewById(R.id.tvFilmListTitle);
        recyclerView = view.findViewById(R.id.RVFilmGrid);
        btAddNewFilmToListFromGrid = view.findViewById(R.id.btAddNewFilmToListFromGrid);
    }

    private void initData() {
        if (getArguments() != null) {
            filmList = (FilmList) getArguments().getSerializable("FilmList");
            tvFilmListTitle.setText(filmList.getListName());

            recyclerView.setLayoutManager(new GridLayoutManager(requireContext(), 3));
            recyclerView.setHasFixedSize(true);
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        service.getFilmList(session.getUserId(), filmList.getListName())
                .observe(getViewLifecycleOwner(), filmListLiveData -> {
                    recyclerView.setAdapter(new FilmGridAdapter(filmListLiveData.getContent()));
                });
    }

    private void setListeners() {
        btAddNewFilmToListFromGrid.setOnClickListener(v ->  {
            Bundle bundle = new Bundle();
            bundle.putSerializable("FilmList", filmList);

            Fragment addFilmFragment = new AddFilmFragment();
            addFilmFragment.setArguments(bundle);

            requireActivity().getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.fragment_container, addFilmFragment)
                    .addToBackStack(null)
                    .commit();
        });
    }
}
