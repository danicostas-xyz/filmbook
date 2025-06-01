package xyz.danicostas.filmapp.view.fragment;

import static androidx.core.content.ContextCompat.startActivity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
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
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import xyz.danicostas.filmapp.R;
import xyz.danicostas.filmapp.model.entity.Film;
import xyz.danicostas.filmapp.model.entity.ApiResponseSearchFilmByTitle;
import xyz.danicostas.filmapp.model.service.ApiFilmService;
import xyz.danicostas.filmapp.model.service.TMDBApiService;
import xyz.danicostas.filmapp.view.activity.FilmDetailActivity;
import xyz.danicostas.filmapp.view.activity.LoginActivity;
import xyz.danicostas.filmapp.view.activity.RegisterActivity;
import xyz.danicostas.filmapp.view.adapter.SearchResultAdapter;
import xyz.danicostas.filmapp.viewmodel.FilmListsViewModel;


public class SearchFragment extends Fragment {

    private RecyclerView recyclerView;
    private SearchResultAdapter adapter;
    private EditText editTextSearch;
    private Handler handler = new Handler();
    private Runnable searchRunnable;
    ActivityResultLauncher<Intent> registerActivityLauncher;

    public SearchFragment() { /* Required empty public constructor */ }
    @Override
    public void onCreate(Bundle savedInstanceState) { super.onCreate(savedInstanceState); }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_search, container, false);
        setOrigin();
        recyclerView = view.findViewById(R.id.rvSearch);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext(), RecyclerView.VERTICAL, false));
        recyclerView.setAdapter(adapter);
        recyclerView.setHasFixedSize(true);
        editTextSearch = view.findViewById(R.id.editTextSearch);
        setActivityForResultLauncher();


        editTextSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                // Cancelamos la búsqueda anterior
                handler.removeCallbacks(searchRunnable);
                // Nueva búsqueda con retraso de 500ms
                searchRunnable = () -> obtainMoviesByTitle(recyclerView, s.toString(), adapter);
                handler.postDelayed(searchRunnable, 500);
            }
            @Override
            public void afterTextChanged(Editable s) {}
        });


        return view;
    }

    public void setOrigin() {
        String origin = getArguments() != null ? getArguments().getString("Origin") : null;

        if ("Review".equals(origin)) {
            adapter = new SearchResultAdapter(new ArrayList<>(), film -> {
                Bundle result = new Bundle();
                result.putSerializable("Film", film);
                getParentFragmentManager().setFragmentResult("SelectFilm", result);
                getParentFragmentManager().popBackStack();
            },0);
        } else {
            adapter = new SearchResultAdapter(new ArrayList<>(), film -> {
                Intent intent = new Intent(getContext(), FilmDetailActivity.class);
                intent.putExtra("Film ID", film.getId());
                registerActivityLauncher.launch(intent);
            },1);
        }
    }

    public void obtainMoviesByTitle(RecyclerView recyclerView, String query, SearchResultAdapter adapter){

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