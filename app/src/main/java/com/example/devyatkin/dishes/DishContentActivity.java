package com.example.devyatkin.dishes;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
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
        getMenuInflater().inflate(R.menu.edit_dish, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            Toast.makeText(this, "<" + getResources().getString(R.string.action_settings) + "> не доступно", Toast.LENGTH_LONG).show();
            return true;
        }
        if (id == R.id.action_dish_edit) {
            Intent intent = new Intent(this, DishEdit.class);
            startActivity(intent);
            //Toast.makeText(this, "<" + getResources().getString(R.string.action_dish_edit) + "> не доступно", Toast.LENGTH_LONG).show();
            return true;
        }
        if (id == R.id.action_dish_delete) {
            Toast.makeText(this, "<" + getResources().getString(R.string.action_dish_delete) + "> не доступно", Toast.LENGTH_LONG).show();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
