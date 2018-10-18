package com.example.devyatkin.dishes;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.InputType;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import java.io.File;

import static android.view.View.INVISIBLE;
import static android.view.View.VISIBLE;

public class DishDetail extends AppCompatActivity {

    private Dish dish;

    private EditText name;
    private EditText ingredient;
    private EditText cooking;
    private Spinner type;

    private String[] categories;

    private boolean editMode = false;

    private Menu mainMenu;

    private void enableEdit(boolean enable){

        int input_type = InputType.TYPE_NULL;
        int color = R.color.colorAlpha;

        if (enable){
            input_type = InputType.TYPE_CLASS_TEXT;
            color = R.color.colorEdit;
            type.setVisibility(VISIBLE);

            //show menu with editing
            mainMenu.setGroupVisible(R.id.action_group_saving, true);
            //hide menu with adding
            mainMenu.setGroupVisible(R.id.action_group_editing, false);

        }
        else{
            type.setVisibility(INVISIBLE);

            //hide menu with editing
            mainMenu.setGroupVisible(R.id.action_group_saving, false);
            //show menu with adding
            mainMenu.setGroupVisible(R.id.action_group_editing, true);
        }

        name.setRawInputType(input_type);
        name.setBackgroundResource(color);

        type.setBackgroundResource(color);

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

            type = findViewById(R.id.choose_category_dish);
            categories = getResources().getStringArray(R.array.category_list);

            ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, R.layout.dish_category_spinner_item, categories);
            adapter.setDropDownViewResource(R.layout.dish_category_spinner_dropdown_item);
            type.setAdapter(adapter);

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

            //enableEdit(false);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        this.mainMenu = menu;
        getMenuInflater().inflate(R.menu.edit_dish, menu);
        enableEdit(false);
        MenuItem add = menu.findItem(R.id.action_dish_add);
        add.setVisible(false);
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

        //group menu with EDIT REMOVE

        if (id == R.id.action_dish_edit) {
            enableEdit(true);
            return true;
        }
        if (id == R.id.action_dish_delete) {
            Toast.makeText(this, "<" + getResources().getString(R.string.action_dish_delete) + "> не доступно", Toast.LENGTH_LONG).show();
            return true;
        }


        //group menu with SAVE SAVEAS CANCEL
        if (id == R.id.action_dish_save){

            //TODO: file save

            Toast.makeText(this, dish.getName() + " успешно сохранено", Toast.LENGTH_LONG).show();
            enableEdit(false);
            return true;
        }

        if (id == R.id.action_dish_saveAs){

            //TODO: file save as

            Toast.makeText(this, dish.getName() + " успешно пересохранено", Toast.LENGTH_LONG).show();
            enableEdit(false);
            return true;
        }

        if (id == R.id.action_dish_cancel){

            //TODO: cancel changes

            Toast.makeText(this, "Изменения отменены", Toast.LENGTH_LONG).show();
            enableEdit(false);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
