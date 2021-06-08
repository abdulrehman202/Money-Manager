package com.example.moneymanager;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.List;

public class CustomAdapterExpenses extends ArrayAdapter<Transaction> {
    public CustomAdapterExpenses(@NonNull Context context, int resource, @NonNull List<Transaction> objects) {
        super(context, resource, objects);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {

        View v= convertView;
        if(v==null)
        {
            LayoutInflater layoutInflater;
            layoutInflater = LayoutInflater.from(getContext());
            v = layoutInflater.inflate(R.layout.transaction_row,null);
        }

        Transaction transaction = getItem(position);

        if(transaction!=null)
        {
            ImageView icon = v.findViewById(R.id.img_icon);
            TextView category = v.findViewById(R.id.list_row_Category);
            TextView date = v.findViewById(R.id.list_row_Date);
            TextView amount = v.findViewById(R.id.list_row_Amount);
            TextView id = v.findViewById(R.id.txtvw_transaction_id);
            TextView description = v.findViewById(R.id.list_row_Description);

            if(transaction.getType().equals("Expense"))
                amount.setTextColor(Color.RED);

            else amount.setTextColor(Color.GREEN);

            String get_category = transaction.getCategory();

            if(get_category.equals("Grocery"))
                icon.setImageResource(R.drawable.grocery);

            else if(get_category.equals("Home"))
                icon.setImageResource(R.drawable.home);

            else if(get_category.equals("Bills"))
                icon.setImageResource(R.drawable.bill);

            else if(get_category.equals("Personal"))
                icon.setImageResource(R.drawable.personal);

            else if(get_category.equals("Transport"))
                icon.setImageResource(R.drawable.transport);

            else if(get_category.equals("Other"))
                icon.setImageResource(R.drawable.other);

            else if(get_category.equals("Salary"))
                icon.setImageResource(R.drawable.salary);

            else if(get_category.equals("Pocket Money"))
                icon.setImageResource(R.drawable.pocket_money);

            else if(get_category.equals("Investment"))
                icon.setImageResource(R.drawable.investment);


            else if(get_category.equals("Freelancing"))
                icon.setImageResource(R.drawable.freelance);

            else if(get_category.equals("Saving"))
                icon.setImageResource(R.drawable.savings);

            else if(get_category.equals("Gym"))
                icon.setImageResource(R.drawable.gym);

            else if(get_category.equals("Food"))
                icon.setImageResource(R.drawable.food);

            else if(get_category.equals("Education"))
                icon.setImageResource(R.drawable.education);

            else if(get_category.equals("Health"))
                icon.setImageResource(R.drawable.health);

            category.setText(get_category);
            amount.setText("PKR "+ transaction.getAmount());
            date.setText(transaction.getDate());
            id.setText(String.valueOf(transaction.getId()));
            description.setText(transaction.getDescription());

        }
        return v;
    }
}
