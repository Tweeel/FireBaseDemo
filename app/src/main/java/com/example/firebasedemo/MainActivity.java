package com.example.firebasedemo;

import static android.text.TextUtils.isEmpty;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

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

        FirebaseFirestore mFirestore = FirebaseFirestore.getInstance();
        FirebaseDatabase database = FirebaseDatabase.getInstance("https://fir-demo-88800-default-rtdb.europe-west1.firebasedatabase.app/");


        logout.setOnClickListener(view -> {
            FirebaseAuth.getInstance().signOut();
            Toast.makeText(MainActivity.this,"Logged Out!",Toast.LENGTH_SHORT).show();
            startActivity(new Intent(MainActivity.this,StartActivity.class));
            finish();
        });

        add.setOnClickListener(v -> {
            String txt_name = edit.getText().toString();
            if(isEmpty(txt_name))
                Toast.makeText(MainActivity.this,"No Name Entred!",Toast.LENGTH_SHORT).show();
            else{
                /* Write a message to the realtime database*/
//                    Map<String,String> userMap = new HashMap<>();
//                    userMap.put("name",txt_name);
//                    mFirestore.collection("Users").add(userMap).addOnSuccessListener(new OnSuccessListener<DocumentReference>(){
//                        @Override
//                        public void onSuccess(DocumentReference documentReference) {
//                            Toast.makeText(MainActivity.this,"date sended!",Toast.LENGTH_SHORT).show();
//                        }
//                    });

                /* Write a message to the firestore database*/
                DatabaseReference myRef = database.getReference();
                myRef.child("Languages").child("Name").setValue(txt_name);

                Toast.makeText(MainActivity.this,"date sended!",Toast.LENGTH_SHORT).show();
            }
        });

        ArrayList<String> list = new ArrayList<>();
        ArrayAdapter adapter = new ArrayAdapter<String>(this,R.layout.list_item,list);
        listView.setAdapter(adapter);

        DatabaseReference reference = database.getReference().child("Languages");

        reference.addValueEventListener(new ValueEventListener(){
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                list.clear();
                for(DataSnapshot Snapshot : snapshot.getChildren()) {
                    list.add(Snapshot.getValue().toString());
                }
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}