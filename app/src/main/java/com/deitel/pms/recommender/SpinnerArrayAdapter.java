package com.deitel.pms.recommender;

import android.content.Context;
import android.graphics.Color;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.deitel.pms.R;

import java.util.List;

public class SpinnerArrayAdapter {

    String selectedItem;

    public ArrayAdapter<String> getArrayAdapter(Context context, List<String> interestList) {
        return new ArrayAdapter<String>(context, R.layout.spinner_item, interestList){
            @Override
            public boolean isEnabled(int position){
                // Disable the first item from Spinner
                // First item will be use for hint
                return position != 0;
            }
            @Override
            public View getDropDownView(int position, View convertView,
                                        @NonNull ViewGroup parent) {
                View view = super.getDropDownView(position, convertView, parent);
                TextView tv = (TextView) view;
                if(position == 0){
                    // Set the hint text color gray
                    tv.setTextColor(Color.GRAY);
                }
                else {
                    tv.setTextColor(Color.BLACK);
                }
                return view;
            }
        };
    }

    public void listener(Spinner spinner, Context context) {
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                String selectedItemText = (String) adapterView.getItemAtPosition(i);
                setSelectedItem(selectedItemText);
                if(i > 0){
                    // Notify the selected item text
                    Toast.makeText
                            (context, "Selected : " + selectedItemText, Toast.LENGTH_SHORT)
                            .show();
                }
            }
            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
    }

    public void setSelectedItem(String item) {
        this.selectedItem = item;
    }
    public String getSelectedItem() {
        return this.selectedItem;
    }
}
