package com.SandY.stomanage.Guider;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import com.SandY.stomanage.Adapters.AdapterTextSubText;
import com.SandY.stomanage.R;
import com.SandY.stomanage.dataObject.OrderObj;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class OrderHistory extends AppCompatActivity {

    EditText _search;
    ListView _itemslist;
    TextView _header;
    ImageButton _clear;

    String uid;
    String cid;

    ArrayList <OrderObj> orders;
    ArrayList <String> ordersKeys;

    ArrayList <String> ordersNames;
    ArrayList <String> ordersopen;
    ArrayList <String> printedOrdersKeys;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.template_activity_listview_search);

        Intent intent = getIntent();
        cid = intent.getStringExtra("cid");
        uid = intent.getStringExtra("uid");

        attachFromXml();
        modifyActivity();
        setClicks();
        printItemList(_search.getText().toString());
    }

    @Override
    protected void onResume() {
        super.onResume();
        printItemList(_search.getText().toString());
    }

    private void attachFromXml() {
        _search = (EditText) findViewById(R.id.searchText);
        _itemslist = (ListView) findViewById(R.id.itemslist);
        _header = (TextView) findViewById(R.id.header);
        _clear = (ImageButton) findViewById(R.id.clear);
    }

    private void modifyActivity(){
        _header.setText(getResources().getString(R.string.my_orders));
        _search.setHint(getResources().getString(R.string.order_name));
        _clear.setVisibility(View.INVISIBLE);
    }

    private void printItemList(String search) {
        DatabaseReference DBRef = FirebaseDatabase.getInstance().getReference();
        DatabaseReference ref = DBRef.child("Order history").child(cid).child(uid);
        ref.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                orders = new ArrayList<>();
                ordersKeys = new ArrayList<>();
                for (DataSnapshot ds : snapshot.getChildren()) {
                    orders.add(ds.getValue(OrderObj.class));
                    ordersKeys.add(ds.getKey());
                }
                ordersNames = new ArrayList<>();
                ordersopen = new ArrayList<>();
                printedOrdersKeys = new ArrayList<>();
                for (OrderObj order : orders) {
                    if (order.get_name().contains(search)) {
                        ordersNames.add(order.get_name());
                        if (order.is_open()) ordersopen.add(getResources().getString(R.string.open));
                        else ordersopen.add(getResources().getString(R.string.close));
                        printedOrdersKeys.add(ordersKeys.get(orders.indexOf(order)));
                    }
                }
                AdapterTextSubText adapter = new AdapterTextSubText(OrderHistory.this, ordersNames, ordersopen);
                _itemslist.setAdapter(adapter);
            }

            @Override
            public void onCancelled(DatabaseError error) {
                //TODO set error
            }
        });
    }


    private void setClicks(){
        _itemslist.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(OrderHistory.this, ViewOrder.class);
                intent.putExtra("path", "Order history");
                intent.putExtra("uid", uid);
                intent.putExtra("cid", cid);
                intent.putExtra("oid", printedOrdersKeys.get(position));
                startActivity(intent);
            }
        });
    }
}