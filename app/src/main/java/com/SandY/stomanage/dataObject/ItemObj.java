package com.SandY.stomanage.dataObject;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.io.Serializable;
import java.util.HashMap;

public class ItemObj implements Serializable {

    private String _name;
    private boolean _returnedable;
    private String _supplier;
    private double _quantity;


    public ItemObj(){}

    public ItemObj(String name, boolean returnedable, String supplier){
        this._name = name;
        this._returnedable = returnedable;
        this._supplier = supplier;
        this._quantity = 0;
    }

    public void WriteToDB(String cid){
        DatabaseReference DBRef = FirebaseDatabase.getInstance().getReference();
        String iid = DBRef.push().getKey();
        DBRef = DBRef.child("Warehouses").child(cid).child(iid);
        DBRef.setValue(this);
    }

    public void updateToDB(String cid, String iid){
        DatabaseReference DBRef = FirebaseDatabase.getInstance().getReference();
        DBRef = DBRef.child("Warehouses").child(cid).child(iid);
        DBRef.setValue(this);
    }

    public static void deletFromDB(String cid, String iid){
        DatabaseReference DBRef = FirebaseDatabase.getInstance().getReference();
        DBRef = DBRef.child("Warehouses").child(cid).child(iid);
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

    public double get_quantity() { return _quantity; }

    public void set_quantity(double _quantity) { this._quantity = _quantity; }

}