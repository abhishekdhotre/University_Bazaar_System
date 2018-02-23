package com.example.cse6324.university_bazaar_system;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseAuthInvalidUserException;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class RegisterActivity extends AppCompatActivity {

    private EditText inputEmail, inputPassword, confPassword, name, phonenum, sid;
    private FirebaseAuth auth;
    private RelativeLayout progressBar;
    private LinearLayout regLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        auth = FirebaseAuth.getInstance();
        regLayout = findViewById(R.id.reg_layout);
        name = findViewById(R.id.name);
        inputEmail = findViewById(R.id.emailAdd);
        inputPassword = findViewById(R.id.reg_password);
        confPassword = findViewById(R.id.conf_password);
        sid = findViewById(R.id.student_id);
        phonenum = findViewById(R.id.phone);
        progressBar = findViewById(R.id.progress_layout);
        Button btnReg = findViewById(R.id.btn_reg);

        btnReg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String fullName = name.getText().toString();
                final String email = inputEmail.getText().toString();
                final String pass = inputPassword.getText().toString();
                final String conf_pass = confPassword.getText().toString();;
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
                    auth.signInWithEmailAndPassword(email, "invalidpass").addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (!task.isSuccessful()) {
                                try {
                                    throw task.getException();
                                } catch (FirebaseAuthInvalidCredentialsException existingEmail) {
                                    regLayout.setVisibility(View.VISIBLE);
                                    progressBar.setVisibility(View.GONE);
                                    Toast.makeText(getApplicationContext(), "Email address has already been registered", Toast.LENGTH_SHORT).show();
                                } catch (FirebaseAuthInvalidUserException valid) {
                                    auth.createUserWithEmailAndPassword(email, pass).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                                        @Override
                                        public void onComplete(@NonNull Task<AuthResult> task) {
                                            if (task.isSuccessful()) {
                                                Map<String, Object> user = new HashMap<>();
                                                user.put("name", fullName);
                                                user.put("email", email);
                                                user.put("phone", phone);
                                                user.put("sid", studentID);

                                                FirebaseFirestore db = FirebaseFirestore.getInstance();
                                                db.collection("users").document(auth.getUid()).set(user);
                                                Intent intent = new Intent(RegisterActivity.this, EntryScreenActivity.class);
                                                startActivity(intent);
                                                finish();
                                            }
                                        }
                                    });
                                } catch (Exception e) {
                                    regLayout.setVisibility(View.VISIBLE);
                                    progressBar.setVisibility(View.GONE);
                                    Toast.makeText(getApplicationContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
                                }
                            }
                        }
                    });
                } else {
                    Toast.makeText(getApplicationContext(), "Please fix the errors", Toast.LENGTH_SHORT).show();
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
