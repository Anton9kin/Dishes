package com.example.devyatkin.dishes;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.InputType;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import java.io.File;

import static android.view.View.INVISIBLE;
import static android.view.View.VISIBLE;

public class DishDetail extends AppCompatActivity {

    private String filePath;
    private Dish dish;

    private EditText name;
    private EditText ingredient;
    private EditText cooking;
    private Spinner type;
    private Spinner typeSub;

    private String[] categories = null;
    private String[] subCategories = null;

    private boolean editMode = false;

    private Menu mainMenu;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dish_detail);
        Toolbar contentDishToolbar = findViewById(R.id.content_dish_toolbar);
        setSupportActionBar(contentDishToolbar);

        Bundle arg = getIntent().getExtras();

        if (arg != null){
            editMode = arg.getBoolean("edit");

            filePath = arg.getString("file");

            File file = new File(filePath);

            DishParser parser = new DishParser(this);
            if (parser.parse(file))
                dish = parser.getDish();

            //dish = arg.getParcelable(Dish.class.getSimpleName());

            name = findViewById(R.id.content_dish_name);
            name.setText(dish.getName());

            typeSub = findViewById(R.id.choose_subcategory_dish);
            subCategories = getResources().getStringArray(R.array.second_subcategory_list);

            ArrayAdapter<String> subAdapter = new ArrayAdapter<String>(this, R.layout.dish_category_spinner_item, subCategories);
            subAdapter.setDropDownViewResource(R.layout.dish_category_spinner_dropdown_item);
            typeSub.setAdapter(subAdapter);

            type = findViewById(R.id.choose_category_dish);
            categories = getResources().getStringArray(R.array.category_list);

            ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, R.layout.dish_category_spinner_item, categories);
            adapter.setDropDownViewResource(R.layout.dish_category_spinner_dropdown_item);
            type.setAdapter(adapter);

            type.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                    if (position == 1){
                        typeSub.setVisibility(VISIBLE);
                    }else{
                        typeSub.setVisibility(INVISIBLE);
                    }
                }

                @Override
                public void onNothingSelected(AdapterView<?> parent) {

                }
            });

            getSupportActionBar().setTitle(dish.getType());

            ingredient = findViewById(R.id.content_dish_ingredients);
            ingredient.setText(dish.getIngredient());

            cooking = findViewById(R.id.content_dish_cooking);
            cooking.setText(dish.getCooking());

            ImageView image = findViewById(R.id.content_dish_image);

            dish.setImagePath(DishesFileSystem.getImagePath(dish.getType(), file));

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
        this.mainMenu = menu;
        getMenuInflater().inflate(R.menu.edit_dish, menu);
        enableEdit(editMode);
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

            //TODO: delete file

            DishesFileSystem.deleteFile(dish.getType(), dish.getName());
            super.onBackPressed();
            return true;
        }


        //group menu with SAVE SAVEAS CANCEL
        if (id == R.id.action_dish_save){

            //TODO: file save

            if (name.getText().toString().isEmpty()){
                Toast.makeText(this, "Введите название блюда", Toast.LENGTH_LONG).show();
                return true;
            }

            DishParser parser = new DishParser(this);
            Dish editDish = new Dish();
            editDish.setName(name.getText().toString());
            editDish.setIngredient(ingredient.getText().toString());
            editDish.setCooking(cooking.getText().toString());

            if (typeSub.getVisibility() == VISIBLE) {
                editDish.setType(typeSub.getSelectedItem().toString());
            }
            else {
                editDish.setType(type.getSelectedItem().toString());
            }

            editDish.setImagePath(Environment.getExternalStorageDirectory() + "/" + DishesFileSystem.getPathByMenu(editDish.getType()));

            parser.save(editDish);

            getSupportActionBar().setTitle(editDish.getType());

            Toast.makeText(this, dish.getName() + " успешно сохранено", Toast.LENGTH_LONG).show();
            enableEdit(false);
            return true;
        }

        if (id == R.id.action_dish_cancel){

            //TODO: cancel changes

            name.setText(dish.getName());
            ingredient.setText(dish.getIngredient());
            cooking.setText(dish.getCooking());

            Toast.makeText(this, "Изменения отменены", Toast.LENGTH_LONG).show();
            enableEdit(false);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

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
            typeSub.setVisibility(INVISIBLE);

            //hide menu with editing
            mainMenu.setGroupVisible(R.id.action_group_saving, false);
            //show menu with adding
            mainMenu.setGroupVisible(R.id.action_group_editing, true);
        }

        name.setRawInputType(input_type);
        name.setBackgroundResource(color);

        type.setBackgroundResource(color);
        typeSub.setBackgroundResource(color);

        ingredient.setRawInputType(input_type);
        ingredient.setBackgroundResource(color);

        cooking.setRawInputType(input_type);
        cooking.setBackgroundResource(color);
    }
}
