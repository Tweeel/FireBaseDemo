package com.example.firebasedemo;

import static android.text.TextUtils.isEmpty;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class RegisterActivity extends AppCompatActivity {

    private EditText email,password;
    Button register;

    private FirebaseAuth Auth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        email=findViewById(R.id.editTextTextEmailAddress);
        password=findViewById(R.id.editTextTextPassword);
        register = findViewById(R.id.register);

        Auth = FirebaseAuth.getInstance();

        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String text_email=email.getText().toString();
                String text_password=password.getText().toString();

                if(isEmpty(text_email) || isEmpty(text_password))
                    Toast.makeText(RegisterActivity.this,"Empty credentials!",Toast.LENGTH_SHORT).show();
                else if (text_password.length()<6)
                    Toast.makeText(RegisterActivity.this,"Password too short!",Toast.LENGTH_SHORT).show();
                else
                    registerUser(text_email,text_password);
            }
        });
    }

    private void registerUser(String text_email, String text_password) {

        Auth.createUserWithEmailAndPassword(text_email, text_password).addOnCompleteListener(RegisterActivity.this
                ,new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if(task.isSuccessful()){
                    Toast.makeText(RegisterActivity.this,"Registering user seccessful!",Toast.LENGTH_SHORT).show();
                    startActivity(new Intent(RegisterActivity.this,MainActivity.class));
                    finish();
                }
                else
                    Toast.makeText(RegisterActivity.this,"Registeration faild!",Toast.LENGTH_SHORT).show();
            }
        });
    }
}