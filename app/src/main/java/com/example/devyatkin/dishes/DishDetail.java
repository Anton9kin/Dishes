package com.example.devyatkin.dishes;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.InputType;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import java.io.File;

public class DishDetail extends AppCompatActivity {

    private Dish dish;

    private EditText name;
    private EditText ingredient;
    private EditText cooking;


    private void enableEdit(boolean enable){

        int input_type = InputType.TYPE_NULL;
        int color = R.color.colorAccentAlpha;

        if (enable){
            input_type = InputType.TYPE_CLASS_TEXT;
            color = R.color.colorEdit;

        }
        name.setRawInputType(input_type);
        name.setBackgroundResource(color);

        ingredient.setRawInputType(input_type);
        ingredient.setBackgroundResource(color);

        cooking.setRawInputType(input_type);
        cooking.setBackgroundResource(color);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dish_detail);
        Toolbar contentDishToolbar = findViewById(R.id.content_dish_toolbar);
        setSupportActionBar(contentDishToolbar);

        Bundle arg = getIntent().getExtras();

        if (arg != null){
            dish = arg.getParcelable(Dish.class.getSimpleName());

            name = findViewById(R.id.content_dish_name);
            name.setText(dish.getName());

            ingredient = findViewById(R.id.content_dish_ingredients);
            ingredient.setText(dish.getIngredient());

            cooking = findViewById(R.id.content_dish_cooking);
            cooking.setText(dish.getCooking());

            ImageView image = findViewById(R.id.content_dish_image);

            File imgFile = new File(dish.getImagePath());
            if (imgFile.exists()){
                Bitmap bitmap = BitmapFactory.decodeFile(imgFile.getAbsolutePath());
                image.setImageBitmap(bitmap);
            }
            else
                image.setImageResource(R.drawable.ic_noimage);

            enableEdit(false);
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
            enableEdit(true);
            //Intent intent = new Intent(this, DishEdit.class);
            //intent.putExtra(Dish.class.getSimpleName(), dish);
            //startActivity(intent);
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
