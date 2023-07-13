package com.example.socialapp.Adaptars;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.socialapp.R;
import com.example.socialapp.models.MessageModel;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

public class MesaageAdaptar extends  RecyclerView.Adapter {

    ArrayList<MessageModel> messageModelArrayList;
    Context context;
    String recID;

    public MesaageAdaptar(ArrayList<MessageModel> messageModelArrayList, Context context) {
        this.messageModelArrayList = messageModelArrayList;
        this.context = context;
    }

    public MesaageAdaptar(ArrayList<MessageModel> messageModelArrayList, Context context, String recID) {
        this.messageModelArrayList = messageModelArrayList;
        this.context = context;
        this.recID = recID;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        if(viewType== SENDER_VIEWTYPE){
            View view = LayoutInflater.from(context).inflate(R.layout.samle_sender,parent,false);
            return new SenderViewHolder(view);
        }else{
            View view = LayoutInflater.from(context).inflate(R.layout.sample_recevier,parent,false);
            return new RecvierViewHolder(view);
        }

    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {

        MessageModel messageModel = messageModelArrayList.get(position);



        holder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {

                new AlertDialog.Builder(context).setTitle("Delete").setMessage("Are You sure you want to delete this message?")
                        .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {


                                FirebaseDatabase database = FirebaseDatabase.getInstance();

                                String senderroom = FirebaseAuth.getInstance().getUid()+ recID;
                                database.getReference().child("chats").child(senderroom).child(messageModel.getMessageid()).setValue(null);

                            }
                        }).setNegativeButton("No", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {

                            }
                        }).show();

                return false;
            }
        });








        if(holder.getClass()==SenderViewHolder.class){
            ((SenderViewHolder)holder).senderMsg.setText(messageModel.getMessage());
         // ((SenderViewHolder)holder).senderTime.setText((int) messageModel.getTimestamp());

        }else{
            ((RecvierViewHolder)holder).recevierMsg.setText(messageModel.getMessage());
         //   ((RecvierViewHolder)holder).recevierTime.setText((int) messageModel.getTimestamp());
        }


    }

    @Override
    public int getItemCount() {
        return messageModelArrayList.size();
    }



    int SENDER_VIEWTYPE=1;
    int RECEVIER_VIEW_TYPE=2;



    @Override
    public int getItemViewType(int position) {

        if(messageModelArrayList.get(position).getuID().equals(FirebaseAuth.getInstance().getUid())){

            return SENDER_VIEWTYPE;
        }else{
            return  RECEVIER_VIEW_TYPE;
        }


    }

    public class RecvierViewHolder extends RecyclerView.ViewHolder{

        TextView recevierMsg,recevierTime;


        public RecvierViewHolder(@NonNull View itemView) {
            super(itemView);

            recevierMsg = itemView.findViewById(R.id.recveirmessgage);
            recevierTime = itemView.findViewById(R.id.receviertime);

        }
    }

    public class SenderViewHolder extends RecyclerView.ViewHolder{


        TextView senderMsg,senderTime;

        public SenderViewHolder(@NonNull View itemView) {
            super(itemView);

            senderMsg = itemView.findViewById(R.id.sendertext);
            senderTime = itemView.findViewById(R.id.sendertime);

        }
    }
}
