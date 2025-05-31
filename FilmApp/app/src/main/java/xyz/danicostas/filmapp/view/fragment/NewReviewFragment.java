package xyz.danicostas.filmapp.view.fragment;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RatingBar;
import android.widget.TextView;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import java.util.Calendar;
import java.util.Date;

import xyz.danicostas.filmapp.R;
import xyz.danicostas.filmapp.model.entity.Review;
import xyz.danicostas.filmapp.model.service.UserService;
import xyz.danicostas.filmapp.model.service.UserSession;
import xyz.danicostas.filmapp.viewmodel.FilmListsViewModel;


public class NewReviewFragment extends Fragment {

    private TextView tvNewReview;
    private EditText etNewReviewTitle, etNewReviewDescription, etReviewDate;
    private RatingBar ratingBar;
    private Button btAddNewReview;
    private UserService userService = UserService.getInstance();
    private UserSession session = UserSession.getInstance();
    private Date normalizedReviewDate;

    public NewReviewFragment() { /* Required empty public constructor */ }
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
        etNewReviewTitle = view.findViewById(R.id.etNewReviewTitle);
        etNewReviewDescription = view.findViewById(R.id.etNewReviewDescription);
        etReviewDate = view.findViewById(R.id.etReviewDate);
        ratingBar = view.findViewById(R.id.ratingBar);
        btAddNewReview = view.findViewById(R.id.btAddNewReview);
    }

    private void setOnClickListeners()  {
        btAddNewReview.setOnClickListener(v -> {
            Log.d("btAddNewReview", "Pulsando botón btAddNewReview");
            Review review = new Review()

        });
        etReviewDate.setOnClickListener(v -> {
            showDatePicker();
        });
    }

    private void showDatePicker() {
        final Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DAY_OF_MONTH);

        DatePickerDialog datePickerDialog = new DatePickerDialog(
                getContext(),
                (view, year1, month1, dayOfMonth1) -> {
                    // Al seleccionar fecha:
                    String selectedDate = String.format("%02d/%02d/%04d", dayOfMonth1, month1 + 1, year1);
                    etReviewDate.setText(selectedDate);
                    Calendar c1 = Calendar.getInstance();
                    c1.set(year1, month1, dayOfMonth1);
                    normalizedReviewDate = c1.getTime();
                },
                year, month, day
        );
        datePickerDialog.show();
    }
}