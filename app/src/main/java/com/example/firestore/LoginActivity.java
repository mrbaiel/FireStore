package com.example.firestore;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.firestore.Model.Users;
import com.example.firestore.Prevalent.Prevalent;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.rey.material.widget.CheckBox;

import io.paperdb.Paper;

public class LoginActivity extends AppCompatActivity {
    private EditText phone_number, password;
    private Button loginButton;
    private ProgressDialog loadingBar;
    private String parentDBName = "Users";
    private CheckBox checkBoxRememberMe;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_login);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        loginButton = (Button) findViewById(R.id.login_button);
        phone_number = (EditText) findViewById(R.id.login_phone_number);
        password = (EditText) findViewById(R.id.login_password);
        loadingBar = new ProgressDialog(this);
        checkBoxRememberMe = (CheckBox)findViewById(R.id.checkbox);
        Paper.init(this) ;
        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loginUser();
            }
        });
    }

    public void loginUser() {
        String phoneInput = phone_number.getText().toString();
        String passwordInput = password.getText().toString();

        if (TextUtils.isEmpty(phoneInput)) {
            Toast.makeText(this, "Введите имя номер телефона", Toast.LENGTH_SHORT).show();
        } else if (TextUtils.isEmpty(passwordInput)) {
            Toast.makeText(this, "Введите имя пароль", Toast.LENGTH_SHORT).show();
        } else {
            loadingBar.setTitle("Вход в аккаунт");
            loadingBar.setMessage("Пожалуйста подождите");
            loadingBar.setCanceledOnTouchOutside(false);
            loadingBar.show();

            ValidateUser(phoneInput, passwordInput);
        }
    }

    private void ValidateUser(final String phoneNumber, String password) {
        if(checkBoxRememberMe.isChecked()){
            Paper.book().write(Prevalent.UserPhoneKey, phoneNumber);
            Paper.book().write(Prevalent.UserPasswordKey, password);
        }
        final DatabaseReference RootRef;
        RootRef = FirebaseDatabase.getInstance().getReference();
        RootRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.child(parentDBName).child(phoneNumber).exists()) {
                    Users usersData = dataSnapshot.child(parentDBName).child(phoneNumber).getValue(Users.class);

                    if(usersData.getPhone().equals(phoneNumber)){
                        if(usersData.getPassword().equals(password)){
                            loadingBar.dismiss();
                            Toast.makeText(LoginActivity.this, "Success!!!", Toast.LENGTH_SHORT).show();
                            Intent homeIntent = new Intent(LoginActivity.this, HomeActivity.class);
                            startActivity(homeIntent);
                        }
                        else{
                            Toast.makeText(LoginActivity.this, "Неверный пароль!", Toast.LENGTH_SHORT).show();
                        }
                    }
                    else{
                        Toast.makeText(LoginActivity.this, "Неверный номер телефона", Toast.LENGTH_SHORT).show();
                    }
                }
                else {
                    Toast.makeText(LoginActivity.this, "Вход не выполнен", Toast.LENGTH_SHORT).show();

                    loadingBar.dismiss();

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
}