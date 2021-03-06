package com.example.cse6324.university_bazaar_system;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class ClubHomeActivity extends AppCompatActivity {

    EditText etPost;
    FirebaseFirestore db;
    int count = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_club_home);

        Intent intent = getIntent();
        String club_name = intent.getStringExtra("key");

        etPost = (EditText)findViewById(R.id.tvPostMsg);
        Button btn = findViewById(R.id.btnPost);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(etPost.getText().toString().length() == 0){
                    Toast.makeText(ClubHomeActivity.this, "Enter text to post", Toast.LENGTH_SHORT).show();
                }
                else {
                    count++;
                    CollectionReference doc = FirebaseFirestore.getInstance().collection("posts").document("AdvanceSE").collection("allposts");
                    Map<String, Object> data = new HashMap<>();
                    data.put("postedat", new Date().getTime());
                    data.put("postedby", "Ilwin Dcunha");
                    data.put("postmsg", etPost.getText().toString());
                    try{
                        doc.document("post" + count).set(data);
                    }catch(Exception ex){
                        String abc = ex.getMessage();
                    }

                    Toast.makeText(ClubHomeActivity.this, "Posted Successfully", Toast.LENGTH_SHORT).show();
                    finish();
                    startActivity(getIntent());
                }
            }
        });

        ReadAllPosts(club_name);
    }

    private void ReadAllPosts(String clubName) {
        final LinearLayout linearLayout = findViewById(R.id.llMsgDisplay);
        FirebaseFirestore.getInstance().collection("posts").document(clubName).collection("allposts").orderBy("postedat", Query.Direction.DESCENDING)
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
