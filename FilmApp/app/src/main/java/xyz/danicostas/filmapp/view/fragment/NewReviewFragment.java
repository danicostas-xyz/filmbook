package xyz.danicostas.filmapp.view.fragment;

import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.ViewModelProvider;

import com.google.android.material.datepicker.MaterialDatePicker;

import java.util.Calendar;
import java.util.Date;

import xyz.danicostas.filmapp.R;
import xyz.danicostas.filmapp.model.entity.Film;
import xyz.danicostas.filmapp.model.entity.Review;
import xyz.danicostas.filmapp.model.service.UserService;
import xyz.danicostas.filmapp.model.service.UserSession;
import xyz.danicostas.filmapp.view.activity.LoginActivity;
import xyz.danicostas.filmapp.view.activity.RegisterActivity;
import xyz.danicostas.filmapp.viewmodel.FilmListsViewModel;


public class NewReviewFragment extends Fragment {

    private EditText etNewReviewTitle, etNewReviewDescription, etReviewDate, etSelectFilm;
    private RatingBar ratingBar;
    private Button btAddNewReview;
    private UserService userService = UserService.getInstance();
    private UserSession session = UserSession.getInstance();
    private Date normalizedReviewDate;
    private Film film;

    public NewReviewFragment() { /* Required empty public constructor */ }
    @Override
    public void onCreate(Bundle savedInstanceState) { super.onCreate(savedInstanceState); }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_new_review, container, false);
        initViews(view);
        initData();
        setViews();
        setOnClickListeners();

        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        setViews();
    }

    private void initViews(View view) {
        etNewReviewTitle = view.findViewById(R.id.etNewReviewTitle);
        etNewReviewDescription = view.findViewById(R.id.etNewReviewDescription);
        etReviewDate = view.findViewById(R.id.etReviewDate);
        etSelectFilm = view.findViewById(R.id.etSelectFilm);
        ratingBar = view.findViewById(R.id.ratingBar);
        btAddNewReview = view.findViewById(R.id.btAddNewReview);
    }

    private void setViews() {
        if (normalizedReviewDate != null) {
            @SuppressLint("DefaultLocale") String date = String.format("%02d/%02d/%04d", normalizedReviewDate.getDate(),
                    (normalizedReviewDate.getMonth() + 1),
                    normalizedReviewDate.getYear() + 1900 );
            etReviewDate.setText(date);
        }

        if (film != null) {
            etSelectFilm.setText(film.getTitle());
        }
    }

    private void initData() {
        Bundle args = null;
        film = null;
        normalizedReviewDate = null;

        try {
            args = getArguments();
        } catch (Exception e) {
            Log.d("NewReviewFragment", "initData: No arguments");
        }

        if (args.getSerializable("Film") != null) {
            film = (Film) args.getSerializable("Film");
        }

        if (args.getSerializable("Date") != null) {
            normalizedReviewDate = (Date) args.getSerializable("Date");
        }

    }

    private void setOnClickListeners()  {
        btAddNewReview.setOnClickListener(v -> {
            // TODO: GESTIONAR VALIDACIÓN DEL FORMULARIO PARA EVITAR CAMPOS VACÍOS
            Log.d("btAddNewReview", "Pulsando botón btAddNewReview");
            Review review = new Review(
                    session.getUserId(),
                    etNewReviewTitle.getText().toString(),
                    etNewReviewDescription.getText().toString(),
                    film.getPosterPath(),
                    film.getId(),
                    film.getTitle(),
                    (normalizedReviewDate != null) ? normalizedReviewDate : new Date(),
                    ratingBar.getRating() * 2
            );

            userService.addReview(review, session.getUserId(), () -> {
                Log.d("NewReview.addReview", "Review Añadida");
                requireActivity().getSupportFragmentManager().popBackStack();
            });
        });

        etReviewDate.setOnClickListener(v -> {
            showDatePicker();
        });

        etSelectFilm.setOnClickListener(v -> {
            selectFilm(v);
        });
    }

    private void showDatePicker() {
        // Creamos el picker con fecha inicial como hoy
        MaterialDatePicker<Long> datePicker = MaterialDatePicker.Builder.datePicker()
                .setTitleText("Selecciona la fecha")
                .setSelection(MaterialDatePicker.todayInUtcMilliseconds())
                .setTheme(R.style.ThemeOverlay_App_DatePicker)
                .build();

        // Mostrar el picker (usa el FragmentManager del fragment actual)
        datePicker.show(getParentFragmentManager(), "MATERIAL_DATE_PICKER");

        // Listener al seleccionar la fecha
        datePicker.addOnPositiveButtonClickListener(selection -> {
            Calendar calendar1 = Calendar.getInstance();
            calendar1.setTimeInMillis(selection);

            String selectedDate = String.format("%02d/%02d/%04d",
                    calendar1.get(Calendar.DAY_OF_MONTH),
                    calendar1.get(Calendar.MONTH) + 1,
                    calendar1.get(Calendar.YEAR));

            etReviewDate.setText(selectedDate);
            normalizedReviewDate = calendar1.getTime();
        });

        setViews();
    }


    private void selectFilm(View v) {

        getParentFragmentManager()
                .setFragmentResultListener("SelectFilm", this, (requestKey, result) -> {
            film = (Film) result.getSerializable("Film");
        });

        Bundle args = new Bundle();
        args.putString("Origin", "Review");

        SearchFragment searchFragment = new SearchFragment();
        searchFragment.setArguments(args);

        FragmentTransaction transaction = getParentFragmentManager().beginTransaction();
        transaction.replace(R.id.fragment_container, searchFragment);
        transaction.addToBackStack(null);
        transaction.commit();
    }
}