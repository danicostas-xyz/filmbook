package xyz.danicostas.filmapp.view.fragment;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.Date;
import java.util.List;

import xyz.danicostas.filmapp.R;
import xyz.danicostas.filmapp.model.entity.Review;
import xyz.danicostas.filmapp.view.adapter.ReviewAdapter;


public class DiaryFragment extends Fragment {

    private RecyclerView recyclerView;
    private ReviewAdapter adapter;
    private List<Review> listOfReviews;

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
        recyclerView = view.findViewById(R.id.rvReviews);
        adapter = new ReviewAdapter(mockListOfReviews());
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext(),
                RecyclerView.VERTICAL, false));
        recyclerView.setAdapter(adapter);
        recyclerView.setHasFixedSize(true);
        return view;
    }

    private List<Review> mockListOfReviews() {
        Review review1 = new Review(
                "review001",
                "user123",
                "Lynch en su máxima expresión",
                "Una pesadilla surrealista llena de simbolismo y atmósfera inquietante. Una obra maestra del cine experimental.",
                "/mxveW3mGVc0DzLdOmtkZsgd7c3B.jpg",
                "985",
                "Eraserhead",
                new Date(),
                9
        );

        Review review2 = new Review(
                "review002",
                "user456",
                "Una obra maestra del cine neo-noir",
                "Un retrato psicológico brutal de la alienación y la locura en una ciudad decadente. De Niro está impresionante.",
                "/ekstpH614fwDX8DUln1a2Opz0N8.jpg",
                "103",
                "Taxi Driver",
                new Date(),
                10
        );

        Review review3 = new Review(
                "review003",
                "user789",
                "El espíritu de la infancia en su máxima expresión",
                "Una historia encantadora, llena de magia y calidez. Perfecta para todas las edades.",
                "/rtGDOeG9LzoerkDGZF9dnVeLppL.jpg",
                "8392",
                "Mi vecino Totoro",
                new Date(),
                8
        );

        return List.of(review1, review2, review3);

    }

}