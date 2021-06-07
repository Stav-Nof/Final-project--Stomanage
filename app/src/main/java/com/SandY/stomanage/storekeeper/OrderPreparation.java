package com.SandY.stomanage.storekeeper;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.Switch;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import com.SandY.stomanage.Adapters.AdapterTextSubTextImage;
import com.SandY.stomanage.R;
import com.SandY.stomanage.dataObject.ItemObj;
import com.SandY.stomanage.dataObject.OrderObj;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Set;

public class OrderPreparation extends AppCompatActivity {

    EditText _search;
    ListView _itemslist;
    TextView _header;
    ImageButton _clear;
    Switch _edit;

    String uid;
    String cid;
    String oid;

    OrderObj order;

    HashMap<String, ItemObj> items;
    ArrayList<String> itemNames;
    ArrayList<String> quantity;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_preparation);

        Intent intent = getIntent();
        uid = intent.getStringExtra("uid");
        cid = intent.getStringExtra("cid");
        oid = intent.getStringExtra("oid");

        attachFromXml();
        modifyActivity();
        printItemList(_search.getText().toString());
        setClicks();
//        searchAction();
    }

    private void attachFromXml() {
        _search = findViewById(R.id.searchText);
        _itemslist = findViewById(R.id.itemslist);
        _header = findViewById(R.id.header);
        _clear = findViewById(R.id.clear);
        _edit = findViewById(R.id.order_edit);
    }

    private void modifyActivity(){
        DatabaseReference DBRef = FirebaseDatabase.getInstance().getReference().child("Orders").child(cid).child(uid).child(oid);
        DBRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                order = snapshot.getValue(OrderObj.class);
                _header.setText(String.format(getResources().getString(R.string.name_and_date), order.get_name(), order.getStringDate()));
                _edit.setChecked(order.is_open());
            }

            @Override
            public void onCancelled(DatabaseError error) {

            }
        });
        _clear.setVisibility(View.INVISIBLE);
    }

    private void printItemList(String search) {
        DatabaseReference DBRef = FirebaseDatabase.getInstance().getReference().child("Warehouses").child(cid);
        DBRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                items = new HashMap<>();
                for (DataSnapshot ds : snapshot.getChildren()) {
                    String key = ds.getKey();
                    ItemObj item = ds.getValue(ItemObj.class);
                    items.put(key, item);
                }
                itemNames = new ArrayList<>();
                quantity = new ArrayList<>();
                Set<String> keys = order.get_order().keySet();

                for (String s : keys) {
                    String itemName = items.get(s).get_name();
                    if (itemName.contains(search)) {
                        itemNames.add(itemName);
                        quantity.add(order.get_order().get(s).toString());
                    }
                }
                AdapterTextSubTextImage adapter = new AdapterTextSubTextImage(OrderPreparation.this, itemNames, quantity, "Equipment\\" + cid, ".png", getResources().getDrawable(R.drawable.image_not_available));
                _itemslist.setAdapter(adapter);
            }

            @Override
            public void onCancelled(DatabaseError error) {

            }

        });

    }

    private void setClicks(){
        _clear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                _search.setText("");
            }
        });

        _edit.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                DatabaseReference DBRef = FirebaseDatabase.getInstance().getReference().child("Orders").child(cid).child(uid).child(oid).child("_open");
                DBRef.setValue(isChecked);
            }
        });

        //TODO move order to order history ,open tabs and send notification when click on order completed
    }

    private void searchAction(){
        _search.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) { }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                printItemList(_search.getText().toString());
                if (_search.getText().toString().equals("")) _clear.setVisibility(View.INVISIBLE);
                else _clear.setVisibility(View.VISIBLE);
            }

            @Override
            public void afterTextChanged(Editable s) { }
        });
    }
}