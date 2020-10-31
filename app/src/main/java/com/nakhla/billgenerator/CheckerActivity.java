package com.nakhla.billgenerator;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.EditText;
import android.widget.TextView;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class CheckerActivity extends AppCompatActivity {
    List<EditText> allEds = new ArrayList<EditText>();
    TextView textView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_checker);

        textView = findViewById(R.id.hello);
        Intent intent = getIntent();
        String [] stringArray = intent.getStringArrayExtra("string-array");

        for (int i = 0; i < stringArray.length; i++) {
            System.out.println(stringArray[i]);
        }
    }
}
