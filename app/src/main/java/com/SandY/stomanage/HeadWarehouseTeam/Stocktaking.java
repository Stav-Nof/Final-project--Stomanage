package com.SandY.stomanage.HeadWarehouseTeam;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SwitchCompat;

import com.SandY.stomanage.Adapters.AdapterTextSubText;
import com.SandY.stomanage.PDF;
import com.SandY.stomanage.R;
import com.SandY.stomanage.dataObject.ItemObj;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class Stocktaking extends AppCompatActivity {

    EditText _search;
    ListView _itemslist;
    TextView _header, _close;
    ImageButton _clear;
    SwitchCompat _switch;

    String cid;

    ArrayList<String> itemsKeys, itemName, quantity, oldQuantity;
    ArrayList<ItemObj> items;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.template_activity_listview_text_button_search);

        Intent intent = getIntent();
        cid = intent.getStringExtra("cid");

        attachFromXml();
        modifyActivity();
        printItemList(_search.getText().toString());
        setClicks();
        //searchAction();

    }

    @Override
    protected void onResume() {
        super.onResume();
        modifyActivity();
        printItemList(_search.getText().toString());
    }

    private void attachFromXml() {
        _search = (EditText) findViewById(R.id.searchText);
        _itemslist = (ListView) findViewById(R.id.itemslist);
        _header = (TextView) findViewById(R.id.header);
        _clear = (ImageButton) findViewById(R.id.clear);
        _close = (TextView) findViewById(R.id.close);
        _switch = (SwitchCompat) findViewById(R.id.order_edit);
    }

    private void modifyActivity(){
        _switch.setVisibility(View.GONE);
        _header.setText(getResources().getString(R.string.stocktaking));
        _search.setHint(getResources().getString(R.string.chapters_name));
        _clear.setVisibility(View.INVISIBLE);
        _close.setText(getResources().getString(R.string.update));
    }

    private void printItemList(String search) {
        DatabaseReference DBRef = FirebaseDatabase.getInstance().getReference().child("Warehouses").child(cid);
        DBRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                itemsKeys = new ArrayList<>();
                items = new ArrayList<>();
                quantity = new ArrayList<>();
                itemName = new ArrayList<>();
                oldQuantity = new ArrayList<>();
                for (DataSnapshot ds : snapshot.getChildren()){
                    itemsKeys.add(ds.getKey());
                    ItemObj item = ds.getValue(ItemObj.class);
                    items.add(item);
                    oldQuantity.add(item.get_quantity() + "");
                    itemName.add(item.get_name() + " - " + item.get_supplier());
                    quantity.add("0.0");
                }
                AdapterTextSubText adapter = new AdapterTextSubText(Stocktaking.this, itemName, quantity);
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
                _itemslist.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        Dialog dialog = new Dialog(Stocktaking.this);
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

                        headerDialog.setText(getResources().getString(R.string.select_quantity));
                        textDialog.setText(itemName.get(position));
                        fieldDialog.setText(quantity.get(position));
                        updateDialog.setText(getResources().getString(R.string.update));

                        updateDialog.setOnClickListener(new View.OnClickListener() {
                            @RequiresApi(api = Build.VERSION_CODES.N)
                            @Override
                            public void onClick(View v) {
                                if (fieldDialog.getText().toString().isEmpty()){
                                    fieldDialog.setText("0");
                                }
                                quantity.set(position, fieldDialog.getText().toString());
                                dialog.dismiss();
                            }
                        });
                    }
                });
            }
        });

        _close.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
            @Override
            public void onClick(View v) {
                ProgressDialog dialog = ProgressDialog.show(Stocktaking.this, "", "Loading. Please wait...", true);
                dialog.show();

                //update DB
                DatabaseReference DBRef = FirebaseDatabase.getInstance().getReference().child("Warehouses").child(cid);
                for (String s : itemsKeys){
                    DBRef.child(s).child("_quantity").setValue(Double.parseDouble(quantity.get(itemsKeys.indexOf(s))));
                }
                itemName.add(0,getResources().getString(R.string.item_name));
                oldQuantity.add(0,getResources().getString(R.string.old_quantity));
                quantity.add(0,getResources().getString(R.string.new_quantity));
                ArrayList<ArrayList<String>> toPDF = new ArrayList<>();
                toPDF.add(itemName);
                toPDF.add(oldQuantity);
                toPDF.add(quantity);

                Intent intent = new Intent(Stocktaking.this, PDF.class);
                intent.putExtra("header", getResources().getString(R.string.stocktaking));
                intent.putExtra("data1", toPDF.get(0));
                intent.putExtra("data2", toPDF.get(1));
                intent.putExtra("data3", toPDF.get(2));




                dialog.dismiss();
                startActivity(intent);
                finish();


            }
        });
    }
}
