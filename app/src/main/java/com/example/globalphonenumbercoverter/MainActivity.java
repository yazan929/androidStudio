package com.example.globalphonenumbercoverter;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;


import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;


import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;


public class MainActivity extends AppCompatActivity {

    int useFunctionNo = 0;
    EditText phoneNumber, Name;
    Button convertButton, resetButton, copyButton, showSavedData, saveDataButton;
    TextView infotext2, infotext3, textView, countryName;
    ImageView frontImage, backImage, flagImage;
    Task<DocumentReference> mDocRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        copyButton = (Button) findViewById(R.id.copyButton);
        resetButton = (Button) findViewById(R.id.resetButton);
        showSavedData = (Button) findViewById(R.id.showSavedData);
        convertButton = (Button) findViewById(R.id.convertButton);
        textView = (TextView) findViewById(R.id.textView);
        infotext2 = (TextView) findViewById(R.id.infotext2);
        countryName = (TextView) findViewById(R.id.countryName);
        infotext3 = (TextView) findViewById(R.id.infotext3);
        saveDataButton = (Button) findViewById(R.id.saveDataButton);
        phoneNumber = (EditText) findViewById(R.id.phoneNumber);
        Name = (EditText) findViewById(R.id.Name);
        flagImage = (ImageView) findViewById(R.id.flagImage);
        backImage = (ImageView) findViewById(R.id.backImage);
        frontImage = (ImageView) findViewById(R.id.frontImage);
        int myNum = 0;

