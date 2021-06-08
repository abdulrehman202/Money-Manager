package com.example.moneymanager;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.io.ByteArrayOutputStream;
import java.util.Calendar;

import static com.example.moneymanager.R.string.Income;

public class PreviewTransaction extends AppCompatActivity {

    private ImageView imageView;
    private Button view_receipt,finish,upload_new_receipt;
    private AlertDialog alertDialog;
    private TextView Category,Amount,id,type,new_Date;
    private EditText Description,new_amount;
    private Spinner Medium,new_category;
    private RadioGroup radioGroup;
    private Transaction transaction;
    private UserDbController dbController;

    String category,desc,med,date,type_,what_to_do_with_this_trans;
    byte[] bytes;
    int amount,id_;

    @Override
    protected void onStart() {
        super.onStart();
        System.gc();
    }

    @Override
    public void finish() {
        super.finish();
        System.gc();
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_preview_transaction);

        try{
            init();
            final Intent intent = getIntent();
            transaction = dbController.getTransaction(String.valueOf(intent.getIntExtra("TID",-1)));

            populateSpinner();
            setSpinnerValue();

        category = transaction.getCategory();
        amount = transaction.getAmount();
        desc = transaction.getDescription();
        med = transaction.getMedium();
        date = transaction.getDate();
        type_ = transaction.getType();
        id_ = transaction.getId();

        id.setText(String.valueOf(id_));
        Category.setText(category);
        Amount.setText("PKR "+amount);
        type.setText(type_);
        Description.setText(desc);
        new_Date.setText(date);
        bytes = getReceipt();
        new_amount.setText(String.valueOf(transaction.getAmount()));

        populateCategorySpinner(transaction.getType());

            new_Date.setOnClickListener(new View.OnClickListener(){
                @Override
                public void onClick(View v) {
                    final Calendar c = Calendar.getInstance();
                    int mYear = c.get(Calendar.YEAR);
                    int mMonth = c.get(Calendar.MONTH);
                    int mDay = c.get(Calendar.DAY_OF_MONTH);

                    DatePickerDialog datePickerDialog = new DatePickerDialog(getApplicationContext(),
                            new DatePickerDialog.OnDateSetListener() {

                                @Override
                                public void onDateSet(DatePicker view, int year,
                                                      int monthOfYear, int dayOfMonth) {
                                    monthOfYear++;
                                    new_Date.setText(dayOfMonth + "-" + monthOfYear + "-" + year);
                                }
                            }, mYear, mMonth, mDay);

                    datePickerDialog.show();
                }
            });

        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch (checkedId)
                {
                    case R.id.only_DeleteRB:
                        what_to_do_with_this_trans = "Delete";
                        break;

                    case R.id.only_EditRB:
                        what_to_do_with_this_trans = "Edit";
                        break;
                }
            }
        });

        upload_new_receipt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent upload_photo = new Intent(Intent.ACTION_PICK);
                upload_photo.setType("image/*");
                startActivityForResult(upload_photo,1);
            }
        });

        finish.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {

                transaction.setAmount(Integer.parseInt(new_amount.getText().toString()));
                transaction.setCategory(new_category.getSelectedItem().toString());
                transaction.setReceipt(bytes);
                transaction.setDescription(Description.getText().toString());
                transaction.setDate(new_Date.getText().toString());
                transaction.setMedium(Medium.getSelectedItem().toString());

                switch (what_to_do_with_this_trans){
                    case "Delete":{
                        dbController.delete_Transaction(transaction.getId());
                        Toast.makeText(getApplicationContext(),"Transaction Deleted",Toast.LENGTH_SHORT).show();
                        break;
                    }

                    case "Edit":{
                        dbController.editTransaction(transaction);
                        Toast.makeText(getApplicationContext(),"Transaction Updated",Toast.LENGTH_SHORT).show();
                        break;
                    }

                    default:{
                        break;
                    }
                }
                setResult(AppCompatActivity.RESULT_OK, new Intent().putExtra("do","Refresh"));
                finish();
            }
        });

        view_receipt.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                if(bytes!=null)
                    imageView.setImageBitmap(getBitmap());

                alertDialog.show();
            }
        });
        }
        catch(Exception e)
        {
            Toast.makeText(getApplicationContext(),e.getMessage(),Toast.LENGTH_SHORT).show();
        }
    }

    private void populateCategorySpinner(String type) {

    String[] expense = new String[]{
            getResources().getString(R.string.Home),
            getResources().getString(R.string.Bills),
            getResources().getString(R.string.Grocery),
            getResources().getString(R.string.Personal),
            getResources().getString(R.string.Transport),
            getResources().getString(R.string.Food),
            getResources().getString(R.string.Education),
            getResources().getString(R.string.Gym),
            getResources().getString(R.string.Other),
            getResources().getString(R.string.Health)
    };

    String[] income = new String[]{
            getResources().getString(R.string.Salary),
            getResources().getString(R.string.Investment),
            getResources().getString(R.string.Freelancing),
            getResources().getString(R.string.Pocket_Money),
            getResources().getString(R.string.Savings),
            getResources().getString(R.string.Other)
    };
        ArrayAdapter<String> arrayAdapter;
        switch (type)
    {
        case "Income":
            {
                arrayAdapter = new ArrayAdapter<>(getApplicationContext(),android.R.layout.simple_spinner_dropdown_item,income);
                break;
            }
        case "Expense":{
            arrayAdapter = new ArrayAdapter<>(getApplicationContext(),android.R.layout.simple_spinner_dropdown_item,expense);
            break;
        }
        default:
            throw new IllegalStateException("Unexpected value: " + type);
    }
        new_category.setAdapter(arrayAdapter);

        for(int i= 0; i < new_category.getAdapter().getCount(); i++)
        {
            if(new_category.getAdapter().getItem(i).toString().contains(transaction.getCategory()))
            {
                new_category.setSelection(i);
                return;
            }
        }
    }

    private byte[] getReceipt() {
        byte[] x = dbController.getTransactionReceipt(String.valueOf(id_));
        return  x;
    }

    private Bitmap getBitmap()
    {
       return BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
    }
    private void setSpinnerValue() {
        for(int i= 0; i < Medium.getAdapter().getCount(); i++)
        {
            if(Medium.getAdapter().getItem(i).toString().contains(transaction.getMedium()))
            {
                Medium.setSelection(i);
                return;
            }
        }
        Medium.setSelection(0);
    }

    private void populateSpinner() {
        String[] transaction_medium = {"Cash","Bank Transfer","Debit Card"};
        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<>(getApplicationContext(),android.R.layout.simple_spinner_dropdown_item,transaction_medium);
        arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        Medium.setAdapter(arrayAdapter);
    }
    private void init()
    {
        final AlertDialog.Builder builder = new AlertDialog.Builder(PreviewTransaction.this);
        View view1 = getLayoutInflater().inflate(R.layout.view_receipt,null);
        builder.setView(view1);
        alertDialog = builder.create();
        alertDialog.setCanceledOnTouchOutside(true);

        imageView = view1.findViewById(R.id.img_showReceipt);
        view_receipt = findViewById(R.id.btn_preview_receipt);
        id = findViewById(R.id.trans_id);
        type = findViewById(R.id.trans_type);
        Category= findViewById(R.id.preview_category);
        Amount= findViewById(R.id.preview_expense);
        Description= findViewById(R.id.preview_description);
        new_Date= findViewById(R.id.preview_date);
        Medium= findViewById(R.id.preview_transaction_medium);
        dbController = new UserDbController(getApplicationContext());
        finish = findViewById(R.id.btn_Done_transaction_preview);
        radioGroup = findViewById(R.id.what_to_do_RG);
        what_to_do_with_this_trans = "Edit";
        upload_new_receipt = findViewById(R.id.btn_Upload_New_Receipt_preview);
        new_amount = findViewById(R.id.preview_change_amount);
        new_category = findViewById(R.id.preview_transaction_change_category);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(resultCode==RESULT_OK && requestCode==1)
        {
            imageView.setImageURI(data.getData());
            bytes = imageToByte();
        }
    }

    public byte[] imageToByte() {
        imageView.invalidate();
        BitmapDrawable drawable = (BitmapDrawable) imageView.getDrawable();
        Bitmap bitmap = drawable.getBitmap();
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, stream);
        byte[] byteArray = stream.toByteArray();
        return byteArray;
    }

    @Override
    protected void onStop() {
        super.onStop();

        Drawable drawable = imageView.getDrawable();
        if(drawable!=null)
        {
            drawable.setCallback(null);
        }

        imageView.setImageBitmap(null);
        imageView.setBackground(null);
    }
}
