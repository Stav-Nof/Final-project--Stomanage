package com.SandY.stomanage.dataObject;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.io.Serializable;
import java.util.HashMap;

public class RegimentObj implements Serializable {

    private String _name;
    private String _ageGroup;


    public RegimentObj(String name, String ageGroup) {
        this._name = name;
        this._ageGroup = ageGroup;

    }

    public RegimentObj() { }

    public void WriteNewToDB(String cid){
        DatabaseReference DBRef = FirebaseDatabase.getInstance().getReference();
        String rid = DBRef.push().getKey();
        DBRef = DBRef.child("Regiment").child(cid).child(rid);
        DBRef.setValue(this);
    }

    public static void deletFromDB(String cid, String rid){
        DatabaseReference DBRef = FirebaseDatabase.getInstance().getReference();
        DBRef = DBRef.child("Regiment").child(cid).child(rid);
        DBRef.setValue(null);
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