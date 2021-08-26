package com.SandY.stomanage.HeadWarehouseTeam;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import com.SandY.stomanage.Adapters.AdapterTextSubText;
import com.SandY.stomanage.Administrator.AClass;
import com.SandY.stomanage.R;
import com.SandY.stomanage.dataObject.FactoryObj;
import com.SandY.stomanage.dataObject.OrderObj;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import java.util.ArrayList;

public class Factories extends AppCompatActivity {

    ImageButton _new;
    EditText _search;
    ListView _itemslist;
    TextView _header;
    ImageButton _clear;

    String cid;

    ArrayList<String> factoriesKeys, factoriesName, factoriesDate;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.template_activity_listview_add_search);

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
        _new = (ImageButton) findViewById(R.id.createNew);
        _search = (EditText) findViewById(R.id.searchText);
        _itemslist = (ListView) findViewById(R.id.itemslist);
        _header = (TextView) findViewById(R.id.header);
        _clear = (ImageButton) findViewById(R.id.clear);
    }

    private void modifyActivity(){
        _header.setText(getResources().getString(R.string.factories));
        _search.setHint(getResources().getString(R.string.order_name));
        _clear.setVisibility(View.INVISIBLE);
    }

    private void printItemList(String search) {
        DatabaseReference DBRef = FirebaseDatabase.getInstance().getReference().child("Factories").child(cid);
        DBRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                factoriesKeys = new ArrayList<>();
                factoriesName = new ArrayList<>();
                factoriesDate = new ArrayList<>();
                for (DataSnapshot ds : snapshot.getChildren()){
                    factoriesKeys.add(ds.getKey());
                    FactoryObj factory = ds.getValue(FactoryObj.class);
                    factoriesName.add(factory.get_name());
                    factoriesDate.add(factory.get_date().toString());
                }
                AdapterTextSubText adapter = new AdapterTextSubText(Factories.this, factoriesName, factoriesDate);
                _itemslist.setAdapter(adapter);
            }

            @Override
            public void onCancelled(DatabaseError error) {
                //TODO set error
            }
        });
    }

    private void setClicks(){
        _new.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Factories.this, NewFactory.class);
                intent.putExtra("cid", cid);
                startActivity(intent);
            }
        });

        _itemslist.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                new AlertDialog.Builder(Factories.this)
                        .setTitle(factoriesName.get(position))
                        .setMessage(getResources().getString(R.string.close_factory))
                        .setPositiveButton(getResources().getString(R.string.ok), new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                                ProgressDialog dialogLoading = ProgressDialog.show(Factories.this, "", "Loading. Please wait...", true);
                                dialogLoading.show();

                                DatabaseReference DBRef = FirebaseDatabase.getInstance().getReference();
                                DBRef.addListenerForSingleValueEvent(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(DataSnapshot snapshot) {
                                        FactoryObj factory = snapshot.child("Factories").child(cid).child(factoriesKeys.get(position)).getValue(FactoryObj.class);
                                        for (String s : factory.get_order().keySet()){
                                            OrderObj order = snapshot.child("Orders").child(cid).child(s).child(factory.get_order().get(s)).getValue(OrderObj.class);
                                            FirebaseDatabase.getInstance().getReference().child("Orders").child(cid).child(s).child(factory.get_order().get(s)).setValue(null);
                                            FirebaseDatabase.getInstance().getReference().child("Order history").child(cid).child(s).child(factory.get_order().get(s)).setValue(order);
                                        }
                                        FirebaseDatabase.getInstance().getReference().child("Factories").child(cid).child(factoriesKeys.get(position)).setValue(null);
                                        dialogLoading.dismiss();
                                        printItemList(_search.getText().toString());
                                    }

                                    @Override
                                    public void onCancelled(DatabaseError error) {
                                        //TODO set error
                                    }
                                });
                            }
                        })
                        .setNegativeButton(getResources().getString(R.string.cancel), new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                            }
                        })
                        .create().show();
            }
        });
    }

}
