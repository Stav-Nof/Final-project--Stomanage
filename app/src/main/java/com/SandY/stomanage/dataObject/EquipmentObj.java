package com.SandY.stomanage.dataObject;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.io.Serializable;
import java.util.HashMap;

public class EquipmentObj implements Serializable {

    private String _name;
    private boolean _returnedable;
    private String _supplier;
    private HashMap<String, String>  notes;

    public EquipmentObj(){}

    public EquipmentObj(String name, boolean returnedable, String supplier){
        this._name = name;
        this._returnedable = returnedable;
        this._supplier = supplier;
        notes = new HashMap<>();
    }

    public void WriteToDB(){
        DatabaseReference DBRef = FirebaseDatabase.getInstance().getReference();
        String Eid = DBRef.push().getKey();
        DBRef = DBRef.child("Equipment").child(Eid);
        DBRef.setValue(this);
    }

    public void updateToDB(String Eid){
        DatabaseReference DBRef = FirebaseDatabase.getInstance().getReference();
        DBRef = DBRef.child("Equipment").child(Eid);
        DBRef.setValue(this);
    }

    public static void deletFromDB(String Eid){
        DatabaseReference DBRef = FirebaseDatabase.getInstance().getReference();
        DBRef = DBRef.child("Equipment").child(Eid);
        DBRef.setValue(null);
    }

    public String get_name() {
        return _name;
    }

    public void set_name(String _name) {
        this._name = _name;
    }

    public boolean is_returnedable() {
        return _returnedable;
    }

    public void set_returnedable(boolean _returnedable) {
        this._returnedable = _returnedable;
    }

    public String get_supplier() {
        return _supplier;
    }

    public void set_supplier(String _supplier) {
        this._supplier = _supplier;
    }

    public HashMap<String, String> getNotes() {
        return notes;
    }

    public void setNotes(HashMap<String, String> notes) {
        this.notes = notes;
    }

}