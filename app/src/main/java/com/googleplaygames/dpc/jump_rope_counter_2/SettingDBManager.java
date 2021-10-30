package com.googleplaygames.dpc.jump_rope_counter_2;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;

public class SettingDBManager {
    public static SQLiteDatabase db;

    public static SettingItem GetSetting(){
        SettingItem settingItem = new SettingItem();
        Cursor c = db.rawQuery("SELECT * FROM sort_setting", null);
        if(c.getCount() > 0){
            c.moveToNext();

            settingItem.sortType = c.getInt(c.getColumnIndex("sort_type"));
            settingItem.gradeSettingType = c.getInt(c.getColumnIndex("grade_setting_type"));
            settingItem.kindsOfJumpRopeSettingType = c.getInt(c.getColumnIndex("kinds_of_jump_rope_setting_type"));
            settingItem.recordSortStyle = c.getInt(c.getColumnIndex("record_sort_style"));

        }else {
            settingItem.sortType = 0;
            settingItem.gradeSettingType = 0;
            settingItem.kindsOfJumpRopeSettingType = 0;
            settingItem.recordSortStyle = 0;
        }
        c.close();

        c = db.rawQuery("SELECT * FROM selected_grade_setting", null);
        settingItem.selectedGradeSetting = new ArrayList<Integer>();
        for (int i = 0; i < c.getCount(); i++){
            c.moveToNext();
            settingItem.selectedGradeSetting.add(c.getInt(c.getColumnIndex("value")));
        }
        c.close();

        settingItem.selectedKindsOfJumpRopeSetting = new ArrayList<Integer>();
        c = db.rawQuery("SELECT * FROM selected_kinds_of_jump_rope_setting", null);
        for (int i = 0; i < c.getCount(); i++){
            c.moveToNext();
            settingItem.selectedKindsOfJumpRopeSetting.add(c.getInt(c.getColumnIndex("value")));
        }
        c.close();

        return settingItem;
    }

    public static void SaveSetting(SettingItem _settingItem){
        db.execSQL("DELETE FROM sort_setting");
        db.execSQL("INSERT INTO sort_setting (sort_type, grade_setting_type, kinds_of_jump_rope_setting_type, record_sort_style) " +
                "VALUES (" + Integer.toString(_settingItem.sortType) + ", " +
                Integer.toString(_settingItem.gradeSettingType) + ", " +
                Integer.toString(_settingItem.kindsOfJumpRopeSettingType) + ", " +
                Integer.toString(_settingItem.recordSortStyle) + ")");

        db.execSQL("DELETE FROM selected_grade_setting");
        for (int i = 0; i < _settingItem.selectedGradeSetting.size(); i++){
            db.execSQL("INSERT INTO selected_grade_setting (value) " +
                    "VALUES (" + Integer.toString(_settingItem.selectedGradeSetting.get(i)) + ")");
        }

        db.execSQL("DELETE FROM selected_kinds_of_jump_rope_setting");
        for (int i = 0; i < _settingItem.selectedKindsOfJumpRopeSetting.size(); i++){
            db.execSQL("INSERT INTO selected_kinds_of_jump_rope_setting (value) " +
                    "VALUES (" + Integer.toString(_settingItem.selectedKindsOfJumpRopeSetting.get(i)) + ")");
        }
    }
}
