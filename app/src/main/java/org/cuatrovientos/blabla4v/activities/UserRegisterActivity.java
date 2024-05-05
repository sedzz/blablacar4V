package org.cuatrovientos.blabla4v.activities;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;

import org.cuatrovientos.blabla4v.R;

import java.util.HashMap;
import java.util.Map;

public class UserRegisterActivity extends AppCompatActivity {

    TextView btnRegister;
    EditText etUsername, etEmail, etDNI, etPassword, etPlace;
    FirebaseFirestore mFireStore;
    FirebaseAuth mAuth;



    @SuppressLint({"MissingInflatedId", "WrongViewCast"})
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_register);
        mFireStore = FirebaseFirestore.getInstance();
        mAuth = FirebaseAuth.getInstance();

        etUsername = findViewById(R.id.txtName);
        etEmail = findViewById(R.id.txtEmail);
        etDNI = findViewById(R.id.txtDni);
        etPassword = findViewById(R.id.txtPassword);
        etPlace = findViewById(R.id.txtPlace);
        btnRegister = findViewById(R.id.btnRegistry);

        btnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String userName = etUsername.getText().toString().trim();
                String email = etEmail.getText().toString().trim();
                String dni = etDNI.getText().toString().trim();
                String password = etPassword.getText().toString().trim();
                String place = etPlace.getText().toString().trim();

                if(userName.isEmpty() || email.isEmpty() || dni.isEmpty() || password.isEmpty() || place.isEmpty()){
                    Toast.makeText(UserRegisterActivity.this, "No pueden haber campos vacíos", Toast.LENGTH_SHORT).show();
                } else if(password.length() < 6){
                    Toast.makeText(UserRegisterActivity.this, "La contraseña debe tener al menos 6 caracteres", Toast.LENGTH_SHORT).show();
                } else {
                    registerUser(userName, email, dni, password, place);
                }
            }
        });

    }

    private void registerUser(String userName, String email, String dni, String password, String place) {
        mAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                String id = mAuth.getCurrentUser().getUid();
                Map<String, Object> map = new HashMap<>();
                map.put("id",id);
                map.put("name",userName);
                map.put("email",email);
                map.put("dni",dni);
                map.put("home",place);

                mFireStore.collection("user").document(id).set(map).addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        finish();
                        startActivity(new Intent(UserRegisterActivity.this, MainActivity.class));
                        Toast.makeText(UserRegisterActivity.this, "User registered successfully", Toast.LENGTH_SHORT).show();
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(UserRegisterActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(UserRegisterActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
}