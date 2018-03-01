package com.example.cse6324.university_bazaar_system;

import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
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

import org.w3c.dom.Document;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class ClubHomeActivity extends AppCompatActivity {

    private int count = 0;
    private EditText etPostMessage;
    private TextView tvNewPost, tvNewPostDetails;
    private FirebaseFirestore db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_club_home);

        etPostMessage = (EditText)findViewById(R.id.etPostMsg);

        ReadSingleContact();
    }

    private void displayClubPosts() {
        DocumentReference docRef;
        docRef = FirebaseFirestore.getInstance().document("posts/Advance SE");
        CollectionReference reference =  docRef.getParent();
        Query mQuery = FirebaseFirestore.getInstance().collection("posts");
    }
    private void ReadSingleContact() {
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

    public void post(View v){

        if(etPostMessage.getText().length() == 0){
            Toast.makeText(this,"Enter text to post",Toast.LENGTH_SHORT);
        }

        else{
            count++;
            CollectionReference docRef = FirebaseFirestore.getInstance().collection("posts").document("AdvanceSE").collection("allposts");
            Map<String,Object> data = new HashMap<>();
            data.put("postdat",(new Date()).getTime());
            data.put("postedby", FirebaseAuth.getInstance().getCurrentUser().toString());
            data.put("postmsg",etPostMessage.getText().toString());
            docRef.document("post"+count).set(data);

            Toast.makeText(ClubHomeActivity.this,"Posted Successfully",Toast.LENGTH_SHORT);
        }

    }


}
