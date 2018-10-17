package com.example.devyatkin.dishes;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

public class CategoryAdapter extends ArrayAdapter<DishCategory> {
    private LayoutInflater inflater;
    private int layout;
    private List<DishCategory> categories;

    public CategoryAdapter(Context context, int resource, List<DishCategory> categories){
        super(context, resource, categories);
        this.categories = categories;
        this.layout = resource;
        this.inflater = LayoutInflater.from(context);
    }

    public View getView(int position, View convertView, ViewGroup parent){

        ViewHolder viewHolder;
        if (convertView == null) {
            convertView = inflater.inflate(this.layout, parent, false);
            viewHolder = new CategoryAdapter.ViewHolder(convertView);
            convertView.setTag(viewHolder);
        }else{
            viewHolder = (CategoryAdapter.ViewHolder)convertView.getTag();
        }

        DishCategory category = categories.get(position);

        viewHolder.categoryView.setImageResource(category.getIconResource());
        viewHolder.categoryName.setText(category.getName());

        return convertView;
    }

    private class ViewHolder{
        final ImageView categoryView;
        final TextView categoryName;

        public ViewHolder(View view){
            categoryView = view.findViewById(R.id.category_image);
            categoryName = view.findViewById(R.id.category_name);
        }
    }
}
