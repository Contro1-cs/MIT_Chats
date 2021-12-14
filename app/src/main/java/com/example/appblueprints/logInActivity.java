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
    private Button logInBtn;
    private FirebaseAuth auth;
    private EditText logInEmail, loginPass;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_log_in);
        FirebaseAuth.getInstance();
        logInBtn = findViewById(R.id.logInBtn);
        logInEmail = findViewById(R.id.askEmail);
        loginPass = findViewById(R.id.askPass);
        FirebaseAuth.getInstance().toString();

        logInBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String email = logInEmail.getText().toString();
                String pass = loginPass.getText().toString();

                if(!email.isEmpty() && !pass.isEmpty()){
                    auth.signInWithEmailAndPassword(email,pass).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if(task.isSuccessful()){
                                Toast.makeText(logInActivity.this, "Logged in successfully!", Toast.LENGTH_SHORT).show();
                                startActivity(new Intent(logInActivity.this, MainActivity.class));
                                finish();
                            }else{
                                Toast.makeText(logInActivity.this, task.getException().toString(), Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                }
            }
        });
    }
}