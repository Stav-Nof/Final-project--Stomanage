package com.SandY.stomanage.dataObject;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import java.io.Serializable;

public class UserObj implements Serializable {
    private String firstName;
    private String lastName;
    private String email;
    private String tid;
    private String userPerm;
    private String Responsibility;

    public UserObj(){}

    public UserObj(String fname, String lname, String email, String tid, String userPerm){
        this.firstName = fname;
        this.lastName = lname;
        this.email = email;
        this.tid = tid;
        this.userPerm = userPerm;
        this.Responsibility = null;
    }

    public void WriteToDB(String Uid){
        DatabaseReference DBRef = DBRef = FirebaseDatabase.getInstance().getReference();
        DBRef.child("Users").child(Uid).setValue(this);
    }

    public void updateToDB(String Uid){
        DatabaseReference DBRef = FirebaseDatabase.getInstance().getReference();
        DBRef = DBRef.child("Users").child(Uid);
        DBRef.setValue(this);
    }

    public static void deletFromDB(String Uid){
        DatabaseReference DBRef = FirebaseDatabase.getInstance().getReference();
        DBRef = DBRef.child("Users").child(Uid);
        DBRef.setValue(null);
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getUserPerm() {
        return userPerm;
    }

    public void setUserPerm(String userPerm) {
        this.userPerm = userPerm;
    }

    public String getResponsibility() {
        return Responsibility;
    }

    public void setResponsibility(String responsibility) {
        Responsibility = responsibility;
    }

    public String getTid() {
        return tid;
    }

    public void setTid(String tid) {
        this.tid = tid;
    }
}
