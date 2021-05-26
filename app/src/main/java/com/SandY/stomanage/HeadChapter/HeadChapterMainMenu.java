package com.SandY.stomanage.HeadChapter;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import com.SandY.stomanage.GlobalConstants;
import com.SandY.stomanage.R;
import com.SandY.stomanage.dataObject.ClassObj;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class HeadChapterMainMenu extends AppCompatActivity {

    TextView _name;
    CardView _chapter, _users, _warehouse, _factory, _classes, _newYear;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_headchapter_main_menu);

        attachFromXml();
        modifyActivity();
        setOnClickListeners();

    }

    private void attachFromXml() {
        _name = (TextView)findViewById(R.id.name);
        _chapter = (CardView)findViewById(R.id.chaptersCard);
        _users = (CardView)findViewById(R.id.userCard);
        _warehouse = (CardView)findViewById(R.id.arehousCard);
        _factory = (CardView)findViewById(R.id.factoriesCard);
        _classes = (CardView)findViewById(R.id.classesCard);
        _newYear = (CardView)findViewById(R.id.newYearCard);
    }

    private void modifyActivity(){
        DatabaseReference DBRef = FirebaseDatabase.getInstance().getReference().child("Users").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child("firstName");
        DBRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                _name.setText(getResources().getString(R.string.welcome) + " " + dataSnapshot.getValue(String.class));
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                _name.setText(getResources().getString(R.string.welcome));
            }
        });
    }

    private void setOnClickListeners() {
        _chapter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(HeadChapterMainMenu.this, ChapterList.class);
                startActivity(intent);
            }
        });

        _users.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DatabaseReference DBRef = FirebaseDatabase.getInstance().getReference().child("Users").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child("cid");
                DBRef.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot snapshot) {
                        Intent intent = new Intent(HeadChapterMainMenu.this, UsersList.class);
                        intent.putExtra("cid", snapshot.getValue(String.class));
                        startActivity(intent);
                    }
                    @Override
                    public void onCancelled(DatabaseError error) {
                        //TODO set error
                    }
                });
            }
        });
        _warehouse.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DatabaseReference DBRef = FirebaseDatabase.getInstance().getReference().child("Users").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child("cid");
                DBRef.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot snapshot) {
                        Intent intent = new Intent(HeadChapterMainMenu.this, WarehouseItemList.class);
                        intent.putExtra("cid", snapshot.getValue(String.class));
                        startActivity(intent);
                    }
                    @Override
                    public void onCancelled(DatabaseError error) {
                        //TODO set error
                    }
                });
            }
        });

        _classes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DatabaseReference DBRef = FirebaseDatabase.getInstance().getReference().child("Users").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child("cid");
                DBRef.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot snapshot) {
                        Intent intent = new Intent(HeadChapterMainMenu.this, Classes.class);
                        intent.putExtra("chid", snapshot.getValue(String.class));
                        startActivity(intent);
                    }
                    @Override
                    public void onCancelled(DatabaseError error) {
                        //TODO set error
                    }
                });
            }
        });

        _newYear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new AlertDialog.Builder(HeadChapterMainMenu.this)
                        .setTitle(getResources().getString(R.string.new_year_action))
                        .setMessage(getResources().getString(R.string.new_year_action_message))
                        .setPositiveButton(getResources().getString(R.string.ok), new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                                ProgressDialog progressDialog = new ProgressDialog(HeadChapterMainMenu.this);
                                progressDialog.setTitle(getResources().getString(R.string.updating));
                                progressDialog.show();
                                DatabaseReference DBRef = FirebaseDatabase.getInstance().getReference().child("Users").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child("cid");
                                DBRef.addListenerForSingleValueEvent(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                                        String chid = snapshot.getValue(String.class);
                                        DatabaseReference DBRefClasses = FirebaseDatabase.getInstance().getReference().child("Classes").child(chid);
                                        DBRefClasses.addListenerForSingleValueEvent(new ValueEventListener() {
                                            @Override
                                            public void onDataChange(@NonNull DataSnapshot snapshot) {
                                                for(DataSnapshot ds : snapshot.getChildren()) {
                                                    String key = ds.getKey();
                                                    ClassObj Class = ds.getValue(ClassObj.class);
                                                    if (Class.get_ageGroup().equals(GlobalConstants.ageGroup.ללא.toString())) continue;
                                                    else if (Class.get_ageGroup().equals(GlobalConstants.ageGroup.ד.toString())) Class.set_ageGroup(GlobalConstants.ageGroup.ה.toString());
                                                    else if (Class.get_ageGroup().equals(GlobalConstants.ageGroup.ה.toString())) Class.set_ageGroup(GlobalConstants.ageGroup.ו.toString());
                                                    else if (Class.get_ageGroup().equals(GlobalConstants.ageGroup.ו.toString())) Class.set_ageGroup(GlobalConstants.ageGroup.ז.toString());
                                                    else if (Class.get_ageGroup().equals(GlobalConstants.ageGroup.ז.toString())) Class.set_ageGroup(GlobalConstants.ageGroup.ח.toString());
                                                    else if (Class.get_ageGroup().equals(GlobalConstants.ageGroup.ח.toString())) Class.set_ageGroup(GlobalConstants.ageGroup.ט.toString());
                                                    else if (Class.get_ageGroup().equals(GlobalConstants.ageGroup.ט.toString())) Class.set_ageGroup(GlobalConstants.ageGroup.י.toString());
                                                    else if (Class.get_ageGroup().equals(GlobalConstants.ageGroup.י.toString())) Class.set_ageGroup(GlobalConstants.ageGroup.יא.toString());
                                                    else if (Class.get_ageGroup().equals(GlobalConstants.ageGroup.יא.toString())) Class.set_ageGroup(GlobalConstants.ageGroup.יב.toString());
                                                    else if (Class.get_ageGroup().equals(GlobalConstants.ageGroup.יב.toString())) {
                                                        ClassObj.deletFromDB(chid, key);
                                                        continue;
                                                    }
                                                    Class.updateDB(chid, key);
                                                }
                                                progressDialog.dismiss();
                                            }

                                            @Override
                                            public void onCancelled(@NonNull DatabaseError error) {

                                            }
                                        });
                                    }

                                    @Override
                                    public void onCancelled(@NonNull DatabaseError error) {

                                    }
                                });
                            }
                        })
                        .setNegativeButton(getResources().getString(R.string.cancel), new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                            }
                        })
                        .create().show();
            }
        });
    }
}