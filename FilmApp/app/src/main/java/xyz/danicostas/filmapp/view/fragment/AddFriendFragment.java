package xyz.danicostas.filmapp.view.fragment;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.textfield.TextInputEditText;

import java.util.ArrayList;
import java.util.List;

import xyz.danicostas.filmapp.R;
import xyz.danicostas.filmapp.model.entity.User;
import xyz.danicostas.filmapp.model.service.UserService;
import xyz.danicostas.filmapp.model.service.UserSession;
import xyz.danicostas.filmapp.view.adapter.SearchFriendAdapter;

public class AddFriendFragment extends Fragment implements SearchFriendAdapter.OnFriendAddClickListener {

    private EditText etSearchFriend;
    private RecyclerView rvSearchResults;
    private SearchFriendAdapter adapter;
    private final UserService userService = UserService.getInstance();
    private final UserSession userSession = UserSession.getInstance();

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_add_friend, container, false);

        etSearchFriend = view.findViewById(R.id.etSearchFriend);
        rvSearchResults = view.findViewById(R.id.rvSearchResults);

        setupRecyclerView();
        setupSearchListener();

        return view;
    }

    private void setupRecyclerView() {
        adapter = new SearchFriendAdapter(this);
        rvSearchResults.setLayoutManager(new LinearLayoutManager(getContext()));
        rvSearchResults.setAdapter(adapter);
    }

    private void setupSearchListener() {
        etSearchFriend.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                String query = s.toString().trim();
                if (!query.isEmpty()) {
                    searchUsers(query);
                } else {
                    adapter.updateList(new ArrayList<>());
                }
            }

            @Override
            public void afterTextChanged(Editable s) {}
        });
    }

    private void searchUsers(String query) {
        userService.searchUsers(query, users -> {
            if (getActivity() != null) {
                getActivity().runOnUiThread(() -> adapter.updateList(users));
            }
        });
    }

    @Override
    public void onFriendAddClick(User user) {
        userService.addFriend(user,
                () -> {
                    if (getActivity() != null) {
                        getActivity().runOnUiThread(() -> {
                            Toast.makeText(getContext(), "Amigo añadido con éxito", Toast.LENGTH_SHORT).show();
                            // actualizar la lista de amigosç
                            if (getParentFragmentManager().getBackStackEntryCount() > 0) {
                                getParentFragmentManager().popBackStack();
                            }
                        });
                    }
                },
                error -> {
                    if (getActivity() != null) {
                        getActivity().runOnUiThread(() ->
                                Toast.makeText(getContext(), error, Toast.LENGTH_SHORT).show()
                        );
                    }
                }
        );
    }
}