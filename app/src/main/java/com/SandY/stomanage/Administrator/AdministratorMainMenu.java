package com.SandY.stomanage.Administrator;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import com.SandY.stomanage.R;

public class AdministratorMainMenu extends AppCompatActivity {

    TextView _name;
    CardView _troops, _users, _equipment, _warehouses;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_administrator_main_menu);

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
        _name.setText(getResources().getString(R.string.welcome) + " name");//TODO
    }

    private void setOnClickListeners() {
        _troops.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(AdministratorMainMenu.this, Troops.class);
                startActivity(intent);
            }
        });

        _users.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(AdministratorMainMenu.this, Users.class);
                startActivity(intent);
            }
        });

        _equipment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(AdministratorMainMenu.this, Equipment.class);
                startActivity(intent);
            }
        });

        _warehouses.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(AdministratorMainMenu.this, TroopSelectToWarehouses.class);
                startActivity(intent);
            }
        });
    }
}