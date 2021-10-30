package com.googleplaygames.dpc.jump_rope_counter_2;

import android.content.res.Resources;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Collections;

public class RecordDBManager {
    public static SQLiteDatabase db;

    public static ArrayList<RecordItem> GetRecords(String _groupName){
        ArrayList<RecordItem> result = new ArrayList<RecordItem>();

        Cursor c = db.rawQuery("SELECT * FROM " + GetGroupTableName(_groupName), null);

        for (int i = 0; i < c.getCount(); i++){
            c.moveToNext();

            RecordItem item = new RecordItem(
                    c.getString(c.getColumnIndex("name")),
                    c.getInt(c.getColumnIndex("count")),
                    c.getInt(c.getColumnIndex("elapsed_time")),
                    c.getInt(c.getColumnIndex("grade")),
                    c.getInt(c.getColumnIndex("kinds_of_jump_rope")),
                    Long.parseLong(c.getString(c.getColumnIndex("add_time"))),
                    c.getInt(c.getColumnIndex("id")));

            if(IsAbleToAddByGrade(item) && IsAbleToAddByKindsOfJumpRope(item)){
                result.add(item);
            }
        }

        c.close();

        RecordArrayListSorter.Sort(result);
        return result;
    }

    public static ArrayList<String> GetGroups() {
        ArrayList<String> groups = new ArrayList<String>();

        Cursor c = db.rawQuery("SELECT name FROM groups", null);

        for (int i = 0; i < c.getCount(); i++) {
            c.moveToNext();
            groups.add(c.getString(c.getColumnIndex("name")));
        }

        c.close();

        return groups;
    }

    private static String GetGroupTableName(String _groupName){
        Cursor c = db.rawQuery("SELECT id FROM groups "+
                "WHERE name='" + _groupName + "'", null);
        c.moveToFirst();
        String result = "group_id_" +
                Integer.toString(c.getInt(c.getColumnIndex("id")));
        c.close();
        return result;
    }

    public static String AddNewGroup(String _groupName, Resources _resources) {
        if(!_groupName.replaceAll(" ", "").isEmpty()){
            if (!RecordDBManager.GetGroups().contains(_groupName)) {
                db.execSQL("INSERT INTO groups (name) " +
                        "VALUES ('" + _groupName + "')");
                db.execSQL("DROP TABLE IF EXISTS " + GetGroupTableName(_groupName));
                db.execSQL("CREATE TABLE " + GetGroupTableName(_groupName) +
                        " (id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                        "name TEXT, " +
                        "count INTEGER, " +
                        "elapsed_time INTEGER, " +
                        "grade INTEGER, " +
                        "kinds_of_jump_rope INTEGER, " +
                        "add_time TEXT)");
                return _resources.getString(R.string.add_new_group_success);
            } else {
                return _resources.getString(R.string.add_new_group_failure_group_name_already_exists);
            }
        }else{
            return _resources.getString(R.string.add_new_group_failure_no_group_name);
        }
    }

    public static String AddNewGroupWithExistingGroup(
            String _groupName,
            ArrayList<Integer> _existingGroupNames,
            Resources _resources) {
        String result = AddNewGroup(_groupName, _resources);
        ArrayList<String> groups = GetGroups();
        if(result.equals(_resources.getString(R.string.add_new_group_success)))
        {
            for (int i = 0; i < _existingGroupNames.size(); i++){
                ArrayList<RecordItem> records = GetRecords(groups.get(_existingGroupNames.get(i)));
                for (int j = 0; j < records.size(); j++){
                    AddNewRecord(_groupName, records.get(j), _resources);
                }
            }
            return _resources.getString(R.string.add_new_group_with_existing_group_success);
        }else{
            return result;
        }
    }

    public static void DeleteGroup(String _groupName) {
        db.execSQL("DROP TABLE IF EXISTS " + GetGroupTableName(_groupName));
        db.execSQL("DELETE FROM groups " +
                "WHERE name='" + _groupName + "'");
    }

