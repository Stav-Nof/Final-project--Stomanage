package com.SandY.stomanage.HeadWarehouseTeam;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import com.SandY.stomanage.Guider.MyOrders;
import com.SandY.stomanage.Guider.OpenTab;
import com.SandY.stomanage.Guider.OrderHistory;
import com.SandY.stomanage.R;
import com.SandY.stomanage.dataObject.UserObj;
import com.SandY.stomanage.storekeeper.OrderList;
import com.SandY.stomanage.storekeeper.TabsList;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import net.cachapa.expandablelayout.ExpandableLayout;


public class HeadWarehouseTeamMainMenu extends AppCompatActivity {

    ExpandableLayout Guide_options, storekeeper_options;

    TextView _name;
    CardView guideOrder, orderHistory, GuideOpenTabs, storekeeperOrders, storekeeperOpenTabs, storekeeper, stocktaking, equipment, factory;
    ImageButton _logout;

    String uid;
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
        Guide_options = findViewById(R.id.Guide_options);
        storekeeper_options = findViewById(R.id.storekeeper_options);
        _name = findViewById(R.id.name);

        guideOrder = findViewById(R.id.guideOrderCard);
        orderHistory = findViewById(R.id.orderHistoryCard);
        GuideOpenTabs = findViewById(R.id.GuideOpenTabsCard);

        storekeeperOrders = findViewById(R.id.storekeeperOrdersCard);
        storekeeperOpenTabs = findViewById(R.id.storekeeperOpenTabsCard);

        storekeeper = findViewById(R.id.storekeeperCard);
        stocktaking = findViewById(R.id.stocktakingCard);
        equipment = findViewById(R.id.equipmentCard);
        factory = findViewById(R.id.FactoryCard);

        _logout = findViewById(R.id.logOut);
    }

    private void modifyActivity(){
        uid = FirebaseAuth.getInstance().getCurrentUser().getUid();

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
        findViewById(R.id.Guide_options_button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (Guide_options.isExpanded())Guide_options.collapse();
                else Guide_options.expand();
                storekeeper_options.collapse();
            }
        });

        findViewById(R.id.storekeeper_options_button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (storekeeper_options.isExpanded())storekeeper_options.collapse();
                else storekeeper_options.expand();
                Guide_options.collapse();
            }
        });

        guideOrder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(HeadWarehouseTeamMainMenu.this, MyOrders.class);
                intent.putExtra("uid", uid);
                intent.putExtra("cid", user.getCid());
                startActivity(intent);
            }
        });

        orderHistory.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(HeadWarehouseTeamMainMenu.this, OrderHistory.class);
                intent.putExtra("uid", uid);
                intent.putExtra("cid", user.getCid());
                startActivity(intent);
            }
        });

        GuideOpenTabs.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(HeadWarehouseTeamMainMenu.this, OpenTab.class);
                intent.putExtra("uid", uid);
                intent.putExtra("cid", user.getCid());
                startActivity(intent);
            }
        });

        storekeeperOrders.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(HeadWarehouseTeamMainMenu.this, OrderList.class);
                intent.putExtra("uid", uid);
                startActivity(intent);
            }
        });

        storekeeperOpenTabs.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(HeadWarehouseTeamMainMenu.this, TabsList.class);
                intent.putExtra("cid", user.getCid());
                startActivity(intent);
            }
        });

        storekeeper.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(HeadWarehouseTeamMainMenu.this, Storekeepers.class);
                startActivity(intent);
            }
        });

        stocktaking.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(HeadWarehouseTeamMainMenu.this, Stocktaking.class);
                intent.putExtra("cid", user.getCid());
                startActivity(intent);
            }
        });

        equipment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(HeadWarehouseTeamMainMenu.this, Equipment.class);
                intent.putExtra("cid", user.getCid());
                startActivity(intent);
            }
        });

        factory.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(HeadWarehouseTeamMainMenu.this, Factories.class);
                intent.putExtra("cid", user.getCid());
                startActivity(intent);
            }
        });

        _logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FirebaseAuth.getInstance().signOut();
                finish();
            }
        });
    }
}