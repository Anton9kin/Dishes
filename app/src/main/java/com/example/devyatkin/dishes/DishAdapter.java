package com.example.devyatkin.dishes;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Environment;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.io.File;
import java.util.List;

public class DishAdapter extends RecyclerView.Adapter<DishAdapter.ViewHolder> {

    private LayoutInflater inflater;
    private List<Dish> dishes;

    DishAdapter(Context context, List<Dish> dishes){
        this.dishes = dishes;
        this.inflater = LayoutInflater.from(context);
    }

    @Override
    public DishAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType){
        View view = inflater.inflate(R.layout.list_dish, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(DishAdapter.ViewHolder holder, int position){
        Dish dish = dishes.get(position);

        File imgFile = new File(dish.getPath());
        if (imgFile.exists()){
            Bitmap bitmap = BitmapFactory.decodeFile(imgFile.getAbsolutePath());
            holder.dishView.setImageBitmap(bitmap);
        }
        else
            holder.dishView.setImageResource(R.drawable.ic_noimage);

        holder.dishName.setText(dish.getName());
    }

    @Override
    public int getItemCount(){
        return dishes.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        final ImageView dishView;
        final TextView dishName;

        ViewHolder(View view){
            super(view);
            dishView = (ImageView)view.findViewById(R.id.dish_image);
            dishName = (TextView)view.findViewById(R.id.dish_name);
        }
    }
}
