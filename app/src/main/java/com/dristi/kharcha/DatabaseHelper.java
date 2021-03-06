package com.dristi.kharcha;


import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.database.sqlite.SQLiteStatement;

import java.util.ArrayList;

public class DatabaseHelper extends SQLiteOpenHelper {

    static String name = "gharbachat";
    static int version = 1;

    String sqlCreateUserTable = "CREATE TABLE if not exists`register` (\n" +
            "\t`id`\tINTEGER PRIMARY KEY AUTOINCREMENT,\n" +
            "\t`username`\tTEXT,\n" +
            "\t`password`\tTEXT,\n" +
            "\t`confirmpass`\tTEXT,\n" +
            "\t`email`\tTEXT,\n" +
            "\t`gender`\tTEXT\n" +
            ")";

    String sqlCreateExpenseTable = "CREATE TABLE if not exists `expense` (\n" +
            "\t`id`\tINTEGER PRIMARY KEY AUTOINCREMENT,\n" +
            "\t`date`\tTEXT,\n" +
            "\t`amount`\tINTEGER,\n" +
            "\t`category`\tTEXT,\n" +
            "\t`description`\tTEXT,\n" +
            "\t`cashcredit`\tTEXT\n" +
            ")";

    String sqlCreateIncomeTable = "CREATE TABLE  if not exists`income` (\n" +
            "\t`id`\tINTEGER PRIMARY KEY AUTOINCREMENT,\n" +
            "\t`date`\tTEXT,\n"+
            "\t`category`\tTEXT,\n" +
            "\t`amount`\tINTEGER,\n" +
            "\t`description`\tTEXT\n" +
            ")";

    String sqlCreatelbTable = "CREATE TABLE if not exists `lb` (\n" +
            "\t`id`\tINTEGER PRIMARY KEY AUTOINCREMENT,\n" +
            "\t`date`\tTEXT,\n" +
            "\t`amount`\tINTEGER,\n" +
            "\t`category`\tTEXT,\n" +
            "\t`description`\tTEXT,\n" +
            "\t`cashcredit`\tTEXT\n" +
            ")";

    String sqlCreateBudgetTable = "CREATE TABLE if not exists `budget` (\n" +
            "\t`id`\tINTEGER PRIMARY KEY AUTOINCREMENT,\n" +
            "\t`fromdate`\tTEXT,\n" +
            "\t`amount`\tINTEGER,\n" +
            "\t`category`\tTEXT,\n" +
            "\t`todate`\tTEXT\n" +
            ")";

    String sqlCreateWeekTable = "CREATE TABLE if not exists `predict` (\n" +
            "\t`week`\tINTEGER NOT NULL PRIMARY KEY AUTOINCREMENT,\n" +
            "\t`amount`\tINTEGER\n" +
            ")";

    public DatabaseHelper(Context context) {
        super(context, name,null, version);

        getWritableDatabase().execSQL(sqlCreateUserTable);

        getWritableDatabase().execSQL(sqlCreateExpenseTable);

        getWritableDatabase().execSQL(sqlCreateIncomeTable);

        getWritableDatabase().execSQL(sqlCreatelbTable);

        getWritableDatabase().execSQL(sqlCreateBudgetTable);

        getWritableDatabase().execSQL(sqlCreateWeekTable);
    }

    public void insertBudget (ContentValues contentValues)
    {
        getWritableDatabase().insert("budget","",contentValues);
    }

    public void insertUser (ContentValues contentValues)
    {
        getWritableDatabase().insert("register","",contentValues);
    }

    public void insertPredictVal (ContentValues contentValues)
    {
        getWritableDatabase().insert("predict","",contentValues);
    }

    public  void updateuser(String id,ContentValues contentValues)
    {
        getWritableDatabase().update("register",contentValues,"id="+id,null);
    }

