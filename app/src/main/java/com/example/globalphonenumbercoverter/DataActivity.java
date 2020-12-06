package com.example.globalphonenumbercoverter;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.android.gms.tasks.OnCompleteListener;

public class DataActivity extends AppCompatActivity {



    Button getBack;
    EditText multiLine;
    String myText = "";



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_data);
        multiLine = (EditText) findViewById(R.id.multiLine);
        getBack = (Button) findViewById(R.id.getBack);
        getNumbers();



        getBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ChangeActivity();
            }
        });


    }
    public void ChangeActivity() {
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }
    public void getNumbers() {
        FirebaseFirestore.getInstance()
                .collection("Users")
                .document("FNxtfSdA4OPogYBiAoj3")
                .collection("SavedNumbers")
//                .whereEqualTo("capital", true)
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                Log.d("DataActivity", document.getId() + " => " + document.getData());

                                // For each of the documents
                                if (document.get("number") != null && document.get("number") != "")
                                    myText = myText + document.get("name") + ":\t\t" + document.get("number") + "\n";
                            }
                            Log.d("DataActivity, Full Text", myText);

                        } else {
                            Log.d("DataActivity", "Error getting documents: ", task.getException());
                        }
                        multiLine.setText(myText);

                    }
                });
    }
}