package com.SandY.stomanage.dataObject;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.io.Serializable;
import java.util.HashMap;

public class ClassObj implements Serializable {

    private String _name;
    private String _ageGroup;


    public ClassObj(String name, String ageGroup) {
        this._name = name;
        this._ageGroup = ageGroup;

    }

    public ClassObj() { }

    public void WriteNewToDB(String chid){
        DatabaseReference DBRef = FirebaseDatabase.getInstance().getReference();
        String clid = DBRef.push().getKey();
        DBRef = DBRef.child("Classes").child(chid).child(clid);
        DBRef.setValue(this);
    }

    public static void deletFromDB(String chid, String clid){
        DatabaseReference DBRef = FirebaseDatabase.getInstance().getReference();
        DBRef = DBRef.child("Classes").child(chid).child(clid);
        DBRef.setValue(null);
    }
    public void updateDB(String chid, String clid){
        DatabaseReference DBRef = FirebaseDatabase.getInstance().getReference();
        DBRef = DBRef.child("Classes").child(chid).child(clid);
        DBRef.setValue(this);
    }

    public String get_name() {
        return _name;
    }

    public void set_name(String _name) {
        this._name = _name;
    }

    public String get_ageGroup() {
        return _ageGroup;
    }

    public void set_ageGroup(String _ageGroup) {
        this._ageGroup = _ageGroup;
    }
}