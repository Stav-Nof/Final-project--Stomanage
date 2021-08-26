package com.SandY.stomanage.dataObject;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import java.io.Serializable;
import java.util.HashMap;

public class FactoryObj implements Serializable {

    private String _name;
    private DateObj _date;
    private HashMap<String, String> _classes;
    private HashMap<String, String> _order;

    public FactoryObj() {
    }

    public FactoryObj(String name, DateObj date,HashMap<String, String> classes, HashMap<String, String> order){
        this._name = name;
        this._date = date;
        this._classes = classes;
        this._order = order;
    }

    public FactoryObj(String name, DateObj date, HashMap<String, String> classes){
        this._name = name;
        this._date = date;
        this._classes = classes;
        this._order = new HashMap<>();
    }

    public String WriteToDB(String cid){
        DatabaseReference DBRef = FirebaseDatabase.getInstance().getReference();
        String fid = DBRef.push().getKey();
        DBRef = DBRef.child("Factories").child(cid).child(fid);
        DBRef.setValue(this);
        return fid;
    }

    public static void deletFromDB(String cid, String fid){
        DatabaseReference DBRef = FirebaseDatabase.getInstance().getReference();
        DBRef = DBRef.child("Factories").child(cid).child(fid);
        DBRef.setValue(null);
    }

    public String get_name() {
        return _name;
    }

    public void set_name(String _name) {
        this._name = _name;
    }

    public DateObj get_date() {
        return _date;
    }

    public void set_date(DateObj _date) {
        this._date = _date;
    }

    public HashMap<String, String> get_classes() {
        return _classes;
    }

    public void set_classes(HashMap<String, String> _classes) {
        this._classes = _classes;
    }

    public HashMap<String, String> get_order() {
        return _order;
    }

    public void set_order(HashMap<String, String> _order) {
        this._order = _order;
    }
}