    public Userinfo getuserinfo(String id)
    {
        String sql = "Select * from register where id="+id;
        Cursor c = getReadableDatabase().rawQuery(sql,null);
        Userinfo info= new Userinfo();
        while(c.moveToNext())
        {
            info.id = c.getString(c.getColumnIndex("id"));
            info.username = c.getString(c.getColumnIndex("username"));
            info.password = c.getString(c.getColumnIndex("password"));
            info.email = c.getString(c.getColumnIndex("email"));
            info.confirmpass = c.getString(c.getColumnIndex("confirmpass"));
            info.gender = c.getString(c.getColumnIndex("gender"));
        }
        c.close();
        return info;
    }

    public boolean isloginsuccessful(String username,String password)
    {
        String sql= "Select count (*) from register where username='"+username+"' and password='"+password+"'";
        SQLiteStatement statement=getReadableDatabase().compileStatement(sql);
        long l = statement.simpleQueryForLong();
        statement.close();
        if(l==1)
        {
            return true;
        }
        return false;
    }

    public long getUserId(String username,String password)
    {
        String sql= "Select id from register where username='"+username+"' and password='"+password+"'";
        SQLiteStatement statement=getReadableDatabase().compileStatement(sql);
        long l = statement.simpleQueryForLong();
        statement.close();
        return l;
    }

    public void insertexpense (ContentValues contentValues)
    {
        getWritableDatabase().insert("expense","",contentValues);
    }

    public void deleteexpense (int id){

        getWritableDatabase().delete("expense","id =" +id,null);

    }

    public void updateexpense(Integer id,ContentValues contentValues){
        getWritableDatabase().update("expense",contentValues,"id="+id,null);
    }

    public ArrayList<ExpenseInfo> getexpenselist()
    {
        String sql= "Select * from expense ";
        Cursor c=getReadableDatabase().rawQuery(sql,null);
        ArrayList<ExpenseInfo> list=new ArrayList<>();
        while(c.moveToNext())
        {
            ExpenseInfo info= new ExpenseInfo();
            info.id = c.getInt(c.getColumnIndex("id"));
            info.date = c.getString(c.getColumnIndex("date"));
            info.amount = c.getInt(c.getColumnIndex("amount"));
            info.category = c.getString(c.getColumnIndex("category"));
            info.description = c.getString(c.getColumnIndex("description"));
            info.cashcredit = c.getString(c.getColumnIndex("cashcredit"));
            list.add(info);
        }
        c.close();
        return list;
    }

    public ArrayList<BudgetInfo> getbudgetlist()
    {
        String sql = "Select * from budget ";
        Cursor c = getReadableDatabase().rawQuery(sql,null);
        ArrayList<BudgetInfo> list=new ArrayList<>();
        while(c.moveToNext())
        {
            BudgetInfo info= new BudgetInfo();
            info.id = c.getInt(c.getColumnIndex("id"));
            info.fromdate = c.getString(c.getColumnIndex("fromdate"));
            info.amount = c.getInt(c.getColumnIndex("amount"));
            info.category = c.getString(c.getColumnIndex("category"));
            info.todate = c.getString(c.getColumnIndex("todate"));
            list.add(info);
        }
        c.close();
        return list;
    }

    public ExpenseInfo getexpenseinfo(int id){
        String sql= "Select * from expense where id="+id;
        Cursor c=getReadableDatabase().rawQuery(sql,null);
        ExpenseInfo info= new ExpenseInfo();
        while(c.moveToNext())
        {
            info.id = c.getInt(c.getColumnIndex("id"));
            info.date = c.getString(c.getColumnIndex("date"));
            info.amount = c.getInt(c.getColumnIndex("amount"));
            info.category = c.getString(c.getColumnIndex("category"));
            info.description = c.getString(c.getColumnIndex("description"));
            info.cashcredit = c.getString(c.getColumnIndex("cashcredit"));
        }
        c.close();
        return info;
    }

    public void insertincome (ContentValues contentValues)
    {
        getWritableDatabase().insert("income","",contentValues);
    }

    public void deleteincome (int id){

        getWritableDatabase().delete("income","id =" +id,null);

    }

    public void updateincome(Integer id,ContentValues contentValues){
        getWritableDatabase().update("income",contentValues,"id="+id,null);
    }

