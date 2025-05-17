package com.example.donordarah.activities;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.donordarah.R;
import com.example.donordarah.viewmodels.UserData;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.Calendar;

public class PostActivity extends AppCompatActivity {

    ProgressDialog pd;

    EditText text1, text2;
    Spinner spinner1, spinner2;
    Button btnpost;

    FirebaseDatabase fdb;
    DatabaseReference db_ref;
    FirebaseAuth mAuth;

    Calendar cal;
    String uid;
    String Time, Date;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post);

        pd = new ProgressDialog(this);
        pd.setMessage("Loading...");
        pd.setCancelable(false);
        pd.setCanceledOnTouchOutside(false);

        // AndroidX ActionBar setup
        if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle("Post Blood Request");
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

        text1 = findViewById(R.id.getMobile);
        text2 = findViewById(R.id.getLocation);
        spinner1 = findViewById(R.id.SpinnerBlood);
        spinner2 = findViewById(R.id.SpinnerDivision);
        btnpost = findViewById(R.id.postbtn);

        mAuth = FirebaseAuth.getInstance();

        FirebaseUser cur_user = mAuth.getCurrentUser();

        if (cur_user == null) {
            // User belum login, redirect ke LoginActivity
            startActivity(new Intent(PostActivity.this, LoginActivity.class));
            finish();
            return;
        } else {
            uid = cur_user.getUid();
        }

        fdb = FirebaseDatabase.getInstance();
        db_ref = fdb.getReference("posts");

        // Waktu & tanggal saat ini
        cal = Calendar.getInstance();

        int day = cal.get(Calendar.DAY_OF_MONTH);
        int month = cal.get(Calendar.MONTH) + 1;  // Calendar.MONTH mulai dari 0
        int year = cal.get(Calendar.YEAR);
        int hour = cal.get(Calendar.HOUR);
        int min = cal.get(Calendar.MINUTE);

        String ampm = (cal.get(Calendar.AM_PM) == Calendar.PM) ? "PM" : "AM";

        // Format waktu (jam:menit AM/PM)
        Time = String.format("%02d:%02d %s", (hour == 0 ? 12 : hour), min, ampm);
        Date = String.format("%02d/%02d/%d", day, month, year);

        btnpost.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String contact = text1.getText().toString().trim();
                String location = text2.getText().toString().trim();

                if (contact.isEmpty()) {
                    text1.setError("Enter your contact number!");
                    text1.requestFocus();
                    return;
                }

                if (location.isEmpty()) {
                    text2.setError("Enter your location!");
                    text2.requestFocus();
                    return;
                }

                pd.show();

                Query findname = fdb.getReference("users").child(uid);
                findname.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        pd.dismiss();

                        if (dataSnapshot.exists()) {
                            UserData userData = dataSnapshot.getValue(UserData.class);
                            if (userData != null) {
                                db_ref.child(uid).child("Name").setValue(userData.getName());
                            } else {
                                Toast.makeText(PostActivity.this, "Failed to get user data", Toast.LENGTH_LONG).show();
                                return;
                            }
                            db_ref.child(uid).child("Contact").setValue(contact);
                            db_ref.child(uid).child("Address").setValue(location);
                            db_ref.child(uid).child("Division").setValue(spinner2.getSelectedItem().toString());
                            db_ref.child(uid).child("BloodGroup").setValue(spinner1.getSelectedItem().toString());
                            db_ref.child(uid).child("Time").setValue(Time);
                            db_ref.child(uid).child("Date").setValue(Date);

                            Toast.makeText(PostActivity.this, "Your post has been created successfully", Toast.LENGTH_LONG).show();
                            startActivity(new Intent(PostActivity.this, Dashboard.class));
                            finish();

                        } else {
                            Toast.makeText(PostActivity.this, "Database error occurred.", Toast.LENGTH_LONG).show();
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {
                        pd.dismiss();
                        Log.e("PostActivity", "Database error: " + databaseError.getMessage());
                        Toast.makeText(PostActivity.this, "Database error occurred: " + databaseError.getMessage(), Toast.LENGTH_LONG).show();
                    }
                });
            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            onBackPressed();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
