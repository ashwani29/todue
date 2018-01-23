package com.example.ashwani.a4_11_17;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Parcel;
import android.os.Parcelable;
import android.util.Log;

import java.io.Serializable;
import java.util.ArrayList;

import static android.R.attr.id;
import static android.R.attr.thickness;
import static android.content.ContentValues.TAG;
import static java.sql.Types.INTEGER;


/**
 * Created by ashwani on 04-11-2017.
 */

public class TodoDatabase extends SQLiteOpenHelper implements Serializable {

    public static final String DB_NAME = "todo.db";
    public static final int DB_VERSION = 1;
    public static final String COLUMN_ID = "Id";
    public static final String COLUMN_TITLE = "title";
    public static final String COLUMN_DESCRIPTION = "description";
    public static final String COLUMN_STATUS = "status";
    public static final String COLUMN_REMINDER = "reminder";

    public static final String TABLE_NAME = "todo";
    public static final String PK_AUTOINCREMENT = " INTEGER PRIMARY KEY AUTOINCREMENT ";
    public static final String CREATE_TABLE = "CREATE TABLE ";
    public static final String INTEGER = " INTEGER ";
    public static final String TEXT = " TEXT ";
    public static final String COMMA = " , ";
    public static final String RBR = " ( ";
    public static final String LBR = " ) ";
    public static final String TERM = " ; ";




    public TodoDatabase(Context context, String name, SQLiteDatabase.CursorFactory factory, int version ) {
        super(context, name, factory, version);

    }

    public ArrayList<Todo> fetchALLTodos() {
        ArrayList<Todo> todoArrayList = new ArrayList<>();
        SQLiteDatabase sqLiteDatabase = getReadableDatabase();
        Cursor c = sqLiteDatabase.query(
                TABLE_NAME,
                null,
                null,
                null,
                null,
                null,
                COLUMN_ID + " desc"
        );

        while (c.moveToNext()) {
            int id_index = c.getColumnIndex(COLUMN_ID);
            int desc_index = c.getColumnIndex(COLUMN_DESCRIPTION);
            int title_index = c.getColumnIndex(COLUMN_TITLE);
            int status_index = c.getColumnIndex(COLUMN_STATUS);

            int id = c.getInt(id_index);
            Log.e("TAG", "ID IS " + id);
            String title = c.getString(title_index);
            String desc = c.getString(desc_index);
            int status = c.getInt(status_index);
            Todo todo = new Todo(id, title, desc);
            todoArrayList.add(todo);
        }

        return todoArrayList;
    }

    public boolean insertTodo(Todo todo) {
        SQLiteDatabase sqLiteDataBase = getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(COLUMN_TITLE, todo.getTitle());
        contentValues.put(COLUMN_DESCRIPTION, todo.getDescription());
        contentValues.put(COLUMN_STATUS, todo.getStatus());

        long rowInserted = sqLiteDataBase.insert(TABLE_NAME,
                null,
                contentValues);

        Log.e(TAG, "insertTodo" + " " + todo.getId() + " " + todo.getTitle() + " " + todo.getDescription() + " " + todo.getStatus());
        if (rowInserted == -1) {
            return false;
        }
        return true;
    }

    public void deleteTodo(Todo todo){
        SQLiteDatabase sqLiteDatabase = getWritableDatabase();
        sqLiteDatabase.delete(TABLE_NAME," Id = ?",new String[]{String.valueOf(todo.getId())});
        Log.e("TAG", "delete command called");
    }

    public void updateTodo( int id, String dateTime ){
        ContentValues contentValues = new ContentValues();
        contentValues.put(COLUMN_REMINDER, dateTime);
        SQLiteDatabase sqLiteDatabase = getWritableDatabase();
        sqLiteDatabase.update(TABLE_NAME, contentValues, " Id = ?",new String[]{String.valueOf(id)});
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String query = CREATE_TABLE + TABLE_NAME + RBR +
                COLUMN_ID + PK_AUTOINCREMENT + COMMA +
                COLUMN_TITLE + TEXT + COMMA +
                COLUMN_DESCRIPTION + TEXT + COMMA+
                COLUMN_REMINDER + TEXT + COMMA+
                COLUMN_STATUS + TEXT + LBR + TERM;

        Log.e("TAG", "query is" + " " + query);
        db.execSQL(query);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }


}