    public static String EditGroup(
            String _groupName,
            String _newGroupName,
            Resources _resources){
        if(!_newGroupName.replaceAll(" ", "").isEmpty()){
            if (!RecordDBManager.GetGroups().contains(_newGroupName)) {
                db.execSQL("UPDATE groups" +
                        " SET name='" + _newGroupName + "' WHERE name='" + _groupName + "'");
                return _resources.getString(R.string.edit_group_success);
            } else {
                return _resources.getString(R.string.edit_group_failure_group_name_already_exists);
            }
        }else{
            return _resources.getString(R.string.edit_group_failure_no_group_name);
        }
    }

    public static String AddNewRecord(
            String _groupName,
            RecordItem _recordItem,
            Resources _resources){
        if(!_recordItem.name.replaceAll(" ", "").isEmpty()){
            db.execSQL("INSERT INTO " + GetGroupTableName(_groupName) +
                    " (name, count, elapsed_time, grade, kinds_of_jump_rope, add_time) " +
                    "VALUES ('" + _recordItem.name + "', " +
                    Integer.toString(_recordItem.count) + ", " +
                    Integer.toString(_recordItem.elapsedTime) + ", " +
                    Integer.toString(_recordItem.grade) + ", " +
                    Integer.toString(_recordItem.kindsOfJumpRope) + ", " +
                    "'" + Long.toString(_recordItem.addTime)+ "')");
            return _resources.getString(R.string.add_new_record_success);
        }else {
            return _resources.getString(R.string.add_new_record_failure_no_name);
        }
    }

    public static void DeleteRecord(String _groupName, int _id){
        db.execSQL("DELETE FROM " + GetGroupTableName(_groupName) +
                " WHERE id=" + Integer.toString(_id));
    }

    public static String EditRecord(
            String _groupName,
            String _newGroupName,
            RecordItem _recordItem,
            Resources _resources){
        if(!_recordItem.name.replaceAll(" ", "").isEmpty()){
            if(!_groupName.matches(_newGroupName)){
                DeleteRecord(_groupName, _recordItem.id);
                AddNewRecord(_newGroupName, _recordItem, _resources);
            }else {
                db.execSQL("UPDATE " + GetGroupTableName(_groupName) +
                        " SET name='" + _recordItem.name +
                        "', count=" + Integer.toString(_recordItem.count) +
                        ", elapsed_time=" + Integer.toString(_recordItem.elapsedTime) +
                        ", grade=" + Integer.toString(_recordItem.grade) +
                        ", kinds_of_jump_rope=" + Integer.toString(_recordItem.kindsOfJumpRope) +
                        " WHERE id=" + Integer.toString( _recordItem.id));
            }
            return _resources.getString(R.string.edit_record_success);
        }else {
            return _resources.getString(R.string.edit_record_failure_no_name);
        }
    }

    private static boolean IsAbleToAddByGrade(RecordItem _item){
        if(Public.settingItem.gradeSettingType == SettingItem.SETTING_TYPE_NONE){
            return true;
        }
        else if(Public.settingItem.gradeSettingType == SettingItem.SETTING_TYPE_CONTAIN){
            return Public.settingItem.selectedGradeSetting.contains(_item.grade);
        }
        else if(Public.settingItem.gradeSettingType == SettingItem.SETTING_TYPE_REMOVE){
            return !Public.settingItem.selectedGradeSetting.contains(_item.grade);
        }
        return false;
    }

    private static boolean IsAbleToAddByKindsOfJumpRope(RecordItem _item){
        if(Public.settingItem.kindsOfJumpRopeSettingType == SettingItem.SETTING_TYPE_NONE){
            return true;
        }
        else if(Public.settingItem.kindsOfJumpRopeSettingType == SettingItem.SETTING_TYPE_CONTAIN){
            return Public.settingItem.selectedKindsOfJumpRopeSetting.contains(_item.kindsOfJumpRope);
        }
        else if(Public.settingItem.kindsOfJumpRopeSettingType == SettingItem.SETTING_TYPE_REMOVE){
            return !Public.settingItem.selectedKindsOfJumpRopeSetting.contains(_item.kindsOfJumpRope);
        }
        return false;
    }

