package com.example.javapassmtdua;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;
import java.util.List;

public class DataAdapter extends RecyclerView.Adapter <DataAdapter.MyViewHolder> {
    private Context context;
    private List<EPLListFood> EPLlistFood;
    private DataAdapterListener listener;

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView tv_number, tv_nameFood;
        public ImageView ivlogo_food;

        boolean isProgressVisible = false;
        public MyViewHolder(@NonNull View view) {
            super(view);
            tv_number = view.findViewById(R.id.tv_number);
            tv_nameFood = view.findViewById(R.id.tv_nameFood);
            ivlogo_food = view.findViewById(R.id.ivlogo_food);
            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    listener.onDataSelected(EPLlistFood.get(getAdapterPosition()));
                }
            });
        }
    }


    public DataAdapter(Context context, ArrayList<EPLListFood> EPLlistFood, DataAdapterListener listener) {
        this.context = context;
        this.EPLlistFood = EPLlistFood;
        this.listener = listener;
    }

    @NonNull
    @Override
    public DataAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_layout, parent, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull DataAdapter.MyViewHolder holder, int position) {
        final EPLListFood movie = this.EPLlistFood.get(position);
        holder.tv_nameFood.setText(movie.getStrName());
        holder.tv_number.setText(movie.getStrNumber());
        Glide.with(holder.itemView.getContext()).load(movie.getStrImage()).into(holder.ivlogo_food);
    }

    @Override
    public int getItemCount() {
        return this.EPLlistFood.size();
    }


    public interface DataAdapterListener {
        void onDataSelected(EPLListFood movie);
    }


}
