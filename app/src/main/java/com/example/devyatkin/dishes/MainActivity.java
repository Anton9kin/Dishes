package com.example.devyatkin.dishes;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    private static final String SHOW_DISH_CONTENT = "com.example.devyatkin.SHOW_DISH_CONTENT";

    private ListView mainList;
    private boolean isDishList = false;
    private Menu menu;
    private MenuItem itemAdd;

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
        mainList = findViewById(R.id.main_list);

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
            if (isDishList)
                createCategoryList();
            else
                super.onBackPressed();
        }
    }


    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        switch(id){
            case R.id.nav_receipt: createCategoryList(); break;
            case R.id.nav_favorites: break;
            case R.id.nav_search:  break;
        }

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        this.menu = menu;

        getMenuInflater().inflate(R.menu.edit_dish, menu);

        itemAdd = menu.findItem(R.id.action_dish_add);
        itemAdd.setVisible(false);

        menu.setGroupVisible(R.id.action_group_editing, false);
        menu.setGroupVisible(R.id.action_group_saving, false);

        createCategoryList();

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

        if (id == R.id.action_dish_add){
            //load Activity with new dish
            loadDishContentActivity(new Dish());
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private List<Dish> getDishList(int index) {

        List<Dish> dishList = new ArrayList<>();

        List<File> dir_list = DishesFileSystem.getDirList(index);

        for (File file : dir_list){
            //neccessary get info about dish: name, list of ingridient, cooking, source of image
            DishParser parser = new DishParser(this);

            if (file != null && parser.parse(file)){
                Dish dish = parser.getDish();
                dish.setImagePath(DishesFileSystem.getImagePath(index, dish.getName()));
                dishList.add(dish);
            }
        }
        return dishList;
    }


    //switch to DishDetail
    private void loadDishContentActivity(Dish dish){
        //load DishDetail
        Intent intent = new Intent(SHOW_DISH_CONTENT);
        intent.putExtra(Dish.class.getSimpleName(), dish);
        startActivity(intent);
    }

    private void createDishesList(int index){

        isDishList = true;
        itemAdd.setVisible(true);

        List<Dish> dishes = getDishList(index);
        //create adapter
        DishAdapter dishAdapter = new DishAdapter(this, R.layout.list_dish, dishes);
        mainList.setAdapter(dishAdapter);

        if (dishes.size() == 0){
            mainList.setOnItemClickListener(null);
        }else{
            // слушатель выбора в списке
            mainList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                public void onItemClick(AdapterView<?> parent, View v, int position, long id)
                {
                    // получаем выбранный пункт
                    Dish dish = (Dish)parent.getItemAtPosition(position);
                    //load Activity with content of dish
                    loadDishContentActivity(dish);
                }
            });
        }
    }

    public void createCategoryList(){
        isDishList = false;
        itemAdd.setVisible(false);

        mainList = findViewById(R.id.main_list);
        String[] categoriesStr = getResources().getStringArray(R.array.category_list);

        List<DishCategory> categories  = new ArrayList();

        for(String name : categoriesStr){
            int icon = R.drawable.ic_noimage;

            if (name == categoriesStr[0])
                icon = R.drawable.ic_first_dishes;
            else if (name == categoriesStr[1])
                icon = R.drawable.ic_second_dishes;
            else if (name == categoriesStr[2])
                icon = R.drawable.ic_salads;
            else if (name == categoriesStr[3])
                icon = R.drawable.ic_snacks;
            else if (name == categoriesStr[4])
                icon = R.drawable.ic_deserts;
            else if (name == categoriesStr[5])
                icon = R.drawable.ic_bake;
            else if (name == categoriesStr[6])
                icon = R.drawable.ic_drinks;

            categories.add(new DishCategory(name, icon));
        }

        CategoryAdapter adapter = new CategoryAdapter(this, R.layout.list_category, categories);

        mainList.setAdapter(adapter);

        mainList.setOnItemClickListener(new AdapterView.OnItemClickListener(){
            @Override
            public void onItemClick(AdapterView<?> parent, View v, int position, long id)
            {
                createDishesList(position);
            }
        });
    }

}
