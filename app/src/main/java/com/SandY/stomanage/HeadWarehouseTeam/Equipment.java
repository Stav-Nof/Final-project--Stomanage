package com.SandY.stomanage.HeadWarehouseTeam;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import com.SandY.stomanage.Adapters.AdapterTextX3;
import com.SandY.stomanage.R;
import com.SandY.stomanage.dataObject.ItemObj;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import java.util.ArrayList;

public class Equipment extends AppCompatActivity {

    EditText _search;
    ListView _itemslist;
    TextView _header;
    ImageButton _clear;

    String cid;

    ArrayList<String> itemsKeys, quantity, itemName, suppliers;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.template_activity_listview_search);

        Intent intent = getIntent();
        cid = intent.getStringExtra("cid");

        attachFromXml();
        modifyActivity();
        printItemList(_search.getText().toString());
        //setClicks();
        searchAction();
    }

    protected void onResume() {
        super.onResume();
        printItemList(_search.getText().toString());
    }

    private void attachFromXml() {
        _search = findViewById(R.id.searchText);
        _itemslist = findViewById(R.id.itemslist);
        _header = findViewById(R.id.header);
        _clear = findViewById(R.id.clear);
    }

    private void modifyActivity(){
        _header.setText(getResources().getString(R.string.equipment));
        _search.setHint(getResources().getString(R.string.chapters_name));
        _clear.setVisibility(View.INVISIBLE);
    }

    private void printItemList(String search) {
        DatabaseReference DBRef = FirebaseDatabase.getInstance().getReference().child("Warehouses").child(cid);
        DBRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                itemsKeys = new ArrayList<>();
                quantity = new ArrayList<>();
                itemName = new ArrayList<>();
                suppliers = new ArrayList<>();
                for (DataSnapshot ds : snapshot.getChildren()){
                    itemsKeys.add(ds.getKey());
                    ItemObj item = ds.getValue(ItemObj.class);
                    quantity.add(item.get_quantity() + "");
                    itemName.add(item.get_name());
                    suppliers.add(item.get_supplier());
                }
                AdapterTextX3 adapter = new AdapterTextX3(Equipment.this, itemName, suppliers ,quantity);
                _itemslist.setAdapter(adapter);
            }

            @Override
            public void onCancelled(DatabaseError error) {
                //TODO set error
            }
        });
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
