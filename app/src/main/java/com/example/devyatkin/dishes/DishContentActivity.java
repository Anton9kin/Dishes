package com.example.devyatkin.dishes;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

public class DishContentActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dish_content);

        Bundle arg = getIntent().getExtras();

        TextView name = findViewById(R.id.content_dish_name);
        name.setText(arg.get("name").toString());

        TextView ingredient = findViewById(R.id.content_dish_ingredients);
        ingredient.setText(arg.get("ingredients").toString());

        TextView cooking = findViewById(R.id.content_dish_cooking);
        cooking.setText(arg.get("cooking").toString());

        ImageView image = findViewById(R.id.content_dish_image);
        image.setImageResource(R.drawable.ic_meat);
    }
}
