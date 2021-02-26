package com.SandY.stomanage.dataObject;


import android.view.View;

import androidx.annotation.NonNull;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.io.Serializable;

public class UserObj implements Serializable {
    private String fname;
    private String lname;
    private String id;
    private String troop;
    private String email;
    private String phone;
    private String userPerm;
    private String Responsibility;

    public UserObj(){}

    public UserObj(String fname, String lname, String id, String troop, String email, String phone, String userPerm, String Responsibility){
        this.fname = fname;
        this.lname = lname;
        this.id = id;
        this.troop = troop;
        this.email = email;
        this.phone = phone;
        this.userPerm = userPerm;
        this.Responsibility = Responsibility;
    }

    public void WritToDB(String Uid){
        DatabaseReference DBRef = DBRef = FirebaseDatabase.getInstance().getReference();
        DBRef.child("Users").child(Uid).setValue(this);
    }

    public void ReadFromDB(String Uid){
        UserObj user = this;
        DatabaseReference DBRef = FirebaseDatabase.getInstance().getReference().child("Users").child(Uid);
        DBRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                GetUserFromSnapshot(snapshot, user);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private static void GetUserFromSnapshot(DataSnapshot snapshot, UserObj user){
        user = snapshot.getValue(UserObj.class);
    }

    public String getFname() { return fname; }

    public void setFname(String fname) {
        this.fname = fname;
    }

    public String getLname() {
        return lname;
    }

    public void setLname(String lname) {
        this.lname = lname;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTroop() {
        return troop;
    }

    public void setTroop(String troop) {
        this.troop = troop;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getUserPerm() {
        return userPerm;
    }

    public void setUserPerm(String userPerm) {
        this.userPerm = userPerm;
    }

    public String getResponsibility() { return Responsibility; }

    public void setResponsibility(String responsibility) { Responsibility = responsibility; }
}
