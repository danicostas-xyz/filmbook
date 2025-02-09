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
import xyz.danicostas.filmapp.adapter.FriendListAdapter;
import xyz.danicostas.filmapp.model.entity.User;
import xyz.danicostas.filmapp.model.service.UserService;


public class FriendsFragment extends Fragment {

    private List<User> friendList;

    public FriendsFragment() {
        // Required empty public constructor
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        friendList = UserService.getInstance().getFriendList();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Create a mock list of friends
        List<User> friendList = Arrays.asList(
                new User("John Doe", "https://example.com/john.jpg"),
                new User("Jane Smith", "https://example.com/jane.jpg"),
                new User("Carlos Rivera", "https://example.com/carlos.jpg"),
                new User("Emily Davis", "https://example.com/emily.jpg"),
                new User("Sophia Brown", "https://example.com/sophia.jpg")
        );

        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_friends, container, false);

        // Set up the RecyclerView
        RecyclerView recyclerView = view.findViewById(R.id.recyclerViewFriends);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext(), RecyclerView.VERTICAL, false));
        FriendListAdapter adapter = new FriendListAdapter(getContext(), friendList);
        recyclerView.setAdapter(adapter);

        return view;
    }
}
