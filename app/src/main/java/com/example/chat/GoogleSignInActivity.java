package com.example.chat;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthCredential;
import com.google.firebase.auth.GoogleAuthProvider;

public class GoogleSignInActivity extends AppCompatActivity implements
        View.OnClickListener {


    private final String TAG="GoogleSignInActivity";

    private final int SIGN_IN_REQUEST_CODE=9001;

    private FirebaseAuth firebaseAuth;

    private GoogleSignInClient mGoogleSignInClient;
    private TextView mStatusTextView;
    private TextView mDetailTextView;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_google_sign_in);

/*
        mStatusTextView = findViewById(R.id.status);
        mDetailTextView = findViewById(R.id.detail);
*/

        // Button listeners
        findViewById(R.id.sign_in_button).setOnClickListener(this);
        findViewById(R.id.signOutButton).setOnClickListener(this);
        findViewById(R.id.disconnectButton).setOnClickListener(this);


        //configure google signin
        GoogleSignInOptions gso=new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken("713995705042-40tbmlep9gbth7ru02v4dd4ovmktj4j0.apps.googleusercontent.com")
                .requestEmail()
                .build();

        mGoogleSignInClient= GoogleSignIn.getClient(this,gso);

        firebaseAuth=FirebaseAuth.getInstance();
    }

    @Override
    public void onStart() {
        super.onStart();
        // Check if user is signed in (non-null) and update UI accordingly.
        FirebaseUser currentUser = firebaseAuth.getCurrentUser();
        updateUI(currentUser);
    }

    private void updateUI(FirebaseUser currentUser) {
if(currentUser!=null) {
    findViewById(R.id.sign_in_button).setVisibility(View.GONE);
    findViewById(R.id.signOutAndDisconnect).setVisibility(View.VISIBLE);
}
         else {

            findViewById(R.id.sign_in_button).setVisibility(View.VISIBLE);
            findViewById(R.id.signOutAndDisconnect).setVisibility(View.GONE);
        }

    }


    @Override
    public void onClick(View v) {

        int id=v.getId();
        if(id==R.id.sign_in_button){
        signIn();

        }else if(id==R.id.signOutButton){


        }else if(id==R.id.disconnectButton){


        }


    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode==SIGN_IN_REQUEST_CODE){

           /* GoogleSignInResult result = Auth.GoogleSignInApi.getSignInResultFromIntent(data);
            if(result.isSuccess()){
                GoogleSignInAccount account = result.getSignInAccount();
                firebaseAuthwithGoogle(account);

            }
            else{
                Log.e(TAG,"SignIn failed");
                updateUI(null);
            }
           */
           Task<GoogleSignInAccount> task=GoogleSignIn.getSignedInAccountFromIntent(data);

            try{

                //Google Sign was Successful , authenticate with Firebase
                GoogleSignInAccount account=task.getResult(ApiException.class);
                firebaseAuthwithGoogle(account);
            }catch (ApiException e){

                     //Google sign in failed
                Log.e(TAG,"SignIn failed",e);
                updateUI(null);


            }

        }

    }

    private void firebaseAuthwithGoogle(GoogleSignInAccount account) {

        Log.e(TAG,"firebase auth with googel "+account.getId());


        AuthCredential credential= GoogleAuthProvider.getCredential(account.getIdToken(),null);
        firebaseAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if(task.isComplete()){
                            Log.e(TAG,"Sign In with Google success");
                            FirebaseUser user=firebaseAuth.getCurrentUser();
                            updateUI(user);

                        }else if(task.isCanceled()){
                            Log.e(TAG,"Sign In with Google canceled");
                            //Snackbar.make(R.id., "Authentication Failed.", Snackbar.LENGTH_SHORT).show();
                            updateUI(null);



                        }else{

                            Log.e(TAG,"Sign In with Google Failed"+task.getException());
                            updateUI(null);
                        }

                    }
                });



    }

    private void signOut() {
        // Firebase sign out
        firebaseAuth.signOut();

        // Google sign out
        mGoogleSignInClient.signOut().addOnCompleteListener(this,
                new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        updateUI(null);
                    }
                });
    }

    private void revokeAccess() {
        // Firebase sign out
        firebaseAuth.signOut();

        // Google revoke access
        mGoogleSignInClient.revokeAccess().addOnCompleteListener(this,
                new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        updateUI(null);
                    }
                });
    }

    private void signIn() {


        Intent signinIntent=mGoogleSignInClient.getSignInIntent();
        startActivityForResult(signinIntent,SIGN_IN_REQUEST_CODE);
    }
}
