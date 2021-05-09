package com.dristi.kharcha;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

public class Adapter_Listview extends ArrayAdapter<ExpenseInfo>{

    Context context;
    DatabaseHelper databaseHelper;

    SharedPreferences preferences;

    public Adapter_Listview(@NonNull Context context, ArrayList<ExpenseInfo> list) {
        super(context, 0, list);
        this.context = context;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {

        final View view = LayoutInflater.from(context).inflate(R.layout.listview_inflator,null);

        TextView name=view.findViewById(R.id.name),
                description = view.findViewById(R.id.description),
                amount=view.findViewById(R.id.amount);

        ImageView imageView = view.findViewById(R.id.image);

        final CheckBox checkBox = view.findViewById(R.id.check);

        databaseHelper = new DatabaseHelper(getContext());

        final ExpenseInfo info = getItem(position);

        name.setText(info.category);
        amount.setText(String.valueOf(info.amount));

        final int id = info.id;

        checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                AlertDialog.Builder builder = new AlertDialog.Builder(getContext());

                builder.setTitle("Transaction settled!!");
                builder.setMessage("Are you sure you want to delete this?");

                builder.setPositiveButton("YES", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        databaseHelper.deletelb(info.id);

                        Intent intent = new Intent(context, Lend_Borrow.class);
                        intent.putExtra("id", 1);
                        context.startActivity(intent);

                    }
                });

                builder.setNegativeButton("CANCEL", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        checkBox.setChecked(false);
                    }
                });

                checkBox.setChecked(false);
                builder.show();
            }
        });


        view.setOnCreateContextMenuListener(new View.OnCreateContextMenuListener() {
            @Override
            public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
                menu.add(id, v.getId(), 1,"Edit");
                menu.add(id, v.getId(), 2,"Delete");
                menu.add(id, v.getId(), 3,"Add Installment");
            }
        });

        if(info.description.isEmpty()){
            description.setText("To/From: No description");
        }
        else{
            description.setText("To/From: "+info.description);
        }

        switch (info.category){
            case "Lend":
                imageView.setImageResource(R.drawable.ic_lend);
                break;
            case "Borrow":
                imageView.setImageResource(R.drawable.ic_borrow);
                break;
            default:
                break;
        }
                /*preferences = context.getSharedPreferences("idinfo",0);
                SharedPreferences.Editor editor = preferences.edit();
                editor.putString("id",info.id );*/


        return view;
    }
}
