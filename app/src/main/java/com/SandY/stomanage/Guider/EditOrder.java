package com.SandY.stomanage.Guider;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
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


public class EditOrder extends AppCompatActivity {

    ImageButton _new;
    EditText _search;
    ListView _itemslist;
    TextView _header;
    ImageButton _clear;

    String uid;
    String cid;
    String oid;

    ArrayList<String> itemsKeys;
    ArrayList<ItemObj> items;

    OrderObj order;

    ArrayList<String> itemName;
    ArrayList<String> quantity;


    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.template_activity_listview_add_search);

        Intent intent = getIntent();
        cid = intent.getStringExtra("cid");
        uid = intent.getStringExtra("uid");
        oid = intent.getStringExtra("oid");

        attachFromXml();
        modifyActivity();
        printItemList(_search.getText().toString());
        setClicks();

    }

    @Override
    protected void onResume() {
        super.onResume();
        modifyActivity();
        printItemList(_search.getText().toString());
    }

    private void attachFromXml() {
        _new = (ImageButton) findViewById(R.id.createNew);
        _search = (EditText) findViewById(R.id.searchText);
        _itemslist = (ListView) findViewById(R.id.itemslist);
        _header = (TextView) findViewById(R.id.header);
        _clear = (ImageButton) findViewById(R.id.clear);
    }

    private void modifyActivity(){
        DatabaseReference DBRef = FirebaseDatabase.getInstance().getReference().child("Orders").child(cid).child(uid).child(oid);
        DBRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                order = snapshot.getValue(OrderObj.class);
                _header.setText(order.get_name());
            }

            @Override
            public void onCancelled(DatabaseError error) {
                //TODO set error
            }
        });
        _search.setHint(getResources().getString(R.string.chapters_name));
        _clear.setVisibility(View.INVISIBLE);
    }

    private void printItemList(String search) {
        DatabaseReference DBRef = FirebaseDatabase.getInstance().getReference().child("Warehouses").child(cid);
        DBRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                itemsKeys = new ArrayList<>();
                items = new ArrayList<>();
                for (DataSnapshot ds : snapshot.getChildren()){
                    itemsKeys.add(ds.getKey());
                    items.add(ds.getValue(ItemObj.class));
                }
                if (order.get_order() != null){
                    itemName = new ArrayList<>();
                    quantity = new ArrayList<>();
                    for (int i = 0; i < itemsKeys.size(); i++){
                        if (order.get_order().containsKey(itemsKeys.get(i))){
                            if (items.get(i).get_name().contains(search)){
                                itemName.add(items.get(i).get_name());
                                quantity.add(order.get_order().get(itemsKeys.get(i)).toString());
                            }
                        }
                    }
                    AdapterTextSubTextImage adapter = new AdapterTextSubTextImage(EditOrder.this, itemName, quantity, "Equipment\\" + cid, ".png", getResources().getDrawable(R.drawable.image_not_available));
                    _itemslist.setAdapter(adapter);
                }
            }

            @Override
            public void onCancelled(DatabaseError error) {

            }
        });
    }

    private void setClicks(){
        _new.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(EditOrder.this, ItemSelectForEditOrder.class);
                intent.putExtra("cid", cid);
                intent.putExtra("uid", uid);
                intent.putExtra("oid", oid);
                startActivity(intent);
            }
        });
    }
}