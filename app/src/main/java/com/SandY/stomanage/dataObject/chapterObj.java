package com.SandY.stomanage.dataObject;

import android.app.ProgressDialog;
import android.content.ContentResolver;
import android.content.Context;
import android.graphics.Bitmap;
import android.net.Uri;
import android.provider.MediaStore;
import android.widget.Toast;

import androidx.annotation.NonNull;

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

import java.io.File;
import java.io.InputStream;
import java.io.Serializable;
import java.util.HashMap;

public class chapterObj implements Serializable {

    private String _name;
    private String _leadership;

    public chapterObj(String _name, String _leadership) {
        this._name = _name;
        this._leadership = _leadership;
    }

    public chapterObj() { }

    public void WriteNewToDB(){
        DatabaseReference DBRef = FirebaseDatabase.getInstance().getReference();
        String cid = DBRef.push().getKey();
        DBRef = DBRef.child("Classes").child(cid);
        DBRef.setValue(this);
    }

    public void updateToDB(String cid){
        DatabaseReference DBRef = FirebaseDatabase.getInstance().getReference();
        DBRef = DBRef.child("Classes").child(cid);
        DBRef.setValue(this);
    }

    public static void deletFromDB(String cid){
        DatabaseReference DBRef = FirebaseDatabase.getInstance().getReference();
        DBRef = DBRef.child("Classes").child(cid);
        DBRef.setValue(null);
    }

    public String get_leadership() {
        return _leadership;
    }

    public void set_leadership(String _leadership) {
        this._leadership = _leadership;
    }

    public String get_name() {
        return _name;
    }

    public void set_name(String _name) {
        this._name = _name;
    }

}