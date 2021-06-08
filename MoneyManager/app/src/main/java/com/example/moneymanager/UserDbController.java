package com.example.moneymanager;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;

public class UserDbController extends SQLiteOpenHelper{

    private static final String DATABASE_NAME = "MoneyManager.db";

    private static final String SQL_CREATE_ENTIRIES_USER = "CREATE TABLE "+ FeedReaderContract.FeedEntry.TABLE_NAME_User
            +" ( "+ FeedReaderContract.FeedEntry.COLUMN_NAME_User_Id+" INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL,"+
            FeedReaderContract.FeedEntry.COLUMN_NAME_User_Name+" TEXT NOT NULL, "+
            FeedReaderContract.FeedEntry.COLUMN_NAME_User_Email+" TEXT NOT NULL, "+
            FeedReaderContract.FeedEntry.COLUMN_NAME_User_Username+" TEXT NOT NULL, "+
            FeedReaderContract.FeedEntry.COLUMN_NAME_User_Password+" TEXT NOT NULL);";

    private static final String SQL_DELETE_ENTIRIES_USER = "DROP TABLE IF EXISTS "+ FeedReaderContract.FeedEntry.TABLE_NAME_User;


    private static final String SQL_CREATE_ENTIRIES_TRANSACTION = "CREATE TABLE "+ FeedReaderContract.FeedEntry.TABLE_NAME_Transaction
            +" ("+ FeedReaderContract.FeedEntry.COLUMN_NAME_Transaction_Id+" INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, "+
            FeedReaderContract.FeedEntry.COLUMN_NAME_Transaction_Amount+" INTEGER NOT NULL, "+
            FeedReaderContract.FeedEntry.COLUMN_NAME_Transaction_Category+" TEXT NOT NULL, "+
            FeedReaderContract.FeedEntry.COLUMN_NAME_Transaction_Receipt+" BLOB, "+
            FeedReaderContract.FeedEntry.COLUMN_NAME_Transaction_Description+" TEXT, "+
            FeedReaderContract.FeedEntry.COLUMN_NAME_Transaction_Date+" DATE, "+
            FeedReaderContract.FeedEntry.COLUMN_NAME_Transaction_TransactionMedium+" TEXT, "+
            FeedReaderContract.FeedEntry.COLUMN_NAME_Transaction_TransactionType+" TEXT NOT NULL, "+
            FeedReaderContract.FeedEntry.COLUMN_NAME_Transaction_UserId+" INTEGER NOT NULL);";


    private static final String SQL_DELETE_ENTIRIES_TRANSACTION = "DROP TABLE IF EXISTS "+ FeedReaderContract.FeedEntry.TABLE_NAME_Transaction;


    public UserDbController(Context context) {
        super(context, DATABASE_NAME, null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(SQL_CREATE_ENTIRIES_USER);db.execSQL(SQL_CREATE_ENTIRIES_TRANSACTION);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL(SQL_DELETE_ENTIRIES_USER);
        onCreate(db);

        db.execSQL(SQL_DELETE_ENTIRIES_TRANSACTION);
        onCreate(db);

    }

    @Override
    public void onDowngrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        onUpgrade(db, oldVersion, newVersion);
    }

    public boolean AddUser(User user) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();

        values.put(FeedReaderContract.FeedEntry.COLUMN_NAME_User_Name, user.getName());
        values.put(FeedReaderContract.FeedEntry.COLUMN_NAME_User_Email, user.getEmail());
        values.put(FeedReaderContract.FeedEntry.COLUMN_NAME_User_Username, user.getUsername());
        values.put(FeedReaderContract.FeedEntry.COLUMN_NAME_User_Password, user.getPassword());

        long newRowid = db.insert(FeedReaderContract.FeedEntry.TABLE_NAME_User, null, values);

