package com.example.firestore.UI.Admin;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.firestore.R;

public class AdminCategoryActivity extends AppCompatActivity {
    private ImageView book, dishes, headphones, phone, gamepad, work, ball, clothes;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_admin_category);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;});

        init();

        book.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(AdminCategoryActivity.this, AdminAddNewProductActivity.class);
                intent.putExtra("Category", "book");
                startActivity(intent);
            }
        });
        dishes.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(AdminCategoryActivity.this, AdminAddNewProductActivity.class);
                intent.putExtra("Category", "dishes");
                startActivity(intent);
            }
        });
        headphones.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(AdminCategoryActivity.this, AdminAddNewProductActivity.class);
                intent.putExtra("Category", "headphones");
                startActivity(intent);
            }
        });
        phone.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(AdminCategoryActivity.this, AdminAddNewProductActivity.class);
                intent.putExtra("Category", "phone");
                startActivity(intent);
            }
        });
        gamepad.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(AdminCategoryActivity.this, AdminAddNewProductActivity.class);
                intent.putExtra("Category", "gamepad");
                startActivity(intent);
            }
        });
        work.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(AdminCategoryActivity.this, AdminAddNewProductActivity.class);
                intent.putExtra("Category", "work");
                startActivity(intent);
            }
        });
        ball.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(AdminCategoryActivity.this, AdminAddNewProductActivity.class);
                intent.putExtra("Category", "ball");
                startActivity(intent);
            }
        });

        clothes.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(AdminCategoryActivity.this, AdminAddNewProductActivity.class);
                intent.putExtra("Category", "clothes");
                startActivity(intent);
            }
        });
    }
    private void init(){
        book = findViewById(R.id.book);
        dishes = findViewById(R.id.dishes);
        headphones = findViewById(R.id.headphones);
        phone = findViewById(R.id.phone);
        gamepad = findViewById(R.id.gamepad);
        work = findViewById(R.id.work);
        ball = findViewById(R.id.ball);
        clothes = findViewById(R.id.clothes);
    }
}