package com.example.affirmations;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import android.content.Intent;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class activity_login extends AppCompatActivity {

    DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReferenceFromUrl("https://learnoset-a50c5-default-rtdb.firebaseio.com/");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        final EditText phone = findViewById(R.id.phone);
        final EditText password = findViewById(R.id.password);
        final Button loginBtn = findViewById(R.id.loginBtn);
        final TextView registerNowBtn = findViewById(R.id.registerNowBtn);

        loginBtn.setOnClickListener(v -> {

            final String phoneTxt = phone.getText().toString();
            final String passwordTxt = password.getText().toString();

            if(phoneTxt.isEmpty() || passwordTxt.isEmpty()){
                Toast.makeText(activity_login.this, "Please enter your mobile or password", Toast.LENGTH_SHORT).show();
            }
            else{

                databaseReference.child("users").addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {

                        // check if mobile/phone is exist in firebase database
                        if(snapshot.hasChild(phoneTxt)){

                            // mobile is exist in firebase database
                            // now get password of user from firebase data and match it with user entered password
                            final String getPassword = snapshot.child(phoneTxt).child("password").getValue(String.class);

                            if(getPassword.equals(passwordTxt)){
                                Toast.makeText(activity_login.this, "Successfully Logged in", Toast.LENGTH_SHORT).show();

                                // open MainActivity on success
                                startActivity(new Intent(activity_login.this, MainActivity.class));
                                finish();
                            }
                            else{
                                Toast.makeText(activity_login.this, "Wrong Password", Toast.LENGTH_SHORT).show();
                            }
                        }
                        else{
                            Toast.makeText(activity_login.this, "Wrong Mobile Number", Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
            }
        });

        registerNowBtn.setOnClickListener(v -> {

            // open Register activity
            startActivity(new Intent(activity_login.this, activity_register.class));
        });
    }
}