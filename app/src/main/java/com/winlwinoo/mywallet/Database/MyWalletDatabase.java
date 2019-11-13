package com.winlwinoo.mywallet.Database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.winlwinoo.mywallet.DataModel.CategoryData;
import com.winlwinoo.mywallet.DataModel.ItemData;
import com.winlwinoo.mywallet.DataModel.PieChartData;

import java.util.ArrayList;
import java.util.List;

public class MyWalletDatabase extends SQLiteOpenHelper {

    SQLiteDatabase sqLiteDatabase;

    private static final String DATABASE_NAME = "db_my_wallet";
    private static final String TB_CATEGORY = "tb_category";
    private static final String TB_ITEM = "tb_item";
    private static final String ID = "id";
    private static final String CATEGORY_NAME = "category_name";
    private static final String CATEGORY_IMAGE = "category_image";
    private static final String ITEM_COUNT = "item_count";
    private static final String ITEM_NAME = "item_name";
    private static final String ITEM_PRICE = "item_price";
    private static final String ITEM_DATE_TIME = "item_date_time";

    public MyWalletDatabase(Context context) {
        super(context, DATABASE_NAME, null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        //create table table_name (id INTEGER PRIMARY KEY AUTOINCREMENT,category_name TEXT , category_image BLOB);
        db.execSQL("CREATE TABLE "+TB_CATEGORY
                +" ("+ID+" INTEGER PRIMARY KEY AUTOINCREMENT, "
                +CATEGORY_NAME+" TEXT ,"+ CATEGORY_IMAGE+" BLOB )");

        //create table table_name (id INTEGER PRIMARY KEY AUTOINCREMENT,item_name TEXT , category_name TEXT , item_price INTEGER , item_date_time TEXT);
        db.execSQL("CREATE TABLE "+TB_ITEM
                +"("+ID+" INTEGER PRIMARY KEY AUTOINCREMENT, "
        + ITEM_NAME+" TEXT ,"
        +CATEGORY_NAME+" TEXT ,"
        +ITEM_PRICE+" INTEGER ,"
        +ITEM_DATE_TIME+" TEXT)");

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS "+TB_CATEGORY);
    }

    public List<PieChartData> getPieChartData(String selected_date){
        List<PieChartData> pieChartDataList = new ArrayList<>();
        Cursor cursor=null;
        try {

            sqLiteDatabase = this.getWritableDatabase();
            String sql = "SELECT "+CATEGORY_NAME+","+ITEM_PRICE+" FROM "+TB_ITEM+" WHERE "+ITEM_DATE_TIME+"='"+selected_date+"' GROUP BY "+CATEGORY_NAME;

            cursor = sqLiteDatabase.rawQuery(sql,null);

            if (cursor.moveToFirst()){
                do {
                    PieChartData pieChartData = new PieChartData();
                    pieChartData.setCategory_name(cursor.getString(0));
                    pieChartData.setPrise(cursor.getInt(1));

                    pieChartDataList.add(pieChartData);
                }while (cursor.moveToNext());
            }
            Log.i("PIE CHART DATA => ","SUCCESS, "+pieChartDataList.size());
            cursor.close();

        }catch (Exception e){
            e.printStackTrace();
            Log.e("PIE CHART DATA => ",e.getMessage());
        }

        return pieChartDataList;
    }

    public void insertCategory(String category_name, byte[] image){
        try {
            sqLiteDatabase = this.getWritableDatabase();

            ContentValues contentValues = new ContentValues();
            contentValues.put(CATEGORY_NAME,category_name);
            contentValues.put(CATEGORY_IMAGE,image);
            sqLiteDatabase.insert(TB_CATEGORY,null,contentValues);
            sqLiteDatabase.close();
            Log.i("INSERT CATEGORY DB => ","COMPLETE");
        }catch (Exception e){
            e.printStackTrace();
            Log.e("INSERT CATEGORY DB => ",e.getMessage());
        }

    }

    /*public Integer getCurrentItemCount(String category_name){
        int count=0;
        Cursor cursor = null;

        try {

            sqLiteDatabase = this.getWritableDatabase();

            String sql = "SELECT * FROM "+TB_CATEGORY + " WHERE "+CATEGORY_NAME+"='"+category_name+"'";

            cursor = sqLiteDatabase.rawQuery(sql,null);

            if (cursor.moveToFirst()){
                do {
                    count = cursor.getInt(3);
                }while (cursor.moveToNext());
            }
            Log.i("SELECT COUNT => ","SUCCESS : "+count);
            cursor.close();
        }catch (Exception e){
            e.printStackTrace();
            Log.e("SELECT COUNT => ",e.getMessage());
        }
        return count;
    }

    public void updateItemCount(String category_name , int item_count){
        try {
            sqLiteDatabase = this.getWritableDatabase();

            ContentValues contentValues = new ContentValues();
            contentValues.put(ITEM_COUNT,item_count);
            sqLiteDatabase.update(TB_CATEGORY,contentValues,CATEGORY_NAME+"=?",new String[]{category_name});
            sqLiteDatabase.close();
            Log.i("UPDATE ITEM COUNT => ","COMPLETE");
        }catch (Exception e){
            e.printStackTrace();
            Log.e("UPDATE ITEM COUNT => ",e.getMessage());
        }

    }*/

    public void insertItem(String item_name , String category_name , int item_price , String item_date_time){
        try {
            sqLiteDatabase = this.getWritableDatabase();

            ContentValues contentValues = new ContentValues();
            contentValues.put(ITEM_NAME,item_name);
            contentValues.put(CATEGORY_NAME,category_name);
            contentValues.put(ITEM_PRICE,item_price);
            contentValues.put(ITEM_DATE_TIME,item_date_time);
            sqLiteDatabase.insert(TB_ITEM,null,contentValues);
            sqLiteDatabase.close();
            Log.i("INSERT ITEM DB => ","COMPLETE");
        }catch (Exception e){
            e.printStackTrace();
            Log.e("INSERT ITEM DB => ",e.getMessage());
        }
    }

    public List<CategoryData> getAllCategory(){
        List<CategoryData> categoryDataList = new ArrayList<>();
        Cursor cursor = null;

        try {

            sqLiteDatabase = this.getWritableDatabase();

            String sql = "SELECT * FROM "+TB_CATEGORY;

            cursor = sqLiteDatabase.rawQuery(sql,null);

            if (cursor.moveToFirst()){
                do {
                    CategoryData categoryData = new CategoryData();
                    categoryData.setCategory_name(cursor.getString(1));

                    categoryDataList.add(categoryData);
                }while (cursor.moveToNext());
            }
            Log.i("SELECT CATEGORY DB => ","SUCCESS");
            cursor.close();
        }catch (Exception e){
            e.printStackTrace();
            Log.e("SELECT CATEGORY DB => ",e.getMessage());
        }
        return categoryDataList;
    }

    public List<CategoryData> getAllCategoryByDate(String date){
        List<CategoryData> categoryDataList = new ArrayList<>();
        Cursor cursor = null;

        try {

            sqLiteDatabase = this.getWritableDatabase();

            //select category_name,image,count(i) from c,i where category.name=item.name and date = date group by c.name;
            String sql = "SELECT "
                    +TB_CATEGORY+"."+CATEGORY_NAME+","
                    +TB_CATEGORY+"."+CATEGORY_IMAGE+", COUNT("
                    +TB_ITEM+"."+ITEM_NAME+
                    ") FROM "+TB_CATEGORY+","
                    +TB_ITEM+" WHERE "+TB_CATEGORY+"."+CATEGORY_NAME
                    +"="+TB_ITEM+"."+CATEGORY_NAME
                    +" AND "+TB_ITEM+"."+ITEM_DATE_TIME
                    +"='"+date+"' GROUP BY "+TB_CATEGORY+"."+CATEGORY_NAME;

            cursor = sqLiteDatabase.rawQuery(sql,null);

            if (cursor.moveToFirst()){
                do {
                    CategoryData categoryData = new CategoryData();
                    categoryData.setCategory_name(cursor.getString(0));
                    categoryData.setImage(cursor.getBlob(1));
                    categoryData.setItem_count(cursor.getInt(2));

                    categoryDataList.add(categoryData);
                }while (cursor.moveToNext());
            }
            Log.i("SELECT CATEGORY => ","SUCCESS");
            cursor.close();
        }catch (Exception e){
            e.printStackTrace();
            Log.e("SELECT CATEGORY => ",e.getMessage());
        }
        return categoryDataList;
    }

    public Integer getTotalUsageAmount(String selected_date){
        int total_usage_amount=0;
        Cursor cursor=null;
        try {
            sqLiteDatabase = this.getWritableDatabase();
            String sql = "SELECT SUM("+ITEM_PRICE+") FROM "+TB_ITEM +" WHERE "+ITEM_DATE_TIME+"='"+selected_date+"'";
            cursor = sqLiteDatabase.rawQuery(sql,null);
            if (cursor.moveToFirst()){
                do {
                   total_usage_amount = cursor.getInt(0);
                }while (cursor.moveToNext());
            }
            Log.i("TOTAL AMOUNT =>","SUCCESS");
            cursor.close();
        }catch (Exception e){
            e.printStackTrace();
            Log.e("TOTAL AMOUNT => ",e.getMessage());
        }

        return total_usage_amount;
    }

    public Integer getItemUsageAmount( String category_name, String date){
        int total_item_usage_amount=0;
        Cursor cursor=null;
        try {
            sqLiteDatabase = this.getWritableDatabase();
            String sql = "SELECT SUM("+ITEM_PRICE+") FROM "+TB_ITEM+" WHERE "+CATEGORY_NAME+"='"+category_name+"' AND "+ITEM_DATE_TIME +"='"+date+"'";
            cursor = sqLiteDatabase.rawQuery(sql,null);
            if (cursor.moveToFirst()){
                do {
                    total_item_usage_amount = cursor.getInt(0);
                }while (cursor.moveToNext());
            }
            Log.i("TOTAL ITEM AMOUNT =>","SUCCESS");
            cursor.close();
        }catch (Exception e){
            e.printStackTrace();
            Log.e("TOTAL ITEM AMOUNT => ",e.getMessage());
        }

        return total_item_usage_amount;
    }

    public List<ItemData> getAllItemDataCategory(String category_name , String date){
        List<ItemData> itemDataList = new ArrayList<>();
        Cursor cursor = null;

        try {

            sqLiteDatabase = this.getWritableDatabase();

            String sql = "SELECT * FROM "+TB_ITEM +" WHERE "+CATEGORY_NAME +"='"+category_name+"' AND "+ITEM_DATE_TIME+"='"+date+"'";

            cursor = sqLiteDatabase.rawQuery(sql,null);

            if (cursor.moveToFirst()){
                do {
                    ItemData itemData = new ItemData();
                    itemData.setUsage_name(cursor.getString(1));
                    itemData.setCategory_name(cursor.getString(2));
                    itemData.setUsage_amount(cursor.getInt(3));
                    itemData.setUsage_date(cursor.getString(4));
                    itemDataList.add(itemData);
                }while (cursor.moveToNext());
            }
            Log.i("SELECT ITEM DB => ","SUCCESS");
        }catch (Exception e){
            e.printStackTrace();
            Log.e("SELECT ITEM DB => ",e.getMessage());
        }
        return itemDataList;
    }

}
