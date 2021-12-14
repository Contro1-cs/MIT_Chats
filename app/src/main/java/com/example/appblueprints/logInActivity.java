package com.example.appblueprints;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class logInActivity extends AppCompatActivity {
    private Button loginBtn,askSignUp;
    private FirebaseAuth mAuth;
    private EditText logInEmail, loginPass;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_log_in);
        FirebaseAuth.getInstance();
        logInEmail = findViewById(R.id.askEmail);
        loginPass = findViewById(R.id.askPass);
        loginBtn = findViewById(R.id.loginBtn);
        FirebaseAuth.getInstance().toString();



    }
}