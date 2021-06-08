package com.example.moneymanager;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;

import android.provider.ContactsContract;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import static com.example.moneymanager.R.string.Income;

public class ShowAllTransactions extends Fragment {

    private ListView showall;
    private ImageView imageView;
    private UserDbController dbController;
    private Bundle b;
    private int id;
    private ImageButton btn_delete_all;
    private Spinner spinner;
    private String[] categories_to_sort;
    private ArrayList<Transaction> transactions;
    private String selected_item;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
         View view = inflater.inflate(R.layout.fragment_show_all_transactions, container, false);

         init(view);
         FetchData();

         spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
             @Override
             public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                 selected_item = spinner.getSelectedItem().toString();
                 FetchData();

             }

             @Override
             public void onNothingSelected(AdapterView<?> parent) {

             }
         });

         btn_delete_all.setOnClickListener(new View.OnClickListener() {
             @Override
             public void onClick(View v) {

                 if(transactions.size()>0)
                 ShowConfirmationMessage();

                 else Toast.makeText(getContext(),"There is no Transaction to Delete",Toast.LENGTH_SHORT).show();
             }
         });

         return view;
    }

    private void getTransactionBySelection() {

        if(selected_item.equals("All")) {
            transactions = dbController.getAllTransactions(String.valueOf(id));
        }

        else if(selected_item.equals(getResources().getString(R.string.Expense)) || selected_item.equals(getResources().getString(Income)) ) {
            transactions = dbController.getTransactionByType(String.valueOf(id), selected_item);
        }

        else {
            transactions = dbController.getTransactionByCategory(String.valueOf(id), selected_item);
        }
    }
    private void deleteTransactionBySelection() {

        if(selected_item.equals("All")) {
            dbController.deleteAllTransactions(id);
        }

        else if(selected_item.equals(getResources().getString(R.string.Expense)) || selected_item.equals(getResources().getString(Income)) ) {
        dbController.deleteTransactionsByType(id,selected_item);
        }

        else {
            dbController.deleteTransactionsByCategory(id,selected_item);
        }
    }

    private void FetchData()
    {
        try {

            getTransactionBySelection();

            if (transactions.size() == 0){
                showall.setBackground(getResources().getDrawable(R.drawable.nodata));
            }

            else showall.setBackgroundColor(getResources().getColor(R.color.white));

            final CustomAdapterExpenses customAdapterExpenses = new CustomAdapterExpenses(getContext(), 0, transactions);
            showall.setAdapter(customAdapterExpenses);

            showall.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                    try{
                    System.gc();
                    Intent intent = new Intent(getActivity(),PreviewTransaction.class);
                    intent.putExtra("TID",customAdapterExpenses.getItem(position).getId());
                    ShowAllTransactions.this.startActivityForResult(intent,1000);
                    }
                    catch(Exception e)
                        {
                            Toast.makeText(getContext(),e.getMessage(),Toast.LENGTH_SHORT).show();
                        }
                }
            });
        }
        catch (Exception e)
        {
            Toast.makeText(getContext(),e.getMessage(),Toast.LENGTH_SHORT).show();
        }

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode==1000 && resultCode== Activity.RESULT_OK){
        String key = data.getStringExtra("do");
        if(key.equals("Refresh"))
        {
        FetchData();
        }

        }
    }

    private void init(View view)
    {
        dbController = new UserDbController(getContext());
        showall = view.findViewById(R.id.list_show_all_Transactions);
        imageView = view.findViewById(R.id.img_no_data);
        btn_delete_all = view.findViewById(R.id.btn_DeleteAll);
        spinner = view.findViewById(R.id.spinner_sort);
        b = getArguments();
        id = b.getInt("ID");
        categories_to_sort = new String[]{"All",
                getResources().getString(R.string.Expense),
                getResources().getString(Income),
                getResources().getString(R.string.Home),
                getResources().getString(R.string.Bills),
                getResources().getString(R.string.Grocery),
                getResources().getString(R.string.Personal),
                getResources().getString(R.string.Transport),
                getResources().getString(R.string.Other),
                getResources().getString(R.string.Salary),
                getResources().getString(R.string.Investment),
                getResources().getString(R.string.Freelancing),
                getResources().getString(R.string.Pocket_Money),
                getResources().getString(R.string.Savings),
                getResources().getString(R.string.Food),
                getResources().getString(R.string.Education),
                getResources().getString(R.string.Gym),
                getResources().getString(R.string.Health),
        };

        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<>(getContext(),android.R.layout.simple_spinner_dropdown_item,categories_to_sort);
        arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(arrayAdapter);

        selected_item = "All";

    }

    private void ShowConfirmationMessage()
    {
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setCancelable(true);
        builder.setTitle("Delete All Transactions");
        builder.setMessage("Are you sure you want to delete '"+selected_item+"' Transactions?");
        builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                deleteTransactionBySelection();
                getTransactionBySelection();
                FetchData();
            }
        }).setNegativeButton("No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        });
        builder.create().show();
    }

}
