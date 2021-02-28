package com.SandY.stomanage.Administrator;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

import com.SandY.stomanage.MainActivity;
import com.SandY.stomanage.R;

public class AdministratorMainMenu extends AppCompatActivity {

    Button troop;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_administrator_main_menu);

        attachFromXml();
        setOnClickListeners();

    }

    private void attachFromXml() {
        troop = (Button)findViewById(R.id.troopButtob);
    }

    private void setOnClickListeners() {
        troop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(AdministratorMainMenu.this, Troop.class);
                startActivity(intent);
            }
        });
    }
}