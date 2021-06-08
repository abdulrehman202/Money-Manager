package com.example.moneymanager;

import android.app.slice.Slice;
import android.database.Cursor;
import android.graphics.Color;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import org.eazegraph.lib.charts.PieChart;
import org.eazegraph.lib.models.PieModel;

import java.util.ArrayList;
import java.util.List;

public class main extends Fragment {
    private ImageButton add_income;
    private TextView mymoney;
    private PieChart pieChartView;
    private ListView recent_Transactions;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        final View view = inflater.inflate(R.layout.fragment_main, container, false);

        final Bundle b= getArguments();
        int userid = b.getInt("ID");

        mymoney = view.findViewById(R.id.txt_money_i_have);
        pieChartView = view.findViewById(R.id.piechart);
        recent_Transactions = view.findViewById(R.id.list_recent_expenses);

        try {
            updateData(userid);
        }
        catch (Exception e)
        {
            Toast.makeText(getContext(),e.getMessage(),Toast.LENGTH_SHORT).show();
        }
        add_income = view.findViewById(R.id.btn_add_account);
        add_income.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Fragment fragment = new add_income();
                fragment.setArguments(b);
                ((FirstScreen)getActivity()).switch_fragment(fragment);
            }
        });

        return view;
    }

    public void updateData(int User_Id )
    {
        try{
        int each_expense = 0,total_Income = 0,color_id=0,total_expense=0;
        String category;
        PieModel pieModel = null;
        UserDbController transactionDbController = new UserDbController(getContext());
        total_Income = transactionDbController.getIncomeTotal(String.valueOf(User_Id));
        total_expense = transactionDbController.getExpenseTotal(String.valueOf(User_Id));

        if(total_expense>0){
        Cursor cur = transactionDbController.getUserExpenses(String.valueOf(User_Id));

        if(cur.getCount()!=0) {
            cur.moveToFirst();

            do {
                category = cur.getString(0);
                each_expense = cur.getInt(1);

                if(category.equals(getResources().getString(R.string.Home)))
                    pieModel = new PieModel(category,each_expense,Color.parseColor("#74B9AF"));

               else if(category.equals(getResources().getString(R.string.Grocery)))
                    pieModel = new PieModel(category,each_expense,Color.parseColor("#5969DB"));

               else if(category.equals(getResources().getString(R.string.Bills)))
                    pieModel = new PieModel(category,each_expense,Color.parseColor("#009688"));

                else if(category.equals(getResources().getString(R.string.Other)))
                    pieModel = new PieModel(category,each_expense,Color.parseColor("#F74949"));

                else if(category.equals(getResources().getString(R.string.Personal)))
                    pieModel = new PieModel(category,each_expense,Color.parseColor("#F0DB5C"));

                else if(category.equals(getResources().getString(R.string.Transport)))
                    pieModel = new PieModel(category,each_expense,Color.parseColor("#8CFD8E"));

                else if(category.equals(getResources().getString(R.string.Gym)))
                    pieModel = new PieModel(category,each_expense,Color.parseColor("#33691E"));

                else if(category.equals(getResources().getString(R.string.Health)))
                    pieModel = new PieModel(category,each_expense,Color.parseColor("#CDFF4DB8"));

                else if(category.equals(getResources().getString(R.string.Food)))
                    pieModel = new PieModel(category,each_expense,Color.parseColor("#F5943E"));

                else if(category.equals(getResources().getString(R.string.Education)))
                    pieModel = new PieModel(category,each_expense,Color.parseColor("#F0FF0C"));


                total_Income-=each_expense;
               pieChartView.addPieSlice(pieModel);
            } while(cur.moveToNext());

            pieChartView.startAnimation();
        }
        }

        else
            {
                pieChartView.addPieSlice(new PieModel(100,Color.parseColor("#979797")));
            }
        mymoney.setText("PKR "+total_Income);
        loadRecentTransactions(String.valueOf(User_Id));
        }
        catch (Exception e)
        {
            Toast.makeText(getContext(),e.getMessage(),Toast.LENGTH_SHORT).show();
        }
    }

    private void loadRecentTransactions(String Id)
    {
        UserDbController transactionDbController = new UserDbController(getContext());
        ArrayList<Transaction> transactionArrayList = transactionDbController.getRecentTransaction(Id);

        CustomAdapterExpenses customAdapterExpenses = new CustomAdapterExpenses(getContext(),0,transactionArrayList);
        recent_Transactions.setAdapter(customAdapterExpenses);
    }

}
