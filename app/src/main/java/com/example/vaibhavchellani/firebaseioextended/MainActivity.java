package com.example.vaibhavchellani.firebaseioextended;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.InputFilter;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.remoteconfig.FirebaseRemoteConfig;
import com.google.firebase.remoteconfig.FirebaseRemoteConfigSettings;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private EditText messageEditText;
    private Button sendButton;

    private FirebaseRemoteConfig mRemoteConfig=FirebaseRemoteConfig.getInstance();

    private ListView messageListView;
    private listviewAdapter mlistviewAdapter;
    private String mUsername="ANONYMOUS";
    private FirebaseAuth mFirebaseAuth;
    private FirebaseUser mFirebaseUser;
    private DatabaseReference mDatabaseReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //linking xml and java
        messageEditText=(EditText) findViewById(R.id.messageEditText);
        sendButton=(Button) findViewById(R.id.sendButton);
        messageListView=(ListView)findViewById(R.id.messageListView);
        final List<Message> messages=new ArrayList<Message>();
        Message newMessage=new Message();
        mlistviewAdapter=new listviewAdapter(this,R.layout.row_layout,messages);
        messageListView.setAdapter(mlistviewAdapter);

        sendButton.setEnabled(false);

        //STEP 1.  configure remote config to developer mode
        mRemoteConfig.setConfigSettings(new FirebaseRemoteConfigSettings.Builder().
                setDeveloperModeEnabled(true)
                .build()
        );

        //STEP 2. set Default Values
        HashMap<String,Object> defaults=new HashMap<>();
        //setting message lenght to a default of 10
        defaults.put("max_message_length",10);
        mRemoteConfig.setDefaults(defaults);

        //STEP 3 . use fetch to download defaults from console
        final Task<Void> fetch = mRemoteConfig.fetch(0);
        fetch.addOnSuccessListener(this, new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                mRemoteConfig.activateFetched();
                updateMaxTextLength();
            }
        });



        updateMaxTextLength();


        //getting reference to database
        mDatabaseReference= FirebaseDatabase.getInstance().getReference();


        // if edit text is empty this disables the send button .
        messageEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }
            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (charSequence.toString().trim().length() > 0) {
                    sendButton.setEnabled(true);
                } else {
                    sendButton.setEnabled(false);
                }
            }
            @Override
            public void afterTextChanged(Editable editable) {
            }
        });

        // Send button listener
        sendButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Message addNewMessage=new Message();
                addNewMessage.setUserName(mUsername+" : ");
                addNewMessage.setUserMessage(messageEditText.getText().toString());

                //the below line replaces existing data in the database , but we have to append to the list not replace
                //mDatabaseReference.child("messages").setValue(addNewMessage);

                mDatabaseReference.child("messages").push().setValue(addNewMessage);

                messageEditText.setText("");
            }
        });

        mDatabaseReference.child("messages").addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                Message newmessage=dataSnapshot.getValue(Message.class);
                messages.add(newmessage);
                mlistviewAdapter.notifyDataSetChanged();
            }
            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {
            }
            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {
            }
            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });
    }

    private void updateMaxTextLength(){
        int maxlength=(int) mRemoteConfig.getLong("max_message_length");
        /*messageEditText.setFilters(new InputFilter[]{new InputFilter.LengthFilter(10)})*/;
        messageEditText.setFilters(new InputFilter[]{new InputFilter.LengthFilter(maxlength)});
    }
}
