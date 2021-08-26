package com.SandY.stomanage.storekeeper;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SwitchCompat;
import com.SandY.stomanage.Adapters.AdapterTextSubTextImage;
import com.SandY.stomanage.R;
import com.SandY.stomanage.dataObject.ItemObj;
import com.SandY.stomanage.dataObject.OrderObj;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import org.jetbrains.annotations.NotNull;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Set;

public class OrderPreparation extends AppCompatActivity {

    EditText _search;
    ListView _itemslist;
    TextView _header, _close;
    ImageButton _clear;
    SwitchCompat _edit;

    String uid;
    String cid;
    String oid;

    OrderObj order;

    HashMap<String, ItemObj> items;
    ArrayList<String> itemNames;
    ArrayList<String> quantity;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.template_activity_listview_text_button_search);

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
        _close = findViewById(R.id.close);
    }

    private void modifyActivity(){
        DatabaseReference DBRef = FirebaseDatabase.getInstance().getReference().child("Orders").child(cid).child(uid).child(oid);
        DBRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                order = snapshot.getValue(OrderObj.class);
                _header.setText(String.format(getResources().getString(R.string.name_and_date), order.get_name(), order.getStringDate()));
                _edit.setChecked(order.is_open());
                _close.setText(getResources().getString(R.string.order_ready));
            }

            @Override
            public void onCancelled(DatabaseError error) {

            }
        });
        _clear.setVisibility(View.INVISIBLE);
    }

    private void printItemList(String search) {
        DatabaseReference DBRef = FirebaseDatabase.getInstance().getReference().child("Warehouses").child(cid);
        DBRef.addValueEventListener(new ValueEventListener() {
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

        _close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ProgressDialog dialog = ProgressDialog.show(OrderPreparation.this, "", getResources().getString(R.string.closing_order), true);
                dialog.show();

                Set<String> keys = order.get_order().keySet();
                for (String s : keys) {
                    double updatedQuantity = items.get(s).get_quantity() - order.get_order().get(s);
                    if (updatedQuantity < 0) updatedQuantity = 0;
                    FirebaseDatabase.getInstance().getReference().child("Warehouses").child(cid).child(s).child("_quantity").setValue(updatedQuantity);
                    if (items.get(s).is_returnedable()) {
                        FirebaseDatabase.getInstance().getReference().child("Open Tabs").child(cid).child(uid).child(s).addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NotNull DataSnapshot snapshot) {
                                double quantityInDB = -1;
                                if (snapshot.exists()){
                                    quantityInDB = snapshot.getValue(Double.class);
                                    quantityInDB = quantityInDB + order.get_order().get(s);
                                }
                                else quantityInDB = order.get_order().get(s);
                                FirebaseDatabase.getInstance().getReference().child("Open tabs").child(cid).child(uid).child(s).setValue(quantityInDB);
                                FirebaseDatabase.getInstance().getReference().child("Orders").child(cid).child(uid).child(oid).setValue(null);
                                order.set_open(false);
                                order.set_taken(true);
                                FirebaseDatabase.getInstance().getReference().child("Order history").child(cid).child(uid).child(oid).setValue(order);
                                dialog.dismiss();
                                finish();
                            }

                            @Override
                            public void onCancelled(@NotNull DatabaseError error) {

                            }
                        });
                    }
                }
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