    public ArrayList<ExpenseInfo> getincomelist()
    {
        String sql= "Select * from income ";
        Cursor c=getReadableDatabase().rawQuery(sql,null);
        ArrayList<ExpenseInfo> list=new ArrayList<>();
        while(c.moveToNext())
        {
            ExpenseInfo info= new ExpenseInfo();
            info.id = c.getInt(c.getColumnIndex("id"));
            info.date = c.getString(c.getColumnIndex("date"));
            info.amount = c.getInt(c.getColumnIndex("amount"));
            info.category = c.getString(c.getColumnIndex("category"));
            info.description = c.getString(c.getColumnIndex("description"));
            list.add(info);
        }
        c.close();
        return list;
    }

    public ExpenseInfo getincomeinfo(int id){
        String sql= "Select * from income where id="+id;
        Cursor c=getReadableDatabase().rawQuery(sql,null);
        ExpenseInfo info= new ExpenseInfo();
        while(c.moveToNext())
        {
            info.id = c.getInt(c.getColumnIndex("id"));
            info.date = c.getString(c.getColumnIndex("date"));
            info.amount = c.getInt(c.getColumnIndex("amount"));
            info.category = c.getString(c.getColumnIndex("category"));
            info.description = c.getString(c.getColumnIndex("description"));
        }
        c.close();
        return info;
    }

    public int getcategorytotal(String category){

        String sql = "Select * from expense where category = '" + category + "'";
        Cursor c = getReadableDatabase().rawQuery(sql,null);
        int sum = 0,total = 0;
        while (c.moveToNext()){
            sum = c.getInt(c.getColumnIndex("amount"));
            total = total + sum;
        }
        c.close();
        return total;
    }

    public int getlinecategorytotal(String category, String date){

        String sql = "Select * from expense where category = '" + category + "' AND date >= date('" + date + "') AND date < date('" + date + "', '+7 day')";
        Cursor c = getReadableDatabase().rawQuery(sql,null);
        int sum = 0,total = 0;
        while (c.moveToNext()){
            sum = c.getInt(c.getColumnIndex("amount"));
            total = total + sum;
        }
        c.close();
        return total;
    }

    public ArrayList<ExpenseInfo> getincomeinfo(String date){

        String sql = "Select * from income where date ='" + date + "'";
        Cursor c = getReadableDatabase().rawQuery(sql,null);
        ArrayList<ExpenseInfo> list = new ArrayList<>();
        while(c.moveToNext())
        {
            ExpenseInfo info= new ExpenseInfo();
            info.id = c.getInt(c.getColumnIndex("id"));
            info.amount = c.getInt(c.getColumnIndex("amount"));
            info.category = c.getString(c.getColumnIndex("category"));
            info.description = c.getString(c.getColumnIndex("description"));
            list.add(info);
        }
        c.close();
        return list;
    }

    public ArrayList<ExpenseInfo> getexpenseinfo(String date){

        String sql= "Select * from expense where date ='" + date + "'";
        Cursor c=getReadableDatabase().rawQuery(sql,null);
        ArrayList<ExpenseInfo> list=new ArrayList<>();
        while(c.moveToNext())
        {
            ExpenseInfo info= new ExpenseInfo();
            info.id = c.getInt(c.getColumnIndex("id"));
            info.date = c.getString(c.getColumnIndex("date"));
            info.amount = c.getInt(c.getColumnIndex("amount"));
            info.category = c.getString(c.getColumnIndex("category"));
            info.description = c.getString(c.getColumnIndex("description"));
            info.cashcredit = c.getString(c.getColumnIndex("cashcredit"));
            list.add(info);
        }
        c.close();
        return list;
    }

    /*public long getbalance(){
        String sql = "Select SUM (amount) as Total from income";
        SQLiteStatement statement=getReadableDatabase().compileStatement(sql);
        long l = statement.simpleQueryForLong();
        statement.close();
        return l;

    }*/

    public void insertlb (ContentValues contentValues)
    {
        getWritableDatabase().insert("lb","",contentValues);
    }

    public void deletelb (int id){

        getWritableDatabase().delete("lb","id =" +id,null);

    }

    public void deletebudget (int id){

        getWritableDatabase().delete("budget","id =" +id,null);

    }

