package com.example.chat;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class MainActivity extends AppCompatActivity {

    private FirebaseUser user;
    private FirebaseAuth firebaseAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);





    }

    @Override
    protected void onStart() {
        super.onStart();
        firebaseAuth=FirebaseAuth.getInstance();
           user=firebaseAuth.getCurrentUser();
 if(user!=null){

     Intent intent=new Intent(MainActivity.this,Home.class);
     startActivity(intent);

 }else{

     Intent intent=new Intent(MainActivity.this, GoogleSignInActivity.class);
     startActivity(intent);
     finish();
 }
    }
}
