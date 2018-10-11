package com.example.devyatkin.dishes;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
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

        ViewHolder viewHolder;
        if (convertView == null) {
            convertView = inflater.inflate(this.layout, parent, false);
            viewHolder = new ViewHolder(convertView);
            convertView.setTag(viewHolder);
        }else{
            viewHolder = (ViewHolder)convertView.getTag();
        }

        Dish dish = dishes.get(position);

        File imgFile = new File(dish.getImagePath());
        if (imgFile.exists()){
            Bitmap bitmap = BitmapFactory.decodeFile(imgFile.getAbsolutePath());
            viewHolder.dishView.setImageBitmap(bitmap);
        }
        else
            viewHolder.dishView.setImageResource(R.drawable.ic_noimage);

        viewHolder.dishName.setText(dish.getName());

        return convertView;
    }

    private class ViewHolder{
        final ImageView dishView;
        final TextView dishName;

        public ViewHolder(View view){
            dishView = (ImageView)view.findViewById(R.id.dish_image);
            dishName = (TextView)view.findViewById(R.id.dish_name);
        }
    }
}
