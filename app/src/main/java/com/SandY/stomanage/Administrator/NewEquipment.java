package com.SandY.stomanage.Administrator;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.SandY.stomanage.R;
import com.SandY.stomanage.dataObject.EquipmentObj;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import de.hdodenhof.circleimageview.CircleImageView;

public class NewEquipment extends AppCompatActivity {

    public static final int CAMERA_PERMISSION = 489;
    public static final int USING_CAMERA_CODE = 812;

    CircleImageView _equipmentImage;
    EditText _name, _supplier;
    CheckBox _returnedable;
    ImageButton _add;

    Uri uri = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_equipment);

        attachFromXml();
        setClicks();
    }

    private void attachFromXml(){
        _equipmentImage = findViewById(R.id.equipmentImage);
        _name = findViewById(R.id.name);
        _returnedable = findViewById(R.id.returnedable);
        _supplier = findViewById(R.id.supplier);
        _add = findViewById(R.id.createNew);

    }


    private void setClicks() {
        _equipmentImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (ContextCompat.checkSelfPermission(NewEquipment.this, Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED){
                    Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                    startActivityForResult(intent, USING_CAMERA_CODE);
                }
                else{
                    if (ActivityCompat.shouldShowRequestPermissionRationale(NewEquipment.this, Manifest.permission.CAMERA)){
                        new AlertDialog.Builder(NewEquipment.this)
                                .setTitle(getResources().getString(R.string.perm_needed))
                                .setMessage(getResources().getString(R.string.storage_perm_message_read))
                                .setPositiveButton(getResources().getString(R.string.ok), new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        ActivityCompat.requestPermissions(NewEquipment.this, new String[] {Manifest.permission.CAMERA}, CAMERA_PERMISSION);
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
                        ActivityCompat.requestPermissions(NewEquipment.this, new String[] {Manifest.permission.CAMERA}, CAMERA_PERMISSION);
                    }
                }
            }
        });

        _add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (_name.getText().toString().isEmpty()) {
                    _name.setError(getResources().getString(R.string.class_name));
                    _name.requestFocus();
                    return;
                }
                if (_supplier.getText().toString().isEmpty()) {
                    _supplier.setError(getResources().getString(R.string.class_name));
                    _supplier.requestFocus();
                    return;
                }

                ProgressDialog progressDialog = new ProgressDialog(NewEquipment.this);
                progressDialog.setTitle(getResources().getString(R.string.uploading));
                progressDialog.show();

                EquipmentObj equipment = new EquipmentObj(_name.getText().toString(), _returnedable.isChecked(), _supplier.getText().toString());
                equipment.WriteToDB();

                if (uri != null) {
                    StorageReference storageRef = FirebaseStorage.getInstance().getReference();
                    storageRef = storageRef.child("Equipment").child(equipment.get_name() + ".png");
                    storageRef.putFile(uri).addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {
                            if (task.isSuccessful()) {
                                Toast.makeText(NewEquipment.this, getResources().getString(R.string.uploading_success), Toast.LENGTH_LONG).show();
                                progressDialog.dismiss();
                                finish();
                            } else {
                                String message = task.getException().getMessage();
                                Toast.makeText(NewEquipment.this, "Error Occurred: " + message, Toast.LENGTH_SHORT).show();
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
        });

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == USING_CAMERA_CODE  && resultCode == RESULT_OK){
            Bitmap bitmap = (Bitmap) data.getExtras().get("data");
            String path = MediaStore.Images.Media.insertImage(NewEquipment.this.getContentResolver(), bitmap, "Title", null);
            uri = Uri.parse(path);
            _equipmentImage.setImageURI(uri);
        }
        else Toast.makeText(NewEquipment.this, "Action canceled", Toast.LENGTH_SHORT).show();
    }
}