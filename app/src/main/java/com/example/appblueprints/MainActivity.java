package com.example.appblueprints;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;



public class MainActivity extends AppCompatActivity {

    private FirebaseAuth firebaseAuth;
    private Button profileBtn;
    private Button logOutBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        firebaseAuth = FirebaseAuth.getInstance();
        profileBtn = findViewById(R.id.profileBtn);



        profileBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(MainActivity.this, SetUpActivity.class));
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        FirebaseUser currentUser = firebaseAuth.getCurrentUser();
        if(currentUser == null){
            startActivity(new Intent(MainActivity.this, activitySignUp.class));
            Toast.makeText(MainActivity.this, "Please login first", Toast.LENGTH_SHORT).show();
        }
    }
}