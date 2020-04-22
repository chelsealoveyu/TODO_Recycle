package com.joseph.todo_recycle;

import android.content.Context;
import android.database.Cursor;
import android.database.DatabaseErrorHandler;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.ArrayList;

public class SQLiteHelper {
    private static final String dbName = "myMemotest";
    private static final String table1 = "MemoTable";
    private static final int dbVersion = 1;

    private OpenHelper openHelper;
    private SQLiteDatabase db;

    private Context context;

    public SQLiteHelper(Context context){
        this.context = context;
        this.openHelper = new OpenHelper(context,dbName,null,dbVersion);
        db = openHelper.getWritableDatabase();
    }
    private class OpenHelper extends SQLiteOpenHelper{


        public OpenHelper(@Nullable Context context, @Nullable String name, @Nullable SQLiteDatabase.CursorFactory factory, int version) {
            super(context, name, factory, version);
        }

        @Override
        public void onCreate(SQLiteDatabase sqLiteDatabase) {
            String create = "CREATE TABLE "+ table1 +"("+
                    "seq integer PRIMARY KEY AUTOINCREMENT,"+
                    "maintext text,"+
                    "subtext text,"+
                    "isdone integer)";
            sqLiteDatabase.execSQL(create);


        }

        @Override
        public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
            sqLiteDatabase.execSQL("DROP TABLE IF EXISTS "+table1);
            onCreate(sqLiteDatabase);

        }
    }

    public void insertMemo(Memo memo){
        // INSERT INTO MemoTable VALUES(NULL, 'MAINTEXT', 'SUBTEXT',0);

        String sql = "INSERT INTO "+table1+" VALUES(NULL, '"+memo.matintext+"', '"+memo.subtext+"', "+memo.getIsdone()+");";
        db.execSQL(sql);
    }

    public void deleteMemo(int position){

        //DELETE FROM MemoTable WHERE seq = 0;
        String sql = "DELETE FROM "+table1+" WHERE seq = "+position+";";
        db.execSQL(sql);
    }

    // SELECT * FROM MemoTable;
    public ArrayList<Memo> selectAll(){
        String sql = "SELECT * FROM "+table1;


        ArrayList<Memo> list = new ArrayList<>();
        Cursor cursor = db.rawQuery(sql,null);
        cursor.moveToFirst();

        while(!cursor.isAfterLast()){

            Memo memo = new Memo(cursor.getInt(0), cursor.getString(1), cursor.getString(2), cursor.getInt(3));
            list.add(memo);
            cursor.moveToNext();
        }
        cursor.close();
        return list;
    }








}