        if (newRowid == -1) {
            return false;
        } else {
            return true;
        }
    }

    public int getID()
    {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cur = db.rawQuery("Select max(ID) from " + FeedReaderContract.FeedEntry.TABLE_NAME_User,new String[]{});

        if(cur!=null) {
            cur.moveToFirst();
            return cur.getInt(0);
        }

        return -1;
    }
    public Cursor AuthenticateUser(String Username,String Password)
    {
        SQLiteDatabase db = this.getReadableDatabase();
//        String query = "SELECT "+FeedReaderContract.FeedEntry.COLUMN_NAME_User_Id+" FROM " +FeedReaderContract.FeedEntry.TABLE_NAME_User+" WHERE "+FeedReaderContract.FeedEntry.COLUMN_NAME_User_Username+"=? & "+FeedReaderContract.FeedEntry.COLUMN_NAME_User_Password+"=?";

        String[] column = {FeedReaderContract.FeedEntry.COLUMN_NAME_User_Id,FeedReaderContract.FeedEntry.COLUMN_NAME_User_Name,FeedReaderContract.FeedEntry.COLUMN_NAME_User_Email};
        Cursor result = db.query(FeedReaderContract.FeedEntry.TABLE_NAME_User,column,FeedReaderContract.FeedEntry.COLUMN_NAME_User_Username+" =? AND "+FeedReaderContract.FeedEntry.COLUMN_NAME_User_Password+"=?", new String[]{Username,Password},null,null,null,null);

        return result;

//        if(result.getCount() == 0)
//            return -1;
//
//        result.moveToFirst();
//
//        return result.getInt(result.getColumnIndex(FeedReaderContract.FeedEntry.COLUMN_NAME_User_Id));
    }

    public boolean uniqueUsername(String Username)
    {
        SQLiteDatabase db = this.getReadableDatabase();
//        String query = "SELECT "+FeedReaderContract.FeedEntry.COLUMN_NAME_User_Id+" FROM " +FeedReaderContract.FeedEntry.TABLE_NAME_User+" WHERE "+FeedReaderContract.FeedEntry.COLUMN_NAME_User_Username+"=? & "+FeedReaderContract.FeedEntry.COLUMN_NAME_User_Password+"=?";

//        String[] column = {FeedReaderContract.FeedEntry.COLUMN_NAME_User_Username};
        Cursor result = db.query(FeedReaderContract.FeedEntry.TABLE_NAME_User,null,FeedReaderContract.FeedEntry.COLUMN_NAME_User_Username+" =?", new String[]{Username},null,null,null,null);

        if(result.getCount() == 0)
            return true;

        return false;
    }

    public boolean AddTransaction(Transaction transaction) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();

        values.put(FeedReaderContract.FeedEntry.COLUMN_NAME_Transaction_Amount, transaction.getAmount());
        values.put(FeedReaderContract.FeedEntry.COLUMN_NAME_Transaction_Category, transaction.getCategory());
        values.put(FeedReaderContract.FeedEntry.COLUMN_NAME_Transaction_Receipt, transaction.getReceipt());
        values.put(FeedReaderContract.FeedEntry.COLUMN_NAME_Transaction_Description, transaction.getDescription());
        values.put(FeedReaderContract.FeedEntry.COLUMN_NAME_Transaction_Date, transaction.getDate());
        values.put(FeedReaderContract.FeedEntry.COLUMN_NAME_Transaction_TransactionMedium, transaction.getMedium());
        values.put(FeedReaderContract.FeedEntry.COLUMN_NAME_Transaction_TransactionType, transaction.getType());
        values.put(FeedReaderContract.FeedEntry.COLUMN_NAME_Transaction_UserId, transaction.getUser_id());

        long newRowid = db.insert(FeedReaderContract.FeedEntry.TABLE_NAME_Transaction, null, values);

        if (newRowid == -1)
            return false;

        return true;
    }

    public int getExpenseTotal(String Userid)
    {
        SQLiteDatabase db = this.getReadableDatabase();
        String[] column= {"SUM("+FeedReaderContract.FeedEntry.COLUMN_NAME_Transaction_Amount+")"};
        Cursor result = db.query(FeedReaderContract.FeedEntry.TABLE_NAME_Transaction,column,FeedReaderContract.FeedEntry.COLUMN_NAME_Transaction_UserId+"=? AND "+ FeedReaderContract.FeedEntry.COLUMN_NAME_Transaction_TransactionType +" =? ", new String[]{Userid,"Expense"},null,null,null,null);

        if(result.getCount()!=0)
        {
            result.moveToFirst();
            return result.getInt(0);
        }

        return 0;
    }

    public int getIncomeTotal(String Userid)
    {
        SQLiteDatabase db = this.getReadableDatabase();
        String[] column= {"SUM("+FeedReaderContract.FeedEntry.COLUMN_NAME_Transaction_Amount+")"};
        Cursor result = db.query(FeedReaderContract.FeedEntry.TABLE_NAME_Transaction,column,FeedReaderContract.FeedEntry.COLUMN_NAME_Transaction_UserId+"=? AND "+ FeedReaderContract.FeedEntry.COLUMN_NAME_Transaction_TransactionType +" =? ", new String[]{Userid,"Income"},null,null,null,null);

        if(result.getCount()!=0)
        {
            result.moveToFirst();
            return result.getInt(0);
        }

        return 0;
    }

    public Cursor getUserExpenses(String Userid)
    {
        SQLiteDatabase db = this.getReadableDatabase();
        String[] column= {FeedReaderContract.FeedEntry.COLUMN_NAME_Transaction_Category,"SUM("+FeedReaderContract.FeedEntry.COLUMN_NAME_Transaction_Amount+")"};
        Cursor result = db.query(FeedReaderContract.FeedEntry.TABLE_NAME_Transaction,column,FeedReaderContract.FeedEntry.COLUMN_NAME_Transaction_UserId+"=? AND "+ FeedReaderContract.FeedEntry.COLUMN_NAME_Transaction_TransactionType +" =? ", new String[]{Userid,"Expense"},FeedReaderContract.FeedEntry.COLUMN_NAME_Transaction_Category,null,null,null);

        return result;
    }

    public ArrayList<Transaction> getRecentTransaction(String Userid)
    {
        ArrayList<Transaction> transactionArrayList = new ArrayList<>();


        SQLiteDatabase db = this.getReadableDatabase();
        String[] column= {FeedReaderContract.FeedEntry.COLUMN_NAME_Transaction_Id,FeedReaderContract.FeedEntry.COLUMN_NAME_Transaction_Amount,FeedReaderContract.FeedEntry.COLUMN_NAME_Transaction_Description,FeedReaderContract.FeedEntry.COLUMN_NAME_Transaction_Category,FeedReaderContract.FeedEntry.COLUMN_NAME_Transaction_Date,FeedReaderContract.FeedEntry.COLUMN_NAME_Transaction_TransactionType};
        Cursor result = db.query(FeedReaderContract.FeedEntry.TABLE_NAME_Transaction,column,FeedReaderContract.FeedEntry.COLUMN_NAME_Transaction_UserId+"=? ", new String[]{Userid},null,null,null,null);

        int i = 0;

        if(result.getCount()!=0)
        {
            result.moveToLast();
            do
            {
                Transaction transaction = new Transaction();
                transaction.setId(result.getInt(result.getColumnIndex(FeedReaderContract.FeedEntry.COLUMN_NAME_Transaction_Id)));
                transaction.setDate(result.getString(result.getColumnIndex(FeedReaderContract.FeedEntry.COLUMN_NAME_Transaction_Date)));
                transaction.setCategory(result.getString(result.getColumnIndex(FeedReaderContract.FeedEntry.COLUMN_NAME_Transaction_Category)));
                transaction.setAmount(result.getInt(result.getColumnIndex(FeedReaderContract.FeedEntry.COLUMN_NAME_Transaction_Amount)));
                transaction.setType(result.getString(result.getColumnIndex(FeedReaderContract.FeedEntry.COLUMN_NAME_Transaction_TransactionType)));
                transaction.setDescription(result.getString(result.getColumnIndex(FeedReaderContract.FeedEntry.COLUMN_NAME_Transaction_Description)));

                transactionArrayList.add(transaction);
                i++;
            }while (result.moveToPrevious() && i <5);

        }
        return transactionArrayList;
    }

    public ArrayList<Transaction> getAllTransactions(String Userid)
    {
        ArrayList<Transaction> transactionArrayList = new ArrayList<>();


        SQLiteDatabase db = this.getReadableDatabase();
        String[] column= {FeedReaderContract.FeedEntry.COLUMN_NAME_Transaction_Id,FeedReaderContract.FeedEntry.COLUMN_NAME_Transaction_Description,FeedReaderContract.FeedEntry.COLUMN_NAME_Transaction_Amount,FeedReaderContract.FeedEntry.COLUMN_NAME_Transaction_Category,FeedReaderContract.FeedEntry.COLUMN_NAME_Transaction_Date,FeedReaderContract.FeedEntry.COLUMN_NAME_Transaction_TransactionType};
        Cursor result = db.query(FeedReaderContract.FeedEntry.TABLE_NAME_Transaction,column,FeedReaderContract.FeedEntry.COLUMN_NAME_Transaction_UserId+"=? ", new String[]{Userid},null,null,null,null);

        if(result.getCount()!=0)
        {
            result.moveToLast();
            do
            {
                Transaction transaction = new Transaction();
                transaction.setId(result.getInt(result.getColumnIndex(FeedReaderContract.FeedEntry.COLUMN_NAME_Transaction_Id)));
                transaction.setDate(result.getString(result.getColumnIndex(FeedReaderContract.FeedEntry.COLUMN_NAME_Transaction_Date)));
                transaction.setCategory(result.getString(result.getColumnIndex(FeedReaderContract.FeedEntry.COLUMN_NAME_Transaction_Category)));
                transaction.setAmount(result.getInt(result.getColumnIndex(FeedReaderContract.FeedEntry.COLUMN_NAME_Transaction_Amount)));
                transaction.setType(result.getString(result.getColumnIndex(FeedReaderContract.FeedEntry.COLUMN_NAME_Transaction_TransactionType)));
                transaction.setDescription(result.getString(result.getColumnIndex(FeedReaderContract.FeedEntry.COLUMN_NAME_Transaction_Description)));
                transactionArrayList.add(transaction);

            }while (result.moveToPrevious());
        }
        return transactionArrayList;
    }

    public Transaction getTransaction(String Transactionid)
    {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor result = db.query(FeedReaderContract.FeedEntry.TABLE_NAME_Transaction,null,FeedReaderContract.FeedEntry.COLUMN_NAME_Transaction_Id+"=? ", new String[]{Transactionid},null,null,null,null);

        if(result.getCount()!=0)
        {
            result.moveToFirst();
            Transaction transaction = new Transaction();

            transaction.setId(result.getInt(result.getColumnIndex(FeedReaderContract.FeedEntry.COLUMN_NAME_Transaction_Id)));
            transaction.setAmount(result.getInt(result.getColumnIndex(FeedReaderContract.FeedEntry.COLUMN_NAME_Transaction_Amount)));
            transaction.setCategory(result.getString(result.getColumnIndex(FeedReaderContract.FeedEntry.COLUMN_NAME_Transaction_Category)));
            transaction.setDescription(result.getString(result.getColumnIndex(FeedReaderContract.FeedEntry.COLUMN_NAME_Transaction_Description)));
            transaction.setDate(result.getString(result.getColumnIndex(FeedReaderContract.FeedEntry.COLUMN_NAME_Transaction_Date)));
            transaction.setType(result.getString(result.getColumnIndex(FeedReaderContract.FeedEntry.COLUMN_NAME_Transaction_TransactionType)));
            transaction.setMedium(result.getString(result.getColumnIndex(FeedReaderContract.FeedEntry.COLUMN_NAME_Transaction_TransactionMedium)));
            transaction.setUser_id(result.getInt(result.getColumnIndex(FeedReaderContract.FeedEntry.COLUMN_NAME_Transaction_UserId)));

            return transaction;
        }
        return null;
    }

    public byte[] getTransactionReceipt(String Transactionid)
    {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor result = db.query(FeedReaderContract.FeedEntry.TABLE_NAME_Transaction, new String[]{FeedReaderContract.FeedEntry.COLUMN_NAME_Transaction_Receipt},FeedReaderContract.FeedEntry.COLUMN_NAME_Transaction_Id+"=? ", new String[]{Transactionid},null,null,null,null);

        if(result.getCount()!=0)
        {
            result.moveToFirst();

           return result.getBlob(result.getColumnIndex(FeedReaderContract.FeedEntry.COLUMN_NAME_Transaction_Receipt));
        }
        return null;
    }

    public ArrayList<Transaction> getTransactionByType(String Userid,String transactionType)
    {
        ArrayList<Transaction> transactionArrayList = new ArrayList<>();


        SQLiteDatabase db = this.getReadableDatabase();
        String[] column= {FeedReaderContract.FeedEntry.COLUMN_NAME_Transaction_Id,FeedReaderContract.FeedEntry.COLUMN_NAME_Transaction_Amount,FeedReaderContract.FeedEntry.COLUMN_NAME_Transaction_Category,FeedReaderContract.FeedEntry.COLUMN_NAME_Transaction_Date,FeedReaderContract.FeedEntry.COLUMN_NAME_Transaction_TransactionType,FeedReaderContract.FeedEntry.COLUMN_NAME_Transaction_Description};
        Cursor result = db.query(FeedReaderContract.FeedEntry.TABLE_NAME_Transaction,column,FeedReaderContract.FeedEntry.COLUMN_NAME_Transaction_UserId+"=? AND "+ FeedReaderContract.FeedEntry.COLUMN_NAME_Transaction_TransactionType+" =?", new String[]{Userid,transactionType},null,null,null,null);

        if(result.getCount()!=0)
        {
            result.moveToLast();
            do
            {
                Transaction transaction = new Transaction();
                transaction.setId(result.getInt(result.getColumnIndex(FeedReaderContract.FeedEntry.COLUMN_NAME_Transaction_Id)));
                transaction.setDate(result.getString(result.getColumnIndex(FeedReaderContract.FeedEntry.COLUMN_NAME_Transaction_Date)));
                transaction.setCategory(result.getString(result.getColumnIndex(FeedReaderContract.FeedEntry.COLUMN_NAME_Transaction_Category)));
                transaction.setAmount(result.getInt(result.getColumnIndex(FeedReaderContract.FeedEntry.COLUMN_NAME_Transaction_Amount)));
                transaction.setType(result.getString(result.getColumnIndex(FeedReaderContract.FeedEntry.COLUMN_NAME_Transaction_TransactionType)));
                transaction.setDescription(result.getString(result.getColumnIndex(FeedReaderContract.FeedEntry.COLUMN_NAME_Transaction_Description)));
                transactionArrayList.add(transaction);

            }while (result.moveToPrevious());
        }
        return transactionArrayList;
    }

    public ArrayList<Transaction> getTransactionByCategory(String Userid,String transactionCategory)
    {
        ArrayList<Transaction> transactionArrayList = new ArrayList<>();


        SQLiteDatabase db = this.getReadableDatabase();
        String[] column= {FeedReaderContract.FeedEntry.COLUMN_NAME_Transaction_Id,FeedReaderContract.FeedEntry.COLUMN_NAME_Transaction_Description,FeedReaderContract.FeedEntry.COLUMN_NAME_Transaction_Amount,FeedReaderContract.FeedEntry.COLUMN_NAME_Transaction_Category,FeedReaderContract.FeedEntry.COLUMN_NAME_Transaction_Date,FeedReaderContract.FeedEntry.COLUMN_NAME_Transaction_TransactionType};
        Cursor result = db.query(FeedReaderContract.FeedEntry.TABLE_NAME_Transaction,column,FeedReaderContract.FeedEntry.COLUMN_NAME_Transaction_UserId+"=? AND "+ FeedReaderContract.FeedEntry.COLUMN_NAME_Transaction_Category+" =?", new String[]{Userid,transactionCategory},null,null,null,null);

        if(result.getCount()!=0)
        {
            result.moveToLast();
            do
            {
                Transaction transaction = new Transaction();
                transaction.setId(result.getInt(result.getColumnIndex(FeedReaderContract.FeedEntry.COLUMN_NAME_Transaction_Id)));
                transaction.setDate(result.getString(result.getColumnIndex(FeedReaderContract.FeedEntry.COLUMN_NAME_Transaction_Date)));
                transaction.setCategory(result.getString(result.getColumnIndex(FeedReaderContract.FeedEntry.COLUMN_NAME_Transaction_Category)));
                transaction.setAmount(result.getInt(result.getColumnIndex(FeedReaderContract.FeedEntry.COLUMN_NAME_Transaction_Amount)));
                transaction.setType(result.getString(result.getColumnIndex(FeedReaderContract.FeedEntry.COLUMN_NAME_Transaction_TransactionType)));
                transaction.setDescription(result.getString(result.getColumnIndex(FeedReaderContract.FeedEntry.COLUMN_NAME_Transaction_Description)));
                transactionArrayList.add(transaction);

            }while (result.moveToPrevious());
        }
        return transactionArrayList;
    }

    public void delete_Transaction(int Transaction_id)
    {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(FeedReaderContract.FeedEntry.TABLE_NAME_Transaction, FeedReaderContract.FeedEntry.COLUMN_NAME_Transaction_Id + "=?", new String[]{String.valueOf(Transaction_id)});
    }

    public void deleteAllTransactions(int User_id)
    {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(FeedReaderContract.FeedEntry.TABLE_NAME_Transaction, FeedReaderContract.FeedEntry.COLUMN_NAME_Transaction_UserId + "=?", new String[]{String.valueOf(User_id)});
    }

    public void deleteTransactionsByType(int User_id,String type)
    {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(FeedReaderContract.FeedEntry.TABLE_NAME_Transaction, FeedReaderContract.FeedEntry.COLUMN_NAME_Transaction_UserId + "=? AND "+ FeedReaderContract.FeedEntry.COLUMN_NAME_Transaction_TransactionType+ "=?", new String[]{String.valueOf(User_id),type});
    }

    public void deleteTransactionsByCategory(int User_id,String category)
    {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(FeedReaderContract.FeedEntry.TABLE_NAME_Transaction, FeedReaderContract.FeedEntry.COLUMN_NAME_Transaction_UserId + "=? AND "+ FeedReaderContract.FeedEntry.COLUMN_NAME_Transaction_Category+ "=?", new String[]{String.valueOf(User_id),category});
    }

    public void editTransaction(Transaction transaction)
    {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();

        values.put(FeedReaderContract.FeedEntry.COLUMN_NAME_Transaction_Amount, transaction.getAmount());
        values.put(FeedReaderContract.FeedEntry.COLUMN_NAME_Transaction_Category, transaction.getCategory());
        values.put(FeedReaderContract.FeedEntry.COLUMN_NAME_Transaction_Receipt, transaction.getReceipt());
        values.put(FeedReaderContract.FeedEntry.COLUMN_NAME_Transaction_Description, transaction.getDescription());
        values.put(FeedReaderContract.FeedEntry.COLUMN_NAME_Transaction_Date, transaction.getDate());
        values.put(FeedReaderContract.FeedEntry.COLUMN_NAME_Transaction_TransactionMedium, transaction.getMedium());

        db.update(FeedReaderContract.FeedEntry.TABLE_NAME_Transaction, values,  FeedReaderContract.FeedEntry.COLUMN_NAME_Transaction_Id+"=?", new String[]{String.valueOf(transaction.getId())});

    }

    public Cursor getUserInfo(String userid)
    {
        SQLiteDatabase db = this.getReadableDatabase();

        String[] column = {FeedReaderContract.FeedEntry.COLUMN_NAME_User_Id,FeedReaderContract.FeedEntry.COLUMN_NAME_User_Name,FeedReaderContract.FeedEntry.COLUMN_NAME_User_Email, FeedReaderContract.FeedEntry.COLUMN_NAME_User_Username};
        Cursor result = db.query(FeedReaderContract.FeedEntry.TABLE_NAME_User,column,FeedReaderContract.FeedEntry.COLUMN_NAME_User_Id+"=?", new String[]{userid},null,null,null,null);

        return result;
    }

    public String getUserPassword(String userid)
    {
        SQLiteDatabase db = this.getReadableDatabase();

        String[] column = {FeedReaderContract.FeedEntry.COLUMN_NAME_User_Password};
        Cursor result = db.query(FeedReaderContract.FeedEntry.TABLE_NAME_User,column,FeedReaderContract.FeedEntry.COLUMN_NAME_User_Id+"=?", new String[]{userid},null,null,null,null);

        result.moveToFirst();

        return result.getString(0);
    }

    public void updateUserPassword(String userid,String newPassword)
    {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();

        values.put(FeedReaderContract.FeedEntry.COLUMN_NAME_User_Password,newPassword);
        db.update(FeedReaderContract.FeedEntry.TABLE_NAME_User, values,  FeedReaderContract.FeedEntry.COLUMN_NAME_User_Id+"=?", new String[]{userid});

    }
}

