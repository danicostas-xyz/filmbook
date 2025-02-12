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
import java.util.List;

import xyz.danicostas.filmapp.R;
import xyz.danicostas.filmapp.model.entity.Film;
import xyz.danicostas.filmapp.view.adapter.FilmListAdapter;
import xyz.danicostas.filmapp.view.adapter.SearchResultAdapter;
import xyz.danicostas.filmapp.viewmodel.FilmListsViewModel;


public class SearchFragment extends Fragment {

    private RecyclerView recyclerView;
    private SearchResultAdapter adapter;
    private FilmListsViewModel viewModel;

    public SearchFragment() { /* Required empty public constructor */ }
    @Override
    public void onCreate(Bundle savedInstanceState) { super.onCreate(savedInstanceState); }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_search, container, false);

        recyclerView = view.findViewById(R.id.RVSearch);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext(), RecyclerView.VERTICAL, false));

        Film nosferatu = new Film();
            nosferatu.setId(426063);
            nosferatu.setTitle("Nosferatu");
            nosferatu.setOriginalTitle("Nosferatu");
            nosferatu.setOverview("A gothic tale of obsession between a haunted young woman and the terrifying vampire infatuated with her, causing untold horror in its wake.");
            nosferatu.setPosterPath("https://image.tmdb.org/t/p/w500/5qGIxdEO841C0tdY8vOdLoRVrr0.jpg");
            nosferatu.setBackdropPath(null); // No se proporcionó backdropPath
            nosferatu.setReleaseDate("2024-12-25");
            nosferatu.setVoteAverage(6.7);
            nosferatu.setVoteCount(2002);
            nosferatu.setMediaType(null); // No se proporcionó mediaType
            nosferatu.setAdult(false);
            nosferatu.setOriginalLanguage("en");
            nosferatu.setGenreIds(List.of(27, 18)); // Supongamos que 27 = Horror, 18 = Drama
            nosferatu.setPopularity(0.0); // No se proporcionó, por lo que dejamos 0.0
            nosferatu.setVideo(false);

        List<Film> lista = List.of(nosferatu, nosferatu, nosferatu, nosferatu, nosferatu, nosferatu, nosferatu, nosferatu, nosferatu, nosferatu);

        adapter = new SearchResultAdapter(new ArrayList<>());
        recyclerView.setAdapter(adapter);

        return view;
    }
}