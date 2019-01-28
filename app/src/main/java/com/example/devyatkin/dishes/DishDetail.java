package com.example.devyatkin.dishes;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Point;
import android.media.MediaScannerConnection;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.InputType;
import android.view.Display;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.Toast;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static android.view.View.INVISIBLE;
import static android.view.View.VISIBLE;

public class DishDetail extends AppCompatActivity {

    private static final int CAMERA = 1;
    private static final int GALLERY = 2;
    private String filePath;
    private Dish dish;
    private Dish editDish;

    private EditText name;
    private ImageView image;
    private EditText ingredient;
    private EditText cooking;
    private Spinner type;
    private Spinner typeSub;
    private Bitmap bitmap;

    private String[] categories = null;
    private String[] subCategories = null;

    private boolean editMode = false;

    private Menu mainMenu;
    private MenuItem favItem;

    private boolean isFavorite = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dish_detail);
        Toolbar contentDishToolbar = findViewById(R.id.content_dish_toolbar);
        setSupportActionBar(contentDishToolbar);

        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);

        Bundle arg = getIntent().getExtras();

        if (arg != null){
            editMode = arg.getBoolean("edit");

            filePath = arg.getString("file");
            File file = new File(filePath);

            if (!filePath.isEmpty()){
                DishParser parser = new DishParser(this);
                if (parser.parse(file))
                    dish = parser.getDish();
            }else{
                dish = new Dish();
            }

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

            image = findViewById(R.id.content_dish_image);

            if (!filePath.isEmpty())
                dish.setImagePath(DishesFileSystem.getImagePath(file));

            File imgFile = new File(dish.getImagePath());
            if (imgFile.exists()){
                Bitmap bitmap = BitmapFactory.decodeFile(imgFile.getAbsolutePath());
                image.setImageBitmap(bitmap);
                image.setClipToOutline(true);
                image.setBackgroundResource(R.drawable.rectangle_arround);
            }
            else
                image.setImageResource(R.drawable.ic_noimage);
        }

        List<View> listView = new ArrayList<>();
        listView.add(image);
        autoLayout(listView);
    }

    private void autoLayout(List<View> listView) {
        //change image's height for adaption for different screens

        Display display = getWindowManager().getDefaultDisplay();

        Point size = new Point();
        display.getSize(size);

        //common height is equal 40% from screen width
        int common_height = (int) (size.x * 0.4);
        int common_width = (int) (size.x * 0.6);

        //set common height to all images
        for (View view:
                listView) {
            view.getLayoutParams().height = common_height;
            view.getLayoutParams().width = common_width;
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

        favItem = menu.findItem(R.id.action_dish_favorite);

        //favorite
        List<String> favoriteList = DishesFileSystem.getListFavorites();
        if (favoriteList == null) {
            favItem.setTitle(R.string.action_dish_favorite);
            isFavorite = false;
            return true;
        }

        for (String fav : favoriteList){
            if (fav.compareTo(filePath) == 0){
                favItem.setTitle(R.string.action_dish_infavorite);
                isFavorite = true;
            }
        }

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

            AlertDialog.Builder deleteDialog = new AlertDialog.Builder(this);
            deleteDialog.setTitle(R.string.deleteDialog_message);
            deleteDialog.setMessage(dish.getName());
            deleteDialog.setPositiveButton(R.string.deleteDialog_OK, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    DishesFileSystem.deleteFile(dish.getType(), dish.getName());
                    onBackPressed();
                }
            });
            deleteDialog.setNegativeButton(R.string.deleteDialog_Cancel, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {

                }
            });

            deleteDialog.show();
            return true;
        }
        if (id == R.id.action_dish_favorite){
            //TODO: add/remove file to/from favorites
            if (isFavorite == false) {
                DishesFileSystem.saveFavorite(filePath);
                isFavorite = true;
                favItem.setTitle(R.string.action_dish_infavorite);
            }
            else {
                DishesFileSystem.removeFavorite(filePath);
                isFavorite = false;
                favItem.setTitle(R.string.action_dish_favorite);
            }
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
            editDish = new Dish();
            editDish.setName(name.getText().toString());
            editDish.setIngredient(ingredient.getText().toString());
            editDish.setCooking(cooking.getText().toString());

            if (typeSub.getVisibility() == VISIBLE) {
                editDish.setType(typeSub.getSelectedItem().toString());
            }
            else {
                editDish.setType(type.getSelectedItem().toString());
            }
            editDish.setImagePath(Environment.getExternalStorageDirectory() +
                    "/" + DishesFileSystem.getPathByMenu(editDish.getType()) +
                    "/" + getResources().getString(R.string.folder_image) +
                    "/" + editDish.getName() + ".jpg");
            saveImage(bitmap);

            parser.save(editDish);

            getSupportActionBar().setTitle(editDish.getType());

            Toast.makeText(this, editDish.getName() + " успешно сохранено", Toast.LENGTH_LONG).show();
            enableEdit(false);
            return true;
        }

        if (id == R.id.action_dish_cancel){

            //TODO: cancel changes

            name.setText(dish.getName());
            ingredient.setText(dish.getIngredient());
            cooking.setText(dish.getCooking());

            File imgFile = new File(dish.getImagePath());
            if (imgFile.exists()){
                Bitmap bitmap = BitmapFactory.decodeFile(imgFile.getAbsolutePath());
                image.setImageBitmap(bitmap);
            }
            else
                image.setImageResource(R.drawable.ic_noimage);

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

            image.setOnClickListener(new View.OnClickListener(){
                @Override
                public void onClick(View v){
                    showPictureDialog();
                }
            });
        }
        else{
            type.setVisibility(INVISIBLE);
            typeSub.setVisibility(INVISIBLE);

            //hide menu with editing
            mainMenu.setGroupVisible(R.id.action_group_saving, false);
            //show menu with adding
            mainMenu.setGroupVisible(R.id.action_group_editing, true);

            image.setOnClickListener(new View.OnClickListener(){
                @Override
                public void onClick(View v){
                }
            });
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


    private void showPictureDialog(){
        AlertDialog.Builder pictureDialog = new AlertDialog.Builder(this);
        pictureDialog.setTitle("Выберите действие");

        pictureDialog.setItems(getResources().getStringArray(R.array.chooseSrcPhoto), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                switch(which){
                    case 0: choosePhotoFromGallery(); break;
                    case 1: takePhotoFromCamera(); break;
                    case 2: image.setImageResource(R.drawable.ic_noimage); bitmap = null; break;
                }
            }
        });

        pictureDialog.show();
    }

    private void takePhotoFromCamera() {
        Intent gallery = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        startActivityForResult(gallery, CAMERA);
    }

    private void choosePhotoFromGallery() {
        Intent gallery = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(gallery, GALLERY);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data){
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == this.RESULT_CANCELED)
            return;

        if (requestCode == GALLERY){
            if (data != null){
                Uri contentUri = data.getData();
                try{
                    bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), contentUri);
                    image.setImageBitmap(bitmap);
                } catch (IOException e){
                    e.printStackTrace();
                    Toast.makeText(DishDetail.this, "Ошибка!", Toast.LENGTH_SHORT).show();
                }
            }
        }else if (requestCode == CAMERA){
            bitmap = (Bitmap) data.getExtras().get("data");
            image.setImageBitmap(bitmap);
        }
    }

    private String saveImage(Bitmap bm) {

        String imagePath = editDish.getImagePath();


        if (bm == null)
        {
            File f = new File(imagePath);
            if (f.exists()){
                f.delete();
            }
            return "";
        }

        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        bm.compress(Bitmap.CompressFormat.JPEG, 100, bytes);

        try{
            File f = new File(imagePath);
            if (f.exists()){
                f.delete();
            }
            f.createNewFile();

            FileOutputStream fos = new FileOutputStream(f);
            fos.write(bytes.toByteArray());
            MediaScannerConnection.scanFile(this, new String[]{f.getPath()}, new String[]{"image/jpeg"}, null);
            fos.close();

            return f.getAbsolutePath();
        }catch (IOException e){
            e.printStackTrace();
        }

        return "";
    }
}
