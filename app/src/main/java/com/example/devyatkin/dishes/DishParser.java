package com.example.devyatkin.dishes;

import android.util.Log;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.StringReader;

public class DishParser {
    private Dish dish;

    public DishParser(){
        dish = null;
    }

    public Dish getDish() {
        return this.dish;
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
                        if ("dish".equalsIgnoreCase(tagName)){
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
                            if ("name".equalsIgnoreCase(tagName)){
                                dish.setName(textValue);
                            }else if ("type".equalsIgnoreCase(tagName)){
                                dish.setType(textValue);
                            }else if ("ingridient".equalsIgnoreCase(tagName)){
                                dish.setIngredient(textValue);
                            }else if ("cooking".equalsIgnoreCase(tagName)){
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
}
