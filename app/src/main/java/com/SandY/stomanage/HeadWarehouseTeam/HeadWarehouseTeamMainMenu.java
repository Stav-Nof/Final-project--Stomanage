package com.SandY.stomanage.HeadWarehouseTeam;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import com.SandY.stomanage.Administrator.Troops;
import com.SandY.stomanage.R;

public class HeadWarehouseTeamMainMenu extends AppCompatActivity {

    TextView _name;
    CardView _storekeeper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_headwarehouseteam_main_menu);

        attachFromXml();
        modifyActivity();
        setOnClickListeners();

    }

    private void attachFromXml() {
        _name = (TextView)findViewById(R.id.name);
        _storekeeper = (CardView)findViewById(R.id.storekeeperCard);

    }

    private void modifyActivity(){
        _name.setText(getResources().getString(R.string.welcome) + " name");//TODO
    }

    private void setOnClickListeners() {
        _storekeeper.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(HeadWarehouseTeamMainMenu.this, storekeeper.class);
                startActivity(intent);
            }
        });

    }
}