package com.SandY.stomanage.Administrator;

import android.Manifest;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
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
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import com.SandY.stomanage.Adapters.AdapterTextSubTextImage;
import com.SandY.stomanage.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import java.util.ArrayList;
import java.util.List;

public class Warehouses extends AppCompatActivity {

    private static final int WRITE_EXTERNAL_STORAGE_CODE = 819;

    String tid;
    String troopName;

    EditText _search;
    ListView _itemslist;
    TextView _header;
    ImageButton _clear;

    List<String> Ename;
    List<String> eid;
    List<String> EnameToPrint;
    List<String> quantity;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.template_activity_listview_search);

        Intent intent = getIntent();
        troopName = intent.getStringExtra("troopName");
        tid = intent.getStringExtra("tid");

        downloadPermissions();
        attachFromXml();
        modifyActivity();
        setClicks();
        printItemList(_search.getText().toString());
        searchAction();
    }

    private void downloadPermissions(){
        if (ContextCompat.checkSelfPermission(Warehouses.this, Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED){return;}
        else{
            if (ActivityCompat.shouldShowRequestPermissionRationale(Warehouses.this, Manifest.permission.WRITE_EXTERNAL_STORAGE)){
                new AlertDialog.Builder(Warehouses.this)
                        .setTitle(getResources().getString(R.string.perm_needed))
                        .setMessage(getResources().getString(R.string.storage_perm_message_write))
                        .setPositiveButton(getResources().getString(R.string.ok), new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                ActivityCompat.requestPermissions(Warehouses.this, new String[] {Manifest.permission.WRITE_EXTERNAL_STORAGE}, WRITE_EXTERNAL_STORAGE_CODE);
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
            else{
                ActivityCompat.requestPermissions(Warehouses.this, new String[] {Manifest.permission.WRITE_EXTERNAL_STORAGE}, WRITE_EXTERNAL_STORAGE_CODE);
            }
        }
    }

    private void attachFromXml(){
        _search = (EditText) findViewById(R.id.searchText);
        _itemslist = (ListView) findViewById(R.id.itemslist);
        _header = (TextView) findViewById(R.id.header);
        _clear = (ImageButton) findViewById(R.id.clear);
    }

    private void modifyActivity(){
        _header.setText(troopName + " - " + getResources().getString(R.string.warehouses));
        _search.setHint(getResources().getString(R.string.class_name));
        _clear.setVisibility(View.INVISIBLE);
    }

    private void printItemList(String search){
        DatabaseReference DBRef = FirebaseDatabase.getInstance().getReference();
        DatabaseReference ref = DBRef.child("Equipment");
        ref.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                Ename = new ArrayList<>();
                eid = new ArrayList<>();
                for(DataSnapshot ds : snapshot.getChildren()) {
                    eid.add(ds.getKey());
                    Ename.add(ds.child("_name").getValue(String.class));
                }
                quantity = new ArrayList<>();
                EnameToPrint = new ArrayList<>();
                DatabaseReference warehousesRef = DBRef.child("Warehouses").child(tid);
                warehousesRef.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot warehousesSnapshot) {
                        for ( int i = 0; eid.size() > i ; i++ ){
                            if(Ename.get(i).contains(search)){
                                if (warehousesSnapshot.hasChild(eid.get(i))){
                                    Double quantityinDB = warehousesSnapshot.child(eid.get(i)).getValue(Double.class);
                                    quantity.add(Double.toString(quantityinDB));
                                    EnameToPrint.add(Ename.get(i));
                                }
                                else {
                                    quantity.add("0");
                                    EnameToPrint.add(Ename.get(i));
                                }
                            }
                        }
                        AdapterTextSubTextImage adapter = new AdapterTextSubTextImage(Warehouses.this, EnameToPrint, quantity, "Equipment", ".png", getResources().getDrawable(R.drawable.image_not_available));
                        _itemslist.setAdapter(adapter);
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

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

    private void setClicks(){
        _itemslist.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Dialog dialog = new Dialog(Warehouses.this);
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

                headerDialog.setText(getResources().getString(R.string.update_quantity));
                textDialog.setText(EnameToPrint.get(position));
                fieldDialog.setText(quantity.get(position));
                updateDialog.setText(getResources().getString(R.string.update));

                updateDialog.setOnClickListener(new View.OnClickListener() {
                    @RequiresApi(api = Build.VERSION_CODES.N)
                    @Override
                    public void onClick(View v) {
                        if (fieldDialog.getText().toString().isEmpty()){
                            fieldDialog.setText("0");
                        }

                        DatabaseReference DBRef = FirebaseDatabase.getInstance().getReference();
                        DatabaseReference ref = DBRef.child("Warehouses").child(tid).child(eid.get(Ename.indexOf(EnameToPrint.get(position))));
                        ref.setValue(Double.parseDouble(fieldDialog.getText().toString()));
                        dialog.dismiss();
                        printItemList(_search.getText().toString());

                    }
                });
            }
        });
    }

}
