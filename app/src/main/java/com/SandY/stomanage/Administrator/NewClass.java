package com.SandY.stomanage.Administrator;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.SandY.stomanage.GlobalConstants;
import com.SandY.stomanage.R;
import com.SandY.stomanage.dataObject.chapterObj;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

public class NewClass extends AppCompatActivity {

    private static final int READ_EXTERNAL_STORAGE_CODE = 698;
    private static final int PICK_AN_IMAGE_FROM_STORAGE_CODE = 334;

    CircleImageView _chapterImage;
    Spinner _leadership;
    EditText _chapterName;
    ImageButton _add;

    Uri uri = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_chapter);

        attachFromXml();
        modifyActivity();
        setClicks();
    }

    private void attachFromXml(){
        _chapterName = findViewById(R.id.chapterName);
        _chapterImage = findViewById(R.id.chapterImage);
        _add = findViewById(R.id.createNew);
        _leadership = findViewById(R.id.leadership);
    }

    private void modifyActivity(){
        GlobalConstants.leadership[]  leaderships = GlobalConstants.leadership.values();
        ArrayList<String> leadership = new ArrayList();
        leadership.add(getResources().getString(R.string.select_leadership));
        for (int i = 0; i < leaderships.length; i++){
            leadership.add(leaderships[i].toString());
        }
        ArrayAdapter<String> leadershipAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, leadership);
        _leadership.setAdapter(leadershipAdapter);


    }

    private void setClicks() {
        _chapterImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (ContextCompat.checkSelfPermission(NewClass.this, Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED){
                    Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
                    intent.setType("image/*");
                    startActivityForResult(intent, PICK_AN_IMAGE_FROM_STORAGE_CODE);
                }
                else{
                    if (ActivityCompat.shouldShowRequestPermissionRationale(NewClass.this, Manifest.permission.READ_EXTERNAL_STORAGE)){
                        new AlertDialog.Builder(NewClass.this)
                                .setTitle(getResources().getString(R.string.perm_needed))
                                .setMessage(getResources().getString(R.string.storage_perm_message_read))
                                .setPositiveButton(getResources().getString(R.string.ok), new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        ActivityCompat.requestPermissions(NewClass.this, new String[] {Manifest.permission.READ_EXTERNAL_STORAGE}, READ_EXTERNAL_STORAGE_CODE);
                                    }
                                })
                                .setNegativeButton(getResources().getString(R.string.cancel), new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        dialog.dismiss();
                                    }
                                })
                                .create().show();
                    }
                    else{
                        ActivityCompat.requestPermissions(NewClass.this, new String[] {Manifest.permission.READ_EXTERNAL_STORAGE}, READ_EXTERNAL_STORAGE_CODE);
                    }
                }
            }
        });

        _add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (_leadership.getSelectedItemPosition() == 0){
                    Toast.makeText(NewClass.this, getResources().getString(R.string.select_leadership_error), Toast.LENGTH_LONG).show();
                    return;
                }
                if (_chapterName.getText().toString().isEmpty()){
                    _chapterName.setError(getResources().getString(R.string.chapters_name));
                    _chapterName.requestFocus();
                    return;
                }


                DatabaseReference DBRef = FirebaseDatabase.getInstance().getReference();
                DatabaseReference ref = DBRef.child("chapters");
                ValueEventListener valueEventListener = new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        for(DataSnapshot ds : dataSnapshot.getChildren()) {
                            String item = ds.child("_name").getValue(String.class);
                            if (item.equals(_chapterName.getText().toString())){
                                _chapterName.setError(getResources().getString(R.string.name_already_exists));
                                _chapterName.requestFocus();
                                return;
                            }
                        }

                        ProgressDialog progressDialog = new ProgressDialog(NewClass.this);
                        progressDialog.setTitle(getResources().getString(R.string.uploading));
                        progressDialog.show();

                        chapterObj chapter = new chapterObj(_chapterName.getText().toString(), _leadership.getSelectedItem().toString());
                        chapter.WriteNewToDB();

                        if (uri != null){
                            StorageReference storageRef = FirebaseStorage.getInstance().getReference();
                            storageRef = storageRef.child("chapters").child(chapter.get_name() + ".png");
                            storageRef.putFile(uri).addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>(){
                                @Override
                                public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {
                                    if (task.isSuccessful()) {
                                        Toast.makeText(NewClass.this, getResources().getString(R.string.uploading_success), Toast.LENGTH_LONG).show();
                                        progressDialog.dismiss();
                                        finish();
                                    }
                                    else{
                                        String message = task.getException().getMessage();
                                        Toast.makeText(NewClass.this, "Error Occurred: " + message, Toast.LENGTH_SHORT).show();
                                        progressDialog.dismiss();
                                    }
                                }
                            });
                        }
                        else {
                            progressDialog.dismiss();

                            finish();
                        }
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {}
                };
                ref.addListenerForSingleValueEvent(valueEventListener);
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == PICK_AN_IMAGE_FROM_STORAGE_CODE  && resultCode  == RESULT_OK){
            uri = data.getData();
            _chapterImage.setImageURI(uri);
        }
        else Toast.makeText(NewClass.this, "Action canceled", Toast.LENGTH_SHORT).show();
    }
}