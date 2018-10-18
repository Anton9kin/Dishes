package com.example.devyatkin.dishes;

import android.content.Context;
import android.util.Log;
import android.util.Xml;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserFactory;
import org.xmlpull.v1.XmlSerializer;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

public class DishParser {
    private Dish dish;
    private static Context context;



    public DishParser(){
        dish = null;
    }
    public DishParser(Context context){
        dish = null;
        this.context = context;
    }

    public Dish getDish() {
        return this.dish;
    }

    public void setDish(Dish dish) {
        this.dish = dish;
    }

    public boolean parse(File xmlFile){
        boolean status = true;
        boolean inEntry = false;
        String textValue = "";

        try{
            XmlPullParserFactory factory = XmlPullParserFactory.newInstance();
            factory.setNamespaceAware(true);
            XmlPullParser xpp = factory.newPullParser();

            FileInputStream fis = new FileInputStream(xmlFile);

            xpp.setInput(fis, null);
            int eventType = xpp.getEventType();

            while(eventType != XmlPullParser.END_DOCUMENT){

                String tagName = xpp.getName();
                switch (eventType){
                    case XmlPullParser.START_TAG:
                        Log.d("DISHES", "In start tag = "+xpp.getName());
                        if (context.getResources().getString(R.string.xmlTag_document).equalsIgnoreCase(tagName)){
                            inEntry = true;
                            dish = new Dish();
                        }
                        break;

                    case XmlPullParser.TEXT:
                        Log.d("DISHES", "Have text = "+xpp.getText());
                        textValue = xpp.getText();
                        break;

                    case XmlPullParser.END_TAG:
                        Log.d("DISHES", "In end tag = "+xpp.getName());
                        if (inEntry){
                            if (context.getResources().getString(R.string.xmlTag_name).equalsIgnoreCase(tagName)){
                                dish.setName(textValue);
                            }else if (context.getResources().getString(R.string.xmlTag_type).equalsIgnoreCase(tagName)){
                                dish.setType(textValue);
                            }else if (context.getResources().getString(R.string.xmlTag_ingredients).equalsIgnoreCase(tagName)){
                                dish.setIngredient(textValue);
                            }else if (context.getResources().getString(R.string.xmlTag_cooking).equalsIgnoreCase(tagName)){
                                dish.setCooking(textValue);
                            }
                        }
                        break;

                    default:
                }

                eventType = xpp.next();
            }

        } catch (Exception e) {
            status = false;
            e.printStackTrace();
        }

        return status;
    }

    public boolean save(Dish dish){
        this.dish = dish;

        File newXMLFile = new File(this.dish.getImagePath() + "/" + dish.getName() + ".xml");
        try {
            newXMLFile.createNewFile();
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }

        FileOutputStream fileOS = null;
        try{
            fileOS = new FileOutputStream(newXMLFile);

        } catch(FileNotFoundException e) {
            e.printStackTrace();
            return false;
        }
        XmlSerializer serializer = Xml.newSerializer();

        try {
            serializer.setOutput(fileOS, "UTF-8");
            serializer.startDocument(null, Boolean.valueOf(true));
            serializer.setFeature("http://xmlpull.org/v1/doc/features.html#indent-output", true);

            //start document
            serializer.startTag(null, context.getResources().getString(R.string.xmlTag_document));

            //set name
            serializer.startTag(null, context.getResources().getString(R.string.xmlTag_name));
            serializer.text(this.dish.getName());
            serializer.endTag(null, context.getResources().getString(R.string.xmlTag_name));

            //set type
            serializer.startTag(null, context.getResources().getString(R.string.xmlTag_type));
            serializer.text(this.dish.getType());
            serializer.endTag(null, context.getResources().getString(R.string.xmlTag_type));

            //set ingredients
            serializer.startTag(null, context.getResources().getString(R.string.xmlTag_ingredients));
            serializer.text(this.dish.getIngredient());
            serializer.endTag(null, context.getResources().getString(R.string.xmlTag_ingredients));

            //set cooking
            serializer.startTag(null, context.getResources().getString(R.string.xmlTag_cooking));
            serializer.text(this.dish.getCooking());
            serializer.endTag(null, context.getResources().getString(R.string.xmlTag_cooking));

            //stop document
            serializer.endTag(null, context.getResources().getString(R.string.xmlTag_document));

            serializer.endDocument();
            serializer.flush();
            fileOS.close();
            //TextView tv = (TextView)findViewById(R.);

        } catch(Exception e) {
            e.printStackTrace();
            return false;
        }

        return true;
    }
}
