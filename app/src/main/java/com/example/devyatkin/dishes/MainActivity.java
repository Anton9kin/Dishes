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

    private List<File> dir_list;
    private List<String> dir_path = new ArrayList<>();
    private ListView mainList;
    private boolean isDishList = false;
    private MenuItem itemAdd;
    private String currentCategory;
    private String[] categoryList;
    private int lastCategoryList = R.array.category_list;
    /*
    this array MUST BE sync with string-array name="category_list" from categories.xml
     */
    private int[] sourceIcon_mainCategories = {
            R.drawable.ic_first_dishes,
            R.drawable.ic_second_dishes,
            R.drawable.ic_salads,
            R.drawable.ic_snacks,
            R.drawable.ic_deserts,
            R.drawable.ic_bake,
            R.drawable.ic_drinks,
    };
    /*
    this array MUST BE sync with string-array name="second_subcategory_list" from categories.xml
     */
    private int[] getSourceIcon_secondCategories = {
            R.drawable.ic_meat,
            R.drawable.ic_fish,
            R.drawable.ic_chicken,
            R.drawable.ic_potato,
            R.drawable.ic_porridge,
    };


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
    protected void onResume(){

        if (isDishList){
            if (currentCategory.compareTo(getResources().getString(R.string.nav_favorites)) == 0) {
                createDishesList(DishesFileSystem.getListFavorites());
                currentCategory = getResources().getString(R.string.nav_favorites);
            }
            else
                createDishesList(currentCategory);
        }

        super.onResume();
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            if (isDishList)
                createCategoryList(lastCategoryList);
            else if (lastCategoryList != R.array.category_list)
                createCategoryList(R.array.category_list);
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
            case R.id.nav_receipt:
                createCategoryList(R.array.category_list);
                break;
            case R.id.nav_favorites:
                createDishesList(DishesFileSystem.getListFavorites());
                currentCategory = getResources().getString(R.string.nav_favorites);
                break;
            case R.id.nav_search:  break;
        }

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.edit_dish, menu);

        itemAdd = menu.findItem(R.id.action_dish_add);
        itemAdd.setVisible(false);

        menu.setGroupVisible(R.id.action_group_editing, false);
        menu.setGroupVisible(R.id.action_group_saving, false);

        createCategoryList(R.array.category_list);

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
            loadDishContentActivity(new Dish(), true);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private List<Dish> getDishList(List<String> listPath){
        List<Dish> dishList = new ArrayList<>();
        dir_path.clear();

        if (listPath == null)
            return dishList;

        for(String path : listPath){
            File file = new File(path);
            dir_path.add(file.getPath());
            DishParser parser = new DishParser(this);

            if (file != null && parser.parse(file)){
                Dish dish = parser.getDish();
                dish.setImagePath(DishesFileSystem.getImagePath(dish.getType(), file));
                dishList.add(dish);
            }
        }
        return dishList;
    }

    private List<Dish> getDishList(String category) {

        List<Dish> dishList = new ArrayList<>();

        dir_list = DishesFileSystem.getDirList(category);
        dir_path.clear();

        for (File file : dir_list){
            dir_path.add(file.getPath());
            //neccessary get info about dish: name, list of ingridient, cooking, source of image
            DishParser parser = new DishParser(this);

            if (file != null && parser.parse(file)){
                Dish dish = parser.getDish();
                dish.setImagePath(DishesFileSystem.getImagePath(category, file));
                dishList.add(dish);
            }
        }
        return dishList;
    }


    //switch to DishDetail
    private void loadDishContentActivity(Dish dish, boolean edit){
        //load DishDetail
        Intent intent = new Intent(SHOW_DISH_CONTENT);
        intent.putExtra(Dish.class.getSimpleName(), dish);
        intent.putExtra("edit", edit);
        startActivity(intent);
    }
    //switch to DishDetail
    private void loadDishContentActivity(String filePath, boolean edit){
        //load DishDetail
        Intent intent = new Intent(SHOW_DISH_CONTENT);
        intent.putExtra("file", filePath);
        intent.putExtra("edit", edit);
        startActivity(intent);
    }

    private void createDishesList(List<String> listFile){
        isDishList = true;
        List<Dish> dishes = getDishList(listFile);

        //create adapter
        DishAdapter dishAdapter = new DishAdapter(this, R.layout.list_dish, dishes);
        mainList.setAdapter(dishAdapter);

        String[] categories = getResources().getStringArray(R.array.category_list);
        getSupportActionBar().setTitle("Избранное");

        if (dishes.size() == 0){
            mainList.setOnItemClickListener(null);
        }else{
            // слушатель выбора в списке
            mainList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                public void onItemClick(AdapterView<?> parent, View v, int position, long id)
                {
                    // получаем выбранный пункт
                    //Dish dish = (Dish)parent.getItemAtPosition(position);
                    //get selected file
                    String file = dir_path.get(position);
                    //load Activity with content of dish
                    //loadDishContentActivity(dish, false);
                    loadDishContentActivity(file, false);
                }
            });
        }
    }

    private void createDishesList(String category){

        isDishList = true;
        itemAdd.setVisible(true);

        List<Dish> dishes = getDishList(category);
        //create adapter
        DishAdapter dishAdapter = new DishAdapter(this, R.layout.list_dish, dishes);
        mainList.setAdapter(dishAdapter);

        String[] categories = getResources().getStringArray(R.array.category_list);
        getSupportActionBar().setTitle(category);

        if (dishes.size() == 0){
            mainList.setOnItemClickListener(null);
        }else{
            // слушатель выбора в списке
            mainList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                public void onItemClick(AdapterView<?> parent, View v, int position, long id)
                {
                    // получаем выбранный пункт
                    //Dish dish = (Dish)parent.getItemAtPosition(position);
                    //get selected file
                    String file = dir_path.get(position);
                    //load Activity with content of dish
                    //loadDishContentActivity(dish, false);
                    loadDishContentActivity(file, false);
                }
            });
        }
    }

    private List<DishCategory> getListCategory(int categoryType){
        List<DishCategory> categories  = new ArrayList();

        categoryList = getResources().getStringArray(categoryType);

        int[] icon = sourceIcon_mainCategories;

        if (categoryType == R.array.second_subcategory_list)
            icon = getSourceIcon_secondCategories;

        for(int i = 0; i < categoryList.length; i++){
            categories.add(new DishCategory(categoryList[i], icon[i]));
        }

        return  categories;
    }

    public void createCategoryList(final int category){
        isDishList = false;
        itemAdd.setVisible(false);
        lastCategoryList = category;

        if (category == R.array.category_list)
            getSupportActionBar().setTitle(getResources().getString(R.string.app_name));
        if (category == R.array.second_subcategory_list)
            getSupportActionBar().setTitle("Вторые блюда");

        mainList = findViewById(R.id.main_list);

        CategoryAdapter adapter = new CategoryAdapter(this, R.layout.list_category, getListCategory(category));

        mainList.setAdapter(adapter);

        mainList.setOnItemClickListener(new AdapterView.OnItemClickListener(){
            @Override
            public void onItemClick(AdapterView<?> parent, View v, int position, long id)
            {
                String select = categoryList[position];
                currentCategory = select;

                if (select.compareTo("Вторые блюда") == 0){
                    createCategoryList(R.array.second_subcategory_list);
                    return;
                }
                createDishesList(select);
            }
        });
    }

}
