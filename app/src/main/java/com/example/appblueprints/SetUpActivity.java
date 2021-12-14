package com.example.appblueprints;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;

import java.util.HashMap;

import de.hdodenhof.circleimageview.CircleImageView;

public class SetUpActivity extends AppCompatActivity {

    private CircleImageView profilePic;
    private Button saveBtn;
    private EditText askName;
    private ProgressBar progressBar;
    private FirebaseAuth auth;
    private StorageReference storageReference;
    private FirebaseFirestore firestore;
    private String Uid;
    private Uri mImageUri;
    private Uri downloadUri = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_set_up);

        profilePic = findViewById(R.id.profilePic);
        saveBtn = findViewById(R.id.saveBtn);
        askName = findViewById(R.id.askName);
        progressBar = findViewById(R.id.progressBar);
        auth = FirebaseAuth.getInstance();

        progressBar.setVisibility(View.INVISIBLE);

        profilePic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M){
                    if (ContextCompat.checkSelfPermission(SetUpActivity.this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                        ActivityCompat.requestPermissions(SetUpActivity.this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},1);
                    }else{
                        CropImage.activity()
                                .setAspectRatio(1,1)
                                .setGuidelines(CropImageView.Guidelines.ON)
                                .start(SetUpActivity.this);
                    }
                }
            }
        });

        saveBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                progressBar.setVisibility(View.VISIBLE);
                String name = askName.getText().toString();
                StorageReference imageRef = storageReference.child("Profile_pics").child(Uid + ".jpg");
                    if (!name.isEmpty() && mImageUri != null)    {
                        imageRef.putFile(mImageUri).addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
                            @Override
                            public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {
                                if (task.isSuccessful()) {
                                    imageRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                        @Override
                                        public void onSuccess(Uri uri) {
                                            saveToFireStore(task, name, imageRef);
                                        }
                                    });

                                } else {
                                    progressBar.setVisibility(View.INVISIBLE);
                                    Toast.makeText(SetUpActivity.this, task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                                }
                            }
                        });
                    } else {
                        progressBar.setVisibility(View.INVISIBLE);
                        Toast.makeText(SetUpActivity.this, "Please Select picture and write your name", Toast.LENGTH_SHORT).show();
                    }
            }
        });
        profilePic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M){

                    if (ContextCompat.checkSelfPermission(SetUpActivity.this , Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED){
                        ActivityCompat.requestPermissions(SetUpActivity.this , new String[]{Manifest.permission.READ_EXTERNAL_STORAGE} , 1);
                    }else{
                        CropImage.activity()
                                .setGuidelines(CropImageView.Guidelines.ON)
                                .setAspectRatio(1,1)
                                .start(SetUpActivity.this);
                    }
                }
            }
        });
    }

    private void saveToFireStore(Task<UploadTask.TaskSnapshot> task, String name, StorageReference imageRef) {
        imageRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(Uri uri) {
                downloadUri = uri;
                HashMap<String , Object> map = new HashMap<>();
                map.put("name",name);
                map.put("image",downloadUri.toString());

                firestore.collection("Users").document(Uid).set(map).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if(task.isSuccessful()){
                            Toast.makeText(SetUpActivity.this, "Profile Settings saved!", Toast.LENGTH_SHORT).show();
                            startActivity(new Intent(SetUpActivity.this,MainActivity.class));
                            finish();
                        }else{
                            Toast.makeText(SetUpActivity.this, task.getException().toString(), Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE){
            CropImage.ActivityResult result = CropImage.getActivityResult(data);
            if(resultCode == RESULT_OK){
                mImageUri = result.getUri();
                profilePic.setImageURI(mImageUri);
            }else if(resultCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE){
                Toast.makeText(SetUpActivity.this, result.getError().getMessage(), Toast.LENGTH_SHORT).show();
            }
        }
    }
}