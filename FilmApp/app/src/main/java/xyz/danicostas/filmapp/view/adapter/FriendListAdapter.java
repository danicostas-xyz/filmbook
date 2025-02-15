package xyz.danicostas.filmapp.view.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import xyz.danicostas.filmapp.R;
import xyz.danicostas.filmapp.model.entity.User;
import xyz.danicostas.filmapp.view.activity.FriendActivity;

public class FriendListAdapter extends RecyclerView.Adapter<FriendListAdapter.FriendViewHolder> {

    private List<User> friendList;
    private Context context;
    public static final String FRIEND_NAME = "Friend Name";

    // Constructor
    public FriendListAdapter(Context context, List<User> friendList) {
        this.context = context;
        this.friendList = friendList;
    }

    @NonNull
    @Override
    public FriendViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.friend_list, parent, false);
        return new FriendViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull FriendViewHolder holder, int position) {
        User friend = friendList.get(position);
        holder.tvFriendName.setText(friend.getUsername());

        holder.itemView.setOnClickListener(v -> {
            Intent intent = new Intent(context, FriendActivity.class);
            intent.putExtra(FRIEND_NAME, friend.getUsername());
            context.startActivity(intent);
        });
    }

    @Override
    public int getItemCount() {
        return friendList.size();
    }

    // ViewHolder for friend items
    static class FriendViewHolder extends RecyclerView.ViewHolder {
        ImageView ivProfile;
        TextView tvFriendName;

        public FriendViewHolder(@NonNull View itemView) {
            super(itemView);
            ivProfile = itemView.findViewById(R.id.ivFriendProfile);
            tvFriendName = itemView.findViewById(R.id.tvFriendName);
        }
    }
}
