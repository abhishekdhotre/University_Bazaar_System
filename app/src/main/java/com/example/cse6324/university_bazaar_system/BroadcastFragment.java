package com.example.cse6324.university_bazaar_system;

import android.app.Fragment;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabItem;
import android.support.design.widget.TabLayout;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

/**
 * Created by Abhishek on 3/28/2018.
 */

public class BroadcastFragment extends android.support.v4.app.Fragment {

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.fragment_broadcast, container, false);

        view.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                final AlertDialog.Builder builder2 = new AlertDialog.Builder(getContext());
                final EditText input = new EditText(getContext());
                builder2.setTitle("Compose Message");
                input.setLines(5);
                builder2.setView(input);
                builder2.setPositiveButton("Send", new DialogInterface.OnClickListener() {
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
                return true;
            }
        });

        return view;
    }


}
