package com.example.moneymanager;

import android.provider.BaseColumns;

public final class FeedReaderContract {

    FeedReaderContract(){}

    public static class FeedEntry implements BaseColumns{

        public static  final String TABLE_NAME_Transaction = "Transactions";
        public static  final String COLUMN_NAME_Transaction_Id = "ID";
        public static  final String COLUMN_NAME_Transaction_Category = "Category";
        public static  final String COLUMN_NAME_Transaction_Amount = "Amount";
        public static  final String COLUMN_NAME_Transaction_Description = "Description";
        public static  final String COLUMN_NAME_Transaction_Date = "Date";
        public static  final String COLUMN_NAME_Transaction_Receipt = "Receipt";
        public static  final String COLUMN_NAME_Transaction_UserId = "User_ID";
        public static  final String COLUMN_NAME_Transaction_TransactionType = "Type";
        public static  final String COLUMN_NAME_Transaction_TransactionMedium = "Medium";

        public static  final String TABLE_NAME_User = "Users";
        public static  final String COLUMN_NAME_User_Id = "ID";
        public static  final String COLUMN_NAME_User_Username = "Username";
        public static  final String COLUMN_NAME_User_Password = "Password";
        public static  final String COLUMN_NAME_User_Name = "Name";
        public static  final String COLUMN_NAME_User_Email = "Email";

    }
}
