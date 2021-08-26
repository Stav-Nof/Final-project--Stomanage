package com.SandY.stomanage.storekeeper;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import com.SandY.stomanage.R;
import com.SandY.stomanage.dataObject.UserObj;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class StorekeeperMainMenu extends AppCompatActivity {

    TextView _name;
    CardView _orders, _tabs;
    ImageButton _logout;

    String uid;
    UserObj user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_storekeeper_main_menu);

        attachFromXml();
        modifyActivity();
        setOnClickListeners();

    }

    private void attachFromXml() {
        _name = findViewById(R.id.name);
        _orders = findViewById(R.id.ordersCard);
        _tabs = findViewById(R.id.openTabsCard);
        _logout = findViewById(R.id.logOut);
    }

    private void modifyActivity(){
        uid = FirebaseAuth.getInstance().getCurrentUser().getUid();
        DatabaseReference DBRef = FirebaseDatabase.getInstance().getReference().child("Users").child(uid);
        DBRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                user = dataSnapshot.getValue(UserObj.class);
                _name.setText(String.format(getResources().getString(R.string.welcome_messages), user.getFirstName()));
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                _name.setText(getResources().getString(R.string.welcome));
            }
        });
    }

    private void setOnClickListeners() {
        _logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FirebaseAuth.getInstance().signOut();
                finish();
            }
        });

        _orders.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(StorekeeperMainMenu.this, OrderList.class);
                intent.putExtra("uid", uid);
                startActivity(intent);
            }
        });

        _tabs.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(StorekeeperMainMenu.this, TabsList.class);
                intent.putExtra("cid", user.getCid());
                startActivity(intent);
            }
        });
    }
}