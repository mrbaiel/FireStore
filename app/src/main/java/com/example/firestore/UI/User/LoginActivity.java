package com.example.firestore.UI.User;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.firestore.Model.Users;
import com.example.firestore.Prevalent.Prevalent;
import com.example.firestore.R;
import com.example.firestore.UI.Admin.AdminCategoryActivity;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.rey.material.widget.CheckBox;

import io.paperdb.Paper;

public class LoginActivity extends AppCompatActivity {
    private EditText phoneInput, passwordInput;
    private Button loginBtn;
    private ProgressDialog loadingBar;
    private String parentDbName = "Users";
    private CheckBox checkBoxRememberMe;
    private TextView AdminLink, ClientLink;

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

        loginBtn = (Button) findViewById(R.id.login_button);
        phoneInput = (EditText) findViewById(R.id.login_phone_number);
        passwordInput = (EditText) findViewById(R.id.login_password);
        loadingBar = new ProgressDialog(this);
       // checkBoxRememberMe = (CheckBox)findViewById(R.id.checkbox);
        AdminLink = (TextView)findViewById(R.id.admin_panel);
        ClientLink = (TextView)findViewById(R.id.client_panel);
        Paper.init(this) ;
        loginBtn.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                loginUser();
            }
        });
        AdminLink.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AdminLink.setVisibility(View.INVISIBLE);
                ClientLink.setVisibility(View.VISIBLE);
                loginBtn.setText("Вход для админа");
                parentDbName = "Admins";
            }
        });
        ClientLink.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                parentDbName = "Users";
                AdminLink.setVisibility(View.VISIBLE);
                ClientLink.setVisibility(View.INVISIBLE);
                loginBtn.setText("Войти");
            }
        });

    }

    private void loginUser() {
        String phone = phoneInput.getText().toString();
        String password = passwordInput.getText().toString();

        if(TextUtils.isEmpty(phone))
        {
            Toast.makeText(this, "Введите имя номер телефона", Toast.LENGTH_SHORT).show();
        }
        else if(TextUtils.isEmpty(password))
        {
            Toast.makeText(this, "Введите имя пароль", Toast.LENGTH_SHORT).show();
        }
        else
        {
            loadingBar.setTitle("Вход в аккаунт...");
            loadingBar.setMessage("Пожалуйста подождите");
            loadingBar.setCanceledOnTouchOutside(false);
            loadingBar.show();

            ValidateUser(phone, password);
        }
    }

    private void ValidateUser(final String phone, final String password) {

        if(checkBoxRememberMe.isChecked()){
            Paper.book().write(Prevalent.UserPhoneKey, phone);
            Paper.book().write(Prevalent.UserPasswordKey, password);
        }

        final DatabaseReference RootRef;
        RootRef = FirebaseDatabase.getInstance().getReference();

        RootRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(dataSnapshot.child(parentDbName).child(phone).exists())
                {
                    Users usersData = dataSnapshot.child(parentDbName).child(phone).getValue(Users.class);

                    Toast.makeText(LoginActivity.this, "Phone validate:" + usersData.getPhone().toString(), Toast.LENGTH_SHORT).show();
                    Toast.makeText(LoginActivity.this, "Passw:" + usersData.getPassword(), Toast.LENGTH_SHORT).show();

                    if(usersData.getPhone().equals(phone))
                    {
                        Toast.makeText(LoginActivity.this, "Phone validate", Toast.LENGTH_SHORT).show();

                        if(usersData.getPassword().equals(password))
                        {
                            if(parentDbName.equals("Users")){
                                loadingBar.dismiss();
                                Intent homeIntent = new Intent(LoginActivity.this, HomeActivity.class);
                                homeIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                Prevalent.currentOnlineUser = usersData;
                                startActivity(homeIntent);
                                finish();
                            }
                            else if(parentDbName.equals("Admins")){
                                Intent homeIntent = new Intent(LoginActivity.this, AdminCategoryActivity.class);
                                homeIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                Prevalent.currentOnlineUser = usersData;
                                startActivity(homeIntent);
                                finish();
                            }
                        }
                        else {
                            loadingBar.dismiss();
                            Toast.makeText(LoginActivity.this, "Неверный пароль", Toast.LENGTH_SHORT).show();
                        }
                    }
                }
                else {
                    loadingBar.dismiss();
                    Toast.makeText(LoginActivity.this, "Аккаунт с номером " + phone + "не существует", Toast.LENGTH_SHORT).show();

                    Intent registerIntent = new Intent(LoginActivity.this, RegisterActivity.class);
                    startActivity(registerIntent);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

}