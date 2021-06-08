package com.example.moneymanager;

import java.io.Serializable;
import java.sql.Blob;

public class Transaction implements Serializable {

    int id,Amount,user_id;
    byte[] receipt;
    String Category,Description,Date,Type,Medium;

    public Transaction(int id,int amount, int user_id, byte[] receipt, String category, String description, String date, String type, String medium) {
        Amount = amount;
        this.user_id = user_id;
        this.receipt = receipt;
        this.id = id;
        Category = category;
        Description = description;
        Date = date;
        Type = type;
        Medium = medium;
    }

    public Transaction() {
        this.receipt = null;
        Description = "";
        Date = "";
        Type = "";
        Medium = "";
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getAmount() {
        return Amount;
    }

    public void setAmount(int amount) {
        Amount = amount;
    }

    public int getUser_id() {
        return user_id;
    }

    public void setUser_id(int user_id) {
        this.user_id = user_id;
    }

    public byte[] getReceipt() {
        return receipt;
    }

    public void setReceipt(byte[] receipt) {
        this.receipt = receipt;
    }

    public String getCategory() {
        return Category;
    }

    public void setCategory(String category) {
        Category = category;
    }

    public String getDescription() {
        return Description;
    }

    public void setDescription(String description) {
        Description = description;
    }

    public String getDate() {
        return Date;
    }

    public void setDate(String date) {
        Date = date;
    }

    public String getType() {
        return Type;
    }

    public void setType(String type) {
        Type = type;
    }

    public String getMedium() {
        return Medium;
    }

    public void setMedium(String medium) {
        Medium = medium;
    }


}
