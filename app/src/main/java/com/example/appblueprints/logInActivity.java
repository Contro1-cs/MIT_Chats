package com.example.appblueprints;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextClock;
import android.widget.TextView;

public class logInActivity extends AppCompatActivity {
    private TextView askSignUp;
    private EditText logInName,logInPass;
    private Button logInBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_log_in);

        askSignUp.findViewById(R.id.askSignUp);
        logInName.findViewById(R.id.logInName);
        logInPass.findViewById(R.id.logInPass);

        askSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(logInActivity.this, activitySignUp.class));
            }
        });

    }
}