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

public class TroopObj implements Serializable {

    private String _name;
    private String _leadership;
    private HashMap<String, String> _regiments;
    private HashMap<String, Double> _warehouse;

    public TroopObj(String _name, String _leadership) {
        this._name = _name;
        this._leadership = _leadership;
        _regiments = new HashMap<>();
        _warehouse = new HashMap<>();
    }

    public TroopObj() { }

    public void WriteNewToDB(){
        DatabaseReference DBRef = FirebaseDatabase.getInstance().getReference();
        String tid = DBRef.push().getKey();
        DBRef = DBRef.child("Troops").child(tid);
        DBRef.setValue(this);
    }

    public void updateToDB(String tid){
        DatabaseReference DBRef = FirebaseDatabase.getInstance().getReference();
        DBRef = DBRef.child("Troops").child(tid);
        DBRef.setValue(this);
    }

    public static void deletFromDB(String tid){
        DatabaseReference DBRef = FirebaseDatabase.getInstance().getReference();
        DBRef = DBRef.child("Troops").child(tid);
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

    public HashMap<String, String> get_regiments() {
        return _regiments;
    }

    public void set_regiments(HashMap<String, String> _regiments) {
        this._regiments = _regiments;
    }

    public HashMap<String, Double> get_warehouse() {
        return _warehouse;
    }

    public void set_warehouse(HashMap<String, Double> _warehouse) {
        this._warehouse = _warehouse;
    }

    public void regimentsInitialization(){
        if(_regiments == null) {
            _regiments = new HashMap<>();
        }
        return;
    }

    public void warehouseInitialization(){
        if(_warehouse == null) {
            _warehouse = new HashMap<>();
        }
        return;
    }

}