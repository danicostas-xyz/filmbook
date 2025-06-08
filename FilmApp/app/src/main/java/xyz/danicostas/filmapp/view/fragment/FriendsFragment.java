package xyz.danicostas.filmapp.view.fragment;

import android.os.Bundle;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import xyz.danicostas.filmapp.R;
import xyz.danicostas.filmapp.view.adapter.FriendListAdapter;
import xyz.danicostas.filmapp.model.entity.User;
import xyz.danicostas.filmapp.model.service.UserService;
import xyz.danicostas.filmapp.model.service.UserSession;

public class FriendsFragment extends Fragment {

    private RecyclerView recyclerView;
    private FriendListAdapter adapter;
    private FloatingActionButton fabAddFriend;
    private final UserService userService = UserService.getInstance();
    private final UserSession userSession = UserSession.getInstance();
    private List<User> friendList;

    public FriendsFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_friends, container, false);

        recyclerView = view.findViewById(R.id.recyclerViewFriends);
        fabAddFriend = view.findViewById(R.id.fabAddFriend);

        setupRecyclerView();
        setupFabListener();
        loadFriends();

        return view;
    }

    private void setupRecyclerView() {
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext(), RecyclerView.VERTICAL, false));
        recyclerView.setHasFixedSize(true);
        friendList = new ArrayList<>();
        // Añadir FilmBot by default
        friendList.add(new User("FilmBot 🤖", R.drawable.chatbot_icon));
        adapter = new FriendListAdapter(getContext(), friendList);
        recyclerView.setAdapter(adapter);
    }

    private void setupFabListener() {
        fabAddFriend.setOnClickListener(v -> {
            Fragment addFriendFragment = new AddFriendFragment();
            requireActivity().getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.fragment_container, addFriendFragment)
                    .addToBackStack(null)
                    .commit();
        });
    }

    private void loadFriends() {
        userService.getFriends(users -> {
            if (getActivity() != null) {
                getActivity().runOnUiThread(() -> {
                    friendList.clear();
                    friendList.add(new User("FilmBot 🤖", R.drawable.chatbot_icon));

                    // Filtrar duplicados por una ID antes de añadir
                    for (User newUser : users) {
                        boolean isDuplicate = false;
                        for (User existingUser : friendList) {
                            if (existingUser.getId() != null &&
                                    newUser.getId() != null &&
                                    existingUser.getId().equals(newUser.getId())) {
                                isDuplicate = true;
                                break;
                            }
                        }
                        if (!isDuplicate) {
                            friendList.add(newUser);
                        }
                    }
                    adapter.notifyDataSetChanged();
                });
            }
        });
    }

    @Override
    public void onResume() {
        super.onResume();
        if (friendList == null || friendList.isEmpty() || requireActivity().getSupportFragmentManager().getBackStackEntryCount() > 0) {
            loadFriends();
        }
    }

    public void addFriend(User newFriend) {
        if (!friendList.contains(newFriend)) {
            friendList.add(newFriend);
            adapter.notifyItemInserted(friendList.size() - 1);
        }
    }
}
