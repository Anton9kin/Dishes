package com.example.devyatkin.dishes;

import android.Manifest;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Adapter;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import org.w3c.dom.Text;

import java.io.File;
import java.lang.reflect.Array;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    private static final int REQUEST_PERMISSION_WRITE = 1001;
    private String dir_first;
    private String dir_second;
    private String dir_salad;
    private String dir_snack;
    private String dir_bake;
    private String dir_desert;
    private String dir_drink;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        //check and create(if it's necessary) file system for application
        InitDirectory();

        //choose first category of dishes
        //MenuItem menuItem = (MenuItem)navigationView.getMenu().findItem(R.id.first_dishes);
        //menuItem.setChecked(true);
        //onNavigationItemSelected(menuItem);
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
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
            Toast.makeText(this, "Данная настройка недоступна", Toast.LENGTH_LONG).show();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();
        TextView text = (TextView)findViewById(R.id.content_header);
        RecyclerView listDishes = (RecyclerView) findViewById(R.id.dishes_list);

        if (id == R.id.first_dishes) {
            // Handle the camera action

            List<Dish> dishes = getDishList(dir_first);
            //create adapter
            DishAdapter dishAdapter = new DishAdapter(this, dishes);
            listDishes.setAdapter(dishAdapter);

            text.setText("Первые блюда - " + dishes.size());
        } else if (id == R.id.second_dishes) {
            List<Dish> dishes = getDishList(dir_second);
            //create adapter
            DishAdapter dishAdapter = new DishAdapter(this, dishes);
            listDishes.setAdapter(dishAdapter);

            text.setText("Первые блюда - " + dishes.size());
        } else if (id == R.id.salads) {
            List<Dish> dishes = getDishList(dir_salad);
            //create adapter
            DishAdapter dishAdapter = new DishAdapter(this, dishes);
            listDishes.setAdapter(dishAdapter);

            text.setText("Первые блюда - " + dishes.size());
        } else if (id == R.id.snacks) {
            List<Dish> dishes = getDishList(dir_snack);
            //create adapter
            DishAdapter dishAdapter = new DishAdapter(this, dishes);
            listDishes.setAdapter(dishAdapter);

            text.setText("Первые блюда - " + dishes.size());
        } else if (id == R.id.bakes) {
            List<Dish> dishes = getDishList(dir_bake);
            //create adapter
            DishAdapter dishAdapter = new DishAdapter(this, dishes);
            listDishes.setAdapter(dishAdapter);

            text.setText("Первые блюда - " + dishes.size());
        } else if (id == R.id.deserts) {
            List<Dish> dishes = getDishList(dir_desert);
            //create adapter
            DishAdapter dishAdapter = new DishAdapter(this, dishes);
            listDishes.setAdapter(dishAdapter);

            text.setText("Первые блюда - " + dishes.size());
        } else if (id == R.id.drinks) {
            List<Dish> dishes = getDishList(dir_drink);
            //create adapter
            DishAdapter dishAdapter = new DishAdapter(this, dishes);
            listDishes.setAdapter(dishAdapter);

            text.setText("Первые блюда - " + dishes.size());
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    private List<Dish> getDishList(String dir) {

        List<Dish> dishList = new ArrayList<>();

        List<String> dir_list = getDirList(dir);

        for (String name : dir_list){
            //neccessary get info about dish: name, list of ingridient, cooking, source of image
            Dish dish = new Dish(name, "Soup", "List", "Cook", "/sdcard/image.jpg");
            dishList.add(dish);
        }
        return dishList;
    }


    ArrayList<String> getDirList(String path){
        ArrayList<String> list = new ArrayList<>();

        File dir = new File(Environment.getExternalStorageDirectory(), path);
        File[] dir_List = dir.listFiles();

        for(File f : dir_List) {
            if (f.isFile())
                list.add(f.getName());
        }

        return list;
    }

    //re-init file system
    private boolean checkFilePath(String path){
        File file = new File(Environment.getExternalStorageDirectory(), path);
        if (file.exists()){
            //Toast.makeText(this, "Файл (" + path + ") существует", Toast.LENGTH_LONG).show();
            return true;
        }else{
            //Toast.makeText(this, "Файл (" + path + ") отсутсвует", Toast.LENGTH_LONG).show();
            file.mkdirs();
            return false;
        }
    }

    private void InitDirectory(){
        if (!checkPermissions()){
            Toast.makeText(this, "Нет разрешения", Toast.LENGTH_SHORT).show();
            return;
        }

        String path = getString(R.string.folder_root);
        dir_first = path + "/" + getResources().getString(R.string.folder_first);
        dir_second = path + "/" + getResources().getString(R.string.folder_second);
        dir_salad = path + "/" + getResources().getString(R.string.folder_salad);
        dir_snack = path + "/" + getResources().getString(R.string.folder_snacks);
        dir_bake = path + "/" + getResources().getString(R.string.folder_bakes);
        dir_desert = path + "/" + getResources().getString(R.string.folder_deserts);
        dir_drink = path + "/" + getResources().getString(R.string.folder_drinks);

        String path_images = "/" + getResources().getString(R.string.folder_image);

        checkFilePath(dir_first);
        checkFilePath(dir_first + path_images);
        checkFilePath(dir_second);
        checkFilePath(dir_second + path_images);
        checkFilePath(dir_salad);
        checkFilePath(dir_salad + path_images);
        checkFilePath(dir_snack);
        checkFilePath(dir_snack + path_images);
        checkFilePath(dir_bake);
        checkFilePath(dir_bake + path_images);
        checkFilePath(dir_desert);
        checkFilePath(dir_desert + path_images);
        checkFilePath(dir_drink);
        checkFilePath(dir_drink + path_images);
    }

    private boolean checkPermissions(){

        if(!isExternalStorageReadable() || !isExternalStorageWriteable()){
            Toast.makeText(this, "Внешнее хранилище не доступно", Toast.LENGTH_LONG).show();
            return false;
        }
        int permissionCheck = ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE);
        if(permissionCheck!= PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, REQUEST_PERMISSION_WRITE);
            return false;
        }
        return true;
    }

    // проверяем, доступно ли внешнее хранилище для чтения и записи
    public boolean isExternalStorageWriteable(){
        String state = Environment.getExternalStorageState();
        return  Environment.MEDIA_MOUNTED.equals(state);
    }
    // проверяем, доступно ли внешнее хранилище хотя бы только для чтения
    public boolean isExternalStorageReadable(){
        String state = Environment.getExternalStorageState();
        return  (Environment.MEDIA_MOUNTED.equals(state) ||
                Environment.MEDIA_MOUNTED_READ_ONLY.equals(state));
    }
}
