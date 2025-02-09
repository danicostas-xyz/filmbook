package xyz.danicostas.filmapp.fragment;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.Arrays;
import java.util.List;

import xyz.danicostas.filmapp.R;
import xyz.danicostas.filmapp.adapter.FilmListAdapter;
import xyz.danicostas.filmapp.model.entity.Film;
import xyz.danicostas.filmapp.model.entity.FilmList;
import xyz.danicostas.filmapp.model.service.UserService;

public class ProfileFragment extends Fragment {

    private List<FilmList> listOfLists;

    public ProfileFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        listOfLists = UserService.getInstance().getFilmLists();
    }

    /**
     *
     * @param inflater The LayoutInflater object that can be used to inflate
     * any views in the fragment,
     * @param container If non-null, this is the parent view that the fragment's
     * UI should be attached to.  The fragment should not add the view itself,
     * but this can be used to generate the LayoutParams of the view.
     * @param savedInstanceState If non-null, this fragment is being re-constructed
     * from a previous saved state as given here.
     *
     * @return
     */
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment


        List<Film> listaPeliculas = Arrays.asList(
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
        listaDeListas.get(5).setListName("Españolas");

        View view = inflater.inflate(R.layout.fragment_profile, container, false);

        RecyclerView recyclerView = view.findViewById(R.id.RVFilmList);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext(), RecyclerView.VERTICAL, false));
        FilmListAdapter adapter = new FilmListAdapter(listaDeListas);
        recyclerView.setAdapter(adapter);


        return view;
    }
}