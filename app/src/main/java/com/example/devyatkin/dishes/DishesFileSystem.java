package com.example.devyatkin.dishes;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Environment;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.util.ArrayList;

public final class DishesFileSystem {

    private static final int REQUEST_PERMISSION_WRITE = 1001;
    private static Context context = null;

    private static String dir_first;
    private static String dir_second;
    private static String dir_salad;
    private static String dir_snack;
    private static String dir_bake;
    private static String dir_desert;
    private static String dir_drink;

    private static boolean checkContext(){
        if (context == null)
            return false;

        return true;
    }

    public static void setContext(Context current){
        context = current;
    }

    //re-init file system
    public static boolean checkFilePath(String path){
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

    private static String getPathByMenu(int menu){
        String path;
        switch (menu){
            case R.id.first_dishes: path = dir_first; break;
            case R.id.second_dishes: path = dir_second; break;
            case R.id.salads: path = dir_salad; break;
            case R.id.snacks: path = dir_snack; break;
            case R.id.bakes: path = dir_bake; break;
            case R.id.deserts: path = dir_desert; break;
            case R.id.drinks: path = dir_drink; break;
            default: path = dir_first;
        }
        return path;
    }

    //get file list
    public static ArrayList<File> getDirList(int menu){
        ArrayList<File> list = new ArrayList<>();

        File dir = new File(Environment.getExternalStorageDirectory(), getPathByMenu(menu));
        File[] dir_List = dir.listFiles();

        for(File f : dir_List) {
            if (f.isFile())
                list.add(f);
        }

        return list;
    }

    //create image path
    public static String getImagePath(int menu, String name){

        if (!checkContext()){
            return "Context have didn't set";
        }

        File curDir = new File(Environment.getExternalStorageDirectory(), getPathByMenu(menu));
        String imagePath = curDir.getAbsolutePath() +
                "/" + context.getResources().getString(R.string.folder_image) +
                "/" + name + ".jpg";

        return imagePath;
    }


    public static void InitDirectory(){
        if (!checkPermissions()){
            Toast.makeText(context, "Нет разрешения", Toast.LENGTH_SHORT).show();
            return;
        }

        String path = context.getString(R.string.folder_root);
        dir_first = path + "/" + context.getResources().getString(R.string.folder_first);
        dir_second = path + "/" + context.getResources().getString(R.string.folder_second);
        dir_salad = path + "/" + context.getResources().getString(R.string.folder_salad);
        dir_snack = path + "/" + context.getResources().getString(R.string.folder_snacks);
        dir_bake = path + "/" + context.getResources().getString(R.string.folder_bakes);
        dir_desert = path + "/" + context.getResources().getString(R.string.folder_deserts);
        dir_drink = path + "/" + context.getResources().getString(R.string.folder_drinks);

        String path_images = "/" + context.getResources().getString(R.string.folder_image);

        DishesFileSystem.checkFilePath(dir_first);

        DishesFileSystem.checkFilePath(dir_first + path_images);
        DishesFileSystem.checkFilePath(dir_second);
        DishesFileSystem.checkFilePath(dir_second + path_images);
        DishesFileSystem.checkFilePath(dir_salad);
        DishesFileSystem.checkFilePath(dir_salad + path_images);
        DishesFileSystem.checkFilePath(dir_snack);
        DishesFileSystem.checkFilePath(dir_snack + path_images);
        DishesFileSystem.checkFilePath(dir_bake);
        DishesFileSystem.checkFilePath(dir_bake + path_images);
        DishesFileSystem.checkFilePath(dir_desert);
        DishesFileSystem.checkFilePath(dir_desert + path_images);
        DishesFileSystem.checkFilePath(dir_drink);
        DishesFileSystem.checkFilePath(dir_drink + path_images);
    }


    private static boolean checkPermissions(){

        if(!isExternalStorageReadable() || !isExternalStorageWriteable()){
            Toast.makeText(context, "Внешнее хранилище не доступно", Toast.LENGTH_LONG).show();
            return false;
        }
        int permissionCheck = ContextCompat.checkSelfPermission(context, Manifest.permission.WRITE_EXTERNAL_STORAGE);
        /*
        if(permissionCheck!= PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions(MainActivity.class, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, REQUEST_PERMISSION_WRITE);
            return false;
        }
        */
        return true;
    }

    // проверяем, доступно ли внешнее хранилище для чтения и записи
    public static boolean isExternalStorageWriteable(){
        String state = Environment.getExternalStorageState();
        return  Environment.MEDIA_MOUNTED.equals(state);
    }
    // проверяем, доступно ли внешнее хранилище хотя бы только для чтения
    public static boolean isExternalStorageReadable(){
        String state = Environment.getExternalStorageState();
        return  (Environment.MEDIA_MOUNTED.equals(state) ||
                Environment.MEDIA_MOUNTED_READ_ONLY.equals(state));
    }
}
