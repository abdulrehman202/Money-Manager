package com.example.moneymanager;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

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

public class add_expense extends Fragment {

    private Button home,bills,grocery,personal,transport,other,finish,details,temp,food,gym,education,health;
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
        final View view =  inflater.inflate(R.layout.fragment_add_expense, container, false);

        init(view);
        b = getArguments();
//        new_Transaction();

        new_Transaction();


        home.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                if(temp!=null)
                temp.setBackground(getResources().getDrawable(R.color.transparent));
                temp = home;
                home.setBackground(getResources().getDrawable(R.drawable.line_border));
                transaction.setCategory(getResources().getString(R.string.Home));
            }
        });

        bills.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                if(temp!=null)
                    temp.setBackground(getResources().getDrawable(R.color.transparent));
                temp = bills;
                bills.setBackground(getResources().getDrawable(R.drawable.line_border));
                transaction.setCategory(getResources().getString(R.string.Bills));
            }
        });

        grocery.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                if(temp!=null)
                    temp.setBackground(getResources().getDrawable(R.color.transparent));
                temp = grocery;
                grocery.setBackground(getResources().getDrawable(R.drawable.line_border));
                transaction.setCategory(getResources().getString(R.string.Grocery));
            }
        });

        personal.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                if(temp!=null)
                    temp.setBackground(getResources().getDrawable(R.color.transparent));
                temp = personal;
                personal.setBackground(getResources().getDrawable(R.drawable.line_border));
                transaction.setCategory(getResources().getString(R.string.Personal));
            }
        });

        transport.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                if(temp!=null)
                    temp.setBackground(getResources().getDrawable(R.color.transparent));
                temp = transport;
                transport.setBackground(getResources().getDrawable(R.drawable.line_border));
                transaction.setCategory(getResources().getString(R.string.Transport));
            }
        });

        other.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                if(temp!=null)
                    temp.setBackground(getResources().getDrawable(R.color.transparent));
                temp = other;
                other.setBackground(getResources().getDrawable(R.drawable.line_border));
                transaction.setCategory(getResources().getString(R.string.Other));
            }
        });

        education.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                if(temp!=null)
                    temp.setBackground(getResources().getDrawable(R.color.transparent));
                temp = education;
                education.setBackground(getResources().getDrawable(R.drawable.line_border));
                transaction.setCategory(getResources().getString(R.string.Education));
            }
        });

        health.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                if(temp!=null)
                    temp.setBackground(getResources().getDrawable(R.color.transparent));
                temp = health;
                health.setBackground(getResources().getDrawable(R.drawable.line_border));
                transaction.setCategory(getResources().getString(R.string.Health));
            }
        });

        gym.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                if(temp!=null)
                    temp.setBackground(getResources().getDrawable(R.color.transparent));
                temp = gym;
                gym.setBackground(getResources().getDrawable(R.drawable.line_border));
                transaction.setCategory(getResources().getString(R.string.Gym));
            }
        });

        food.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                if(temp!=null)
                    temp.setBackground(getResources().getDrawable(R.color.transparent));
                temp = food;
                food.setBackground(getResources().getDrawable(R.drawable.line_border));
                transaction.setCategory(getResources().getString(R.string.Food));
            }
        });

        details.setOnClickListener(new View.OnClickListener(){
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

        finish.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {

                if(valid_amount_category()){
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
        transaction.setType(getResources().getString(R.string.Expense));
        transaction.setUser_id(b.getInt("ID"));
        Date c = Calendar.getInstance().getTime();
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd-MM-yyyy");
        String formattedDate = simpleDateFormat.format(c);
        transaction.setDate(formattedDate);
    }

    private void init(View view) {
        home = view.findViewById(R.id.btn_category_home);
        bills = view.findViewById(R.id.btn_category_bills);
        grocery = view.findViewById(R.id.btn_category_grocery);
        health = view.findViewById(R.id.btn_category_health);
        gym = view.findViewById(R.id.btn_category_gym);
        food = view.findViewById(R.id.btn_category_food);
        education = view.findViewById(R.id.btn_category_education);
        personal = view.findViewById(R.id.btn_category_personal);
        transport = view.findViewById(R.id.btn_category_transport);
        other = view.findViewById(R.id.btn_category_other);
        finish = view.findViewById(R.id.btn_finish_expense);
        details = view.findViewById(R.id.btn_add_details_expense);
        editText = view.findViewById(R.id.get_expense);
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