        showSavedData.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ChangeActivity();
            }
        });


        // message of the day Part
        Calendar calendar = Calendar.getInstance();
        int day = calendar.get(Calendar.DAY_OF_WEEK);
        String msgOfTheDay = "";
        switch (day) {
            case Calendar.SUNDAY:
                msgOfTheDay = "A Sunday Well Spent Brings A Week Of Content!";
                break;
            case Calendar.MONDAY:
                msgOfTheDay = "Monday Is Not A State Of Mind Put On Your Positive Pants And Get Stuff Done";
                break;
            case Calendar.TUESDAY:
                msgOfTheDay = "Happy Tuesday! Choose To Smile. Choose To Love Choose To Be Happy.";
                break;
            case Calendar.WEDNESDAY:
                msgOfTheDay = "It Is Wellness Wednesday. Take care of Yourself, Stay refreshed, and stress less.";
                break;
            case Calendar.THURSDAY:
                msgOfTheDay = "Better Days Are Just Around The Corner. They are called Friday, Saturday, and Sunday!";
                break;
            case Calendar.FRIDAY:
                msgOfTheDay = "May Today Be The Fridayest Friday That Ever Fridayed";
                break;
            case Calendar.SATURDAY:
                msgOfTheDay = "Happy Saturday Have A Peaceful Weekend.";
                break;
        }
        textView.setText(msgOfTheDay);
        backImage.setImageAlpha(127);


        // message of the day Part Ends


        frontImage.setOnClickListener(new View.OnClickListener() {

            public void onClick(View v) {
                add();

            }
        });
        backImage.setOnClickListener(new View.OnClickListener() {

            public void onClick(View v) {
                minus();

            }
        });


        copyButton.setOnClickListener(new View.OnClickListener() { // Copy Button
            public void onClick(View v) {

                copy();

            }
        });

        resetButton.setOnClickListener(new View.OnClickListener() { // reset Button
            public void onClick(View v) {

                reset();

            }
        });


        saveDataButton.setOnClickListener(new View.OnClickListener() { // Convert Button
            public void onClick(View v) {
                saveNumbers();
            }
        });


        convertButton.setOnClickListener(new View.OnClickListener() { // Convert Button
            public void onClick(View v) {

                if (useFunctionNo == 0) {
                    globalNumMalay();
                }
                if (useFunctionNo == 1) {
                    globalNumUAE();
                }
                if (useFunctionNo == 2) {
                    globalNumUSA();
                }


            }
        });


    }


    private void copy() {
        if (infotext2.getText() != "") {
            ClipboardManager clipboard = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
            ClipData clip = ClipData.newPlainText("abc", infotext2.getText());
            clipboard.setPrimaryClip(clip);
            Toast.makeText(getApplicationContext(), "Your text was copied", Toast.LENGTH_SHORT).show();

        } else {
            Toast.makeText(getApplicationContext(), "You need a converted phone number to be able to copy", Toast.LENGTH_SHORT).show();

        }

    }


    private void updateFunction() {
        if (useFunctionNo == 0) {
            flagImage.setImageResource(R.drawable.malaysia);
            countryName.setText("Malaysia");
        }
        if (useFunctionNo == 1) {
            flagImage.setImageResource(R.drawable.uae);
            countryName.setText("United Arab Emirates");
        }
        if (useFunctionNo == 2) {
            flagImage.setImageResource(R.drawable.usa);
            countryName.setText("United States Of America");
        }
    }

    private void add() {
        if (useFunctionNo < 2) {
            useFunctionNo++;
            updateFunction();
            if (useFunctionNo == 2) {
                frontImage.setImageAlpha(127);
            } else {
                frontImage.setImageAlpha(255);
            }
            if (useFunctionNo == 0) {
                backImage.setImageAlpha(127);
            } else {
                backImage.setImageAlpha(255);
            }
        }
    }

    private void reset() {
        Name.setText("");
        infotext2.setText("");
        infotext3.setText("Please enter a phone number and click the convert button");
        phoneNumber.setText("");
    }

    private void minus() {
        if (useFunctionNo > 0) {
            useFunctionNo--;
            updateFunction();
            if (useFunctionNo == 2) {
                frontImage.setImageAlpha(127);
            } else {
                frontImage.setImageAlpha(255);
            }
            if (useFunctionNo == 0) {
                backImage.setImageAlpha(127);
            } else {
                backImage.setImageAlpha(255);
            }
        }
    }

    private void globalNumMalay() {
        Boolean change = false;
        String theNumber = phoneNumber.getText().toString();
        String newNumber = "";
        String finalNumber = "";
        int numberOfLetters = theNumber.length();

        if (numberOfLetters < 9) {
            Toast.makeText(getApplicationContext(), "You are missing some numbers Please try again.", Toast.LENGTH_SHORT).show();
        } else if (numberOfLetters > 10) {
            Toast.makeText(getApplicationContext(), "Something is wrong your number is too long.", Toast.LENGTH_SHORT).show();
        } else {
            if (numberOfLetters == 9) {
                if (theNumber.charAt(0) != '1') {
                    Toast.makeText(getApplicationContext(), "Something is wrong, 9 digit numbers should start with 1", Toast.LENGTH_SHORT).show();
                } else {
                    newNumber = "+60 " + theNumber;
                    for (int i = 0; i <= 12; i++) {
                        if (i == 6) {
                            finalNumber = finalNumber + "-";
                            finalNumber = finalNumber + newNumber.charAt(i);
                        } else if (i == 9) {
                            finalNumber = finalNumber + " ";
                            finalNumber = finalNumber + newNumber.charAt(i);
                        } else {
                            finalNumber = finalNumber + newNumber.charAt(i);
                        }
                    }
                    infotext2.setText(finalNumber);
                    infotext3.setText("Phone number is above click Copy to copy it or click saveNumber to add it into the database");
                    change = true;
                }
            }

            if (numberOfLetters == 10) {
                if (theNumber.charAt(0) != '0' || theNumber.charAt(1) != '1') {
                    Toast.makeText(getApplicationContext(), "Something is wrong, 10 digit numbers should start with 01", Toast.LENGTH_SHORT).show();
                } else {
                    theNumber = theNumber.substring(1);
                    newNumber = "+60 " + theNumber;

                    for (int i = 0; i <= 12; i++) {

                        if (i == 6) {
                            finalNumber = finalNumber + "-";
                            finalNumber = finalNumber + newNumber.charAt(i);
                        } else if (i == 9) {
                            finalNumber = finalNumber + " ";
                            finalNumber = finalNumber + newNumber.charAt(i);
                        } else {
                            finalNumber = finalNumber + newNumber.charAt(i);
                        }
                    }
                    infotext2.setText(finalNumber);
                    infotext3.setText("Phone number is above click Copy to copy it or click saveNumber to add it into the database");
                    change = true;
                }

            }
        }


        if (!change) {
            String temp = phoneNumber.getText().toString();
            reset();
            //change the texts to empty
            phoneNumber.setText(temp);
        }
        change = false;
    }


    private void globalNumUAE() {
        Boolean change = false;
        String theNumber = phoneNumber.getText().toString();
        String finalNumber = "";
        int numberOfLetters = theNumber.length();

        if (numberOfLetters < 9) {
            Toast.makeText(getApplicationContext(), "You are missing some numbers Please try again.", Toast.LENGTH_SHORT).show();
        } else if (numberOfLetters > 10) {
            Toast.makeText(getApplicationContext(), "Something is wrong your number is too long.", Toast.LENGTH_SHORT).show();
        } else {
            if (numberOfLetters == 9) {
                if (theNumber.charAt(0) != '5') {
                    Toast.makeText(getApplicationContext(), "Something is wrong, 9 digit numbers should start with five", Toast.LENGTH_SHORT).show();
                } else {
                    finalNumber = "+971-" + theNumber;
                    infotext2.setText(finalNumber);
                    infotext3.setText("Phone number is above click Copy to copy it or click saveNumber to add it into the database");
                    change = true;
                }
            }
            if (numberOfLetters == 10) {
                if (theNumber.charAt(0) != '0' || theNumber.charAt(1) != '5') {
                    Toast.makeText(getApplicationContext(), "Something is wrong, 10 digit numbers should start with 05 For UAE Numbers", Toast.LENGTH_SHORT).show();
                } else {
                    theNumber = theNumber.substring(1);
                    finalNumber = "+971-" + theNumber;
                    infotext2.setText(finalNumber);
                    infotext3.setText("Phone number is above click Copy to copy it or click saveNumber to add it into the database");
                    change = true;
                }
            }
        }
        if (!change) {
            String temp = phoneNumber.getText().toString();
            reset();
            //change the texts to empty
            phoneNumber.setText(temp);
        }
        change = false;
    }

    private void globalNumUSA() {
        Boolean change = false;
        String theNumber = phoneNumber.getText().toString();
        String newNumber = "";
        String finalNumber = "";
        int numberOfLetters = theNumber.length();
        if (numberOfLetters < 10) {
            Toast.makeText(getApplicationContext(), "You are missing some numbers Please try again.", Toast.LENGTH_SHORT).show();
        } else if (numberOfLetters > 10) {
            Toast.makeText(getApplicationContext(), "Something is wrong your number is too long.", Toast.LENGTH_SHORT).show();
        } else {
            newNumber = "+1 (" + theNumber;
            for (int i = 0; i <= 13; i++) {
                if (i == 7) {
                    finalNumber = finalNumber + ") ";
                    finalNumber = finalNumber + newNumber.charAt(i);
                } else if (i == 10) {
                    finalNumber = finalNumber + "-";
                    finalNumber = finalNumber + newNumber.charAt(i);
                } else {
                    finalNumber = finalNumber + newNumber.charAt(i);
                }
            }
            infotext2.setText(finalNumber);
            infotext3.setText("Phone number is above click Copy to copy it or click saveNumber to add it into the database");
            change = true;
        }

        if (!change) {
            String temp = phoneNumber.getText().toString();
            reset();
            //change the texts to empty
            phoneNumber.setText(temp);
        }
        change = false;
    }

    public void ChangeActivity() {
        Intent intent = new Intent(this, DataActivity.class);
        startActivity(intent);
    }


    public void saveNumbers() {
        Map<String, Object> numberObj = new HashMap<>();
        infotext2 = (TextView) findViewById(R.id.infotext2);
        Name = (EditText) findViewById(R.id.Name);
        String number = infotext2.getText().toString();
        String theName = Name.getText().toString();
        if (number == "" || theName == "") {
            Toast.makeText(getApplicationContext(), "Please fill your name and convert a number.", Toast.LENGTH_SHORT).show();
        }else{
            numberObj.put("name", theName);
            numberObj.put("number", number);
            mDocRef = FirebaseFirestore.getInstance()
                    .collection("Users")
                    .document("FNxtfSdA4OPogYBiAoj3")
                    .collection("SavedNumbers")
                    .add(numberObj)
                    .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                        @Override
                        public void onSuccess(DocumentReference documentReference) {
                            Log.d("saveNumbers", "DocumentSnapshot written with ID: " + documentReference.getId());
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Log.w("saveNumbers", "Error adding document", e);
                        }
                    });

//        Map<String, Object> dataToSave = new HashMap<String, Object>();
//        dataToSave.put("phoneNum", number);

            //mDocRef.set(dataToSave)
        }
    }


    // my first try without understanding how firebase wroks.
//    public void Savedata(String num) {
//        SaveNumbers saveNumbers = new SaveNumbers(infotext2.getText().toString());
//        db.collection("SaveNumbers").document("1").set(saveNumbers)
//                .addOnCompleteListener(new OnCompleteListener<Void>() {
//                    @Override
//                    public void onComplete(@NonNull Task<Void> task) {
//                        if(task.isSuccessful()){
//                            Toast.makeText(getApplicationContext(), "worked", Toast.LENGTH_SHORT).show();
//                        }
//                        else{
//                            Toast.makeText(getApplicationContext(), "fail", Toast.LENGTH_SHORT).show();
//
//                        }
//                    }
//                });
//
//
//        }


}
