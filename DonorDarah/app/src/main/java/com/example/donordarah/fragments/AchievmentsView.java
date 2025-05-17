package com.example.donordarah.fragments;
import com.example.donordarah.viewmodels.UserData;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import com.example.donordarah.R;
import com.example.donordarah.activities.Dashboard;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.example.donordarah.viewmodels.DonorData;
import java.util.Calendar;
import java.util.TimeZone;

public class AchievmentsView extends Fragment {

    private Calendar calendar;
    private ProgressDialog pd;
    private DatabaseReference db_ref, user_ref;
    private FirebaseAuth mAuth;

    private TextView totalDonate, lastDonate, nextDonate, donateInfo;
    private String[] bloodgroup, divisionlist;
    private String lastDate;

    private View view;
    private Button yes;
    private LinearLayout yesno;

    public AchievmentsView() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.user_achievment_fragment, container, false);

        pd = new ProgressDialog(getActivity());
        pd.setMessage("Loading...");
        pd.setCancelable(true);
        pd.setCanceledOnTouchOutside(false);

        bloodgroup = getResources().getStringArray(R.array.Blood_Group);
        divisionlist = getResources().getStringArray(R.array.division_list);

        lastDonate = view.findViewById(R.id.setLastDonate);
        totalDonate = view.findViewById(R.id.settotalDonate);
        donateInfo = view.findViewById(R.id.donateInfo);
        nextDonate = view.findViewById(R.id.nextDonate);
        yesno = view.findViewById(R.id.yesnolayout);
        yes = view.findViewById(R.id.btnYes);

        getActivity().setTitle("Achievements");

        mAuth = FirebaseAuth.getInstance();

        db_ref = FirebaseDatabase.getInstance().getReference("donors");
        user_ref = FirebaseDatabase.getInstance().getReference("users");

        lastDate = "";

        Query userQ = user_ref.child(mAuth.getCurrentUser().getUid());

        try {
            pd.show();
            userQ.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    if (dataSnapshot.exists()) {
                        final UserData userData = dataSnapshot.getValue(UserData.class);
                        final int getdiv = userData.getDivision();
                        final int getbg = userData.getBloodGroup();

                        Query donorQ = db_ref.child(divisionlist[getdiv])
                                .child(bloodgroup[getbg])
                                .child(mAuth.getCurrentUser().getUid());

                        donorQ.addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                if (dataSnapshot.exists()) {
                                    final DonorData donorData = dataSnapshot.getValue(DonorData.class);
                                    totalDonate.setText(donorData.getTotalDonate() + " times");
                                    if (donorData.getTotalDonate() == 0) {
                                        lastDate = "01/01/2001";
                                        lastDonate.setText("Do not donate yet!");
                                    } else {
                                        lastDate = donorData.getLastDonate();
                                        lastDonate.setText(lastDate);
                                    }

                                    if (!lastDate.isEmpty()) {
                                        // Parsing tanggal dengan split
                                        String[] parts = lastDate.split("/");
                                        int day = 1, month = 1, year = 2001;
                                        if (parts.length == 3) {
                                            try {
                                                day = Integer.parseInt(parts[0]);
                                                month = Integer.parseInt(parts[1]);
                                                year = Integer.parseInt(parts[2]);
                                            } catch (NumberFormatException e) {
                                                e.printStackTrace();
                                            }
                                        }

                                        calendar = Calendar.getInstance(TimeZone.getDefault());
                                        int cur_day = calendar.get(Calendar.DAY_OF_MONTH);
                                        int cur_month = calendar.get(Calendar.MONTH) + 1;
                                        int cur_year = calendar.get(Calendar.YEAR);

                                        int totday = 0;

                                        if (day > cur_day) {
                                            cur_day += 30;
                                            cur_month -= 1;
                                        }
                                        totday += (cur_day - day);

                                        if (month > cur_month) {
                                            cur_month += 12;
                                            cur_year -= 1;
                                        }
                                        totday += ((cur_month - month) * 30);
                                        totday += ((cur_year - year) * 365);

                                        if (totday > 120) {
                                            donateInfo.setText("Have you donated today?");
                                            nextDonate.setVisibility(View.GONE);
                                            yesno.setVisibility(View.VISIBLE);

                                            yes.setOnClickListener(new View.OnClickListener() {
                                                @Override
                                                public void onClick(View v) {
                                                    int cur_day = calendar.get(Calendar.DAY_OF_MONTH);
                                                    int cur_month = calendar.get(Calendar.MONTH) + 1;
                                                    int cur_year = calendar.get(Calendar.YEAR);

                                                    db_ref.child(divisionlist[getdiv])
                                                            .child(bloodgroup[getbg])
                                                            .child(mAuth.getCurrentUser().getUid())
                                                            .child("LastDonate")
                                                            .setValue(cur_day + "/" + cur_month + "/" + cur_year);

                                                    db_ref.child(divisionlist[getdiv])
                                                            .child(bloodgroup[getbg])
                                                            .child(mAuth.getCurrentUser().getUid())
                                                            .child("TotalDonate")
                                                            .setValue(donorData.getTotalDonate() + 1);

                                                    pd.dismiss();
                                                    startActivity(new Intent(getActivity(), Dashboard.class));
                                                }
                                            });

                                        } else {
                                            donateInfo.setText("Next donation available in:");
                                            yesno.setVisibility(View.GONE);
                                            nextDonate.setVisibility(View.VISIBLE);
                                            nextDonate.setText((120 - totday) + " days");
                                        }

                                    }

                                } else {
                                    LinearLayout linearLayout = view.findViewById(R.id.donorAchiev);
                                    linearLayout.setVisibility(View.GONE);
                                    TextView tv = view.findViewById(R.id.ShowInof);
                                    tv.setVisibility(View.VISIBLE);
                                    Toast.makeText(getActivity(), "Update your profile to be a donor first.", Toast.LENGTH_LONG).show();
                                }
                                pd.dismiss();
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError databaseError) {
                                Log.d("User", databaseError.getMessage());
                                pd.dismiss();
                            }
                        });

                    } else {
                        Toast.makeText(getActivity(), "You are not a user." + divisionlist[0] + " " + bloodgroup[0], Toast.LENGTH_LONG).show();
                        pd.dismiss();
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {
                    Log.d("User", databaseError.getMessage());
                    pd.dismiss();
                }
            });

        } catch (Exception e) {
            e.printStackTrace();
            pd.dismiss();
        }

        return view;
    }

}
