package com.example.cse6324.university_bazaar_system;

import android.app.Fragment;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.text.InputType;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

/**
 * Created by Abhishek on 3/28/2018.
 */

public class IndividualFragment extends android.support.v4.app.Fragment {

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.fragment_individual, container, false);
        FirebaseFirestore.getInstance().collection("users")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        LinearLayout llClubList = view.findViewById(R.id.llIndividualList);
                        if (task.isSuccessful()) {
                            for (DocumentSnapshot document : task.getResult()) {
                                final Button btnClubList = new Button(getContext());
                                final String clubname = document.get("name").toString();
                                btnClubList.setText(clubname);
                                btnClubList.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,
                                        LinearLayout.LayoutParams.WRAP_CONTENT));
                                btnClubList.setTag(R.string.FirstTag, clubname);
                                btnClubList.setBackground(view.getResources().getDrawable(R.drawable.post_message_background));
                                btnClubList.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        final AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                                        builder.setTitle("Compose Message");
                                        final EditText input = new EditText(getContext());
                                        input.setLines(5);
                                        builder.setView(input);
                                        builder.setPositiveButton("Send", new DialogInterface.OnClickListener() {
                                            @Override
                                            public void onClick(DialogInterface dialog, int which) {
                                                if(input.getText().toString().length() == 0){
                                                    Toast.makeText(getContext(), "Failure to send message, no message text!", Toast.LENGTH_LONG).show();
                                                }
                                                else{
                                                    Toast.makeText(getContext(), "Message Sent Successfully!", Toast.LENGTH_LONG).show();
                                                }
                                            }
                                        });
                                        AlertDialog dialog = builder.create();
                                        //positiveButton.setBackgroundColor(getResources().getColor(R.color.colorPrimary));
                                        dialog.show();
                                        //builder.show();
                                    }
                                });

                                LinearLayout llButtons = new LinearLayout(getContext());
                                llButtons.setOrientation(LinearLayout.HORIZONTAL);
                                llButtons.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,
                                        LinearLayout.LayoutParams.WRAP_CONTENT));
                                llButtons.addView(btnClubList);
                                llClubList.addView(llButtons);
                            }
                        }
                    }
                });
        return view;
    }
}
