package com.googleplaygames.dpc.jump_rope_counter_2;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class SettingDBHelper extends SQLiteOpenHelper {
    private static final String DBName = "setting.db";
    private static final int DBVersion = 4;

    public SettingDBHelper(Context context){
        super(context, DBName, null, DBVersion);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("DROP TABLE IF EXISTS sort_setting");
        db.execSQL("CREATE TABLE IF NOT EXISTS sort_setting (" +
                "sort_type INTEGER, " +
                "grade_setting_type INTEGER, " +
                "kinds_of_jump_rope_setting_type INTEGER, " +
                "record_sort_style INTEGER)");
        db.execSQL("DROP TABLE IF EXISTS selected_grade_setting");
        db.execSQL("CREATE TABLE IF NOT EXISTS selected_grade_setting (" +
                "id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "value INTEGER)");
        db.execSQL("DROP TABLE IF EXISTS selected_kinds_of_jump_rope_setting");
        db.execSQL("CREATE TABLE IF NOT EXISTS selected_kinds_of_jump_rope_setting (" +
                "id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "value INTEGER)");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        onCreate(db);
    }
}
