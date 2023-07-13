package com.example.socialapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.example.socialapp.databinding.ActivitySignInBinding;
import com.example.socialapp.databinding.ActivitySignupBinding;
import com.example.socialapp.models.Users;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;


public class signupActivity<FirebaseDatabase> extends AppCompatActivity {

    ActivitySignupBinding binding;
    private FirebaseAuth auth;



    com.google.firebase.database.FirebaseDatabase database;

    ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivitySignupBinding.inflate(getLayoutInflater());

        setContentView(binding.getRoot());
        getSupportActionBar().hide();

        auth = FirebaseAuth.getInstance();
        database = com.google.firebase.database.FirebaseDatabase.getInstance();

        progressDialog = new ProgressDialog(signupActivity.this);

        binding.btnSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                progressDialog.setTitle("Creating User");
                progressDialog.setMessage("please Wait!..");
                progressDialog.create();
                progressDialog.show();
                auth.createUserWithEmailAndPassword(binding.etEmail.getText().toString(),binding.etPassword.getText().toString()).
                        addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                            @Override
                            public  void onComplete(@NonNull Task<AuthResult> task) {

                                progressDialog.dismiss();
                                if(task.isSuccessful()){

                                    Users user = new Users(binding.etUsername.getText().toString(),binding.etEmail.getText().toString(),binding.etPassword.getText().toString());
                                    String id = task.getResult().getUser().getUid();

                                    database.getReference().child("Users").child(id).setValue(user);
                                    Toast.makeText(signupActivity.this,"User Created Sussesful",Toast.LENGTH_SHORT).show();
                                }else{
                                    Toast.makeText(signupActivity.this,task.getException().getMessage().toString(),Toast.LENGTH_SHORT).show();
                                }

                            }
                        }
                );
            }
        });

        binding.tvAlredyHaveAccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(signupActivity.this,SignIn_Activity.class);
                startActivity(intent);
            }
        });

    }
}