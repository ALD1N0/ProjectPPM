package com.example.donordarah.adapters;

import android.graphics.Color;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.donordarah.R;
import com.example.donordarah.viewmodels.CustomUserData;

import java.util.List;

public class BloodRequestAdapter extends RecyclerView.Adapter<BloodRequestAdapter.PostHolder> {

    private List<CustomUserData> postLists;

    public class PostHolder extends RecyclerView.ViewHolder {
        TextView Name, bloodgroup, Address, contact, posted;

        public PostHolder(@NonNull View itemView) {
            super(itemView);

            Name = itemView.findViewById(R.id.reqstUser);
            contact = itemView.findViewById(R.id.targetCN);
            bloodgroup = itemView.findViewById(R.id.targetBG);
            Address = itemView.findViewById(R.id.reqstLocation);
            posted = itemView.findViewById(R.id.posted);
        }
    }

    public BloodRequestAdapter(List<CustomUserData> postLists) {
        this.postLists = postLists;
    }

    @NonNull
    @Override
    public PostHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View listitem = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.request_list_item, viewGroup, false);
        return new PostHolder(listitem);
    }

    @Override
    public void onBindViewHolder(@NonNull PostHolder postHolder, int i) {
        if (i % 2 == 0) {
            postHolder.itemView.setBackgroundColor(Color.parseColor("#C13F31"));
        } else {
            postHolder.itemView.setBackgroundColor(Color.parseColor("#FFFFFF"));
        }

        CustomUserData customUserData = postLists.get(i);

        if(customUserData != null) {
            postHolder.Name.setText("Posted by: " + safeString(customUserData.getName()));
            postHolder.Address.setText("From: " + safeString(customUserData.getAddress()) + ", " + safeString(customUserData.getDivision()));
            postHolder.bloodgroup.setText("Needs " + safeString(customUserData.getBloodGroup()));
            postHolder.posted.setText("Posted on: " + safeString(customUserData.getTime()) + ", " + safeString(customUserData.getDate()));
            postHolder.contact.setText(safeString(customUserData.getContact()));
        }
    }

    @Override
    public int getItemCount() {
        return postLists != null ? postLists.size() : 0;
    }

    private String safeString(Object obj) {
        return obj == null ? "" : obj.toString();
    }
}
