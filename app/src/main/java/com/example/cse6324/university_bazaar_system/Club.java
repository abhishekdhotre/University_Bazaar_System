package com.example.cse6324.university_bazaar_system;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseAuthInvalidUserException;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class Club extends AppCompatActivity {


    private EditText clubName, clubDescription, clubAdmin, clubMember;
    private FirebaseAuth auth;
    private ArrayList<String> clubMembers;
    private LinearLayout Llayout,dummyLayout;
    Button btnSave;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_club);

        /*
            get all values from the screen as well as club admin
         */
        Llayout = findViewById(R.id.club_layout);
        dummyLayout = findViewById(R.id.dummyLayout);
        clubName = findViewById(R.id.clubName);
        clubDescription = findViewById(R.id.clubDescription);
        clubMember = findViewById(R.id.emailAdd);
        btnSave = findViewById(R.id.btn_reg);

        /*
        iterate and keep adding if more
         */
        /*
            create connection to firebase
         */

        auth = FirebaseAuth.getInstance();


        /*
        validate
         */
        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final String cName = clubName.getText().toString();
                final String cDesc = clubDescription.getText().toString();
                final ArrayList<String> cMembers= new ArrayList<>();
                cMembers.add(clubMember.getText().toString());
                //hardcode admin document ID

                Map<String, Object> clubDb = new HashMap<>();
                String loginEmail = "MlNEkyqc32ULeQHFyrQyjXvsFuz1";
                //loginEmail=auth.getCurrentUser().getEmail();
                /*

                 */
                try {
                    clubDb.put("club_admin", loginEmail);
                    //club Id hard coded to 101 for use
                    clubDb.put("club_id","101");
                    clubDb.put("club_name", cName);
                    clubDb.put("description", cDesc);
                    clubDb.put("list_of_members", cMembers);
                    /*
                    document hardcoded to ilwin for sprint 2
                     */
                    FirebaseFirestore.getInstance().collection("clubs").document("ilwin").set(clubDb);

                    Toast.makeText(Club.this,"Club has been added",Toast.LENGTH_LONG).show();
                    /*
                    call club list
                     */
                    Intent intent = new Intent(Club.this, EntryScreenActivity.class);
                    startActivity(intent);
                    finish();

                }catch (Exception e){
                    e.printStackTrace();
                }


            }
        });
        /*
            send the data to the database
         */

        Button add = findViewById(R.id.button_add);
        add.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                EditText edt=new EditText(getBaseContext());
                //edt.setla
                edt.setHint(getResources().getString(R.string.hint_addMembers));
                LinearLayout main = findViewById(R.id.dummyLayout);
                main.addView(edt);
            }
        });
    }
}
