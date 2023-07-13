package com.example.socialapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.example.socialapp.Adaptars.MesaageAdaptar;
import com.example.socialapp.databinding.ActivityChatDetailBinding;
import com.example.socialapp.models.MessageModel;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.Date;

public class ChatDetailActivity extends AppCompatActivity {


    ActivityChatDetailBinding binding;
    FirebaseDatabase database;
    FirebaseAuth auth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityChatDetailBinding.inflate(getLayoutInflater());

        setContentView(binding.getRoot());

        getSupportActionBar().hide();
        database = FirebaseDatabase.getInstance();
        auth = FirebaseAuth.getInstance();



       final String senderId = auth.getUid();
        String reciverID = getIntent().getStringExtra("userID");
        String profilepic = getIntent().getStringExtra("profilepic");
        String username = getIntent().getStringExtra("userName");

        binding.username.setText(username);
        Picasso.get().load(profilepic).placeholder(R.drawable.useravatar).into(binding.profileImage);


        final ArrayList<MessageModel> messageModelArrayList = new ArrayList<>();
         MesaageAdaptar mesaageAdaptar = new MesaageAdaptar(messageModelArrayList,this,reciverID);

         binding.chatsRecyclerView.setAdapter(mesaageAdaptar);

        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        binding.chatsRecyclerView.setLayoutManager(layoutManager);

        final  String senderRoom = senderId+reciverID;
        final String  recevierRoom = reciverID+senderId;





        database.getReference().child("chats")
                        .child(senderRoom)
                                .addValueEventListener(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(@NonNull DataSnapshot snapshot) {

                                        messageModelArrayList.clear();
                                        for(DataSnapshot snapshot1 : snapshot.getChildren()){

                                            MessageModel model = snapshot1.getValue(MessageModel.class);
                                            model.setMessageid(snapshot1.getKey());
                                            messageModelArrayList.add(model);
                                        }
                                        mesaageAdaptar.notifyDataSetChanged();
                                    }

                                    @Override
                                    public void onCancelled(@NonNull DatabaseError error) {

                                    }
                                });


        binding.btnSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                String message = binding.etmessage.getText().toString();

                final  MessageModel messageModel = new MessageModel(senderId,message);
                messageModel.setTimestamp(new Date().getTime());

                binding.etmessage.setText("");

                database.getReference().child("chats")
                        .child(senderRoom)
                        .push()
                        .setValue(messageModel).addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void unused) {

                                database.getReference()
                                        .child("chats")
                                        .child(recevierRoom)
                                        .push()
                                        .setValue(messageModel).addOnSuccessListener(new OnSuccessListener<Void>() {
                                            @Override
                                            public void onSuccess(Void unused) {

                                            }
                                        });
                            }
                        });

            }
        });

        binding.IVBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ChatDetailActivity.this,MainActivity.class);
                startActivity(intent);
            }
        });

    }
}