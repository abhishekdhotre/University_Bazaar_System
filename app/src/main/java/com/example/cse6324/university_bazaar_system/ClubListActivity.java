package com.example.cse6324.university_bazaar_system;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

public class ClubListActivity extends AppCompatActivity {

    AdView mAdview;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_club_list);

        MobileAds.initialize(this, "ca-app-pub-3940256099942544~3347511713");

        mAdview = findViewById(R.id.adView);
        AdRequest adRequest = new AdRequest.Builder().build();
        mAdview.loadAd(adRequest);
        Button btnCreateNewClub = findViewById(R.id.btnCreateNewClub);
        btnCreateNewClub.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent myIntent = new Intent(ClubListActivity.this, Club.class);
                ClubListActivity.this.startActivity(myIntent);
            }
        });

        Button btnManageClubs = findViewById(R.id.btnManageClubs);
        btnManageClubs.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent myIntent = new Intent(ClubListActivity.this, ManageClubActivity.class);
                myIntent.putExtra("key", "AdvanceSE"); //Optional parameters
                ClubListActivity.this.startActivity(myIntent);
            }
        });

        FirebaseFirestore.getInstance().collection("clubs")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        LinearLayout llClubList = findViewById(R.id.llClubList);
                        if (task.isSuccessful()) {
                            for (DocumentSnapshot document : task.getResult()) {
                                final Button btnClubList = new Button(ClubListActivity.this);
                                final String clubname = document.get("club_name").toString();
                                btnClubList.setText(clubname);
                                btnClubList.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT,
                                        LinearLayout.LayoutParams.WRAP_CONTENT));
                                btnClubList.setTag(R.string.FirstTag, clubname);
                                btnClubList.setBackground(getDrawable(R.drawable.post_message_background));
                                btnClubList.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        Intent myIntent = new Intent(ClubListActivity.this, ClubHomeActivity.class);
                                        myIntent.putExtra("key", btnClubList.getTag(R.string.FirstTag).toString());
                                        ClubListActivity.this.startActivity(myIntent);
                                    }
                                });

                                final Button btnJoinClub = new Button(ClubListActivity.this);
                                btnJoinClub.setBackground(getDrawable(R.drawable.ic_add_black_24dp));
                                btnJoinClub.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT,
                                        LinearLayout.LayoutParams.WRAP_CONTENT));
                                btnJoinClub.setRight(llClubList.getRight());
                                btnJoinClub.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        Toast.makeText(ClubListActivity.this, "Request Sent!", Toast.LENGTH_LONG).show();
                                    }
                                });

                                LinearLayout llButtons = new LinearLayout(ClubListActivity.this);
                                llButtons.setOrientation(LinearLayout.HORIZONTAL);
                                llButtons.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,
                                        LinearLayout.LayoutParams.WRAP_CONTENT));
                                llButtons.addView(btnClubList);
                                llButtons.addView(btnJoinClub);
                                llClubList.addView(llButtons);
                            }
                        }
                    }
                });



    }
}
