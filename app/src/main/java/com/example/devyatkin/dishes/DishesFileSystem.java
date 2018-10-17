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
import java.util.List;

public final class DishesFileSystem {

    private static final int REQUEST_PERMISSION_WRITE = 1001;
    private static Context context = null;

    private static List<String> dirList =  new ArrayList();

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

    private static String getPathByMenu(int index){
        return dirList.get(index);
    }

    //get file list
    public static ArrayList<File> getDirList(int index){
        ArrayList<File> list = new ArrayList<>();

        File dir = new File(Environment.getExternalStorageDirectory(), getPathByMenu(index));
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

        String[] directories = context.getResources().getStringArray(R.array.category_list_dir);
        String path = context.getString(R.string.folder_root);

        for (String namePath : directories)
            dirList.add(path + "/" + namePath);

        String path_images = "/" + context.getResources().getString(R.string.folder_image);

        for (String childPath : dirList){
            DishesFileSystem.checkFilePath(childPath);
            DishesFileSystem.checkFilePath(childPath + path_images);
        }
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
