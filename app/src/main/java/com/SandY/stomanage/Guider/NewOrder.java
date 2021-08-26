package com.SandY.stomanage.Guider;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import com.SandY.stomanage.R;
import com.SandY.stomanage.dataObject.DateObj;
import com.SandY.stomanage.dataObject.OrderObj;
import java.time.LocalDate;


public class NewOrder extends AppCompatActivity {

    ImageButton _new;
    EditText _name;
    DatePicker _date;

    String uid;
    String cid;

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_order);

        Intent intent = getIntent();
        cid = intent.getStringExtra("cid");
        uid = intent.getStringExtra("uid");

        attachFromXml();
        LocalDate date = LocalDate.now().plusDays(1);
        _date.updateDate(date.getYear(), date.getMonthValue() - 1, date.getDayOfMonth());
        setClicks();
    }

    private void attachFromXml() {
        _new = findViewById(R.id.createNew);
        _name = findViewById(R.id.name);
        _date = findViewById(R.id.date);
    }


    @RequiresApi(api = Build.VERSION_CODES.O)
    private void setClicks(){
        _date.setOnDateChangedListener(new DatePicker.OnDateChangedListener() {
            @Override
            public void onDateChanged(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                LocalDate now = LocalDate.now();
                LocalDate selected = LocalDate.of(year, monthOfYear + 1, dayOfMonth);
                if (selected.isAfter(now));
                else{
                    Toast.makeText(NewOrder.this, getResources().getString(R.string.invalid_date), Toast.LENGTH_LONG).show();
                    LocalDate dateToSet = now.plusDays(1);
                    _date.updateDate(dateToSet.getYear(), dateToSet.getMonthValue() - 1, dateToSet.getDayOfMonth());
                }
            }
        });
        _new.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (_name.getText().toString().isEmpty()){
                    _name.setText(_date.getDayOfMonth() + "-" + (_date.getMonth() + 1) + "-" + _date.getYear());
                }
                DateObj date = new DateObj( _date.getDayOfMonth(), (_date.getMonth() + 1), _date.getYear());
                OrderObj order = new OrderObj(_name.getText().toString(), date);
                String oid = order.WriteToDB(cid, uid);
                Intent intent = new Intent(NewOrder.this, EditOrder.class);
                intent.putExtra("uid", uid);
                intent.putExtra("cid", cid);
                intent.putExtra("oid", oid);
                startActivity(intent);
                finish();
            }
        });
    }
}