    public void updatelb(Integer id,ContentValues contentValues){
        getWritableDatabase().update("lb",contentValues,"id="+id,null);
    }

    public long getlentbalance(){
        String sql = "Select SUM (amount) as Total from lb where category='Lend'";
        SQLiteStatement statement=getReadableDatabase().compileStatement(sql);
        long l = statement.simpleQueryForLong();
        statement.close();
        return l;

    }
    public long getborrowbalance(){
        String sql = "Select SUM (amount) as Total from lb where category='Borrow'";
        SQLiteStatement statement=getReadableDatabase().compileStatement(sql);
        long l = statement.simpleQueryForLong();
        statement.close();
        return l;
    }

    public ArrayList<ExpenseInfo> getlblist()
    {
        String sql= "Select * from lb ";
        Cursor c=getReadableDatabase().rawQuery(sql,null);
        ArrayList<ExpenseInfo> list=new ArrayList<>();
        while(c.moveToNext())
        {
            ExpenseInfo info= new ExpenseInfo();
            info.id = c.getInt(c.getColumnIndex("id"));
            info.date = c.getString(c.getColumnIndex("date"));
            info.amount = c.getInt(c.getColumnIndex("amount"));
            info.category = c.getString(c.getColumnIndex("category"));
            info.description = c.getString(c.getColumnIndex("description"));
            info.cashcredit = c.getString(c.getColumnIndex("cashcredit"));
            list.add(info);
        }
        c.close();
        return list;
    }

    public ExpenseInfo getlbinfo(int id){
        String sql = "Select * from lb where id="+id;
        Cursor c = getReadableDatabase().rawQuery(sql,null);
        ExpenseInfo info = new ExpenseInfo();
        while(c.moveToNext())
        {
            info.id = c.getInt(c.getColumnIndex("id"));
            info.date = c.getString(c.getColumnIndex("date"));
            info.amount = c.getInt(c.getColumnIndex("amount"));
            info.category = c.getString(c.getColumnIndex("category"));
            info.description = c.getString(c.getColumnIndex("description"));
            info.cashcredit = c.getString(c.getColumnIndex("cashcredit"));
        }
        c.close();
        return info;
    }

    public int getlbtotal(int id){
        String sql = "Select amount from lb where id = " +id;
        Cursor c = getReadableDatabase().rawQuery(sql,null);
        int value = 0;
        while (c.moveToNext()){
            value = c.getInt(c.getColumnIndex("amount"));
        }
        c.close();
        return value;
    }


    public long getbalance(){
        long l=0;
        l = getcashtotal() + getcredittotal();
        return l;
    }

    public long getcashtotal(){
        String sql = "Select * from income where category = 'Cash'";
        Cursor c = getReadableDatabase().rawQuery(sql,null);
        long sum = 0,total = 0;
        while (c.moveToNext()){
            sum = c.getInt(c.getColumnIndex("amount"));
            total = total + sum;
        }
        c.close();

        String sql1 = "Select * from expense where cashcredit = 'Cash'";
        Cursor cursor = getReadableDatabase().rawQuery(sql1,null);
        long sum1=0,total1=0;
        while (cursor.moveToNext()){
            sum1 = cursor.getInt(cursor.getColumnIndex("amount"));
            total1 = total1 + sum1;
        }
        cursor.close();

        String sql2 = "Select SUM (amount) as Total from lb where category='Borrow' and cashcredit='Cash'";
        SQLiteStatement statement=getReadableDatabase().compileStatement(sql2);
        long l = statement.simpleQueryForLong();
        statement.close();

        String sql3 = "Select SUM (amount) as Total from lb where category='Lend' and cashcredit='Cash'";
        SQLiteStatement statement1=getReadableDatabase().compileStatement(sql3);
        long l1 = statement1.simpleQueryForLong();
        statement1.close();
        total = total-total1+l-l1;
        return total;
    }

