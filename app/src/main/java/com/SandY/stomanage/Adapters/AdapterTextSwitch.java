package com.SandY.stomanage.Adapters;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Switch;
import android.widget.TextView;

import androidx.appcompat.widget.SwitchCompat;

import com.SandY.stomanage.R;
import java.util.List;

public class AdapterTextSwitch extends ArrayAdapter {
    private List<String> item;
    private List<Boolean> _switch;
    private Activity context;

    public AdapterTextSwitch(Activity context, List<String> itemNames, List<Boolean> _switch) {
        super(context, R.layout.layout_image_text_sub_text, itemNames);
        this.context = context;
        this.item = itemNames;
        this._switch = _switch;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View row = convertView;
        LayoutInflater inflater = context.getLayoutInflater();
        if(convertView == null)
            row = inflater.inflate(R.layout.layout_text_switch, null, true);
        TextView text = (TextView) row.findViewById(R.id.text);
        SwitchCompat switchCompat = (SwitchCompat) row.findViewById(R.id.switch1);

        text.setText(item.get(position));
        switchCompat.setChecked(_switch.get(position));


        return row;

    }

}