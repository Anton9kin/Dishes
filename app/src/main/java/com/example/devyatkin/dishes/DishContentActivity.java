package com.example.devyatkin.dishes;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.design.widget.NavigationView;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;

public class DishContentActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dish_content);
        Toolbar contentDishToolbar = findViewById(R.id.content_dish_toolbar);
        setSupportActionBar(contentDishToolbar);

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

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }
}
