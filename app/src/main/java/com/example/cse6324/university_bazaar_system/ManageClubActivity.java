package com.example.cse6324.university_bazaar_system;

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
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.firestore.SetOptions;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

public class ManageClubActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manage_club);

        ReadClubInformation();

        Button btnSave = findViewById(R.id.btnUpdateClub);
        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try{
                    EditText etClubName = findViewById(R.id.etClubName);
                    EditText etClubDesc = findViewById(R.id.etClubDesc);

                    if(etClubName.getText().toString().equals("") ||
                            etClubDesc.getText().toString().equals("")){
                        Toast.makeText(ManageClubActivity.this, "Club information cannot be blank!"
                                ,Toast.LENGTH_LONG);
                    }else{
                        DocumentReference clubInfo = FirebaseFirestore.getInstance().collection("clubs").document("V0LcWmshXitkk70wnupx");
                        Map<String,Object> updates = new HashMap<>();
                        updates.put("club_name", etClubName.getText().toString());
                        updates.put("description", etClubDesc.getText().toString());
                        clubInfo.update(updates).addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                Toast.makeText(ManageClubActivity.this, "Changes saved successfully!",
                                        Toast.LENGTH_SHORT).show();
                                finish();
                                startActivity(getIntent());
                            }
                        });
                    }
                }catch(Exception ex){
                    Toast.makeText(ManageClubActivity.this, ex.getMessage(),Toast.LENGTH_LONG);
                }

            }
        });
    }

    private void ReadClubInformation() {
        final LinearLayout linearLayout = findViewById(R.id.llRequestList);
        FirebaseFirestore.getInstance().collection("clubs").whereEqualTo("club_id", 1)
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (DocumentSnapshot document : task.getResult()) {
                                EditText etClubName =findViewById(R.id.etClubName);
                                EditText etClubDesc =findViewById(R.id.etClubDesc);
                                etClubName.setText(document.get("club_name").toString());
                                etClubDesc.setText(document.get("description").toString());
                                final Map<String,Object> arrList = (Map<String, Object>) document.get("list_of_members_requested");
                                final Map<String,Object> updatedList = (Map<String, Object>) document.get("list_of_members");
                                for (final Map.Entry<String, Object> object : arrList.entrySet())
                                {
                                    final String memberKey = object.getKey().toString();
                                    DocumentReference docRef = FirebaseFirestore.getInstance().collection("users").document(memberKey);
                                    docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                                        @Override
                                        public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                                            if (task.isSuccessful()) {
                                                final DocumentSnapshot document = task.getResult();
                                                if (document != null) {
                                                    LinearLayout requestList = new LinearLayout(ManageClubActivity.this);
                                                    requestList.setOrientation(LinearLayout.VERTICAL);
                                                    TextView tvListOfMembers = new TextView(ManageClubActivity.this);
                                                    tvListOfMembers.setTextSize(getResources().getDimension(R.dimen.header));
                                                    tvListOfMembers.setText(document.get("name").toString());

                                                    final Button btnAccept = new Button(ManageClubActivity.this);
                                                    btnAccept.setText("Accept");
                                                    btnAccept.setTextColor( getColor(R.color.white));
                                                    btnAccept.setBackground(getDrawable(R.drawable.post_message_background));

                                                    final Button btnReject = new Button(ManageClubActivity.this);
                                                    btnReject.setText("Reject");
                                                    btnReject.setTextColor( getColor(R.color.white));
                                                    btnReject.setBackground(getDrawable(R.drawable.post_message_background));

                                                    LinearLayout buttonlist = new LinearLayout(ManageClubActivity.this);
                                                    buttonlist.setOrientation(LinearLayout.HORIZONTAL);

                                                    requestList.addView(tvListOfMembers);
                                                    buttonlist.addView(btnAccept);
                                                    buttonlist.addView(btnReject);
                                                    requestList.addView(buttonlist);

                                                    linearLayout.addView(requestList);

                                                    btnReject.setTag(R.string.FirstTag, memberKey);
                                                    btnReject.setOnClickListener(new View.OnClickListener() {
                                                        @Override
                                                        public void onClick(View v) {
                                                            String member = btnReject.getTag(R.string.FirstTag).toString();
                                                            //to be replaced by club id
                                                            DocumentReference docRef = FirebaseFirestore.getInstance().collection("clubs").document("V0LcWmshXitkk70wnupx");
                                                            Map<String,Object> updates = new HashMap<>();
                                                            updates.put("list_of_members_requested." + memberKey, FieldValue.delete());
                                                            docRef.update(updates).addOnCompleteListener(new OnCompleteListener<Void>() {
                                                                @Override
                                                                public void onComplete(@NonNull Task<Void> task) {
                                                                    Toast.makeText(ManageClubActivity.this,
                                                                            "Request rejected successfully!", Toast.LENGTH_LONG).show();
                                                                    finish();
                                                                    startActivity(getIntent());
                                                                }
                                                            });
                                                        }
                                                    });

                                                    btnAccept.setTag(R.string.FirstTag, memberKey);
                                                    btnAccept.setOnClickListener(new View.OnClickListener() {
                                                        @Override
                                                        public void onClick(View v) {
                                                            //to be replaced by club id
                                                            DocumentReference docRef = FirebaseFirestore.getInstance().collection("clubs").document("V0LcWmshXitkk70wnupx");
                                                            Map<String,Object> updates = new HashMap<>();
                                                            updates.put("list_of_members_requested." + memberKey, FieldValue.delete());
                                                            docRef.update(updates).addOnCompleteListener(new OnCompleteListener<Void>() {
                                                                @Override
                                                                public void onComplete(@NonNull Task<Void> task) {
                                                                    Toast.makeText(ManageClubActivity.this,
                                                                            "Member successfully added to club!", Toast.LENGTH_LONG).show();
                                                                    finish();
                                                                    startActivity(getIntent());
                                                                }
                                                            });
                                                        }
                                                    });
                                                }
                                            }
                                        }
                                    });

                                };

                            }
                        } else {

                        }
                    }
                });
    }
}
