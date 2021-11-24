package com.example.firebasedemo;

import static android.text.TextUtils.isEmpty;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;

public class MainActivity extends AppCompatActivity {

    private Button logout,add;
    private EditText edit;
    private ListView listView;

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        logout = findViewById(R.id.logout);
        add = findViewById(R.id.add);
        edit = findViewById(R.id.editTextTextPersonName);
        listView = findViewById(R.id.data);

        logout.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View view) {
                FirebaseAuth.getInstance().signOut();
                Toast.makeText(MainActivity.this,"Logged Out!",Toast.LENGTH_SHORT).show();
                startActivity(new Intent(MainActivity.this,StartActivity.class));
                finish();
            }
        });

        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String txt_name = edit.getText().toString();
                if(isEmpty(txt_name))
                    Toast.makeText(MainActivity.this,"No Name Entred!",Toast.LENGTH_SHORT).show();
                else{
                    FirebaseDatabase.getInstance().getReference().child("ProgrammingKnowledge").child("Name").setValue(txt_name);
                    Toast.makeText(MainActivity.this,"Data added!",Toast.LENGTH_SHORT).show();
                }

            }
        });
//        add multiple data to the data base as a hash map
//        HashMap<String,Object> map = new HashMap<>();
//        map.put("Name","houssem");
//        map.put("Email","houssembababendermel@gmail.com");
//        FirebaseDatabase.getInstance().getReference().child("ProgrammingKnowledge").child("MultipleValues").updateChildren(map);

//        to add only one single data
//        FirebaseDatabase.getInstance().getReference().child("ProgrammingKnowledge").child("android").setValue("abcd");
    }
}