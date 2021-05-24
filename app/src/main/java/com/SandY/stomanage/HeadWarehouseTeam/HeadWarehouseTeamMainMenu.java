package com.SandY.stomanage.HeadWarehouseTeam;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import com.SandY.stomanage.Administrator.Warehouses;
import com.SandY.stomanage.R;
import com.SandY.stomanage.dataObject.UserObj;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class HeadWarehouseTeamMainMenu extends AppCompatActivity {

    TextView _name;
    CardView _storekeeper, _equipment;

    UserObj user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_headwarehouseteam_main_menu);

        attachFromXml();
        modifyActivity();
        setOnClickListeners();

    }

    private void attachFromXml() {
        _name = findViewById(R.id.name);
        _storekeeper = findViewById(R.id.storekeeperCard);
        _equipment = findViewById(R.id.equipmentCard);
    }

    private void modifyActivity(){
        DatabaseReference DBRef = FirebaseDatabase.getInstance().getReference().child("Users").child(FirebaseAuth.getInstance().getCurrentUser().getUid());
        DBRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                user = dataSnapshot.getValue(UserObj.class);
                _name.setText(getResources().getString(R.string.welcome)  + " " + user.getFirstName());
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                _name.setText(getResources().getString(R.string.welcome));
            }
        });
    }

    private void setOnClickListeners() {
        _storekeeper.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(HeadWarehouseTeamMainMenu.this, Storekeepers.class);
                startActivity(intent);
            }
        });

        _equipment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DatabaseReference DBRef = FirebaseDatabase.getInstance().getReference().child("Troops").child(user.getCid());
                DBRef.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        Intent intent = new Intent(HeadWarehouseTeamMainMenu.this, Warehouses.class);
                        intent.putExtra("troopName", snapshot.child("_name").getValue(String.class));
                        intent.putExtra("tid", user.getCid());
                        startActivity(intent);
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                        //TODO set error
                    }
                });

            }
        });

    }
}