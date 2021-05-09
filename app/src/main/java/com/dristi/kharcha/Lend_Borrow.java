package com.dristi.kharcha;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;

public class Lend_Borrow extends AppCompatActivity {

    TextView lend,borrow;

    ListView listView;

    DatabaseHelper databaseHelper;

    Adapter_Listview adapter;

    FloatingActionButton menu;

    EditText amount,description;

    int id, fin;

    Button add,cancel;

    Spinner income_spinner, spinnercategories;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lend__borrow);

        fin = getIntent().getIntExtra("id",0);

        if(fin == 1){
            finish();
        }

        lend = findViewById(R.id.lend);
        borrow = findViewById(R.id.borrow);

        databaseHelper = new DatabaseHelper(this);

        lend.setText(String.valueOf(databaseHelper.getlentbalance()));
        borrow.setText(String.valueOf(databaseHelper.getborrowbalance()));

        listView = findViewById(R.id.listview);
        registerForContextMenu(listView);

        databaseHelper =new DatabaseHelper(this);

        adapter = new Adapter_Listview(this,databaseHelper.getlblist());

        menu = findViewById(R.id.menu);

        menu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                add_lend_borrow();
            }
        });
    }

    private int Id;

    @Override
    public boolean onContextItemSelected(MenuItem item) {
        switch(item.getOrder()) {

            case 1:
                Intent intent = new Intent(Lend_Borrow.this,Add_Lend_Borrow.class);
                Id = item.getGroupId();
                intent.putExtra("id",Id);
                startActivity(intent);
                break;

            case 2:
                Id = item.getGroupId();

                //databaseHelper.deletelb(id);
                //expadapt();

                AlertDialog.Builder builder = new AlertDialog.Builder(this);

                builder.setTitle("Delete item !!");
                builder.setMessage("Are you sure you want to delete this?");

                builder.setPositiveButton("YES", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        databaseHelper.deletelb(Id);
                    }
                });

                builder.setNegativeButton("CANCEL", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                });

                expadapt();

                builder.show();
                // remove stuff here
                break;

            case 3:
                Id = item.getGroupId();
                showInstallDialog(Id);

            default:
                return super.onContextItemSelected(item);
        }
        return true;
    }

    public void add_lend_borrow(){

        final Dialog dialog = new Dialog(Lend_Borrow.this,R.style.Theme_AppCompat_Light_Dialog_Alert);
        dialog.setTitle("Add Income");

        View view = LayoutInflater.from(Lend_Borrow.this).inflate(R.layout.activity_add__lend__borrow,null);

        add = view.findViewById(R.id.add);
        cancel = view.findViewById(R.id.cancel);
        amount = view.findViewById(R.id.amount);
        description = view.findViewById(R.id.description);
        income_spinner = view.findViewById(R.id.cashcredit);
        spinnercategories = view.findViewById(R.id.categories);

        spinnercategories.setAdapter(new Income_spinner(this, getlbspinner()));

        income_spinner.setAdapter(new Income_spinner(this, getSpinner()));

        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(isAmountEmpty(amount)) {
                    final String spinnerval = income_spinner.getSelectedItem().toString();
                    final String categoryval = spinnercategories.getSelectedItem().toString();

                    if (spinnerval.equals("Choose category")){
                        Toast.makeText(Lend_Borrow.this,"Choose category",Toast.LENGTH_SHORT).show();
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

                        databaseHelper.insertlb(contentValues);

                        dialog.dismiss();
                    }

                    expadapt();
                    lend.setText(String.valueOf(databaseHelper.getlentbalance()));
                    borrow.setText(String.valueOf(databaseHelper.getborrowbalance()));
                }
            }
        });

        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

        dialog.setContentView(view);
        dialog.show();
    }

    public void showInstallDialog(final int Id_lend){
        final Dialog dialog = new Dialog(Lend_Borrow.this,R.style.Theme_AppCompat_Light_Dialog_Alert);
        dialog.setTitle("Add Installment");

        View view = LayoutInflater.from(Lend_Borrow.this).inflate(R.layout.installment_dialog,null);

        final EditText installment = view.findViewById(R.id.installment);
        Button add = view.findViewById(R.id.add),
                cancel = view.findViewById(R.id.cancel);

        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String install = installment.getText().toString();
                int amountVal = Integer.parseInt(install);

                int amount = databaseHelper.getlbtotal(Id_lend);

                int total = amount - amountVal;

                ContentValues contentValues = new ContentValues();
                contentValues.put("amount",total);

                databaseHelper.updatelb(Id_lend, contentValues);

                expadapt();
                lend.setText(String.valueOf(databaseHelper.getlentbalance()));
                borrow.setText(String.valueOf(databaseHelper.getborrowbalance()));

                dialog.dismiss();
            }
        });

        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

        dialog.setContentView(view);
        dialog.show();

    }


    public ArrayList<String> getSpinner(){

        ArrayList<String> spinner = new ArrayList<>();

        spinner.add("Choose category");
        spinner.add("Cash");
        spinner.add("Credit Card");

        return spinner;
    }

    public ArrayList<String> getlbspinner(){

        ArrayList<String> lbspinner = new ArrayList<>();

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

    public void expadapt(){
        listView.setAdapter(new Adapter_Listview(this,databaseHelper.getlblist()));
    }

    @Override
    public void onResume() {
        super.onResume();
        expadapt();
        lend.setText(String.valueOf(databaseHelper.getlentbalance()));
        borrow.setText(String.valueOf(databaseHelper.getborrowbalance()));
    }
}
