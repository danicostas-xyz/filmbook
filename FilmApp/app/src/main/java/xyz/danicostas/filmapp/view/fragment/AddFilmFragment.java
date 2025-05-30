package xyz.danicostas.filmapp.view.fragment;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Handler;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import xyz.danicostas.filmapp.R;
import xyz.danicostas.filmapp.model.entity.ApiResponseSearchFilmByTitle;
import xyz.danicostas.filmapp.model.entity.Film;
import xyz.danicostas.filmapp.model.entity.FilmList;
import xyz.danicostas.filmapp.model.service.ApiFilmService;
import xyz.danicostas.filmapp.model.service.TMDBApiService;
import xyz.danicostas.filmapp.model.service.UserService;
import xyz.danicostas.filmapp.model.service.UserSession;
import xyz.danicostas.filmapp.view.adapter.SearchResultAdapter;
import xyz.danicostas.filmapp.view.adapter.SearchResultAddFilmAdapter;

public class AddFilmFragment extends Fragment {

    private EditText etAddFilmToList;
    private Handler handler = new Handler();
    private Runnable searchRunnable;
    private RecyclerView rvSearchAddFilm;
    private SearchResultAddFilmAdapter adapter;
    private TextView tvLista;
    private FilmList filmList;

    public AddFilmFragment() { /* Required empty public constructor */ }
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            filmList = (FilmList) getArguments().getSerializable("FilmList");
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_add_film, container, false);
        initViews(view);
        setTitle();
        setListeners();

        return view;
    }

    private void initViews(View view) {
        adapter = new SearchResultAddFilmAdapter(new ArrayList<>(), filmList);
        etAddFilmToList = view.findViewById(R.id.etAddFilmToList);
        tvLista = view.findViewById(R.id.tvLista);
        rvSearchAddFilm =  view.findViewById(R.id.rvSearchAddFilm);
        rvSearchAddFilm.setLayoutManager(new LinearLayoutManager(getContext(), RecyclerView.VERTICAL, false));
        rvSearchAddFilm.setAdapter(adapter);
        rvSearchAddFilm.setHasFixedSize(true);
    }

    private void setTitle () {
        tvLista.setText(filmList.getListName());
    }

    private void setListeners()  {
        etAddFilmToList.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                // Cancelamos la búsqueda anterior
                handler.removeCallbacks(searchRunnable);
                // Nueva búsqueda con retraso de 500ms
                searchRunnable = () -> obtainMoviesByTitle(rvSearchAddFilm, s.toString(), adapter);
                handler.postDelayed(searchRunnable, 500);
            }
            @Override
            public void afterTextChanged(Editable s) {}
        });
    }

    public void obtainMoviesByTitle(RecyclerView recyclerView, String query, SearchResultAddFilmAdapter adapter){

        TMDBApiService api = ApiFilmService.getInstance().getApi();
        Call<ApiResponseSearchFilmByTitle> call = api.getMovieByTitle(ApiFilmService.API_KEY, query, ApiFilmService.LANG_SPANISH);
        call.enqueue(new Callback<ApiResponseSearchFilmByTitle>() {

            @Override
            public void onResponse(Call<ApiResponseSearchFilmByTitle> call, Response<ApiResponseSearchFilmByTitle> response) {
                if (response.isSuccessful()) {
                    Log.d("Success", "Datos traidos del servicio");
                    ApiResponseSearchFilmByTitle apiResponseSearchFilmByTitle = response.body();
                    Log.d("MOVIES BY TITLE", apiResponseSearchFilmByTitle.toString());
                    List<Film> lista = apiResponseSearchFilmByTitle.getResults();
                    lista.forEach(v->Log.d("Peli", v.toString()));
                    adapter.updateList(lista);
                    adapter.notifyDataSetChanged();
                } else {
                    Log.d("Error", response.code() + " " + response.message());
                    return;
                }
            }

            @Override
            public void onFailure(Call call, Throwable t) {
                Log.d("Error", t.toString());
            }
        });
    }
}