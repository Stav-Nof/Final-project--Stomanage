package com.SandY.stomanage.HeadWarehouseTeam;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import com.SandY.stomanage.Guider.MyOrders;
import com.SandY.stomanage.HeadChapter.WarehouseItemList;
import com.SandY.stomanage.R;
import com.SandY.stomanage.dataObject.UserObj;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import net.cachapa.expandablelayout.ExpandableLayout;


public class HeadWarehouseTeamMainMenu extends AppCompatActivity {

    TextView _name;
    CardView _storekeeper, _equipment, _orders;

    UserObj user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_headwarehouseteam_main_menu);

        ExpandableLayout test = findViewById(R.id.expandable_layout_0);
        findViewById(R.id.expand_button_0).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                test.expand();
            }
        });

        attachFromXml();
        modifyActivity();
        setOnClickListeners();

    }

    private void attachFromXml() {
        _name = findViewById(R.id.name);
        _storekeeper = findViewById(R.id.storekeeperCard);
        _equipment = findViewById(R.id.equipmentCard);
        _orders = findViewById(R.id.ordersCard);
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
                DatabaseReference DBRef = FirebaseDatabase.getInstance().getReference().child("chapters").child(user.getCid());
                DBRef.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        Intent intent = new Intent(HeadWarehouseTeamMainMenu.this, WarehouseItemList.class);
                        intent.putExtra("cid", user.getCid());
                        startActivity(intent);
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                        //TODO set error
                    }
                });

            }
        });

        _orders.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DatabaseReference DBRef = FirebaseDatabase.getInstance().getReference().child("chapters").child(user.getCid());
                DBRef.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        Intent intent = new Intent(HeadWarehouseTeamMainMenu.this, MyOrders.class);
                        intent.putExtra("uid", FirebaseAuth.getInstance().getCurrentUser().getUid());
                        intent.putExtra("cid", user.getCid());
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