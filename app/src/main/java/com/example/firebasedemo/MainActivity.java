package com.example.firebasedemo;

import static android.text.TextUtils.isEmpty;

import static com.google.common.io.Files.getFileExtension;

import android.app.ProgressDialog;
import android.content.ContentResolver;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.firestore.SetOptions;
import com.google.firebase.ktx.Firebase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class MainActivity extends AppCompatActivity {

    private EditText edit;
    private Uri imageUri;
    private static final int IMAGE_REQUEST = 2;

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Button logout = findViewById(R.id.logout);
        Button add = findViewById(R.id.add);
        Button upload = findViewById(R.id.add_photo);

        edit = findViewById(R.id.editTextTextPersonName);
        ListView listView = findViewById(R.id.data);

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

        FirebaseFirestore mFirestore = FirebaseFirestore.getInstance();

        Map<String,Object> data = new HashMap<>();

        /*add data with firestore database*/
        /*data.put("name","Tlemcen");
        data.put("state","Bousoujour");
        data.put("country","Algeria");
        mFirestore.collection("cities").document("JSR").set(data).addOnCompleteListener(new OnCompleteListener<Void>(){
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if(task.isSuccessful()){
                    Toast.makeText(MainActivity.this,"date sended!",Toast.LENGTH_SHORT).show();
                }
            }
        });*/

        /*murge data with firestore database*/
        /*data.put("address","11 rue des freres laribi");
        mFirestore.collection("cities").document("JSR").set(data, SetOptions.merge()).addOnCompleteListener(new OnCompleteListener<Void>(){
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if(task.isSuccessful()){
                    Toast.makeText(MainActivity.this,"murge seccessful!",Toast.LENGTH_SHORT).show();
                }
            }
        });*/

        /*add data with usnique id*/
        /*data.put("state","paris");
        data.put("country","france");
        mFirestore.collection("cities").add(data).addOnCompleteListener(new OnCompleteListener<DocumentReference>(){
            @Override
            public void onComplete(@NonNull Task<DocumentReference> task) {
                if(task.isSuccessful())
                    Toast.makeText(MainActivity.this,"value added secessfuly!",Toast.LENGTH_SHORT).show();
            }
        });*/

        /*updating data in firestore database*/
        /*DocumentReference ref = FirebaseFirestore.getInstance().collection("cities").document("JSR");
        ref.update("name","Oran");*/

        /*get data from firestore firebase */
        /*DocumentReference docRef = FirebaseFirestore.getInstance().collection("cities").document("SF");
        docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>(){

            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if(task.isSuccessful()){
                    DocumentSnapshot snapshot = task.getResult();
                    if(snapshot.exists()){
                        Log.d("Document",snapshot.getData().toString());
                    }
                    else{
                        Log.d("Document","no data !!");
                    }
                }
            }
        });*/

        /*get data from firestore firebase base on a condition*/
        /*FirebaseFirestore.getInstance().collection("cities").whereEqualTo("capital",true)
                .get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>(){
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if(task.isSuccessful()){
                    for(QueryDocumentSnapshot doc : task.getResult()){
                        Log.d("Document",doc.getId() + "=>" + doc.getData());
                    }
                }
            }
        });*/

       FirebaseFirestore.getInstance().collection("cities").document("LA")
               .addSnapshotListener(new EventListener<DocumentSnapshot>() {

           @Override
           public void onEvent(@Nullable DocumentSnapshot value, @Nullable FirebaseFirestoreException error) {
           }
       });
    }

    private String  getFileExtension(Uri uri){
        ContentResolver contentResolver = getContentResolver();

        MimeTypeMap mimeTypeMap = MimeTypeMap.getSingleton();

        return mimeTypeMap.getExtensionFromMimeType(contentResolver.getType(uri));
    }

    public void upload(View view) {
        Intent galleryIntent = new Intent(Intent.ACTION_PICK,
                android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(galleryIntent, IMAGE_REQUEST);

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == IMAGE_REQUEST && resultCode == RESULT_OK){
            imageUri = data.getData();
            /*uploadImange*/
            uploadImage();
        }
    }

    private void uploadImage(){
        final ProgressDialog pd = new ProgressDialog(this);
        pd.setMessage("Uploading");
        pd.show();
        if(imageUri != null){
            final StorageReference fileRef = FirebaseStorage.getInstance().getReference().child("uploads").child(System.currentTimeMillis()+
                    "."+getFileExtension(imageUri));
            fileRef.putFile(imageUri).addOnCompleteListener(task -> fileRef.getDownloadUrl().addOnSuccessListener(uri -> {
                String url = uri.toString();
                Log.d("DownloadUrl",url);
                pd.dismiss();
                Toast.makeText(MainActivity.this,"Image upload successfull",Toast.LENGTH_SHORT).show();
            }));
        }

    }
}