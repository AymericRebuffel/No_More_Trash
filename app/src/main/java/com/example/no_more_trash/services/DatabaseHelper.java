package com.example.no_more_trash.services;


import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;
import com.example.no_more_trash.models.User;

import java.util.ArrayList;

public class DatabaseHelper extends SQLiteOpenHelper {

    private static final String TAG = "DatabaseHelper";

    private static final String DATABASE_NAME = "register.db";

    private static final String USER_TABLE = "registeruser";
    private static final String DECHET_TABLE = "dechetuser";


    private static final String ID = "ID";
    private static final String USER_NAME = "username";
    private static final String USER_PASSWORD = "password";
    private static final String USER_EMAIL = "email";
    private static final String USER_FIRSTNAME = "fName";


    private static final String DECHET_NAME = "food";
    private static final String DECHET_DATE = "datedechet";


    private static final String CREATE_TABLE = "CREATE TABLE " + USER_TABLE + " (" +
            ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
            USER_NAME + " TEXT, " +
            USER_PASSWORD + " TEXT, " +
            USER_EMAIL + " TEXT, " +
            USER_FIRSTNAME + " TEXT, " + ");";

    private static final String CREATE_TABLE_DECHET = "CREATE TABLE " + " (" +
            ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
            DECHET_DATE + " TEXT, " +
            USER_NAME + " TEXT, " +
            DECHET_DATE + " TEXT" + ");";


    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, 2);
    }


    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.execSQL(CREATE_TABLE);
        sqLiteDatabase.execSQL(CREATE_TABLE_DECHET);

        //Utilisateur par défaut
        User user = new User("julie", "123", "julie@gmail.com", "julie", 48);

        ContentValues contentValues = new ContentValues();
        contentValues.put("username", user.getUsername());
        contentValues.put("password", user.getPassword());
        contentValues.put("email", user.getEmail());
        contentValues.put("fName", user.getfName());
        sqLiteDatabase.insert(USER_TABLE, null, contentValues);


        User userBruno = new User("bruno", "123", "bruno@gmail.com", "Bruno", 48);
        ContentValues contentValuesBruno = new ContentValues();
        contentValuesBruno.put("username", userBruno.getUsername());
        contentValuesBruno.put("password", userBruno.getPassword());
        contentValuesBruno.put("email", userBruno.getEmail());
        contentValuesBruno.put("fName", userBruno.getfName());
        sqLiteDatabase.insert(USER_TABLE, null, contentValuesBruno);

    }

    @Override
    public void onOpen(SQLiteDatabase db) {
        super.onOpen(db);
        initializeUsers(db);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int oldVersion, int newVersion) {
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + USER_TABLE);
    }

    private void initializeUsers(SQLiteDatabase db) {
        String[] columns = {ID, USER_NAME, USER_PASSWORD, USER_EMAIL, USER_FIRSTNAME};
        Cursor cursor = db.query(USER_TABLE, columns, null, null, null, null, null);
        while (cursor.moveToNext()) {
            // int id = cursor.getInt(0);
            String username = cursor.getString(1);
            String password = cursor.getString(2);
            String email = cursor.getString(3);
            String fname = cursor.getString(4);
            double threshold = cursor.getDouble(5);
            User user = new User(username, password, email, fname, threshold);

        }
        cursor.close();
    }


    //Vérification de la combinaison utilisateur / mot de passe
    public User checkUser(String username, String password) {
        String[] columns = {ID};
        SQLiteDatabase db = getReadableDatabase();
        String selection = USER_NAME + "=?" + " and " + USER_PASSWORD + "=?";
        String[] selectionArgs = {username, password};
        Cursor cursor = db.query(USER_TABLE, columns, selection, selectionArgs, null, null, null);
        int count = cursor.getCount();
        cursor.close();
        db.close();
        if (count > 0) {
            return StaticContentUsers.getUser(username);
        }
        return null;
    }

    /*
    public static void addUserFromDataBase(User user) {
        registeredUsers.add(user);
    }
  */
    public long addUser(User user) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("username", user.getUsername());
        contentValues.put("password", user.getPassword());
        contentValues.put("email", user.getEmail());
        contentValues.put("fName", user.getfName());
        long res = db.insert(USER_TABLE, null, contentValues);
        db.close();
        return res;
    }
}
