package com.googleplaygames.dpc.jump_rope_counter_2;

import java.util.ArrayList;

public class SettingItem {
    public static final int SETTING_TYPE_NONE = 0;
    public static final int SETTING_TYPE_REMOVE = 1;
    public static final int SETTING_TYPE_CONTAIN = 2;

    public static final int SORT_BY_OLD = 0;
    public static final int SORT_BY_RECENT = 1;
    public static final int SORT_BY_HIGH_COUNT = 2;
    public static final int SORT_BY_LOW_COUNT = 3;
    public static final int SORT_BY_HIGH_TIME = 4;
    public static final int SORT_BY_LOW_TIME = 5;
    public static final int SORT_BY_HIGH_JPS = 6;
    public static final int SORT_BY_LOW_JPS = 7;

    public static final int RECORD_SORT_STYLE_SIMPLE = 0;
    public static final int RECORD_SORT_STYLE_DETAIL = 1;

    public int sortType;
    public int gradeSettingType;
    public int kindsOfJumpRopeSettingType;
    public ArrayList<Integer> selectedGradeSetting;
    public ArrayList<Integer> selectedKindsOfJumpRopeSetting;
    public int recordSortStyle;

    public boolean IsSortByCount() {
        return sortType == SORT_BY_HIGH_COUNT || sortType == SORT_BY_LOW_COUNT;
    }

    public boolean IsSortByTime() {
        return sortType == SORT_BY_HIGH_TIME || sortType == SORT_BY_LOW_TIME;
    }

    public boolean IsSortByAddTime() {
        return sortType == SORT_BY_OLD || sortType == SORT_BY_RECENT;
    }

    public boolean IsSortByJPS() {
        return sortType == SORT_BY_HIGH_JPS || sortType == SORT_BY_LOW_JPS;
    }
}
