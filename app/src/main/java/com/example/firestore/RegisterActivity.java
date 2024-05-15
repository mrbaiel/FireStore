package com.example.firestore;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.provider.DocumentsContract;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;

public class RegisterActivity extends AppCompatActivity {
    private EditText registerNameInput, registerPhoneNumberInput, registerPasswordInput;
    private Button registerButton;
    private ProgressDialog loadingBar ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_register);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        registerButton = (Button) findViewById(R.id.register_button);
        registerNameInput = (EditText) findViewById(R.id.register_name_input);
        registerPhoneNumberInput = (EditText) findViewById(R.id.register_phone_number_input);
        registerPasswordInput = (EditText) findViewById(R.id.register_password_input);
        loadingBar = new ProgressDialog(this);

        registerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CreateAccount();
            }
        });
    }

    private void CreateAccount() {
        String username = registerNameInput.getText().toString();
        String phonenumber = registerPhoneNumberInput.getText().toString();
        String password = registerPasswordInput.getText().toString();

        if(TextUtils.isEmpty(username)) {
            Toast.makeText(this, "Введите имя пользователя", Toast.LENGTH_SHORT).show();
        }
        else if(TextUtils.isEmpty(phonenumber)) {
            Toast.makeText(this, "Введите имя номер телефона", Toast.LENGTH_SHORT).show();
        }
        else if(TextUtils.isEmpty(password)) {
            Toast.makeText(this, "Введите имя пароль", Toast.LENGTH_SHORT).show();
        }
        else{
            loadingBar.setTitle("Создается аккаунт...");
            loadingBar.setMessage("Пожалуйста подождите");
            loadingBar.setCanceledOnTouchOutside(false);
            loadingBar.show();

            ValidatePhone(username, phonenumber, password);
        }
    }

    private void ValidatePhone(String username, String phonenumber, String password) {
        final DatabaseReference RootRef;
        RootRef = FirebaseDatabase.getInstance().getReference();

        RootRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(!(dataSnapshot.child("Users").child(phonenumber).exists())) {
                    HashMap<String, Object> userDataMap = new HashMap<>();
                    userDataMap.put("phone number", phonenumber);
                    userDataMap.put("name", username);
                    userDataMap.put("password", password);

                    RootRef.child("Users").child(phonenumber).updateChildren(userDataMap)
                            .addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if(task.isSuccessful()){
                                        loadingBar.dismiss();
                                        Toast.makeText(RegisterActivity.this, "Регистрация прошла!", Toast.LENGTH_SHORT).show();
                                        Intent loginIntent = new Intent(RegisterActivity.this, LoginActivity.class);
                                        startActivity(loginIntent);
                                    }
                                    else {
                                        loadingBar.dismiss();
                                        Toast.makeText(RegisterActivity.this, "Ошибка регистрации", Toast.LENGTH_SHORT).show();
                                    }
                                }
                            });
                }
                else {
                    loadingBar.dismiss();
                    Toast.makeText(RegisterActivity.this, "Номер" + phonenumber +"уже зарегистрирован", Toast.LENGTH_SHORT).show();
                    Intent loginIntent = new Intent(RegisterActivity.this, LoginActivity.class);
                    startActivity(loginIntent);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
}