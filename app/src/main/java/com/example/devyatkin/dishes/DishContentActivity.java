package com.example.devyatkin.dishes;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import java.io.File;

public class DishContentActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dish_content);

        Bundle arg = getIntent().getExtras();
        final Dish dish;

        if (arg != null){
            dish = arg.getParcelable(Dish.class.getSimpleName());

            TextView name = findViewById(R.id.content_dish_name);
            name.setText(dish.getName());

            TextView ingredient = findViewById(R.id.content_dish_ingredients);
            ingredient.setText(dish.getIngredient());

            TextView cooking = findViewById(R.id.content_dish_cooking);
            cooking.setText(dish.getCooking());

            ImageView image = findViewById(R.id.content_dish_image);

            File imgFile = new File(dish.getImagePath());
            if (imgFile.exists()){
                Bitmap bitmap = BitmapFactory.decodeFile(imgFile.getAbsolutePath());
                image.setImageBitmap(bitmap);
            }
            else
                image.setImageResource(R.drawable.ic_noimage);
        }

    }
}
