package com.example.devyatkin.dishes;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    private static final String SHOW_DISH_CONTENT = "com.example.devyatkin.SHOW_DISH_CONTENT";

    private TextView text;
    private ListView listDishes;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        //get content header and dishesList
        text = findViewById(R.id.content_header);
        listDishes = findViewById(R.id.dishes_list);

        //give context to DishesFileSystem
        DishesFileSystem.setContext(this);
        //check and create(if it's necessary) file system for application
        DishesFileSystem.InitDirectory();

    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
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
            Toast.makeText(this, "<" + getResources().getString(R.string.action_settings) + "> не доступно", Toast.LENGTH_LONG).show();
            return true;
        }
        if (id == R.id.action_dish_add) {
            Intent intent = new Intent(this, DishEdit.class);
            startActivity(intent);
            //Toast.makeText(this, "<" + getResources().getString(R.string.action_dish_add) + "> не доступно", Toast.LENGTH_LONG).show();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }


    //switch to DishContentActivity
    private void loadDishContentActivity(Dish dish){
        //load DishContentActivity
        Intent intent = new Intent(SHOW_DISH_CONTENT);
        intent.putExtra(Dish.class.getSimpleName(), dish);
        startActivity(intent);
    }

    private void createDishesList(int menu){
        List<Dish> dishes = getDishList(menu);
        //create adapter
        DishAdapter dishAdapter = new DishAdapter(this, R.layout.list_dish, dishes);
        listDishes.setAdapter(dishAdapter);

        // слушатель выбора в списке
        listDishes.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View v, int position, long id)
            {
                // получаем выбранный пункт
                Dish dish = (Dish)parent.getItemAtPosition(position);
                //load Activity with content of dish
                loadDishContentActivity(dish);
            }
        });
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        switch(id){
            case R.id.first_dishes: text.setText("Первые блюда"); break;
            case R.id.second_dishes: text.setText("Вторые блюда"); break;
            case R.id.salads: text.setText("Салаты"); break;
            case R.id.snacks: text.setText("Закуски"); break;
            case R.id.bakes: text.setText("Выпечка"); break;
            case R.id.deserts: text.setText("Десерты"); break;
            case R.id.drinks: text.setText("Напитки"); break;

            default:
                id = R.id.first_dishes;
                text.setText("Первые блюда");
                break;

        }

        createDishesList(id);

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    private List<Dish> getDishList(int menu) {

        List<Dish> dishList = new ArrayList<>();

        List<File> dir_list = DishesFileSystem.getDirList(menu);

        for (File file : dir_list){
            //neccessary get info about dish: name, list of ingridient, cooking, source of image
            DishParser parser = new DishParser();

            if (file != null && parser.parse(file)){
                Dish dish = parser.getDish();
                dish.setImagePath(DishesFileSystem.getImagePath(menu, dish.getName()));
                dishList.add(dish);
            }
        }
        return dishList;
    }


}
