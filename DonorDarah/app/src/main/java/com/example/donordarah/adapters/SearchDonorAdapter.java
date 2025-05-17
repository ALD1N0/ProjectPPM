package com.example.donordarah.adapters;

import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.donordarah.R;
import com.example.donordarah.viewmodels.DonorData;

import java.util.List;

public class SearchDonorAdapter extends RecyclerView.Adapter<SearchDonorAdapter.PostHolder> {

    private List<DonorData> donorList;

    public SearchDonorAdapter(List<DonorData> donorList) {
        this.donorList = donorList;
    }

    public static class PostHolder extends RecyclerView.ViewHolder {
        TextView name, address, contact, posted, totalDonate;

        public PostHolder(@NonNull View itemView) {
            super(itemView);
            name = itemView.findViewById(R.id.donorName);
            contact = itemView.findViewById(R.id.donorContact);
            totalDonate = itemView.findViewById(R.id.totaldonate);
            address = itemView.findViewById(R.id.donorAddress);
            posted = itemView.findViewById(R.id.lastdonate);
        }
    }

    @NonNull
    @Override
    public PostHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View listItem = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.search_donor_item, parent, false);
        return new PostHolder(listItem);
    }

    @Override
    public void onBindViewHolder(@NonNull PostHolder holder, int position) {
        if (position % 2 == 0) {
            holder.itemView.setBackgroundColor(Color.parseColor("#C13F31"));
        } else {
            holder.itemView.setBackgroundColor(Color.parseColor("#FFFFFF"));
        }

        DonorData donorData = donorList.get(position);
        holder.name.setText("Name: " + donorData.getName());
        holder.contact.setText(donorData.getContact());
        holder.address.setText("Address: " + donorData.getAddress());
        holder.totalDonate.setText("Total Donation: " + donorData.getTotalDonate() + " times");
        holder.posted.setText("Last Donation: " + donorData.getLastDonate());
    }

    @Override
    public int getItemCount() {
        return donorList.size();
    }
}
