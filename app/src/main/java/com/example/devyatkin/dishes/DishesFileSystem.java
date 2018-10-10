package com.example.devyatkin.dishes;

import android.content.Context;
import android.os.Environment;

import java.io.File;
import java.util.ArrayList;

public final class DishesFileSystem {

    private static Context context = null;

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

    //get file list
    public static ArrayList<File> getDirList(String path){
        ArrayList<File> list = new ArrayList<>();

        File dir = new File(Environment.getExternalStorageDirectory(), path);
        File[] dir_List = dir.listFiles();

        for(File f : dir_List) {
            if (f.isFile())
                list.add(f);
        }

        return list;
    }

    //create image path
    public static String getImagePath(String dir, String name){

        if (!checkContext()){
            return "Context have didn't set";
        }

        File curDir = new File(Environment.getExternalStorageDirectory(), dir);
        String imagePath = curDir.getAbsolutePath() +
                "/" + context.getResources().getString(R.string.folder_image) +
                "/" + name + ".jpg";

        return imagePath;
    }
}
