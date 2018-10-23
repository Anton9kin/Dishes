package com.example.devyatkin.dishes;

import android.Manifest;
import android.content.Context;
import android.os.Environment;
import android.support.v4.content.ContextCompat;
import android.widget.Toast;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public final class DishesFileSystem {

    private static final int REQUEST_PERMISSION_WRITE = 1001;
    private static Context context = null;

    private static List<String> dirList =  new ArrayList();
    private static List<String> menuList = new ArrayList();

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

    public static boolean deleteFile(String type, String name){

        File file = new File(Environment.getExternalStorageDirectory(), getPathByMenu(type) + "/" + name + ".xml");
        file.delete();

        File image = new File(Environment.getExternalStorageDirectory(),
                getPathByMenu(type) + "/" + context.getResources().getString(R.string.folder_image) + "/" + name + ".jpg");
        image.delete();

        return true;
    }

    public static String getPathByMenu(int index){
        return dirList.get(index);
    }

    public static String getPathByMenu(String menuItem){
        int size = menuList.size();

        for (int i = 0; i < size; i++){
            if (menuItem.compareTo(menuList.get(i)) == 0){
                return getPathByMenu(i);
            }
        }

        return getPathByMenu(0);
    }

    //get file list
    public static ArrayList<File> getDirList(String category){
        ArrayList<File> list = new ArrayList<>();

        File dir = new File(Environment.getExternalStorageDirectory(), getPathByMenu(category));
        File[] dir_List = dir.listFiles();

        for(File f : dir_List) {
            if (f.isFile())
                list.add(f);
        }

        return list;
    }

    //create image path
    public static String getImagePath(String menu, String name){

        if (!checkContext()){
            return "Context have didn't set";
        }

        File curDir = new File(Environment.getExternalStorageDirectory(), getPathByMenu(menu));
        String imagePath = curDir.getAbsolutePath() +
                "/" + context.getResources().getString(R.string.folder_image) +
                "/" + name + ".jpg";

        return imagePath;
    }

    private static String getFolder(File file){
        int endPos = file.getPath().lastIndexOf('/');
        String folder = file.getPath().substring(0, endPos);
        return folder;
    }

    private static String getName(File file){
        int endPos = file.getName().lastIndexOf('.');
        String name = file.getName().substring(0, endPos);
        return name;
    }

    public static String getImagePath(String menu, File file){
        if (!checkContext()){
            return null;
        }

        String folder = getFolder(file);

        String imagePath = folder +
                "/" + context.getResources().getString(R.string.folder_image) +
                "/" + getName(file) + ".jpg";

        return imagePath;
    }

    private static void createPath(int resource){

        String[] directories = context.getResources().getStringArray(resource);
        String path = context.getString(R.string.folder_root);

        for (String namePath : directories)
            dirList.add(path + "/" + namePath);

        String path_images = "/" + context.getResources().getString(R.string.folder_image);

        for (String childPath : dirList){
            DishesFileSystem.checkFilePath(childPath);
            DishesFileSystem.checkFilePath(childPath + path_images);
        }
    }

    private static void createMenu(int resource){
        String[] menu = context.getResources().getStringArray(resource);
        for (String item : menu){
            menuList.add(item);
        }
    }

    public static void InitDirectory(){
        if (!checkPermissions()){
            Toast.makeText(context, "Нет разрешения", Toast.LENGTH_SHORT).show();
            return;
        }

        createPath(R.array.category_list_dir);
        createPath(R.array.second_subcategory_list_dir);

        createMenu(R.array.category_list);
        createMenu(R.array.second_subcategory_list);
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
