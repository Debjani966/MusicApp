package com.debjanimandal.musicapp;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class MusicAdapter extends RecyclerView.Adapter<MusicAdapter.MusicViewHolder>{

    ArrayList<String> list;
    Context mContext;

    public MusicAdapter(ArrayList<String> list, Context mContext) {
        this.list = list;
        this.mContext = mContext;
    }

    @NonNull
    @Override
    public MusicViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.card_music,parent,false);
        return new MusicViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MusicViewHolder holder, @SuppressLint("RecyclerView") int position) {

        String filepath= list.get(position);
        Log.e("filepath : ",filepath);
        String title=filepath.substring(filepath.lastIndexOf("/")+1);
        holder.textViewFileName.setText(title);
        holder.cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(mContext,MainActivity.class);
                intent.putExtra("title14",title);
                intent.putExtra("filepath14",filepath);
                intent.putExtra("position14",position);
                intent.putExtra("list14",list);
                mContext.startActivity(intent);

            }
        });
    }
    @Override
   public int getItemCount() {
        return list.size();
    }

    public class MusicViewHolder extends RecyclerView.ViewHolder{

        private TextView textViewFileName;
        private CardView cardView;

        public MusicViewHolder(@NonNull View itemView) {
            super(itemView);
            textViewFileName=itemView.findViewById(R.id.textViewFileName);
            cardView=itemView.findViewById(R.id.cardView);
        }
    }
}
