package com.googleplaygames.dpc.jump_rope_counter_2;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Spinner;

import java.util.ArrayList;

public class SortSetting extends AppCompatActivity {
    Spinner Spinner_sortType;
    Spinner Spinner_gradeSettingType;
    Spinner Spinner_kindsOfJumpRopeSettingType;
    Spinner Spinner_gradeSetting;
    Spinner Spinner_kindsOfJumpRopeSetting;
    Spinner Spinner_recordSortStyleSetting;
    Button Button_addGradeSetting;
    Button Button_addKindsOfJumpRopeSetting;
    ListView ListView_selectedGradeSetting;
    ListView ListView_selectedKindsOfJumpRopeSetting;

    ArrayList<Integer> selectedGradeSetting = new ArrayList<Integer>();
    ArrayList<Integer> selectedKindsOfJumpRopeSetting = new ArrayList<Integer>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sort_setting);

        Spinner_sortType = findViewById(R.id.sort_type);
        Spinner_gradeSettingType = findViewById(R.id.grade_setting_type);
        Spinner_kindsOfJumpRopeSettingType =
                findViewById(R.id.kinds_of_jump_rope_setting_type);
        Spinner_gradeSetting = findViewById(R.id.grade_setting);
        Spinner_kindsOfJumpRopeSetting = findViewById(R.id.kinds_of_jump_rope_setting);
        Spinner_recordSortStyleSetting = findViewById(R.id.record_sort_style_setting);
        Button_addGradeSetting = findViewById(R.id.add_grade_setting);
        Button_addKindsOfJumpRopeSetting = findViewById(R.id.add_kinds_of_jump_rope_setting);
        ListView_selectedGradeSetting = findViewById(R.id.selected_grade_setting);
        ListView_selectedKindsOfJumpRopeSetting = findViewById(R.id.selected_kinds_of_jump_rope_setting);

        Spinner_sortType.setAdapter(Public.GetArrayAdapter(
                getApplicationContext(),
                getResources().getStringArray(R.array.sort_type)));
        Spinner_gradeSettingType.setAdapter(Public.GetArrayAdapter(
                getApplicationContext(),
                getResources().getStringArray(R.array.setting_type)));
        Spinner_kindsOfJumpRopeSettingType.setAdapter(Public.GetArrayAdapter(
                getApplicationContext(),
                getResources().getStringArray(R.array.setting_type)));
        Spinner_gradeSetting.setAdapter(Public.GetArrayAdapter(
                getApplicationContext(),
                getResources().getStringArray(R.array.grade)));
        Spinner_kindsOfJumpRopeSetting.setAdapter(Public.GetArrayAdapter(
                getApplicationContext(),
                getResources().getStringArray(R.array.kinds_of_jump_rope)));
        Spinner_recordSortStyleSetting.setAdapter(Public.GetArrayAdapter(
                getApplicationContext(),
                getResources().getStringArray(R.array.record_sort_style)));

        Spinner_gradeSettingType.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                Button_addGradeSetting.setEnabled(position != SettingItem.SETTING_TYPE_NONE);
                Spinner_gradeSetting.setEnabled(position != SettingItem.SETTING_TYPE_NONE);
                if(position == SettingItem.SETTING_TYPE_NONE){
                    selectedGradeSetting.clear();
                    InitListView();
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        Spinner_kindsOfJumpRopeSettingType.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                Button_addKindsOfJumpRopeSetting.setEnabled(position != SettingItem.SETTING_TYPE_NONE);
                Spinner_kindsOfJumpRopeSetting.setEnabled(position != SettingItem.SETTING_TYPE_NONE);
                if(position == SettingItem.SETTING_TYPE_NONE){
                    selectedKindsOfJumpRopeSetting.clear();
                    InitListView();
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        new Handler().postDelayed(new Runnable() {
            public void run() {

                Spinner_gradeSettingType.setSelection(Public.settingItem.gradeSettingType);
                Spinner_kindsOfJumpRopeSettingType.setSelection(Public.settingItem.kindsOfJumpRopeSettingType);
                Spinner_sortType.setSelection(Public.settingItem.sortType);
                Spinner_recordSortStyleSetting.setSelection(Public.settingItem.recordSortStyle);

                Button_addGradeSetting.setEnabled(
                        Spinner_gradeSettingType.getSelectedItemPosition() != SettingItem.SETTING_TYPE_NONE);
                Spinner_gradeSetting.setEnabled(
                        Spinner_gradeSettingType.getSelectedItemPosition() != SettingItem.SETTING_TYPE_NONE);
                Button_addKindsOfJumpRopeSetting.setEnabled(
                        Spinner_kindsOfJumpRopeSettingType.getSelectedItemPosition() != SettingItem.SETTING_TYPE_NONE);
                Spinner_kindsOfJumpRopeSetting.setEnabled(
                        Spinner_kindsOfJumpRopeSettingType.getSelectedItemPosition() != SettingItem.SETTING_TYPE_NONE);

                selectedGradeSetting = Public.settingItem.selectedGradeSetting;
                selectedKindsOfJumpRopeSetting = Public.settingItem.selectedKindsOfJumpRopeSetting;

                InitListView();
            }
        }, 100);

    }

    public void InitListView(){
        GradeSettingAdapter selectedGradeSettingAdapter =
                new GradeSettingAdapter(selectedGradeSetting);
        KindsOfJumpRopeSettingAdapter selectedKindsOfJumpRopeSettingAdapter =
                new KindsOfJumpRopeSettingAdapter(selectedKindsOfJumpRopeSetting);

        ListView_selectedGradeSetting.setAdapter(selectedGradeSettingAdapter);
        ListView_selectedKindsOfJumpRopeSetting.setAdapter(selectedKindsOfJumpRopeSettingAdapter);
    }

    public void OnAddGradeContainSetting(View v){
        if(!selectedGradeSetting.contains(Spinner_gradeSetting.getSelectedItemPosition())){
            selectedGradeSetting.add(Spinner_gradeSetting.getSelectedItemPosition());
        }
        InitListView();
    }

    public void OnAddKindsOfJumpRopeContainSetting(View v){
        if(!selectedKindsOfJumpRopeSetting.contains(Spinner_kindsOfJumpRopeSetting.getSelectedItemPosition())){
            selectedKindsOfJumpRopeSetting.add(Spinner_kindsOfJumpRopeSetting.getSelectedItemPosition());
        }
        InitListView();
    }

    public void OnExit(View v){
        Public.settingItem.gradeSettingType = Spinner_gradeSettingType.getSelectedItemPosition();
        Public.settingItem.kindsOfJumpRopeSettingType = Spinner_kindsOfJumpRopeSettingType.getSelectedItemPosition();
        Public.settingItem.sortType = Spinner_sortType.getSelectedItemPosition();
        Public.settingItem.selectedGradeSetting = selectedGradeSetting;
        Public.settingItem.selectedKindsOfJumpRopeSetting = selectedKindsOfJumpRopeSetting;
        Public.settingItem.recordSortStyle = Spinner_recordSortStyleSetting.getSelectedItemPosition();
        finish();
    }

    public class GradeSettingAdapter extends BaseAdapter {

        private final ArrayList<Integer> items;

        public GradeSettingAdapter(ArrayList<Integer> _items){
            items = _items;
        }

        @Override
        public int getCount() {
            return items.size();
        }

        @Override
        public Object getItem(int _position) {
            return items.get(_position);
        }

        @Override
        public long getItemId(int _position) {
            return _position;
        }

        @Override
        public View getView(int _position, View _convertView, ViewGroup _parent) {
            ContainSettingView view = new ContainSettingView(getApplicationContext());
            view.SetSelectedContainSetting(getResources().getStringArray(R.array.grade)[items.get(_position)]);
            view.SetDeleteSelectedContainSettingListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    selectedGradeSetting.remove(_position);
                    InitListView();
                }
            });
            return view;
        }
    }

    public class KindsOfJumpRopeSettingAdapter extends BaseAdapter {

        private final ArrayList<Integer> items;

        public KindsOfJumpRopeSettingAdapter(ArrayList<Integer> _items){
            items = _items;
        }

        @Override
        public int getCount() {
            return items.size();
        }

        @Override
        public Object getItem(int _position) {
            return items.get(_position);
        }

        @Override
        public long getItemId(int _position) {
            return _position;
        }

        @Override
        public View getView(int _position, View _convertView, ViewGroup _parent) {
            ContainSettingView view = new ContainSettingView(getApplicationContext());
            view.SetSelectedContainSetting(getResources().getStringArray(R.array.kinds_of_jump_rope)[items.get(_position)]);
            view.SetDeleteSelectedContainSettingListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    selectedKindsOfJumpRopeSetting.remove(_position);
                    InitListView();
                }
            });
            return view;
        }
    }
}