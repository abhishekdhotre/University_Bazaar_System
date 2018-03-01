package com.example.cse6324.university_bazaar_system;

import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;

public class ClubHomeActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_club_home);
        ReadAllPosts();
    }

    private void ReadAllPosts() {
        final LinearLayout linearLayout = findViewById(R.id.llMsgDisplay);
        FirebaseFirestore.getInstance().collection("posts").document("AdvanceSE").collection("allposts").orderBy("postedat", Query.Direction.DESCENDING)
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (DocumentSnapshot document : task.getResult()) {
                                TextView tvDisplayPosts = new TextView(ClubHomeActivity.this);
                                tvDisplayPosts.setTextColor(getColor(R.color.white));
                                tvDisplayPosts.setBackground(getDrawable(R.drawable.post_message_background));
                                tvDisplayPosts.setTextSize(20);
                                LinearLayout.LayoutParams tvLayoutParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
                                tvLayoutParams.setMargins(50, 10, 50, 10);
                                tvDisplayPosts.setLayoutParams(tvLayoutParams);
                                tvDisplayPosts.setText(document.get("postmsg").toString());

                                TextView tvPostdetails = new TextView(ClubHomeActivity.this);
                                tvPostdetails.setTextSize(10);
                                tvPostdetails.setTextAlignment(View.TEXT_ALIGNMENT_VIEW_END);
                                tvPostdetails.setLayoutParams(tvLayoutParams);
                                tvPostdetails.setText(document.get("postmsg").toString());
                                StringBuilder fields = new StringBuilder("");
                                fields.append("\nPosted By: ").append(document.get("postedby"));
                                fields.append("\nPosted At: ").append(document.get("postedat"));
                                tvPostdetails.setText(fields.toString());
                                linearLayout.addView(tvDisplayPosts);
                                linearLayout.addView(tvPostdetails);
                            }
                        } else {

                        }
                    }
                });
    }


}
