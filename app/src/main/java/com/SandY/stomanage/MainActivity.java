package com.SandY.stomanage;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import com.SandY.stomanage.Administrator.AdministratorMainMenu;
import com.SandY.stomanage.HeadWarehouseTeam.HeadWarehouseTeamMainMenu;


public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.temp);


        Button admin = findViewById(R.id.button);
        admin.setText("Administrator");
        admin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, AdministratorMainMenu.class);
                startActivity(intent);
            }
        });

        Button HeadWarehouseTeam = findViewById(R.id.button2);
        HeadWarehouseTeam.setText("Storekeeper");
        HeadWarehouseTeam.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, HeadWarehouseTeamMainMenu.class);
                startActivity(intent);
            }
        });

    }
}