package com.example.appblueprints;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.okhttp.internal.http.OkHeaders;
import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;

import java.util.HashMap;

public class postActivity extends AppCompatActivity {
    private Button postBtn;
    private ImageView post;
    private ProgressBar postProgressbar;
    private FirebaseAuth auth;
    private EditText captions;
    private FirebaseFirestore firestore;
    private Uri postImageUri = null;
    private StorageReference storageReference;
    private String currentUserId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post);
        postBtn = findViewById(R.id.addPostBtn);
        post = findViewById(R.id.imagePost);
        captions = findViewById(R.id.addCaptions);
        postProgressbar = findViewById(R.id.postProgressBar);

        postProgressbar.setVisibility(View.INVISIBLE);

        auth = FirebaseAuth.getInstance();
        storageReference = FirebaseStorage.getInstance().getReference();
        currentUserId = auth.getCurrentUser().getUid();
        firestore = FirebaseFirestore.getInstance();

        postBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                postProgressbar.setVisibility(View.VISIBLE);
                String caption = captions.getText().toString();
                if (!caption.isEmpty() && postImageUri != null) {
                    StorageReference postRef = storageReference.child("postImages").child(FieldValue.serverTimestamp().toString()+".jpg");
                    postRef.putFile(postImageUri).addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {
                            if (task.isSuccessful()) {
                                postRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                    @Override
                                    public void onSuccess(Uri uri) {
                                        HashMap<String, Object> postMap = new HashMap<>();
                                        postMap.put("image",uri.toString());
                                        postMap.put("user", currentUserId);
                                        postMap.put("caption",caption);
                                        postMap.put("time",FieldValue.serverTimestamp());

                                        firestore.collection("Posts").add(postMap).addOnCompleteListener(new OnCompleteListener<DocumentReference>() {
                                            @Override
                                            public void onComplete(@NonNull Task<DocumentReference> task) {
                                                if (task.isSuccessful()) {
                                                    Toast.makeText(postActivity.this, "Posted Successfully!", Toast.LENGTH_SHORT).show();
                                                    startActivity(new Intent(postActivity.this, MainActivity.class));
                                                    finish();
                                                }else{
                                                    Toast.makeText(postActivity.this, task.getException().toString(), Toast.LENGTH_SHORT).show();
                                                }
                                            }
                                        });
                                    }
                                });
                            }else{
                                Toast.makeText(postActivity.this, task.getException().toString(), Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                }else{
                Toast.makeText(postActivity.this, "Please add image and captions", Toast.LENGTH_SHORT).show();
                }
                postProgressbar.setVisibility(View.INVISIBLE);
            }
        });

        post.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                CropImage.activity()
                        .setGuidelines(CropImageView.Guidelines.ON)
                        .setAspectRatio(3,2)
                        .setMinCropResultSize(512,512)
                        .start(postActivity.this);
            }
        });

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE){
            CropImage.ActivityResult result = CropImage.getActivityResult(data);
            if(resultCode == RESULT_OK){
                postImageUri = result.getUri();
                post.setImageURI(postImageUri);
            }else if(resultCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE){
                Toast.makeText(postActivity.this, result.getError().toString(), Toast.LENGTH_SHORT).show();
            }
        }
    }
}