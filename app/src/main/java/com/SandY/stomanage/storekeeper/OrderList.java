package com.SandY.stomanage.storekeeper;

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
import com.SandY.stomanage.Adapters.AdapterTextSubText;
import com.SandY.stomanage.R;
import com.SandY.stomanage.dataObject.OrderObj;
import com.SandY.stomanage.dataObject.UserObj;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import java.util.ArrayList;

public class OrderList extends AppCompatActivity {

    EditText _search;
    ListView _itemslist;
    TextView _header;
    ImageButton _clear;

    String uid;
    UserObj user;

    ArrayList<String> oids;
    ArrayList<String> orderNames;
    ArrayList<String> OrderDates;


    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.template_activity_listview_search);

        Intent intent = getIntent();
        uid = intent.getStringExtra("uid");

        attachFromXml();
        modifyActivity();
        printItemList(_search.getText().toString());
        setClicks();
        searchAction();
    }

    private void attachFromXml() {
        _search = findViewById(R.id.searchText);
        _itemslist = findViewById(R.id.itemslist);
        _header = findViewById(R.id.header);
        _clear = findViewById(R.id.clear);
    }

    private void modifyActivity(){
        _header.setText(getResources().getString(R.string.order_list));
        _search.setHint(getResources().getString(R.string.chapters_name));
        _clear.setVisibility(View.INVISIBLE);
    }

    private void printItemList(String search) {
        DatabaseReference DBRef = FirebaseDatabase.getInstance().getReference().child("Users").child(uid);
        DBRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                user = snapshot.getValue(UserObj.class);

                DatabaseReference ordersRef = FirebaseDatabase.getInstance().getReference().child("Orders").child(user.getCid());
                ordersRef.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot snapshot) {
                        oids = new ArrayList<>();
                        orderNames = new ArrayList<>();
                        OrderDates = new ArrayList<>();
                        for (DataSnapshot ds : snapshot.getChildren()){
                            String uid = ds.getKey();
                            DatabaseReference userRef = FirebaseDatabase.getInstance().getReference().child("Users").child(uid);
                            userRef.addListenerForSingleValueEvent(new ValueEventListener() {
                                @Override
                                public void onDataChange(DataSnapshot userSnapshot) {
                                    UserObj tempUser = userSnapshot.getValue(UserObj.class);
                                    if (tempUser.getResponsibility().equals(user.getResponsibility())) {
                                        for (DataSnapshot orderDs : ds.getChildren()) {
                                            OrderObj order = orderDs.getValue(OrderObj.class);
                                            if (order.get_name().contains(search)){
                                                oids.add(orderDs.getKey());
                                                orderNames.add(tempUser.getFirstName() + " " + tempUser.getLastName());
                                                OrderDates.add(order.get_date().get_day() + "-" + order.get_date().get_month() + "-" + order.get_date().get_year());
                                            }
                                        }
                                    }
                                    AdapterTextSubText adapter = new AdapterTextSubText(OrderList.this, OrderDates, orderNames);
                                    _itemslist.setAdapter(adapter);
                                }

                                @Override
                                public void onCancelled(DatabaseError error) {
                                    //TODO
                                }
                            });
                        }
                    }

                    @Override
                    public void onCancelled(DatabaseError error) {

                    }
                });
            }

            @Override
            public void onCancelled(DatabaseError error) {
                //TODO set error
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