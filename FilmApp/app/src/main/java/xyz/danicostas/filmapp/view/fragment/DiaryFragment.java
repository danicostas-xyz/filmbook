package xyz.danicostas.filmapp.view.fragment;

import android.media.Image;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.LinearSnapHelper;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CalendarView;
import android.widget.ImageButton;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import xyz.danicostas.filmapp.R;
import xyz.danicostas.filmapp.model.entity.Review;
import xyz.danicostas.filmapp.model.service.UserService;
import xyz.danicostas.filmapp.model.service.UserSession;
import xyz.danicostas.filmapp.view.adapter.ReviewAdapter;


public class DiaryFragment extends Fragment {

    private RecyclerView recyclerView;
    private ReviewAdapter adapter;
    private final UserService service = UserService.getInstance();
    private List<Review> reviewList;
    private CalendarView calendarView;
    ImageButton btAddNewReviewDiary;
    Date selectedDateAsDate;

    public DiaryFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_diary, container, false);
        initViews(view);
        setListeners();

        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        initData();
    }

    private void initViews (View view) {
        recyclerView = view.findViewById(R.id.rvReviews);
        adapter = new ReviewAdapter(new ArrayList<>());
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext(),
                RecyclerView.VERTICAL, false));
        recyclerView.setAdapter(adapter);
        recyclerView.setHasFixedSize(true);
        calendarView = view.findViewById(R.id.calendarView);
        btAddNewReviewDiary = view.findViewById(R.id.btAddNewReviewDiary);
    }

    private void initData() {
        service.getReviewList(UserSession.getInstance().getUserId(), (list) -> {
            reviewList = list;
            // Inicializa el filtro por la fecha de hoy al cargar el fragment
            Calendar today = Calendar.getInstance();
            today.set(Calendar.HOUR_OF_DAY, 0);
            today.set(Calendar.MINUTE, 0);
            today.set(Calendar.SECOND, 0);
            today.set(Calendar.MILLISECOND, 0);
            Date todayDate = today.getTime();

            selectedDateAsDate = todayDate;

            List<Review> todayList = new ArrayList<>();
            for (Review r : reviewList) {
                Calendar reviewDate = Calendar.getInstance();
                reviewDate.setTime(r.getDate());
                reviewDate.set(Calendar.HOUR_OF_DAY, 0);
                reviewDate.set(Calendar.MINUTE, 0);
                reviewDate.set(Calendar.SECOND, 0);
                reviewDate.set(Calendar.MILLISECOND, 0);
                Date normalizedReviewDate = reviewDate.getTime();

                if (todayDate.equals(normalizedReviewDate)) {
                    todayList.add(r);
                }
            }

            adapter.updateList(todayList);
        });
    }

    private void setListeners() {
        calendarView.setOnDateChangeListener((view, year, month, dayOfMonth) -> {

            Calendar selectedDate = Calendar.getInstance();
            selectedDate.set(year, month, dayOfMonth, 0, 0, 0);
            selectedDate.set(Calendar.MILLISECOND, 0);
            selectedDateAsDate = selectedDate.getTime();

            List<Review> nuevaLista = new ArrayList<>();

            for (Review r: reviewList) {
                Calendar reviewDate = Calendar.getInstance();
                reviewDate.setTime(r.getDate());
                reviewDate.set(Calendar.HOUR_OF_DAY, 0);
                reviewDate.set(Calendar.MINUTE, 0);
                reviewDate.set(Calendar.SECOND, 0);
                reviewDate.set(Calendar.MILLISECOND, 0);
                Date normalizedReviewDate = reviewDate.getTime();

                if (selectedDateAsDate.equals(normalizedReviewDate)) {
                    nuevaLista.add(r);
                    Log.d("CALENDAR", "coincide");
                } else {
                    Log.d("CALENDAR", "setListeners: no coincide" + selectedDateAsDate
                            + normalizedReviewDate);
                }
            }

            adapter.updateList(nuevaLista);

            Log.d("CALENDAR-VIEW", dayOfMonth + "/" + (month+1) + "/" + year);
        });

        btAddNewReviewDiary.setOnClickListener(v -> {
            Fragment fragment = new NewReviewFragment();
            Bundle args = new Bundle();
            args.putSerializable("Date",
                    (selectedDateAsDate != null)
                    ? selectedDateAsDate
                    : new Date()
            );
            fragment.setArguments(args);
            FragmentTransaction transaction = getParentFragmentManager().beginTransaction();
            transaction.replace(R.id.fragment_container, fragment);
            transaction.addToBackStack(null);
            transaction.commit();
        });

    }
}