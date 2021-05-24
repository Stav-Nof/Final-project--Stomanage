package com.SandY.stomanage.HeadWarehouseTeam;


import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import com.SandY.stomanage.Adapters.AdapterTextSubTextImage;
import com.SandY.stomanage.Administrator.Regiment;
import com.SandY.stomanage.R;
import com.SandY.stomanage.dataObject.ClassObj;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import java.util.ArrayList;
import java.util.List;

public class HCClass extends AppCompatActivity {

    private static final int WRITE_EXTERNAL_STORAGE_CODE = 399;

    EditText _search;
    ListView _itemslist;
    TextView  _header;
    ImageButton _clear;

    List<String> items;
    List<String> subItems;
    List<String> cids;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.template_activity_listview_search);

        downloadPermissions();
        attachFromXml();
        modifyActivity();
        setClicks();
        printItemList(_search.getText().toString());
        searchAction();

    }

    @Override
    protected void onResume() {
        super.onResume();
        printItemList(_search.getText().toString());
    }

    private void downloadPermissions(){
        if (ContextCompat.checkSelfPermission(HCClass.this, Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED){return;}
        else{
            if (ActivityCompat.shouldShowRequestPermissionRationale(HCClass.this, Manifest.permission.WRITE_EXTERNAL_STORAGE)){
                new AlertDialog.Builder(HCClass.this)
                        .setTitle(getResources().getString(R.string.perm_needed))
                        .setMessage(getResources().getString(R.string.storage_perm_message_write))
                        .setPositiveButton(getResources().getString(R.string.ok), new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                ActivityCompat.requestPermissions(HCClass.this, new String[] {Manifest.permission.WRITE_EXTERNAL_STORAGE}, WRITE_EXTERNAL_STORAGE_CODE);
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
                ActivityCompat.requestPermissions(HCClass.this, new String[] {Manifest.permission.WRITE_EXTERNAL_STORAGE}, WRITE_EXTERNAL_STORAGE_CODE);
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
        _header.setText(getResources().getString(R.string.classes));
        _search.setHint(getResources().getString(R.string.class_name));
        _clear.setVisibility(View.INVISIBLE);
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
                String classClicked = items.get(position);
                Intent intent = new Intent(HCClass.this, Regiment.class);
                intent.putExtra("className", classClicked);
                intent.putExtra("cid", cids.get(position));
                startActivity(intent);
            }
        });
    }

    private void printItemList(String search){
        DatabaseReference DBRef = FirebaseDatabase.getInstance().getReference();
        DatabaseReference ref = DBRef.child("Classes");
        ValueEventListener valueEventListener = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                cids = new ArrayList<>();
                items = new ArrayList<>();
                subItems = new ArrayList<>();
                for(DataSnapshot ds : dataSnapshot.getChildren()) {
                    String key = ds.getKey();
                    ClassObj Class = ds.getValue(ClassObj.class);
                    if (Class.get_name().contains(search)){
                        cids.add(key);
                        items.add(Class.get_name());
                        subItems.add(Class.get_leadership());
                    }
                }
                AdapterTextSubTextImage adapter = new AdapterTextSubTextImage(HCClass.this, items, subItems, "Class", ".png", getResources().getDrawable(R.drawable.image_not_available));
                _itemslist.setAdapter(adapter);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {}
        };
        ref.addListenerForSingleValueEvent(valueEventListener);
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
