package com.socialbuy.firstcopy;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by kushansingh on 21/09/17.
 */

public class WishListDatabase {

    public static final String DB_NAME = "wishlist.db";
    public static final int VERSION  = 2;

    public static final String TABLE_NAME = "product_details";
    public static final String COL_ID = "id";
    public static final String COL_USERNAME = "user_name";
    public static final String COL_IMAGEURL = "image_url";
    public static final String COL_PROFILELINK = "profile_link";
    public static final String COL_PRODUCTDESC = "product_desc";
    public static final String COL_DATEOFCREATION = "dateofcreation";
    public static final String COL_SELLER_BIO = "sellerbio";
    public static final String COL_PHONE_NUMBER = "sellerphonenumber";

    public static final String CREATE_QUERY = "CREATE TABLE " + TABLE_NAME + " (" + COL_ID + " INTEGER NOT NULL PRIMARY KEY, " + COL_USERNAME + " TEXT NOT NULL, " + COL_IMAGEURL + " TEXT NOT NULL, " + COL_PROFILELINK + " TEXT NOT NULL, " + COL_PRODUCTDESC + " TEXT NOT NULL, " + COL_DATEOFCREATION + " TEXT NOT NULL, " + COL_SELLER_BIO + " TEXT NOT NULL, " + COL_PHONE_NUMBER + " TEXT NOT NULL)";
    public static final String DROP_QUERY = "DROP TABLE IF EXISTS " + TABLE_NAME;


    InternalDatabase internalDatabase;
    SQLiteDatabase sqLiteDatabase;
    Context context;


    public class InternalDatabase extends SQLiteOpenHelper{

        public InternalDatabase(Context context) {
            super(context, DB_NAME, null, VERSION);
        }

        @Override
        public void onCreate(SQLiteDatabase sqLiteDatabase) {

            sqLiteDatabase.execSQL(CREATE_QUERY);

        }

        @Override
        public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {


            if (i1 > i) {


                sqLiteDatabase.execSQL(DROP_QUERY);
                onCreate(sqLiteDatabase);
            }
        }
    }

    public long insert(String userName, String imageUrl, String profileLink, String productDesc, String dateofCreation,String sellerBio, String sellerPhoneNumber){
        ContentValues cv = new ContentValues();

        cv.put(COL_USERNAME,userName);
        cv.put(COL_IMAGEURL,imageUrl);
        cv.put(COL_PROFILELINK,profileLink);
        cv.put(COL_PRODUCTDESC,productDesc);
        cv.put(COL_DATEOFCREATION,dateofCreation);
        cv.put(COL_SELLER_BIO,sellerBio);
        cv.put(COL_PHONE_NUMBER,sellerPhoneNumber);

        long res= sqLiteDatabase.insert(TABLE_NAME,null,cv);
        return res;
    }

    public String getAllDetails(){
        String result = "";
        String userName = "";
        String imageUrl = "";
        String profileLink = "";
        String productDesc = "";
        String dateofCreation = "";
        String sellerBio = "";
        String sellerPhoneNumber = "";

        String query = "SELECT * FROM " + TABLE_NAME;
        Cursor cursor = sqLiteDatabase.rawQuery(query,null);

        int i_username =  cursor.getColumnIndex(COL_USERNAME);
        int i_imageUrl = cursor.getColumnIndex(COL_IMAGEURL);
        int i_profileLink = cursor.getColumnIndex(COL_PROFILELINK);
        int i_productDesc = cursor.getColumnIndex(COL_PRODUCTDESC);
        int i_dateofCreation = cursor.getColumnIndex(COL_DATEOFCREATION);
        int i_sellerBio = cursor.getColumnIndex(COL_SELLER_BIO);
        int i_sellerPhoneNumber = cursor.getColumnIndex(COL_PHONE_NUMBER);

        int i = 0;
        for (cursor.moveToFirst(); !cursor.isAfterLast(); cursor.moveToNext()){

            if (i == 0){
                userName = userName + cursor.getString(i_username);
                imageUrl = imageUrl + cursor.getString(i_imageUrl);
                profileLink = profileLink + cursor.getString(i_profileLink);
                productDesc = productDesc + cursor.getString(i_productDesc);
                dateofCreation = dateofCreation + cursor.getString(i_dateofCreation);
                sellerBio = sellerBio + cursor.getString(i_sellerBio);
                sellerPhoneNumber = sellerPhoneNumber + cursor.getString(i_sellerPhoneNumber);
            }else {
                userName = userName + ">" + cursor.getString(i_username);
                imageUrl = imageUrl + ">" +  cursor.getString(i_imageUrl);
                profileLink = profileLink + ">" +  cursor.getString(i_profileLink);
                productDesc = productDesc +  ">" + cursor.getString(i_productDesc);
                dateofCreation = dateofCreation + ">" +  cursor.getString(i_dateofCreation);
                sellerBio = sellerBio + ">" +  cursor.getString(i_sellerBio);
                sellerPhoneNumber = sellerPhoneNumber + ">" +  cursor.getString(i_sellerPhoneNumber);
            }

            i++;




        }

        result = userName + "<" + imageUrl + "<" + profileLink + "<" + productDesc + "<" + dateofCreation + "<" + sellerBio + "<" + sellerPhoneNumber;

        return result;




    }

    public boolean checkForWishList(String imageUrl){
        boolean isWishlist = false;

        String[] params = new String[]{imageUrl};
        Cursor c = sqLiteDatabase.rawQuery("SELECT * FROM " + TABLE_NAME + " WHERE " + COL_IMAGEURL + " = ?",params);
        if (c.getCount() > 0){
            isWishlist = true;
        }



        return isWishlist;
    }

    public boolean removeFromWishList(String imageUrl){
        String[] params = new String[]{imageUrl};
        return sqLiteDatabase.delete(TABLE_NAME,COL_IMAGEURL + " = ?", params) > 0;
    }


    public WishListDatabase(Context c){

        this.context = c;

    }

    public WishListDatabase open(){
        internalDatabase = new InternalDatabase(context);
        sqLiteDatabase = internalDatabase.getWritableDatabase();

        return this;
    }

    public void close(){
        sqLiteDatabase.close();
    }



}
