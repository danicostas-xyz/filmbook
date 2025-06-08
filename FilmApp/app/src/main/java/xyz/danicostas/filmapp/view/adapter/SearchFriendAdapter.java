package xyz.danicostas.filmapp.view.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;
import java.util.List;

import xyz.danicostas.filmapp.R;
import xyz.danicostas.filmapp.model.entity.User;

public class SearchFriendAdapter extends RecyclerView.Adapter<SearchFriendAdapter.SearchFriendViewHolder> {

    private List<User> userList;
    private OnFriendAddClickListener listener;

    public interface OnFriendAddClickListener {
        void onFriendAddClick(User user);
    }

    public SearchFriendAdapter(OnFriendAddClickListener listener) {
        this.userList = new ArrayList<>();
        this.listener = listener;
    }

    @NonNull
    @Override
    public SearchFriendViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_search_friend, parent, false);
        return new SearchFriendViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull SearchFriendViewHolder holder, int position) {
        User user = userList.get(position);
        holder.tvUsername.setText(user.getUsername());

        // Cargar imagen de perfil
        if (user.getProfileImageResId() != 0) {
            holder.ivUserProfile.setImageResource(user.getProfileImageResId());
        } else if (user.getProfileImageUrl() != null && !user.getProfileImageUrl().isEmpty()) {
            Glide.with(holder.itemView.getContext())
                    .load(user.getProfileImageUrl())
                    .placeholder(R.drawable.default_avatar_vector)
                    .error(R.drawable.default_avatar_vector)
                    .into(holder.ivUserProfile);
        } else {
            holder.ivUserProfile.setImageResource(R.drawable.default_avatar_vector);
        }

        holder.btnAddFriend.setOnClickListener(v -> {
            if (listener != null) {
                listener.onFriendAddClick(user);
            }
        });
    }

    @Override
    public int getItemCount() {
        return userList.size();
    }

    public void updateList(List<User> newList) {
        userList.clear();
        userList.addAll(newList);
        notifyDataSetChanged();
    }

    static class SearchFriendViewHolder extends RecyclerView.ViewHolder {
        ImageView ivUserProfile;
        TextView tvUsername;
        Button btnAddFriend;

        SearchFriendViewHolder(View itemView) {
            super(itemView);
            ivUserProfile = itemView.findViewById(R.id.ivUserProfile);
            tvUsername = itemView.findViewById(R.id.tvUsername);
            btnAddFriend = itemView.findViewById(R.id.btnAddFriend);
        }
    }
}