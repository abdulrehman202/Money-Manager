package com.example.moneymanager;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.os.Parcelable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;


public class add_income extends Fragment {

    private Button salary,savings,freelance,investments,other,pocket_money,temp,details,finish;
    private EditText editText;
    private Transaction transaction;
    private Bundle b;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_add_income, container, false);

        init(view);
         b = getArguments();
//        new_Transaction();

            new_Transaction();

        salary.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View v) {
                if(temp!=null)
                temp.setBackground(getResources().getDrawable(R.color.transparent));
                salary.setBackground(getResources().getDrawable(R.drawable.line_border));
                temp = salary;
                transaction.setCategory(getResources().getString(R.string.Salary));
            }
        });

        savings.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View v) {
                if(temp!=null)
                    temp.setBackground(getResources().getDrawable(R.color.transparent));
                savings.setBackground(getResources().getDrawable(R.drawable.line_border));
                temp = savings;
                transaction.setCategory(getResources().getString(R.string.Savings));
            }
        });

        pocket_money.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View v) {
                if(temp!=null)
                    temp.setBackground(getResources().getDrawable(R.color.transparent));
                pocket_money.setBackground(getResources().getDrawable(R.drawable.line_border));
                temp = pocket_money;
                transaction.setCategory(getResources().getString(R.string.Pocket_Money));
            }
        });

        other.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View v) {
                if(temp!=null)
                    temp.setBackground(getResources().getDrawable(R.color.transparent));
                other.setBackground(getResources().getDrawable(R.drawable.line_border));
                temp = other;
                transaction.setCategory(getResources().getString(R.string.Other));
            }
        });

        freelance.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View v) {
                if(temp!=null)
                    temp.setBackground(getResources().getDrawable(R.color.transparent));
                freelance.setBackground(getResources().getDrawable(R.drawable.line_border));
                temp = freelance;
                transaction.setCategory(getResources().getString(R.string.Freelancing));
            }
        });

        investments.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View v) {
                if(temp!=null)
                    temp.setBackground(getResources().getDrawable(R.color.transparent));
                investments.setBackground(getResources().getDrawable(R.drawable.line_border));
                temp = investments;
                transaction.setCategory(getResources().getString(R.string.Investment));
            }
        });


            details.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    if (valid_amount_category()){

                        transaction.setAmount(Integer.parseInt(editText.getText().toString()));
                        b = new Bundle();
                        b.putSerializable(getResources().getString(R.string.Transaction), transaction);
                        final Fragment detail_fragment = new transaction_detail();
                        detail_fragment.setArguments(b);

                        ((FirstScreen)getContext()).switch_fragment(detail_fragment);

                    }
                }
            });

            finish.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    if (valid_amount_category())
                    {
                        transaction.setAmount(Integer.parseInt(editText.getText().toString()));
                        UserDbController transactionDbController = new UserDbController(getContext());
                           if (!transactionDbController.AddTransaction(transaction))
                                Toast.makeText(getContext(), "Transaction Not Successful! :(", Toast.LENGTH_SHORT).show();

                        b = new Bundle();
                        b.putInt("ID", transaction.getUser_id());
                        Fragment fragment = new main();
                        fragment.setArguments(b);
                        ((FirstScreen) getContext()).switch_fragment(fragment);}

                }
            });

    return view;
    }

    private void new_Transaction()
    {
        transaction = new Transaction();
        transaction.setType(getResources().getString(R.string.Income));
        transaction.setUser_id(b.getInt("ID"));
        Date c = Calendar.getInstance().getTime();
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd-MM-yyyy");
        String formattedDate = simpleDateFormat.format(c);
        transaction.setDate(formattedDate);
    }

    private void init(View view) {

        salary = view.findViewById(R.id.btn_category_salary);
        savings = view.findViewById(R.id.btn_category_savings);
        freelance = view.findViewById(R.id.btn_category_freelance);
        investments = view.findViewById(R.id.btn_category_investment);
        pocket_money = view.findViewById(R.id.btn_category_pocketmoney);
        other = view.findViewById(R.id.btn_category_other_income);
        details = view.findViewById(R.id.btn_add_details_income);
        finish = view.findViewById(R.id.btn_finish_income);
        editText = view.findViewById(R.id.get_income);
        temp = null;

    }

    boolean valid_amount_category()
    {
        String input_amount = editText.getText().toString();
        if(temp==null || input_amount.matches("")) {
            if (temp == null) {
                Toast.makeText(getContext(), "Please Select Category", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(getContext(), "Please Enter Amount", Toast.LENGTH_SHORT).show();
            }
            return false;
        }
        return true;
    }
}
