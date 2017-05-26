package com.example.vaibhavchellani.firebaseioextended;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private EditText messageEditText;
    private Button sendButton;
    private ImageView mimageview;
    private ListView messageListView;
    Uri filePath;

    private listviewAdapter mlistviewAdapter;
    private String mUsername="ANONYMOUS";
    private FirebaseAuth mFirebaseAuth;
    int PICK_IMAGE_REQUEST = 111;
    private FirebaseUser mFirebaseUser;
    private DatabaseReference mDatabaseReference;
    FirebaseStorage storage=FirebaseStorage.getInstance();
    StorageReference mStorageReference=storage.getReference();
    int i=0;

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null && data.getData() != null) {
            filePath = data.getData();

            try {
                //getting image from gallery
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), filePath);
                StorageReference childRef = mStorageReference.child("image"+i+".jpg");

                //uploading the image
                UploadTask uploadTask = childRef.putFile(filePath);

                uploadTask.addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                        Toast.makeText(MainActivity.this, "Upload successful", Toast.LENGTH_SHORT).show();

                        i++;
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(MainActivity.this, "Upload Failed -> " + e, Toast.LENGTH_SHORT).show();
                    }
                });
                //Setting image to ImageView
/*
                imgView.setImageBitmap(bitmap);
*/
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //linking xml and java
        messageEditText=(EditText) findViewById(R.id.messageEditText);
        mimageview=(ImageView)findViewById(R.id.addMessageImageView);
        sendButton=(Button) findViewById(R.id.sendButton);
        messageListView=(ListView)findViewById(R.id.messageListView);
        final List<Message> messages=new ArrayList<Message>();
        Message newMessage=new Message();
        mlistviewAdapter=new listviewAdapter(this,R.layout.row_layout,messages);
        messageListView.setAdapter(mlistviewAdapter);

        sendButton.setEnabled(false);


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

        mimageview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent();
                intent.setType("image/*");
                intent.setAction(Intent.ACTION_PICK);
                startActivityForResult(Intent.createChooser(intent,"SELECT IMAGE "),PICK_IMAGE_REQUEST);
            }
        });






    }
}
