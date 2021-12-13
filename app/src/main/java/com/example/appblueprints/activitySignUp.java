package com.example.appblueprints;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class activitySignUp extends AppCompatActivity {

    private EditText signUpName, signUpPass;
    private Button signUpBtn;
    private FirebaseAuth auth;
    private TextView askLogIn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        auth=FirebaseAuth.getInstance();

        signUpName = findViewById(R.id.signUpName);
        signUpPass = findViewById(R.id.signUpPass);
        signUpBtn = findViewById(R.id.signUpBtn);
        askLogIn = findViewById(R.id.askLogIn);

        askLogIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(activitySignUp.this, logInActivity.class));
            }
        });

        signUpBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String email = signUpName.getText().toString();
                String pass = signUpPass.getText().toString();

                if(!email.isEmpty() || !pass.isEmpty()){
                    auth.createUserWithEmailAndPassword(email, pass).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if(task.isSuccessful()){
                                startActivity(new Intent(activitySignUp.this,logInActivity.class));
                                finish();
                            }else{
                                Toast.makeText(activitySignUp.this, task.getException().toString(), Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                }else{
                    Toast.makeText(activitySignUp.this, "Email or Password is empty!", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}