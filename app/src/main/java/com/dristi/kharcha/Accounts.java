package com.dristi.kharcha;

import android.app.Dialog;
import android.content.ContentValues;
import android.os.Bundle;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.github.clans.fab.FloatingActionButton;
import com.github.clans.fab.FloatingActionMenu;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;

public class Accounts extends Fragment {

    FloatingActionMenu menu;

    FloatingActionButton b1,b2;

    private ArrayList<Categories_item> categorylist;

    private Categories_adapter Adapter;

    LinearLayout income, expense;

    TextView credit, cash, balance;

    DatabaseHelper databaseHelper, dbhelper, dbHelper;

    ListView listView;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.activity_accounts,null);

        databaseHelper = new DatabaseHelper(getActivity());

        credit = view.findViewById(R.id.credit);
        cash = view.findViewById(R.id.cash);
        balance = view.findViewById(R.id.balance);

        listView = view.findViewById(R.id.recent);

        cash.setText(String.valueOf(databaseHelper.getcashtotal()));
        credit.setText(String.valueOf(databaseHelper.getcredittotal()));
        balance.setText(String.valueOf(databaseHelper.getbalance()));

        listView.setAdapter(new ListAdapter(getActivity(),databaseHelper.getrecentexpenselist()));

        menu = view.findViewById(R.id.menu);
        menu.setClosedOnTouchOutside(true);

        menu.setOnMenuToggleListener(new FloatingActionMenu.OnMenuToggleListener() {
            @Override
            public void onMenuToggle(boolean opened) {

            }
        });

        b1=view.findViewById(R.id.b1);
        b2=view.findViewById(R.id.b2);

        b1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                menu.close(true);
                addExpense();
            }
        });


        b2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                menu.close(true);
                addIncome();
            }
        });

        return view;
    }

    public void addExpense(){

        final Dialog dialog = new Dialog(getActivity(),R.style.Theme_AppCompat_Light_Dialog_Alert);
        dialog.setTitle("Add Expense");

        View view = LayoutInflater.from(getActivity()).inflate(R.layout.add_expense,null);

        dbHelper = new DatabaseHelper(getActivity());

        Button add = view.findViewById(R.id.add);
        Button cancel = view.findViewById(R.id.cancel);

        final EditText amount = view.findViewById(R.id.amount);
        final EditText description = view.findViewById(R.id.description);

        final Spinner income_spinner = view.findViewById(R.id.cashcredit);
        final Spinner spinnercategories = view.findViewById(R.id.categories);

        //id=getIntent().getIntExtra("id",0);

        income_spinner.setAdapter(new Income_spinner(getActivity(), getSpinner()));

        initlist();
        Adapter = new Categories_adapter(getActivity(),categorylist);
        spinnercategories.setAdapter(Adapter);
//
//        if(id!=0){
//            ExpenseInfo info = databaseHelper.getexpenseinfo(id);
//            amount.setText(String.valueOf(info.amount));
//            description.setText(info.description);
//            int spinnerPosition = ((ArrayAdapter<String>)income_spinner.getAdapter()).getPosition(info.cashcredit);
//            income_spinner.setSelection(spinnerPosition);
//
//            spinnercategories.setSelection(findIndex(info.category));
//
//            add.setText("Update");
//        }


        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(isAmountEmpty(amount)) {

                    Categories_item categoryval = (Categories_item) spinnercategories.getSelectedItem();
                    String categoryVal = categoryval.getName().toString();

                    if (categoryval.equals("Choose category")){
                        Toast.makeText(getActivity(),"Choose category",Toast.LENGTH_SHORT).show();
                    }

                    //final String[] categoryval = new String[1];

                /*spinnercatergories.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                        Categories_item clickeditem = (Categories_item)parent.getItemAtPosition(position);
                        String categoryval = clickeditem.getName();

                    }

                    @Override
                    public void onNothingSelected(AdapterView<?> parent) {

                    }
                });*/

                    final String spinnerval = income_spinner.getSelectedItem().toString();

                    SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
                    String date = df.format(Calendar.getInstance().getTime());

                    String a = amount.getText().toString();
                    int amountVal = Integer.parseInt(a);

                    String descriptionVal = description.getText().toString();

                    ContentValues contentValues = new ContentValues();
                    contentValues.put("date", date);
                    contentValues.put("amount", amountVal);
                    contentValues.put("description", descriptionVal);
                    contentValues.put("category", categoryVal);
                    contentValues.put("cashcredit",spinnerval);

                    databaseHelper.insertexpense(contentValues);

//                    if(id==0){
//                        databaseHelper.insertexpense(contentValues);
//
//                        Intent intent = new Intent(Add_expense.this, Expense.class);
//                        startActivity(intent);
//                        finish();
//                    }else{
//                        databaseHelper.updateexpense(id,contentValues);
//                        Toast.makeText(Add_expense.this,"Expense updated",Toast.LENGTH_SHORT).show();
//                        finish();
//                    }

                    dialog.dismiss();
//                    ((Navigation_drawer)getActivity()).refreshlist();
                }

                listView.setAdapter(new ListAdapter(getActivity(),databaseHelper.getrecentexpenselist()));

                cash.setText(String.valueOf(databaseHelper.getcashtotal()));
                credit.setText(String.valueOf(databaseHelper.getcredittotal()));
                balance.setText(String.valueOf(databaseHelper.getbalance()));
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

    public void addIncome(){

        final Dialog dialog = new Dialog(getActivity(),R.style.Theme_AppCompat_Light_Dialog_Alert);
        dialog.setTitle("Add Income");

        View view = LayoutInflater.from(getActivity()).inflate(R.layout.add_income,null);

        Button add = view.findViewById(R.id.add);
        Button cancel = view.findViewById(R.id.cancel);

        final EditText amount = view.findViewById(R.id.amount);
        final EditText description = view.findViewById(R.id.description);

        dbhelper = new DatabaseHelper(getActivity());

        final Spinner income_spinner = view.findViewById(R.id.income_spinner);

        income_spinner.setAdapter(new Income_spinner(getActivity(), getSpinner()));

        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(isAmountEmpty(amount)){
                    String categoryval = income_spinner.getSelectedItem().toString();

                    if (categoryval.equals("Choose category")){
                        Toast.makeText(getActivity(),"Choose category",Toast.LENGTH_SHORT).show();
                    }
                    else{
                        String a = amount.getText().toString();
                        int amountval = Integer.parseInt(a);

                        String descriptionval = description.getText().toString();

                        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
                        String date = df.format(Calendar.getInstance().getTime());

                        ContentValues contentValues=new ContentValues();
                        contentValues.put("date",date);
                        contentValues.put("amount",amountval);
                        contentValues.put("description",descriptionval);
                        contentValues.put("category", categoryval);

                        databaseHelper.insertincome(contentValues);

                        /*if(id==0){

                            dialog.dismiss();
                        }
                        else {
                            databaseHelper.updateincome(id,contentValues);
                            dialog.dismiss();
                        }*/
                    }

                    dialog.dismiss();
                }

                cash.setText(String.valueOf(databaseHelper.getcashtotal()));
                credit.setText(String.valueOf(databaseHelper.getcredittotal()));
                balance.setText(String.valueOf(databaseHelper.getbalance()));
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

    private void initlist(){

        categorylist = new ArrayList<>();
        categorylist.add(new Categories_item("Household",R.drawable.ic_household));
        categorylist.add(new Categories_item("Eating-out",R.drawable.ic_eating_out));
        categorylist.add(new Categories_item("Grocery",R.drawable.ic_grocery));
        categorylist.add(new Categories_item("Personal",R.drawable.ic_personal));
        categorylist.add(new Categories_item("Utilities",R.drawable.ic_utilities));
        categorylist.add(new Categories_item("Medical",R.drawable.ic_medical));
        categorylist.add(new Categories_item("Education",R.drawable.ic_education));
        categorylist.add(new Categories_item("Entertainment",R.drawable.ic_entertainment));
        categorylist.add(new Categories_item("Clothing",R.drawable.ic_clothing));
        categorylist.add(new Categories_item("Transportation",R.drawable.ic_transportation));
        categorylist.add(new Categories_item("Shopping",R.drawable.ic_shopping));
        categorylist.add(new Categories_item("Others",R.drawable.savings));

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

    @Override
    public void onStart() {

        cash.setText(String.valueOf(databaseHelper.getcashtotal()));
        credit.setText(String.valueOf(databaseHelper.getcredittotal()));
        balance.setText(String.valueOf(databaseHelper.getbalance()));

        listView.setAdapter(new ListAdapter(getActivity(),databaseHelper.getrecentexpenselist()));
        super.onStart();
    }
}
