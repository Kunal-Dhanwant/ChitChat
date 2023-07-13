package com.example.socialapp;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.example.socialapp.databinding.ActivitySettingBinding;
import com.example.socialapp.models.Users;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

import java.util.HashMap;

public class SettingActivity extends AppCompatActivity {



    FirebaseDatabase database;

    FirebaseStorage storage;
    FirebaseAuth auth;
    ActivitySettingBinding binding;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivitySettingBinding.inflate(getLayoutInflater());
        getSupportActionBar().hide();

        setContentView(binding.getRoot());


        auth = FirebaseAuth.getInstance();
        database = FirebaseDatabase.getInstance();
        storage = FirebaseStorage.getInstance();






      binding.IVBack.setOnClickListener(new View.OnClickListener() {
          @Override
          public void onClick(View v) {
              Intent intent1= new Intent(SettingActivity.this,MainActivity.class);
              startActivity(intent1);
          }
      });



      binding.btnSave.setOnClickListener(new View.OnClickListener() {
          @Override
          public void onClick(View v) {

              String status = binding.etAbout.getText().toString();
              String name = binding.etUsername.getText().toString();

              HashMap<String,Object> obj = new HashMap<>();



              obj.put("username",name);
              obj.put("status",status);

              database.getReference().child("Users").child(auth.getUid()).updateChildren(obj);
          }
      });



      database.getReference().child("Users").child(auth.getUid()).addListenerForSingleValueEvent(new ValueEventListener() {
          @Override
          public void onDataChange(@NonNull DataSnapshot snapshot) {
              Users users = snapshot.getValue(Users.class);

              Picasso.get().load(users.getProfilepic()).placeholder(R.drawable.useravatar).into(binding.profileImage);


              binding.etUsername.setText(users.getUsername());
              binding.etAbout.setText(users.getStatus());

          }

          @Override
          public void onCancelled(@NonNull DatabaseError error) {

          }
      });


      binding.addProfile.setOnClickListener(new View.OnClickListener() {
          @Override
          public void onClick(View v) {

              Intent intent = new Intent();
              intent.setAction(Intent.ACTION_GET_CONTENT);
              intent.setType("image/*");
              startActivityForResult(intent,33);

          }
      });
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);


        if(data.getData() != null){
            Uri sfile = data.getData();

            binding.profileImage.setImageURI(sfile);

            final StorageReference reference = storage.getReference().child("profile_image")
                    .child(auth.getUid());


            reference.putFile(sfile).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {




                    reference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                        @Override
                        public void onSuccess(Uri uri) {
                            database.getReference().child("Users").child(auth.getUid()).child("profilepic")
                                    .setValue(uri.toString());

                            Toast.makeText(SettingActivity.this, "Profile Updated!!!!", Toast.LENGTH_SHORT).show();



                        }
                    });
                }
            });
        }
    }
}