    public int getBudgettotal(String category, String fromd, String tod){
        String sql = "Select * from expense where category = '" + category + "' AND date >= date('" + fromd + "') AND date <= date('" + tod + "')";

        Cursor c = getReadableDatabase().rawQuery(sql,null);
        int total = 0, sum;

        while (c.moveToNext()){
            sum = c.getInt(c.getColumnIndex("amount"));
            total = total + sum;
        }

        c.close();
        return total;
    }
    public long getcredittotal(){

        String sql = "Select * from income where category = 'Credit Card'";
        Cursor c = getReadableDatabase().rawQuery(sql,null);
        long sum = 0,total = 0;
        while (c.moveToNext()){
            sum = c.getInt(c.getColumnIndex("amount"));
            total = total + sum;
        }
        c.close();

        String sql1 = "Select * from expense where cashcredit = 'Credit Card'";
        Cursor cursor = getReadableDatabase().rawQuery(sql1,null);
        long sum1=0,total1=0;
        while (cursor.moveToNext()){
            sum1 = cursor.getInt(cursor.getColumnIndex("amount"));
            total1 = total1 + sum1;
        }
        cursor.close();

        String sql2 = "Select SUM (amount) as Total from lb where category='Borrow' and cashcredit='Credit Card'";
        SQLiteStatement statement=getReadableDatabase().compileStatement(sql2);
        long l = statement.simpleQueryForLong();
        statement.close();

        String sql3 = "Select SUM (amount) as Total from lb where category='Lend' and cashcredit='Credit Card'";
        SQLiteStatement statement1=getReadableDatabase().compileStatement(sql3);
        long l1 = statement1.simpleQueryForLong();
        statement1.close();
        total = total-total1+l-l1;
        return total;
    }

    public long getTotalPredict(){
        String sql = "Select count(week) from predict";
        SQLiteStatement statement=getReadableDatabase().compileStatement(sql);
        long l = statement.simpleQueryForLong();
        statement.close();

        return l;
    }

    public long get_total_train(int id){
        String sql = "Select amount from predict where week =" + id;
        SQLiteStatement statement=getReadableDatabase().compileStatement(sql);
        long l = statement.simpleQueryForLong();

        return l;
    }

    public ArrayList<ExpenseInfo> getrecentexpenselist()
    {
        String sql= "Select * from expense order by date DESC Limit 15";
        Cursor c=getReadableDatabase().rawQuery(sql,null);
        ArrayList<ExpenseInfo> list=new ArrayList<>();
        while(c.moveToNext())
        {
            ExpenseInfo info= new ExpenseInfo();
            info.id = c.getInt(c.getColumnIndex("id"));
            info.date = c.getString(c.getColumnIndex("date"));
            info.amount = c.getInt(c.getColumnIndex("amount"));
            info.category = c.getString(c.getColumnIndex("category"));
            info.description = c.getString(c.getColumnIndex("description"));
            info.cashcredit = c.getString(c.getColumnIndex("cashcredit"));
            list.add(info);
        }
        c.close();
        return list;
    }

    public int predictiontotal(String date){

        String sql = "Select * from expense where date < date('" + date + "') AND date >= date('" + date + "', '-7 day')";
        Cursor c = getReadableDatabase().rawQuery(sql,null);
        int sum = 0,total = 0;
        while (c.moveToNext()){
            sum = c.getInt(c.getColumnIndex("amount"));
            total = total + sum;
        }
        c.close();
        return total;
    }

    public String getstartdate(){
        String date;
        String sql = "SELECT date FROM expense LIMIT 1";
        SQLiteStatement statement = getReadableDatabase().compileStatement(sql);
        date = statement.simpleQueryForString();
        return date;
    }

    public  long gettotalincome(String value){
        String sql2 = "Select SUM (amount) from income where date = '" + value + "'";
        SQLiteStatement statement=getReadableDatabase().compileStatement(sql2);
        long l = statement.simpleQueryForLong();
        statement.close();
        return l;
    }

    public  long gettotalexpense(String value){
        String sql2 = "Select SUM (amount) from expense where date = '" + value + "'";
        SQLiteStatement statement=getReadableDatabase().compileStatement(sql2);
        long l = statement.simpleQueryForLong();
        statement.close();
        return l;
    }


    @Override
    public void onCreate(SQLiteDatabase db) {

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }

}


