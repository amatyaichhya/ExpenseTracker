package com.dristi.kharcha;

import android.content.ContentValues;
import android.content.Intent;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;

public class Add_Lend_Borrow extends AppCompatActivity {

    EditText amount,description;
    int lbid,id;

    Button add,cancel;

    DatabaseHelper databaseHelper;

    Spinner income_spinner, spinnercategories;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add__lend__borrow);

        databaseHelper = new DatabaseHelper(this);

        add=findViewById(R.id.add);
        cancel = findViewById(R.id.cancel);
        amount=findViewById(R.id.amount);
        description=findViewById(R.id.description);
        income_spinner = findViewById(R.id.cashcredit);
        spinnercategories = findViewById(R.id.categories);

        lbid=getIntent().getIntExtra("lbid",0);
        id=getIntent().getIntExtra("id",0);

        spinnercategories.setAdapter(new Income_spinner(this, getlbspinner()));
        income_spinner.setAdapter(new Income_spinner(this, getSpinner()));

        if(id!=0){
            ExpenseInfo info = databaseHelper.getlbinfo(id);
            amount.setText(String.valueOf(info.amount));
            description.setText(info.description);
            int spinnerPosition = ((ArrayAdapter<String>)income_spinner.getAdapter()).getPosition(info.cashcredit);
            income_spinner.setSelection(spinnerPosition);

            spinnercategories.setSelection(((ArrayAdapter<String>)spinnercategories.getAdapter()).getPosition(info.category));

            add.setText("Update");
        }

        if(lbid!=1){
            spinnercategories.setSelection(1);
        }else{
            spinnercategories.setSelection(0);
        }


        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(isAmountEmpty(amount)) {
                    final String spinnerval = income_spinner.getSelectedItem().toString();
                    final String categoryval = spinnercategories.getSelectedItem().toString();

                    if (spinnerval.equals("Choose category")){
                        Toast.makeText(Add_Lend_Borrow.this,"Choose category",Toast.LENGTH_SHORT).show();
                    }
                    else{
                        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
                        String date = df.format(Calendar.getInstance().getTime());

                        String a = amount.getText().toString();
                        int amountVal = Integer.parseInt(a);

                        String descriptionVal = description.getText().toString();

                        ContentValues contentValues = new ContentValues();
                        contentValues.put("date", date);
                        contentValues.put("amount", amountVal);
                        contentValues.put("description", descriptionVal);
                        contentValues.put("category", categoryval);
                        contentValues.put("cashcredit",spinnerval);

                        if(id==0){
                            databaseHelper.insertlb(contentValues);

                            Intent intent = new Intent(Add_Lend_Borrow.this,Lend_Borrow.class);
                            startActivity(intent);
                            finish();
                        }
                        else {
                            databaseHelper.updatelb(id,contentValues);
                            Toast.makeText(Add_Lend_Borrow.this,"Updated!!",Toast.LENGTH_SHORT).show();
                            finish();
                        }
                    }
                }
            }
        });

        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (id==0){
                    Intent intent = new Intent(Add_Lend_Borrow.this,Navigation_drawer.class);
                    startActivity(intent);
                    finish();
                }else{
                    Intent intent = new Intent(Add_Lend_Borrow.this,Navigation_drawer.class);
                    startActivity(intent);
                    finish();
                }
            }
        });
    }

    ArrayList<String> spinner = new ArrayList<>();

    public ArrayList<String> getSpinner(){

        spinner.add("Choose category");
        spinner.add("Cash");
        spinner.add("Credit Card");

        return spinner;
    }

    ArrayList<String> lbspinner = new ArrayList<>();

    public ArrayList<String> getlbspinner(){

        lbspinner.add("Lend");
        lbspinner.add("Borrow");

        return lbspinner;
    }

    public boolean isAmountEmpty(EditText view) {
        if (view.getText().toString().length() > 0) {
            return true;
        } else {
            view.setError("This field cannot be empty");
            //password.setError("Enter Password");
            return false;
        }
    }
}
