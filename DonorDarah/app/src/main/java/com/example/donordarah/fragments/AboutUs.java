package com.example.donordarah.fragments;

import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import android.text.util.Linkify;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.donordarah.R;

public class AboutUs extends Fragment {
    private TextView textView;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.aboutus, container, false);
        getActivity().setTitle("About us");

        textView = view.findViewById(R.id.txtv);
        Linkify.addLinks(textView, Linkify.ALL);
        return  view;
    }
}
