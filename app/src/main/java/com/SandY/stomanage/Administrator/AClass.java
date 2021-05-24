package com.SandY.stomanage.Administrator;


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
import com.SandY.stomanage.R;
import com.SandY.stomanage.dataObject.ClassObj;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import java.util.ArrayList;
import java.util.List;

public class AClass extends AppCompatActivity {

    private static final int WRITE_EXTERNAL_STORAGE_CODE = 399;

    ImageButton _new;
    EditText _search;
    ListView _itemslist;
    TextView  _header;
    ImageButton _clear;

    List<String> items;
    List<String> subItems;
    List<String> tids;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.template_activity_listview_add_search);

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
        if (ContextCompat.checkSelfPermission(AClass.this, Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED){return;}
        else{
            if (ActivityCompat.shouldShowRequestPermissionRationale(AClass.this, Manifest.permission.WRITE_EXTERNAL_STORAGE)){
                new AlertDialog.Builder(AClass.this)
                        .setTitle(getResources().getString(R.string.perm_needed))
                        .setMessage(getResources().getString(R.string.storage_perm_message_write))
                        .setPositiveButton(getResources().getString(R.string.ok), new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                ActivityCompat.requestPermissions(AClass.this, new String[] {Manifest.permission.WRITE_EXTERNAL_STORAGE}, WRITE_EXTERNAL_STORAGE_CODE);
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
                ActivityCompat.requestPermissions(AClass.this, new String[] {Manifest.permission.WRITE_EXTERNAL_STORAGE}, WRITE_EXTERNAL_STORAGE_CODE);
            }
        }
    }

    private void attachFromXml(){
        _new = (ImageButton) findViewById(R.id.createNew);
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

        _new.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(AClass.this, NewClass.class);
                startActivity(intent);
            }
        });

        _itemslist.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String classClicked = items.get(position);
                Intent intent = new Intent(AClass.this, Regiment.class);
                intent.putExtra("className", classClicked);
                intent.putExtra("cid", tids.get(position));
                startActivity(intent);
            }
        });

        _itemslist.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                new AlertDialog.Builder(AClass.this)
                        .setTitle(getResources().getString(R.string.delete) + " " + items.get(position) )
                        .setMessage(getResources().getString(R.string.delete_class_message))
                        .setPositiveButton(getResources().getString(R.string.delete), new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                DatabaseReference DBRef = FirebaseDatabase.getInstance().getReference();
                                DatabaseReference ref = DBRef.child("Classes");
                                ValueEventListener valueEventListener = new ValueEventListener() {
                                    @Override
                                    public void onDataChange(DataSnapshot dataSnapshot) {
                                        String tid = null;
                                        for(DataSnapshot ds : dataSnapshot.getChildren()) {
                                            ClassObj classTemp = ds.getValue(ClassObj.class);
                                            String TidTemp = ds.getKey();
                                            if (classTemp.get_name().equals(items.get(position))){
                                                tid = TidTemp;
                                                break;
                                            }
                                        }
                                        if (tid != null){
                                            ClassObj.deletFromDB(tid);
                                            printItemList(_search.getText().toString());
                                        }
                                    }

                                    @Override
                                    public void onCancelled(DatabaseError databaseError) {}
                                };
                                ref.addListenerForSingleValueEvent(valueEventListener);

                                dialog.dismiss();

                            }
                        })
                        .setNegativeButton(getResources().getString(R.string.cancel), new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                            }
                        })
                        .create().show();
                return false;
            }
        });
    }

    private void printItemList(String search){
        DatabaseReference DBRef = FirebaseDatabase.getInstance().getReference();
        DatabaseReference ref = DBRef.child("Classes");
        ValueEventListener valueEventListener = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                tids = new ArrayList<>();
                items = new ArrayList<>();
                subItems = new ArrayList<>();
                for(DataSnapshot ds : dataSnapshot.getChildren()) {
                    String key = ds.getKey();
                    ClassObj Class = ds.getValue(ClassObj.class);
                    if (Class.get_name().contains(search)){
                        tids.add(key);
                        items.add(Class.get_name());
                        subItems.add(Class.get_leadership());
                    }
                }
                AdapterTextSubTextImage adapter = new AdapterTextSubTextImage(AClass.this, items, subItems, "Classes", ".png", getResources().getDrawable(R.drawable.image_not_available));
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
