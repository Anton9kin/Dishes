package com.example.devyatkin.dishes;

import android.content.Intent;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

public class DishEdit extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dish_edit);
        Toolbar contentDishToolbar = findViewById(R.id.edit_dish_toolbar);
        setSupportActionBar(contentDishToolbar);

        Bundle arg = getIntent().getExtras();
        final Dish dish;

        if (arg != null){
            dish = arg.getParcelable(Dish.class.getSimpleName());

            EditText edit_name = findViewById(R.id.edit_name);
            edit_name.setText(dish.getName());

            EditText edit_type = findViewById(R.id.edit_type);
            edit_type.setText(dish.getType());

            EditText edit_ingredients = findViewById(R.id.edit_ingredients);
            edit_ingredients.setText(dish.getIngredient());

            EditText edit_cooking = findViewById(R.id.edit_cooking);
            edit_cooking.setText(dish.getCooking());


        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.apply_dish, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_dish_apply) {
            Toast.makeText(this, "<" + getResources().getString(R.string.action_dish_apply) + "> не доступно", Toast.LENGTH_LONG).show();
            return true;
        }
        if (id == R.id.action_dish_cancel) {
            Toast.makeText(this, "<" + getResources().getString(R.string.action_dish_cancel) + "> не доступно", Toast.LENGTH_LONG).show();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

}
