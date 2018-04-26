package com.example.cse6324.university_bazaar_system;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseAuthInvalidUserException;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.HashMap;
import java.util.Map;

public class EditProfileActivity extends AppCompatActivity {


    private EditText inputEmail, inputPassword, confPassword, name, phonenum, sid;
    private Button submit, changePassword;
    private FirebaseAuth auth;
    private RelativeLayout progressBar;
    private LinearLayout regLayout;
    User currentUser;
    FirebaseFirestore db;
    FirebaseStorage storage;
    StorageReference storageRef;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_profile);


        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        auth = FirebaseAuth.getInstance();
        regLayout = findViewById(R.id.edit_profile_layout);
        name = findViewById(R.id.edit_profile_name);
        inputEmail = findViewById(R.id.edit_profile_email);
        inputPassword = findViewById(R.id.new_password);
        confPassword = findViewById(R.id.confirm_new_password);
        sid = findViewById(R.id.edit_profile_student_id);
        phonenum = findViewById(R.id.edit_profile_phone);
        progressBar = findViewById(R.id.edit_profile_progress_layout);
        submit = findViewById(R.id.btn_submit);
        changePassword = findViewById(R.id.btn_change_password);


        db = FirebaseFirestore.getInstance();
        storage = FirebaseStorage.getInstance();
        storageRef = storage.getReference();

        final DocumentReference docRef = db.collection("users").document(auth.getUid());

        docRef.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                currentUser = documentSnapshot.toObject(User.class);
                name.setText(currentUser.getName());
                inputEmail.setText(currentUser.getEmail());
                sid.setText(currentUser.getSid());
                phonenum.setText(currentUser.getPhone());
            }
        });

        changePassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                inputPassword.setVisibility(View.VISIBLE);
                confPassword.setVisibility(View.VISIBLE);
                changePassword.setVisibility(View.INVISIBLE);
            }
        });

        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final String fullName = name.getText().toString();
                final String email = inputEmail.getText().toString();
                final String pass = inputPassword.getText().toString();
                final String conf_pass = confPassword.getText().toString();
                ;
                final String studentID = sid.getText().toString();
                final String phone = phonenum.getText().toString();

                boolean allConditions = true;

                //Name
                if (0 == fullName.length()) {
                    name.setError("Name is required");
                    allConditions = false;
                }

                //Email
                if (0 == email.length()) {
                    inputEmail.setError("Email is required");
                    allConditions = false;
                } else if (!isValidEmail(email)) {
                    inputEmail.setError("Enter a valid email address");
                    allConditions = false;
                }

                //Passwords
                if ((inputPassword.getVisibility() == View.VISIBLE) && (confPassword.getVisibility() == View.VISIBLE)) {
                    if (0 == pass.length()) {
                        inputPassword.setError("Password is required");
                        allConditions = false;
                    } else if (8 > pass.length()) {
                        inputPassword.setError("Enter a password of 8 or more characters");
                        allConditions = false;
                    }

                    if (0 == conf_pass.length()) {
                        confPassword.setError("Password is required");
                        allConditions = false;
                    } else if (8 > conf_pass.length()) {
                        confPassword.setError("Enter a password of 8 or more characters");
                        allConditions = false;
                    }
                    if (7 < pass.length() && 7 < conf_pass.length() && !pass.equals(conf_pass)) {
                        confPassword.setError("Passwords do not match");
                        allConditions = false;
                    }
                }

                //Student ID
                if (0 == studentID.length()) {
                    sid.setError("Student ID is required");
                    allConditions = false;
                } else if (10 != studentID.length() || !TextUtils.isDigitsOnly(studentID)) {
                    sid.setError("Enter a valid student ID");
                    allConditions = false;
                }

                //Phone
                if (0 == phone.length()) {
                    phonenum.setError("Phone number is required");
                    allConditions = false;
                } else if (10 != phone.length() || !TextUtils.isDigitsOnly(phone)) {
                    phonenum.setError("Enter a valid phone number");
                    allConditions = false;
                }

                if (allConditions) {
                    progressBar.setVisibility(View.VISIBLE);
                    regLayout.setVisibility(View.GONE);
                    Map<String, Object> user = new HashMap<>();
                    user.put("name", fullName);
                    user.put("email", email);
                    user.put("phone", phone);
                    user.put("sid", studentID);

                    docRef.update(user);
                    progressBar.setVisibility(View.GONE);
                    auth.getCurrentUser().updatePassword(pass);

                    Toast.makeText(EditProfileActivity.this,"Profile Updated Successfully",Toast.LENGTH_SHORT).show();

                    Intent intent = new Intent(EditProfileActivity.this, EntryScreenActivity.class);
                    startActivity(intent);
                    finish();


                }
            }
        });

    }

    private boolean isValidEmail(String email) {
        if (Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            String[] emailSplit = email.split("@");
            if (2 != emailSplit.length) {
                return false;
            } else {
                if (emailSplit[1].equals("mavs.uta.edu")) {
                    return true;
                }
            }
        }
        return false;
    }

}
