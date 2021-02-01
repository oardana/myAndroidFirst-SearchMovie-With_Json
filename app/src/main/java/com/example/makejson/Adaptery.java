package com.example.makejson;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.util.List;

public class Adaptery extends RecyclerView.Adapter<Adaptery.MyViewHolder> {

    private Context MContext;
    private List<MovieModelClass> MData;

    public Adaptery(Context MContext, List<MovieModelClass> MData) {
        this.MContext = MContext;
        this.MData = MData;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View v;
        LayoutInflater inflater = LayoutInflater.from(MContext);
        v = inflater.inflate(R.layout.movie_item,parent,false);
        return new MyViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {

         holder.id.setText(MData.get(position).getId());
         holder.name.setText(MData.get(position).getName());
         // using Glide make Image
        Glide.with(MContext).load(MData.get(position).getImg()).into(holder.img);
    }

    @Override
    public int getItemCount() {
        return MData.size();
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder {
        TextView id;
        TextView name;
        ImageView img;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            id = itemView.findViewById(R.id.id_txt);
            name = itemView.findViewById(R.id.name_txt);
            img = itemView.findViewById(R.id.img);

        }
    }
}
