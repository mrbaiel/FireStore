package com.example.firestore.UI.Admin;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.firestore.R;
import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.gms.tasks.Tasks;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.rey.material.drawable.CircularProgressDrawable;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Objects;

public class AdminAddNewProductActivity extends AppCompatActivity {

    private String categoryName, Description, Price, PName;
    private String saveCurrentData, saveCurrentTime, productRandomKey, downloadImageUrl;
    private ImageView productImage;
    private EditText productName, productDescription, productPrice;
    private Button addNewProductButton;
    private static final int GALLERYPICK = 1;
    private Uri ImageUri;
    private StorageReference ProductImageRef;
    private DatabaseReference ProductRef;
    private ProgressDialog loadingBar;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_admin_add_new_product);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        categoryName = getIntent().getExtras().get("Category").toString();
        init();

        productImage.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                openGallery();
            }
        });

        addNewProductButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                ValidateProduct();
            }
        });
    }

    private void ValidateProduct()
    {
        Description = productDescription.getText().toString();
        Price = productPrice.getText().toString();
        PName = productName.getText().toString();

        if(ImageUri == null)
        {
            Toast.makeText(this, "Добавьте изображение товара", Toast.LENGTH_SHORT).show();
        }
        else if(TextUtils.isEmpty(Description))
        {
            Toast.makeText(this, "Добавьте описание товара", Toast.LENGTH_SHORT).show();
        }
        else if(TextUtils.isEmpty(Price))
        {
            Toast.makeText(this, "Добавьте стоимость товара", Toast.LENGTH_SHORT).show();
        }
        else if(TextUtils.isEmpty(PName))
        {
            Toast.makeText(this, "Добавьте название товара", Toast.LENGTH_SHORT).show();
        }
        else
        {
            StoreProductInfo();
        }
    }

    private void StoreProductInfo()
    {
        loadingBar.setTitle("Загрузка изображения");
        loadingBar.setMessage("Пожалуйста подождите");
        loadingBar.setCanceledOnTouchOutside(false);
        loadingBar.show();


        Calendar calendar = Calendar.getInstance();

        SimpleDateFormat currentDate = new SimpleDateFormat("ddMMyyyy");
        saveCurrentData = currentDate.format(calendar.getTime());

        SimpleDateFormat currentTime = new SimpleDateFormat("HHmmss");
        saveCurrentData = currentTime.format(calendar.getTime());

        productRandomKey = saveCurrentData + saveCurrentTime;

        final StorageReference filePath = ProductImageRef.child(ImageUri.getLastPathSegment() + productRandomKey + ".jpg");

        final UploadTask uploadTask = filePath.putFile(ImageUri);

        uploadTask.addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception error)
            {
                String msg = error.toString();
                Toast.makeText(AdminAddNewProductActivity.this, "Ошибка у вас " + msg, Toast.LENGTH_SHORT).show();
                loadingBar.dismiss();
            }
        }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                Toast.makeText(AdminAddNewProductActivity.this, "Изображение загружено", Toast.LENGTH_SHORT).show();

                Task<Uri> uriTasks = uploadTask.continueWithTask(new Continuation<UploadTask.TaskSnapshot, Task<Uri>>(){

                    @Override
                    public Task<Uri> then(@NonNull Task<UploadTask.TaskSnapshot> task) throws Exception
                    {
                        if (!task.isSuccessful()) {
                            throw task.getException();
                        }
                        downloadImageUrl = filePath.getDownloadUrl().toString();
                        return filePath.getDownloadUrl();
                    }
                }).addOnCompleteListener(new OnCompleteListener<Uri>() {
                    @Override
                    public void onComplete(@NonNull Task<Uri> task) {
                        if(task.isSuccessful()){
                            downloadImageUrl = task.getResult().toString();

                            Toast.makeText(AdminAddNewProductActivity.this, "Изображение загружено", Toast.LENGTH_SHORT).show();
                            
                            SaveProductInfoToDatabase();
                        }
                    }
                });
            }
        });
    }

    private void SaveProductInfoToDatabase()
    {
        HashMap<String, Object> productMap = new HashMap<>();

        productMap.put("description", Description);
        productMap.put("image", ImageUri);
        productMap.put("category", categoryName);
        productMap.put("price", productPrice);
        productMap.put("name", PName);
        productMap.put("pid", productRandomKey);
        productMap.put("data", saveCurrentData);
        productMap.put("time", saveCurrentTime);

        ProductRef.child(productRandomKey).updateChildren(productMap).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if(task.isSuccessful())
                {
                    loadingBar.dismiss();
                    Toast.makeText(AdminAddNewProductActivity.this, "Товар добавлен!", Toast.LENGTH_SHORT).show();
                    Intent adminIntent = new Intent(AdminAddNewProductActivity.this, AdminCategoryActivity.class);
                    startActivity(adminIntent);
                }
                else
                {
                    loadingBar.dismiss();
                    String msg = task.getException().toString();
                    Toast.makeText(AdminAddNewProductActivity.this, "Ошибка " + msg, Toast.LENGTH_SHORT).show();
                }
            }
        });

    }

    private void openGallery()
    {
        Intent galleryIntent = new Intent();
        galleryIntent.setAction(Intent.ACTION_GET_CONTENT);
        galleryIntent.setType("image/*");
        startActivityForResult(galleryIntent, GALLERYPICK);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == GALLERYPICK && resultCode == RESULT_OK && data != null)
        {
            ImageUri = data.getData();
            productImage.setImageURI(ImageUri);
        }
    }

    private void init()
    {
        categoryName = getIntent().getExtras().get("Category").toString();
        productImage = findViewById(R.id.selectPicture);
        productName = findViewById(R.id.product_name);
        productDescription = findViewById(R.id.description);
        productPrice = findViewById(R.id.cost);
        ProductImageRef = FirebaseStorage.getInstance().getReference().child("Product images");
        ProductRef = FirebaseDatabase.getInstance().getReference().child("Products");
        loadingBar = new ProgressDialog(this);
        addNewProductButton = findViewById(R.id.addButton);

    }
}