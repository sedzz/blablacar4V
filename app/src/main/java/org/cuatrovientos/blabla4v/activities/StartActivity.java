package org.cuatrovientos.blabla4v.activities;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

import org.cuatrovientos.blabla4v.R;

public class StartActivity extends AppCompatActivity {

    Button btnRegister;
    Button btnLogin;
    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start);

        btnRegister = findViewById(R.id.btnRegister);
        btnLogin = findViewById(R.id.btnLogin);

        btnRegister.setOnClickListener(v -> {
            startActivity(new Intent(StartActivity.this, UserRegisterActivity.class));
        });

        btnLogin.setOnClickListener(v -> {
           startActivity(new Intent(StartActivity.this, LoginActivity.class));
        });

    }
}