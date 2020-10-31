package com.nakhla.billgenerator;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

import java.util.HashMap;

public class InfoActivity extends AppCompatActivity {

    EditText billEdit,desgEdit,nameThroughEdit,sideEdit,addEdit1,addEdit2,poEdit,postEdit;
    HashMap<String, String> capitalCities = new HashMap<String, String>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_info);

        billEdit = findViewById(R.id.edit_bill_no);
        desgEdit = findViewById(R.id.edit_to);
        nameThroughEdit = findViewById(R.id.edit_through);
        sideEdit = findViewById(R.id.edit_side);
        addEdit1 = findViewById(R.id.edit_address1);
        addEdit2 = findViewById(R.id.edit_address2);
        poEdit = findViewById(R.id.edit_po);
        postEdit = findViewById(R.id.edit_postname);
    }

    public void nextPage(View view){
        capitalCities.put("bill", billEdit.getText().toString());
        capitalCities.put("to", desgEdit.getText().toString());
        capitalCities.put("name", nameThroughEdit.getText().toString());
        capitalCities.put("side", sideEdit.getText().toString());
        capitalCities.put("add1", addEdit1.getText().toString());
        capitalCities.put("add2", addEdit2.getText().toString());
        capitalCities.put("po", poEdit.getText().toString());
        capitalCities.put("post", postEdit.getText().toString());

        Intent intent = new Intent(this, CalculateActivity.class);
        intent.putExtra("map", capitalCities);
        startActivity(intent);
    }
}
