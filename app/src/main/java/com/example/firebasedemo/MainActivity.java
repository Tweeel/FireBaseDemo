package com.example.firebasedemo;

import static android.text.TextUtils.isEmpty;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    private EditText edit;

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Button logout = findViewById(R.id.logout);
        Button add = findViewById(R.id.add);
        edit = findViewById(R.id.editTextTextPersonName);
        ListView listView = findViewById(R.id.data);

        /*this was used in adding data to the firestore database*/
//        FirebaseFirestore mFirestore = FirebaseFirestore.getInstance();

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
                /*
                    Map<String,String> userMap = new HashMap<>();
                    userMap.put("name",txt_name);
                    mFirestore.collection("Users").add(userMap).addOnSuccessListener(new OnSuccessListener<DocumentReference>(){
                        @Override
                        public void onSuccess(DocumentReference documentReference) {
                            Toast.makeText(MainActivity.this,"date sended!",Toast.LENGTH_SHORT).show();
                        }
                    });*/

                /* Write a message to the firestore database*/
                DatabaseReference myRef = database.getReference();
                myRef.child("Languages").child("Name").setValue(txt_name);

                Toast.makeText(MainActivity.this,"date sended!",Toast.LENGTH_SHORT).show();
            }
        });

        ArrayList<String> list = new ArrayList<>();
        ArrayAdapter adapter = new ArrayAdapter<>(this, R.layout.list_item, list);
        listView.setAdapter(adapter);

        DatabaseReference reference = database.getReference().child("Information");

        reference.addValueEventListener(new ValueEventListener(){
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                list.clear();
                for(DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    Information info = new Information(
                            snapshot.child("name").getValue().toString()
                            ,snapshot.child("email").getValue().toString());
                    String txt = info.getName() + " : " + info.getEmail();
                    list.add(txt);
                    //if we want to display languages
//                    list.add(snapshot.getValue().toString());
                }
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}