package com.SandY.stomanage.storekeeper;

import android.app.Dialog;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import com.SandY.stomanage.Adapters.AdapterTextSubTextImage;
import com.SandY.stomanage.R;
import com.SandY.stomanage.dataObject.ItemObj;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import org.jetbrains.annotations.NotNull;
import java.util.ArrayList;

public class UserTabs extends AppCompatActivity {

    EditText _search;
    ListView _itemslist;
    TextView _header;
    ImageButton _clear;

    String cid;
    String uid;

    ArrayList<String> iids;
    ArrayList<String> Quantity;

    ArrayList<String> QuantityToPrint;
    ArrayList<String> itemNameToPrint;
    ArrayList<String> printedIids;



    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.template_activity_listview_search);

        Intent intent = getIntent();
        cid = intent.getStringExtra("cid");
        uid = intent.getStringExtra("uid");

        attachFromXml();
        modifyActivity();
        printItemList(_search.getText().toString());
        setClicks();
        searchAction();
    }

    private void attachFromXml() {
        _search = (EditText) findViewById(R.id.searchText);
        _itemslist = (ListView) findViewById(R.id.itemslist);
        _header = (TextView) findViewById(R.id.header);
        _clear = (ImageButton) findViewById(R.id.clear);
    }

    private void modifyActivity(){
        _header.setText(getResources().getString(R.string.open_tabs));
        _search.setHint(getResources().getString(R.string.chapters_name));
        _clear.setVisibility(View.INVISIBLE);
    }

    private void printItemList(String search) {
        DatabaseReference DBRef = FirebaseDatabase.getInstance().getReference().child("Open tabs").child(cid).child(uid);
        DBRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                iids = new ArrayList<>();
                Quantity = new ArrayList<>();
                QuantityToPrint = new ArrayList<>();
                itemNameToPrint = new ArrayList<>();
                printedIids = new ArrayList<>();
                for (DataSnapshot ds : snapshot.getChildren()){
                    iids.add(ds.getKey());
                    Quantity.add(ds.getValue(double.class).toString());
                }
                DatabaseReference warehousesRef = FirebaseDatabase.getInstance().getReference().child("Warehouses").child(cid);
                warehousesRef.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NotNull DataSnapshot snapshot) {
                        for (int i = 0; i < iids.size(); i++){
                            ItemObj item = snapshot.child(iids.get(i)).getValue(ItemObj.class);
                            if (item.get_name().contains(search)){
                                itemNameToPrint.add(item.get_name());
                                QuantityToPrint.add(Quantity.get(i));
                                printedIids.add(iids.get(i));
                            }
                        }
                        AdapterTextSubTextImage adapter = new AdapterTextSubTextImage(UserTabs.this, itemNameToPrint, QuantityToPrint, "Equipment\\" + cid, ".png", getResources().getDrawable(R.drawable.image_not_available));
                        _itemslist.setAdapter(adapter);
                    }

                    @Override
                    public void onCancelled(@NotNull DatabaseError error) {

                    }
                });
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

        _itemslist.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                Dialog dialog = new Dialog(UserTabs.this);
                dialog.setContentView(R.layout.popup_textview_textview_edittext_button);
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    dialog.getWindow().setBackgroundDrawable(getDrawable(R.drawable.dialog_background));
                }
                dialog.getWindow().setLayout(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                dialog.setCancelable(true);
                dialog.getWindow().getAttributes().windowAnimations = R.style.popUpAnimation;
                dialog.show();

                TextView headerDialog = dialog.findViewById(R.id.header);
                TextView textDialog = dialog.findViewById(R.id.TextView);
                EditText fieldDialog = dialog.findViewById(R.id.EditText);
                Button updateDialog = dialog.findViewById(R.id.Button);

                headerDialog.setText(itemNameToPrint.get(position));
                textDialog.setText(getResources().getString(R.string.quantity_returned));

                updateDialog.setText(getResources().getString(R.string.update));

                updateDialog.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (fieldDialog.getText().toString().isEmpty()) fieldDialog.setText("0");
                        double updatedquantity = Double.parseDouble(QuantityToPrint.get(position)) - Double.parseDouble(fieldDialog.getText().toString());
                        String key = printedIids.get(position);
                        if (updatedquantity > 0)
                            FirebaseDatabase.getInstance().getReference().child("Open tabs").child(cid).child(uid).child(key).setValue(updatedquantity);
                        else FirebaseDatabase.getInstance().getReference().child("Open tabs").child(cid).child(uid).child(key).setValue(null);
                        FirebaseDatabase.getInstance().getReference().child("Warehouses").child(cid).child(key).child("_quantity").addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot snapshot) {
                                double exists = snapshot.getValue(Double.class);
                                FirebaseDatabase.getInstance().getReference().child("Warehouses").child(cid).child(key).child("_quantity").setValue(exists + Double.parseDouble(fieldDialog.getText().toString()));
                                dialog.dismiss();
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError error) {
                            }
                        });
                    }
                });
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
