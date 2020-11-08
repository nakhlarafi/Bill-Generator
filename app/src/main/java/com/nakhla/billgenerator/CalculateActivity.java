package com.nakhla.billgenerator;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.DialogFragment;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.Parcelable;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;

public class CalculateActivity extends AppCompatActivity implements View.OnClickListener, DatePickerDialog.OnDateSetListener {

    LinearLayout layoutList;
    EditText dummy;
    Button buttonAdd,buttonSubmitList;
    //List<String> teamList = new ArrayList<>();
    public int serialNo = 0;
    EditText ed;
    List<EditText> allEds = new ArrayList<EditText>();
    HashMap<String, String> hashMap;
    String dateSet = "";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_calculate);

        Intent intent = getIntent();
        hashMap = (HashMap<String, String>)intent.getSerializableExtra("map");
        //Log.v("HashMapTest", hashMap.get("key"));

        layoutList = findViewById(R.id.layout_list);
        buttonAdd = findViewById(R.id.button_add);
        buttonSubmitList = findViewById(R.id.button_submit_list);

        buttonAdd.setOnClickListener(this);
    }


    public void showDatePickerDialog(){
        DatePickerDialog datePickerDialog = new DatePickerDialog(
                this,
                this,
                Calendar.getInstance().get(Calendar.YEAR),
                Calendar.getInstance().get(Calendar.MONTH),
                Calendar.getInstance().get(Calendar.DAY_OF_MONTH));
        datePickerDialog.show();
    }

    @Override
    public void onDateSet(DatePicker datePicker, int i, int i1, int i2) {
        dateSet = "" + i2 + "/" + (i1+1) + "/" + i;
        dummy.setText(dateSet);
        System.out.println(dateSet+"!!!!!!!!!!!!!");
    }


    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR1)
    @Override
    public void onClick(View v) {
        addView();
    }

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR1)
    private void addView() {
        final View addViewCalc = getLayoutInflater().inflate(R.layout.row_add_value,null,false);

        TextView serialText = (EditText) addViewCalc.findViewById(R.id.serial_text);
        ++serialNo;
        serialText.setText(Integer.toString(serialNo));
        serialText.setEnabled(false);
        final EditText editTextDate = (EditText) addViewCalc.findViewById(R.id.edit_date);
        TextView textViewDate = (TextView) addViewCalc.findViewById(R.id.date_text);
        Button btnDate = (Button) addViewCalc.findViewById(R.id.date_btn);
        editTextDate.generateViewId();
        dummy = editTextDate;
        System.out.println(dateSet+"*********************565655");
        allEds.add(editTextDate);

        EditText editTextChalan = (EditText) addViewCalc.findViewById(R.id.edit_bill);
        TextView textViewChalan = (TextView) addViewCalc.findViewById(R.id.bill_text);
        editTextChalan.generateViewId();
        allEds.add(editTextChalan);


        EditText editTextVolgate = (EditText) addViewCalc.findViewById(R.id.edit_volgate);
        TextView textViewVolgate = (TextView) addViewCalc.findViewById(R.id.name_volgate_text);
        editTextVolgate.generateViewId();
        allEds.add(editTextVolgate);


        EditText editTextQuantity = (EditText) addViewCalc.findViewById(R.id.edit_quantity);
        TextView textViewQuantity = (TextView) addViewCalc.findViewById(R.id.quantity_text);
        editTextQuantity.generateViewId();
        allEds.add(editTextQuantity);


        EditText editTextRate = (EditText) addViewCalc.findViewById(R.id.edit_rate);
        TextView textViewRate = (TextView) addViewCalc.findViewById(R.id.rate_text);
        editTextRate.generateViewId();
        allEds.add(editTextRate);


        EditText editTextAmount = (EditText) addViewCalc.findViewById(R.id.edit_amount);
        TextView textViewAmount = (TextView) addViewCalc.findViewById(R.id.amount_text);
        editTextAmount.generateViewId();
        editTextAmount.setText("0");
        editTextAmount.setEnabled(false);
        allEds.add(editTextAmount);


        EditText editTextRemark = (EditText) addViewCalc.findViewById(R.id.edit_remark);
        TextView textViewRemark = (TextView) addViewCalc.findViewById(R.id.remark_text);
        editTextRemark.generateViewId();
        allEds.add(editTextRemark);


        ImageView imageClose = (ImageView) addViewCalc.findViewById(R.id.img_remove);

        imageClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                removeView(addViewCalc);
            }
        });

        btnDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showDatePickerDialog();
            }
        });

        layoutList.addView(addViewCalc);
    }


    public void setButtonSubmitList(View view){
        int sl = 1;
        String arr[] = new String[serialNo*8];

        System.out.println("************1");

        for (int c=0;c<serialNo*8;c+=8){
            arr[c] = Integer.toString(sl++);
        }
        System.out.println("************2");
        for (int i = 0,ccc=0; i < serialNo*8; i++) {
            if (i%8 != 0  && ccc<allEds.size()){
                arr[i] = allEds.get(ccc).getText().toString();
                ccc++;
                //System.out.println(arr[i]);
            }
        }
        for (int c=7;c<serialNo*8;c+=8){
            double a = Double.parseDouble(arr[c-2]);
            double b = Double.parseDouble(arr[c-3]);
            arr[c-1] = String.format("%.2f", a*b);
            //arr[c] = Integer.toString(sl++);
        }

        for (int c=0;c<serialNo*8;c++){
            System.out.println(arr[c]);
        }


        Intent intent = new Intent(CalculateActivity.this, MainActivity.class);
        intent.putExtra("string-array", arr);
        intent.putExtra("map", hashMap);
        startActivity(intent);

    }

    private void removeView(View v){
        --serialNo;
        layoutList.removeView(v);
    }



}
