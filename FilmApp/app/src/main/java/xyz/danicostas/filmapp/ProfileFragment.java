package xyz.danicostas.filmapp;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import xyz.danicostas.filmapp.adapter.FilmGridAdapter;
import xyz.danicostas.filmapp.adapter.FilmListAdapter;
import xyz.danicostas.filmapp.model.entity.Film;
import xyz.danicostas.filmapp.model.entity.FilmList;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link ProfileFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ProfileFragment extends Fragment {

    public static List<Film> filmList = new ArrayList<>();

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public ProfileFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment ProfileFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static ProfileFragment newInstance(String param1, String param2) {
        ProfileFragment fragment = new ProfileFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }



    }

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
        listaDeListas.get(1).setContent(listaPeliculas);
        listaDeListas.get(2).setContent(listaPeliculas);
        listaDeListas.get(3).setContent(listaPeliculas);
        listaDeListas.get(4).setContent(listaPeliculas);
        listaDeListas.get(5).setContent(listaPeliculas);

        View view = inflater.inflate(R.layout.fragment_profile, container, false);
        RecyclerView recyclerView = view.findViewById(R.id.RVFilmList);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        FilmListAdapter adapter = new FilmListAdapter(listaDeListas);
        recyclerView.setAdapter(adapter);
        return view;
    }
}