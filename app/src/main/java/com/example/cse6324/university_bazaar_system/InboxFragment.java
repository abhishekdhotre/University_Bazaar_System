package com.example.cse6324.university_bazaar_system;

import android.app.Notification;
import android.content.Context;
import android.content.DialogInterface;
import android.net.Uri;
import android.os.Bundle;
import android.app.Fragment;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.firestore.DocumentSnapshot;

import java.util.ArrayList;
import java.util.List;

public class InboxFragment extends android.support.v4.app.Fragment {
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.fragment_inbox, container, false);

        final LinearLayout linearLayout = view.findViewById(R.id.llChatDisplay);

        List<String> messages = new ArrayList<>();
        messages.add("How much did you score in Adv SE?");
        messages.add("Group message");
        messages.add("Free Pizza for everyone at UC after class!");
        messages.add("This is a reminder to submit your test cases on Tuesday!");
        messages.add("Exam grades are posted on blackboard!");
        messages.add("Welcome to Advance Sofware Engineering Group!");

        List<String> senders = new ArrayList<>();
        senders.add("Illwin");
        senders.add("Illwin");
        senders.add("Illwin");
        senders.add("Palash");
        senders.add("Neuvine");
        senders.add("Salman");

        for (int i = 0; i < messages.size() ; i ++) {
            TextView tvDisplayPosts = new TextView(getContext());
            tvDisplayPosts.setTextColor(getResources().getColor(R.color.white));
            tvDisplayPosts.setBackground(getResources().getDrawable(R.drawable.post_message_background));
            tvDisplayPosts.setTextSize(20);
            LinearLayout.LayoutParams tvLayoutParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
            tvLayoutParams.setMargins(50, 10, 50, 10);
            tvDisplayPosts.setLayoutParams(tvLayoutParams);
            tvDisplayPosts.setText(messages.get(i));
            TextView tvPostdetails = new TextView(getContext());
            tvPostdetails.setTextSize(10);
            tvPostdetails.setTextAlignment(View.TEXT_ALIGNMENT_VIEW_END);
            tvPostdetails.setLayoutParams(tvLayoutParams);
            tvPostdetails.setText(messages.get(i));
            tvDisplayPosts.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    final AlertDialog.Builder builder2 = new AlertDialog.Builder(getContext());
                    final EditText input = new EditText(getContext());
                    builder2.setTitle("Reply Message");
                    input.setLines(5);
                    builder2.setView(input);
                    builder2.setPositiveButton("Reply", new DialogInterface.OnClickListener() {
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
                    builder2.show();
                }
            });
            StringBuilder fields = new StringBuilder("");
            fields.append("\nPosted By: ").append(senders.get(i));
            tvPostdetails.setText(fields.toString());
            try{
                linearLayout.addView(tvDisplayPosts);
                linearLayout.addView(tvPostdetails);
            }catch (Exception ex){
                Toast.makeText(getContext(), ex.getMessage(), Toast.LENGTH_SHORT).show();
            }

        }
        return view;
    }
}
