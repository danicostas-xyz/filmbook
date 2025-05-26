package xyz.danicostas.filmapp.view.fragment;

import android.os.Bundle;
import android.os.Handler;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import xyz.danicostas.filmapp.R;
import xyz.danicostas.filmapp.model.entity.ApiResponseSearchFilmByTitle;
import xyz.danicostas.filmapp.model.entity.Film;
import xyz.danicostas.filmapp.model.service.ApiFilmService;
import xyz.danicostas.filmapp.model.service.TMDBApiService;
import xyz.danicostas.filmapp.model.service.UserService;
import xyz.danicostas.filmapp.model.service.UserSession;
import xyz.danicostas.filmapp.view.adapter.SearchResultAdapter;
import xyz.danicostas.filmapp.viewmodel.FilmListsViewModel;


public class NewListFragment extends Fragment {

    private EditText etNewList;
    private Runnable searchRunnable;
    private Button btAddNewList;
    private UserService userService = UserService.getInstance();
    private UserSession session = UserSession.getInstance();

    public NewListFragment() { /* Required empty public constructor */ }
    @Override
    public void onCreate(Bundle savedInstanceState) { super.onCreate(savedInstanceState); }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_new_list, container, false);
        initViews(view);
        setOnClickListeners();

        return view;
    }

    private void initViews(View view) {
        etNewList = view.findViewById(R.id.etNewList);
        btAddNewList = view.findViewById(R.id.btAddNewList);
    }

    private void setOnClickListeners()  {
        btAddNewList.setOnClickListener(v -> {
            Log.d("Botón New List", "Pulsando botón new List");
            userService.addNewList(etNewList.getText().toString(), session.getUserId());
            FragmentTransaction transaction = getParentFragmentManager().beginTransaction();
            transaction.replace(R.id.fragment_container, new ProfileFragment());
            transaction.addToBackStack(null); // este lo podés evitar si no querés volver atrás
            getParentFragmentManager().popBackStack(null, FragmentManager.POP_BACK_STACK_INCLUSIVE); // limpia todo
            transaction.commit();
        });
    }

}