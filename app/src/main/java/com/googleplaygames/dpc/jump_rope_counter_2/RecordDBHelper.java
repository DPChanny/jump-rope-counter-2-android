package com.googleplaygames.dpc.jump_rope_counter_2;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class RecordDBHelper extends SQLiteOpenHelper {
    private static final String DB_NAME = "record.db";
    private static final int DB_VERSION = 4;

    private static final int DB_VERSION_BEFORE_KINDS_OF_JUMP_ROPE_ADD_UPDATE = 3;

    public RecordDBHelper(Context context){
        super(context, DB_NAME, null, DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("DROP TABLE IF EXISTS groups");
        db.execSQL("CREATE TABLE IF NOT EXISTS groups (id INTEGER PRIMARY KEY AUTOINCREMENT, name TEXT)");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        if(oldVersion == DB_VERSION_BEFORE_KINDS_OF_JUMP_ROPE_ADD_UPDATE){
            Cursor c1 = db.rawQuery("SELECT id FROM groups", null);

            for (int i = 0; i < c1.getCount(); i++) {
                c1.moveToNext();

                db.execSQL("UPDATE " +
                        "group_id_" + Integer.toString(c1.getInt(c1.getColumnIndex("id")))
                        + " SET kinds_of_jump_rope=5 WHERE kinds_of_jump_rope=2");
                db.execSQL("UPDATE " +
                        "group_id_" + Integer.toString(c1.getInt(c1.getColumnIndex("id")))
                        + " SET kinds_of_jump_rope=6 WHERE kinds_of_jump_rope=3");
            }

            c1.close();
        }else {
            onCreate(db);
        }
    }
}
