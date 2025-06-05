package com.example.carparkingapp.register;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.carparkingapp.R;

import java.util.List;

public class UserAdapter extends RecyclerView.Adapter<UserAdapter.PostViewHolder> {

    List<User> userList;
    Context context;

    public UserAdapter(Context context, List<User> users) {
        this.context = context;
        this.userList = users;
    }

    @NonNull
    @Override
    public PostViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // Inflate a layout for a single user item
        View view = LayoutInflater.from(context).inflate(R.layout.activity_register, parent, false);
        return new PostViewHolder(view);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull PostViewHolder holder, int position) {
        User user = userList.get(position);
        holder.userName.setText("Username: " + user.getUserName());
        holder.userEmail.setText("UserEmail: " + user.getUserEmail());
        holder.userPassword.setText("UserPassword: " + user.getUserPassword());
        holder.userPhoneNo.setText("UserPhoneNo No: " + user.getUserPhoneNo());
        holder.userAddress.setText("Address: " + user.getUserAddress());
    }

    @Override
    public int getItemCount() {
        return userList.size();
    }

    public static class PostViewHolder extends RecyclerView.ViewHolder {
        TextView userName, userEmail, userPassword, userPhoneNo, userAddress;

        public PostViewHolder(@NonNull View itemView) {
            super(itemView);

            userName = itemView.findViewById(R.id.user_name);
            userEmail = itemView.findViewById(R.id.email);
            userPassword = itemView.findViewById(R.id.phone_number);
            userPhoneNo = itemView.findViewById(R.id.registration_no);
            userAddress = itemView.findViewById(R.id.address);
        }
    }
}