    public static class RecordArrayListSorter {
        public static void Sort(ArrayList<RecordItem> arr) {
            Sort(arr, 0, arr.size() - 1);
            if(Public.settingItem.sortType == SettingItem.SORT_BY_OLD){
                Collections.reverse(arr);
            }
            else if(Public.settingItem.sortType == SettingItem.SORT_BY_HIGH_COUNT){
                Collections.reverse(arr);
            }
            else if(Public.settingItem.sortType == SettingItem.SORT_BY_HIGH_TIME){
                Collections.reverse(arr);
            }
            else if(Public.settingItem.sortType == SettingItem.SORT_BY_HIGH_JPS){
                Collections.reverse(arr);
            }
        }

        private static void Sort(ArrayList<RecordItem> arr, int low, int high) {
            if (low >= high) return;

            int mid = Partition(arr, low, high);
            Sort(arr, low, mid - 1);
            Sort(arr, mid, high);
        }

        private static int Partition(ArrayList<RecordItem> arr, int low, int high) {
            double pivot = 0;
            if(Public.settingItem.IsSortByCount()){
                pivot = arr.get((low + high) / 2).count;
            }else if(Public.settingItem.IsSortByTime()){
                pivot = arr.get((low + high) / 2).elapsedTime;
            }else if(Public.settingItem.IsSortByAddTime()){
                pivot = arr.get((low + high) / 2).addTime;
            }else if(Public.settingItem.IsSortByJPS()){
                pivot = arr.get((low + high) / 2).GetDoubleJPS();
            }
            while (low <= high) {
                if(Public.settingItem.IsSortByCount()){
                    while (arr.get(low).count < pivot) low++;
                    while (arr.get(high).count  > pivot) high--;
                }
                else if(Public.settingItem.IsSortByTime()){
                    while (arr.get(low).elapsedTime < pivot) low++;
                    while (arr.get(high).elapsedTime > pivot) high--;
                }else if(Public.settingItem.IsSortByAddTime()){
                    while (arr.get(low).addTime < pivot) low++;
                    while (arr.get(high).addTime > pivot) high--;
                }else if(Public.settingItem.IsSortByJPS()){
                    while (arr.get(low).GetDoubleJPS() < pivot) low++;
                    while (arr.get(high).GetDoubleJPS() > pivot) high--;
                }
                if (low <= high) {
                    Collections.swap(arr, low, high);
                    low++;
                    high--;
                }
            }
            return low;
        }
    }

    public static String GetGroupAverageJPS(String _groupName){
        Double sum = 0.0;
        ArrayList<RecordItem> records = GetRecords(_groupName);
        for (int i = 0; i < records.size(); i++){
            sum += records.get(i).GetDoubleJPS();
        }
        Double result = sum/((double)records.size());
        if(result.isNaN()){
            return "0";
        }else {
            DecimalFormat frmt = new DecimalFormat();
            frmt.setMaximumFractionDigits(2);
            return frmt.format(result);
        }
    }

    public static String GetGroupAverageCount(String _groupName){
        int sum = 0;
        ArrayList<RecordItem> records = GetRecords(_groupName);
        for (int i = 0; i < records.size(); i++){
            sum += records.get(i).count;
        }
        Double result =  ((double)sum)/((double) records.size());
        if(result.isNaN()){
            return "0";
        }else {
            DecimalFormat frmt = new DecimalFormat();
            frmt.setMaximumFractionDigits(2);
            return frmt.format(result);
        }
    }

    public static String GetGroupAverageElapsedTime(String _groupName){
        int sum = 0;
        ArrayList<RecordItem> records = GetRecords(_groupName);
        for (int i = 0; i < records.size(); i++){
            sum += records.get(i).elapsedTime;
        }
        Double result =  ((double)sum)/((double) records.size());
        if(result.isNaN()){
            return "0";
        }else {
            DecimalFormat frmt = new DecimalFormat();
            frmt.setMaximumFractionDigits(2);
            return frmt.format(result);
        }
    }
}
