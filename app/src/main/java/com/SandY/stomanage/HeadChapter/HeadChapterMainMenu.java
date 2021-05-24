package com.SandY.stomanage.HeadChapter;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import com.SandY.stomanage.Administrator.Equipment;
import com.SandY.stomanage.Administrator.ClassSelectToWarehouses;
import com.SandY.stomanage.Administrator.AClass;
import com.SandY.stomanage.Administrator.Users;
import com.SandY.stomanage.HeadWarehouseTeam.HCClass;
import com.SandY.stomanage.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class HeadChapterMainMenu extends AppCompatActivity {

    TextView _name;
    CardView _troops, _users, _equipment, _warehouses;

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
        _troops = (CardView)findViewById(R.id.troopCard);
        _users = (CardView)findViewById(R.id.userCard);
        _equipment = (CardView)findViewById(R.id.equipmentCard);
        _warehouses = (CardView)findViewById(R.id.warehousesCard);
    }

    private void modifyActivity(){
        DatabaseReference DBRef = FirebaseDatabase.getInstance().getReference().child("Users").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child("firstName");
        DBRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                _name.setText(getResources().getString(R.string.welcome) + " " + dataSnapshot.getValue(HCClass.class));
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                _name.setText(getResources().getString(R.string.welcome));
            }
        });
    }

    private void setOnClickListeners() {
        _troops.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(HeadChapterMainMenu.this, AClass.class);
                startActivity(intent);
            }
        });

        _users.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(HeadChapterMainMenu.this, Users.class);
                startActivity(intent);
            }
        });

        _equipment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(HeadChapterMainMenu.this, Equipment.class);
                startActivity(intent);
            }
        });

        _warehouses.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(HeadChapterMainMenu.this, ClassSelectToWarehouses.class);
                startActivity(intent);
            }
        });
    }
}