package com.SandY.stomanage.dataObject;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.HashMap;

public class OrderObj implements Serializable {

    private boolean _open;
    private boolean _taken;
    private String _name;
    private DateObj _date;
    private HashMap<String, Double> _order;

    public OrderObj() {
    }

    public OrderObj(String name, DateObj date){
        this._open = true;
        this._taken = false;
        this._name = name;
        this._date = date;
        this._order = new HashMap<>();
    }

    public String WriteToDB(String cid, String uid){
        DatabaseReference DBRef = FirebaseDatabase.getInstance().getReference();
        String oid = DBRef.push().getKey();
        DBRef = DBRef.child("Orders").child(cid).child(uid).child(oid);
        DBRef.setValue(this);
        return oid;
    }

    public void updateToDB(String cid, String uid, String oid){
        DatabaseReference DBRef = FirebaseDatabase.getInstance().getReference();
        DBRef = DBRef.child("Orders").child(cid).child(uid).child(oid);
        DBRef.setValue(this);
    }

    public static void deletFromDB(String cid, String uid, String oid){
        DatabaseReference DBRef = FirebaseDatabase.getInstance().getReference();
        DBRef = DBRef.child("Orders").child(cid).child(uid).child(oid);
        DBRef.setValue(null);
    }

    public boolean is_open() {
        return _open;
    }

    public void set_open(boolean _open) {
        this._open = _open;
    }

    public boolean is_taken() {
        return _taken;
    }

    public void set_taken(boolean _taken) {
        this._taken = _taken;
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

    public HashMap<String, Double> get_order() {
        return _order;
    }

    public void set_order(HashMap<String, Double> _order) {
        this._order = _order;
    }


}