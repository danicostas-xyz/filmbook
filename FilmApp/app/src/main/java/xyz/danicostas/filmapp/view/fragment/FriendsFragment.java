package xyz.danicostas.filmapp.view.fragment;

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
import xyz.danicostas.filmapp.view.adapter.FriendListAdapter;
import xyz.danicostas.filmapp.model.entity.User;


public class FriendsFragment extends Fragment {

    public FriendsFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
            // Lista de friends para usuario prueba
        List<User> friendList = Arrays.asList(
                new User("FilmBot 🤖", R.drawable.chatbot_icon),
                new User("Guts", R.drawable.gut_pfp),
                new User("Jane ", R.drawable.test_pfp),
                new User("Carlos ", R.drawable.default_profile),
                new User("Guille", "https://example.com/emily.jpg")
        );

        View view = inflater.inflate(R.layout.fragment_friends, container, false);

        RecyclerView recyclerView = view.findViewById(R.id.recyclerViewFriends);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext(), RecyclerView.VERTICAL, false));
        recyclerView.setHasFixedSize(true);
        FriendListAdapter adapter = new FriendListAdapter(getContext(), friendList);
        recyclerView.setAdapter(adapter);

        return view;
    }
}
