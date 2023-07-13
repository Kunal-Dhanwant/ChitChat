package com.example.socialapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.example.socialapp.Adaptars.MesaageAdaptar;
import com.example.socialapp.databinding.ActivityGroupChatBinding;
import com.example.socialapp.models.MessageModel;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Date;

public class GroupChatActivity extends AppCompatActivity {


    ActivityGroupChatBinding binding;
    FirebaseDatabase database;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityGroupChatBinding.inflate(getLayoutInflater());

        setContentView(binding.getRoot());

        getSupportActionBar().hide();


        database = FirebaseDatabase.getInstance();

        binding.IVBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                Intent intent = new Intent(GroupChatActivity.this,MainActivity.class);
                startActivity(intent);
            }
        });




        final ArrayList<MessageModel> messageModelArrayList = new ArrayList<>();
        final String senderID =FirebaseAuth.getInstance().getUid();


        MesaageAdaptar adaptar = new MesaageAdaptar(messageModelArrayList,this);
        binding.chatsRecyclerView.setAdapter(adaptar);

        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        binding.chatsRecyclerView.setLayoutManager(layoutManager);





        binding.username.setText("Friend's Group" );

        database.getReference().child("group chats")

                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {

                        messageModelArrayList.clear();
                        for(DataSnapshot snapshot1 : snapshot.getChildren()){

                            MessageModel model = snapshot1.getValue(MessageModel.class);

                            messageModelArrayList.add(model);
                        }
                        adaptar.notifyDataSetChanged();
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });



        binding.btnSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String message = binding.etmessage.getText().toString();


                final MessageModel messageModel = new MessageModel(senderID,message);
                messageModel.setTimestamp(new Date().getTime());


                binding.etmessage.setText("");
                database.getReference().child("group chats").push().setValue(messageModel)
                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void unused) {

                            }
                        });
            }
        });



    }
}