package com.SandY.stomanage.Guider;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
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

public class GuiderMainMenu extends AppCompatActivity {

    TextView _name;
    CardView _orders, _history, _tabs, _factories;

    String uid;
    UserObj user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.template_activity_listview_search);

        attachFromXml();
        modifyActivity();
        setOnClickListeners();

    }

    private void attachFromXml() {
        _name = findViewById(R.id.name);
        _orders = findViewById(R.id.ordersCard);
        _history = findViewById(R.id.orderHistoryCard);
        _tabs = findViewById(R.id.openTabsCard);
        _factories = findViewById(R.id.factoriesCard);
    }

    private void modifyActivity(){
        uid = FirebaseAuth.getInstance().getCurrentUser().getUid();
        DatabaseReference DBRef = FirebaseDatabase.getInstance().getReference().child("Users").child(uid);
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
        _orders.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(GuiderMainMenu.this, MyOrders.class);
                intent.putExtra("uid", uid);
                intent.putExtra("cid", user.getCid());
                startActivity(intent);
            }
        });

        _history.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(GuiderMainMenu.this, OrderHistory.class);
                intent.putExtra("uid", uid);
                intent.putExtra("cid", user.getCid());
                startActivity(intent);
            }
        });

        _history.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(GuiderMainMenu.this, OpenTab.class);
                intent.putExtra("uid", uid);
                intent.putExtra("cid", user.getCid());
                startActivity(intent);
            }
        });


    }
}

