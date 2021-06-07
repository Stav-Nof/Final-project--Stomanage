package com.SandY.stomanage.Guider;

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
import com.SandY.stomanage.Adapters.AdapterTextSubTextImage;
import com.SandY.stomanage.R;
import com.SandY.stomanage.dataObject.ItemObj;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import java.util.ArrayList;

public class OpenTab extends AppCompatActivity {

    EditText _search;
    ListView _itemslist;
    TextView _header;
    ImageButton _clear;

    String cid, uid;

    ArrayList<String> itemsKeys;
    ArrayList<ItemObj> items;

    ArrayList<String> itemsnames;
    ArrayList<String> itemsQuantities;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_guider_main_menu);

        Intent intent = getIntent();
        cid = intent.getStringExtra("cid");
        uid = intent.getStringExtra("uid");

        attachFromXml();
        modifyActivity();
        printItemList(_search.getText().toString());
        searchAction();
    }

    private void attachFromXml() {
        _search = findViewById(R.id.searchText);
        _itemslist = findViewById(R.id.itemslist);
        _header = findViewById(R.id.header);
        _clear = findViewById(R.id.clear);
    }

    private void modifyActivity(){
        _header.setText(getResources().getString(R.string.open_tabs));
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
                DatabaseReference tabDBRef = FirebaseDatabase.getInstance().getReference().child("OpenTab").child(cid).child(uid);
                tabDBRef.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot snapshot) {
                        itemsnames = new ArrayList<>();
                        itemsQuantities = new ArrayList<>();
                        for (DataSnapshot ds : snapshot.getChildren()){
                            int index = itemsKeys.indexOf(ds.getKey());
                            if (index == -1) continue;
                            if (items.get(index).get_name().contains(search)){
                                itemsnames.add(items.get(index).get_name());
                                itemsQuantities.add(ds.getValue(Double.class).toString());
                            }
                        }
                        AdapterTextSubTextImage adapter = new AdapterTextSubTextImage(OpenTab.this, itemsnames, itemsQuantities, "Equipment\\" + cid, ".png", getResources().getDrawable(R.drawable.image_not_available));
                        _itemslist.setAdapter(adapter);
                    }
                    @Override
                    public void onCancelled(DatabaseError error) {

                    }
                });
            }
            @Override
            public void onCancelled(DatabaseError error) {

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
