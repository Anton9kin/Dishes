package com.example.devyatkin.dishes;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Environment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.io.File;
import java.util.List;

public class DishAdapter extends ArrayAdapter<Dish> {

    private LayoutInflater inflater;
    private int layout;
    private List<Dish> dishes;

    public DishAdapter(Context context, int resource, List<Dish> dishes){
        super(context, resource, dishes);
        this.dishes = dishes;
        this.layout = resource;
        this.inflater = LayoutInflater.from(context);
    }

    public View getView(int position, View convertView, ViewGroup parent){
        View view = inflater.inflate(this.layout, parent, false);

        ImageView dishView = (ImageView)view.findViewById(R.id.dish_image);
        TextView dishName = (TextView)view.findViewById(R.id.dish_name);

        Dish dish = dishes.get(position);

        File imgFile = new File(dish.getPath());
        if (imgFile.exists()){
            Bitmap bitmap = BitmapFactory.decodeFile(imgFile.getAbsolutePath());
            dishView.setImageBitmap(bitmap);
        }
        else
            dishView.setImageResource(R.drawable.ic_noimage);

        dishName.setText(dish.getName());

        return view;
    }
}
