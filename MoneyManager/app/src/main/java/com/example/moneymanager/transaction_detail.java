package com.example.moneymanager;

import android.Manifest;
import android.Manifest.permission;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TabHost;
import android.widget.TextView;
import android.widget.Toast;

import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.sql.Blob;
import java.util.Calendar;
import java.util.Date;

import static android.app.Activity.RESULT_OK;
import static androidx.core.content.PermissionChecker.checkSelfPermission;


public class transaction_detail extends Fragment {

    private Spinner spinner;
    private Button view_receipt,finish;
    private ImageButton imageButton_attachReceipt;
    private EditText date,description,type,medium;
    private TextView catch_category,catch_expense;
    Uri image;
    private  ImageView imageView,icon;
    Transaction transaction;
    Bundle b;
    AlertDialog alertDialog;
    UserDbController transactionDbController;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if(getContext().checkSelfPermission(permission.READ_EXTERNAL_STORAGE)== PackageManager.PERMISSION_DENIED)
            {
                String permission = (Manifest.permission.READ_EXTERNAL_STORAGE);
                requestPermissions(new String[]{permission},1001);
            }
        }
        // Inflate the layout for this fragment
        final View view = inflater.inflate(R.layout.fragment_transaction_detail, container, false);

        b = getArguments();

        init(view);

        date.setText(transaction.getDate());

        date.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                final Calendar c = Calendar.getInstance();
                int mYear = c.get(Calendar.YEAR);
                int mMonth = c.get(Calendar.MONTH);
                int mDay = c.get(Calendar.DAY_OF_MONTH);

                DatePickerDialog datePickerDialog = new DatePickerDialog(getContext(),
                        new DatePickerDialog.OnDateSetListener() {

                            @Override
                            public void onDateSet(DatePicker view, int year,
                                                  int monthOfYear, int dayOfMonth) {
                                monthOfYear++;
                                date.setText(dayOfMonth + "-" + monthOfYear + "-" + year);
                            }
                        }, mYear, mMonth, mDay);

                datePickerDialog.show();
            }
    });

        imageButton_attachReceipt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent upload_photo = new Intent(Intent.ACTION_PICK);
                upload_photo.setType("image/*");
                startActivityForResult(upload_photo,1);
            }
        });

        view_receipt.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {

                alertDialog.show();
            }
        });

        finish.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                transaction.setDescription(description.getText().toString());
                transaction.setDate(date.getText().toString());
                transaction.setMedium(spinner.getSelectedItem().toString());
                transaction.setReceipt(imageToByte());

                    if (!transactionDbController.AddTransaction(transaction))
                        Toast.makeText(getContext(), "Transaction Not Successful! :(", Toast.LENGTH_SHORT).show();


                b = new Bundle();
                b.putInt("ID", transaction.getUser_id());
                Fragment fragment = new main();
                fragment.setArguments(b);
                ((FirstScreen) getContext()).switch_fragment(fragment);
            }
        });


    return view;
    }

    private void populateSpinner() {
        String[] transaction_medium = {"Cash","Bank Transfer","Debit Card"};
        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<>(getContext(),android.R.layout.simple_spinner_dropdown_item,transaction_medium);
        arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(arrayAdapter);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(resultCode==RESULT_OK && requestCode==1)
        {
            image = data.getData();
            imageView.setImageURI(image);
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

    public void init(View view){
        spinner = view.findViewById(R.id.spinner_transaction_medium);
        view_receipt = view.findViewById(R.id.btn_view_receipt);
        imageButton_attachReceipt = view.findViewById(R.id.btn_attach_receipt);
        date = view.findViewById(R.id.edit_text_add_date);
        catch_category = view.findViewById(R.id.get_category);
        catch_expense = view.findViewById(R.id.get_expense);
        finish = view.findViewById(R.id.btn_finish_transaction_detail);
        description = view.findViewById(R.id.edit_text_add_description);
        icon = view.findViewById(R.id.get_icon);
        transactionDbController = new UserDbController(getContext());

        /** Upload Receipt **/
        final AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        View view1 = getLayoutInflater().inflate(R.layout.view_receipt,null);

        imageView = view1.findViewById(R.id.img_showReceipt);
        //imageView.setImageURI(image);

        builder.setView(view1);
         alertDialog = builder.create();
        alertDialog.setCanceledOnTouchOutside(true);
        /** Upload Receipt ends **/

        populateSpinner();

        transaction = (Transaction) b.getSerializable(getResources().getString(R.string.Transaction));

        setIcon(transaction.getCategory());

        catch_category.setText(transaction.getCategory());
        if(transaction.getType().equals(getResources().getString(R.string.Income))){
            catch_expense.setTextColor(getResources().getColor(R.color.colorPrimaryDark));
            catch_expense.setText("PKR +"+transaction.getAmount());
        }

        else catch_expense.setText("PKR -"+transaction.getAmount());
    }

    private void setIcon(String category) {

        if(category.equals(getResources().getString(R.string.Home)))
            icon.setImageResource(R.drawable.home);

        else if(category.equals(getResources().getString(R.string.Bills)))
            icon.setImageResource(R.drawable.bill);

        else if(category.equals(getResources().getString(R.string.Grocery)))
            icon.setImageResource(R.drawable.grocery);

        else if(category.equals(getResources().getString(R.string.Personal)))
            icon.setImageResource(R.drawable.personal);

        else if(category.equals(getResources().getString(R.string.Transport)))
            icon.setImageResource(R.drawable.transport);

        else if(category.equals(getResources().getString(R.string.Other)))
            icon.setImageResource(R.drawable.other);

        else if(category.equals(getResources().getString(R.string.Food)))
            icon.setImageResource(R.drawable.food);

        else if(category.equals(getResources().getString(R.string.Health)))
            icon.setImageResource(R.drawable.health);

        else if(category.equals(getResources().getString(R.string.Education)))
            icon.setImageResource(R.drawable.education);

        else if(category.equals(getResources().getString(R.string.Gym)))
            icon.setImageResource(R.drawable.gym);

        else if(category.equals(getResources().getString(R.string.Salary)))
            icon.setImageResource(R.drawable.salary);

        else if(category.equals(getResources().getString(R.string.Pocket_Money)))
            icon.setImageResource(R.drawable.pocket_money);

        else if(category.equals(getResources().getString(R.string.Investment)))
            icon.setImageResource(R.drawable.investment);

        else if(category.equals(getResources().getString(R.string.Freelancing)))
            icon.setImageResource(R.drawable.freelance);

        else if(category.equals(getResources().getString(R.string.Savings)))
            icon.setImageResource(R.drawable.savings);

    }
}

