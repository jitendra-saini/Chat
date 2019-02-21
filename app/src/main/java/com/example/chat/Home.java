package com.example.chat;

import android.content.Intent;
import android.icu.text.SimpleDateFormat;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.ui.auth.AuthUI;
import com.firebase.ui.database.FirebaseListAdapter;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;

import java.text.DateFormat;

public class Home extends AppCompatActivity {
    private FirebaseListAdapter<ChatMessage> adapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab=findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                EditText input = (EditText)findViewById(R.id.input);

                // Read the input field and push a new instance
                // of ChatMessage to the Firebase database
                FirebaseDatabase.getInstance()
                        .getReference()
                        .push()
                        .setValue(new ChatMessage(input.getText().toString(),
                                FirebaseAuth.getInstance()
                                        .getCurrentUser()
                                        .getEmail())
                        );

                // Clear the input
                input.setText("");
            }
        });


        ListView listView=findViewById(R.id.list_of_messages);
        adapter =new FirebaseListAdapter<ChatMessage>(this,ChatMessage.class,R.layout.message,FirebaseDatabase.getInstance().getReference()) {
            @Override
            protected void populateView(View v, ChatMessage model, int position) {
                RelativeLayout relativeLayout=v.findViewById(R.id.layout);
                TextView textView=v.findViewById(R.id.message_text);
                TextView textView1=v.findViewById(R.id.message_user);
                TextView textView2=v.findViewById(R.id.message_time);

               /* if (model.getMessageUser().equals(FirebaseAuth.getInstance().getCurrentUser().getEmail().toString())){
                       relativeLayout.setGravity(View.FOCUS_RIGHT);

                }*/
                //set text
                textView.setText(model.getMessageText());
                textView1.setText(model.getMessageUser());
                DateFormat dateFormat = android.text.format.DateFormat.getDateFormat(getApplicationContext());

                textView2.setText(dateFormat.format(model.getMessageTime()));
            }
        };

listView.setAdapter(adapter);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu item) {
        getMenuInflater().inflate(R.menu.toolbar_menu,item);

        return super.onCreateOptionsMenu(item);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(item.getItemId()== R.id.menu_sign_out) {
          FirebaseAuth.getInstance().signOut();
                            Toast.makeText(Home.this,
                                    "You have been signed out.",
                                    Toast.LENGTH_LONG)
                                    .show();

                            // Close activity
                            finish();
            Intent intent =new Intent(Home.this,MainActivity.class);
            startActivity(intent);

        }

        return super.onOptionsItemSelected(item);
    }
}
