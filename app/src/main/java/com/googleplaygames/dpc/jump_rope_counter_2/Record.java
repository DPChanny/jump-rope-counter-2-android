package com.googleplaygames.dpc.jump_rope_counter_2;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.Toast;

import java.util.ArrayList;

public class Record extends AppCompatActivity {

    private Spinner Spinner_groups;
    private ListView ListView_records;

    ArrayList<Integer> selectedGroup = new ArrayList<Integer>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_record);

        Spinner_groups = findViewById(R.id.groups);
        ListView_records = findViewById(R.id.records);

        InitGroup();
    }

    private void InitGroup(){
        Spinner_groups.setAdapter(Public.GetArrayAdapter(
                getApplicationContext(),
                RecordDBManager.GetGroups().toArray()));

        Spinner_groups.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                InitRecord();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent){
            }
        });

        InitRecord();

        findViewById(R.id.delete_group).setEnabled(Spinner_groups.getSelectedItem() != null);
        findViewById(R.id.add_record).setEnabled(Spinner_groups.getSelectedItem() != null);
        findViewById(R.id.edit_group).setEnabled(Spinner_groups.getSelectedItem() != null);
        findViewById(R.id.sort_setting).setEnabled(Spinner_groups.getSelectedItem() != null);
        Spinner_groups.setEnabled(Spinner_groups.getSelectedItem() != null);
    }

    private void InitRecord(){
        if(Public.settingItem.recordSortStyle == SettingItem.RECORD_SORT_STYLE_SIMPLE){
            findViewById(R.id.sort_style_simple_bar).setVisibility(View.VISIBLE);
        }
        else {
            findViewById(R.id.sort_style_simple_bar).setVisibility(View.GONE);
        }

        RecordAdapter recordAdapter;
        if(Spinner_groups.getSelectedItem() == null){
            recordAdapter = new RecordAdapter(new ArrayList<RecordItem>());
        }else {
            recordAdapter = new RecordAdapter(RecordDBManager.GetRecords(Spinner_groups.getSelectedItem().toString()));
        }
        ListView_records.setAdapter(recordAdapter);
        ListView_records.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(getApplicationContext(), RecordDetail.class);
                intent.putExtra("position", position);
                intent.putExtra("groupName", Spinner_groups.getSelectedItem().toString());
                startActivity(intent);
            }
        });
    }

    public class RecordAdapter extends BaseAdapter {

        private final ArrayList<RecordItem> items;

        public RecordAdapter(ArrayList<RecordItem> _items){
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
            return new RecordItemView(getApplicationContext(), items.get(_position), _position);
        }
    }

    public void OnAddGroup(View v){
        Dialog dialog;
        dialog = new Dialog(this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.add_group_way_select);
        dialog.show();

        dialog.findViewById(R.id.add_new_group).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                OnAddNewGroup();
                dialog.dismiss();
            }
        });

        dialog.findViewById(R.id.add_with_existing_group).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!RecordDBManager.GetGroups().isEmpty()){
                    OnAddWithExistingGroup();
                }else {
                    Toast.makeText(
                            getApplicationContext(),
                            getResources().getString(R.string.group_does_not_exists),
                            Toast.LENGTH_LONG).show();
                }
                dialog.dismiss();
            }
        });

        dialog.findViewById(R.id.cancel).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
    }

    public void OnAddNewGroup() {
        Dialog dialog;
        dialog = new Dialog(this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.add_new_group_dialog);
        dialog.show();

        EditText EditText_groupName = dialog.findViewById(R.id.group_name);
        Public.SetFilter(EditText_groupName);

        dialog.findViewById(R.id.add).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String groupName = EditText_groupName.getText().toString();
                Toast.makeText(
                        getApplicationContext(),
                        RecordDBManager.AddNewGroup(
                                groupName,
                                getResources()),
                        Toast.LENGTH_LONG).show();
                InitGroup();
                dialog.dismiss();
            }
        });

        dialog.findViewById(R.id.cancel).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
    }

    public void OnAddWithExistingGroup(){
        Dialog dialog;
        dialog = new Dialog(this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.add_new_group_with_existing_group_dialog);
        dialog.show();

        selectedGroup.clear();

        Spinner Spinner_groupSetting = dialog.findViewById(R.id.group_setting);
        EditText EditText_groupName = dialog.findViewById(R.id.group_name);

        Public.SetFilter(EditText_groupName);

        Spinner_groupSetting.setAdapter(Public.GetArrayAdapter(
                getApplicationContext(),
                RecordDBManager.GetGroups().toArray()));

        InitListView(dialog);

        dialog.findViewById(R.id.add_group_setting).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!selectedGroup.contains(Spinner_groupSetting.getSelectedItemPosition())) {
                    selectedGroup.add(Spinner_groupSetting.getSelectedItemPosition());
                }
                InitListView(dialog);
            }
        });

        dialog.findViewById(R.id.cancel).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

        dialog.findViewById(R.id.add).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String groupName = EditText_groupName.getText().toString();
                Toast.makeText(
                        getApplicationContext(),
                        RecordDBManager.AddNewGroupWithExistingGroup(groupName, selectedGroup, getResources()),
                        Toast.LENGTH_LONG).show();
                InitGroup();
                dialog.dismiss();
            }
        });
    }

    public class GroupSettingAdapter extends BaseAdapter {

        private final ArrayList<Integer> items;
        private final Dialog dialog;

        public GroupSettingAdapter(ArrayList<Integer> _items, Dialog _dialog){
            items = _items;
            dialog = _dialog;
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
            view.SetSelectedContainSetting(RecordDBManager.GetGroups().get(items.get(_position)));
            view.SetDeleteSelectedContainSettingListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    selectedGroup.remove(_position);
                    InitListView(dialog);
                }
            });
            return view;
        }
    }

    private void InitListView(Dialog _dialog) {
        GroupSettingAdapter selectedGroupSettingAdapter =
                new GroupSettingAdapter(selectedGroup, _dialog);

        ((ListView) _dialog.findViewById(R.id.selected_group_setting)).setAdapter(selectedGroupSettingAdapter);
    }

    public void OnDeleteGroup(View v){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(getResources().getString(R.string.alarm));
        builder.setMessage(getResources().getString(R.string.delete_alarm));
        builder.setIcon(android.R.drawable.ic_dialog_alert);
        builder.setPositiveButton(getResources().getString(R.string.yes), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                String group_name = Spinner_groups.getSelectedItem().toString();
                RecordDBManager.DeleteGroup(group_name);
                InitGroup();
            }
        });
        builder.setNegativeButton(getResources().getString(R.string.no), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        });
        AlertDialog dialog = builder.create();
        dialog.show();
    }

    public void OnEditGroup(View v){
        Dialog dialog;
        dialog = new Dialog(this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.edit_group_dialog);
        dialog.show();

        EditText EditText_name = dialog.findViewById(R.id.group_name);
        Public.SetFilter(EditText_name);

        dialog.findViewById(R.id.edit).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String newGroupName = EditText_name.getText().toString();
                Toast.makeText(getApplicationContext(),
                        RecordDBManager.EditGroup(
                                Spinner_groups.getSelectedItem().toString(),
                                newGroupName,
                                getResources()),
                        Toast.LENGTH_LONG).show();
                InitGroup();
                dialog.dismiss();
            }
        });

        dialog.findViewById(R.id.cancel).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
    }

    public void OnAddRecord(View v){
        Dialog dialog;
        dialog = new Dialog(this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.add_record_dialog_record);
        dialog.show();

        Spinner Spinner_kindsOfJumpRope = dialog.findViewById(R.id.kinds_of_jump_rope);
        Spinner Spinner_grade = dialog.findViewById(R.id.grade);
        EditText EditText_count = dialog.findViewById(R.id.count);
        EditText EditText_second = dialog.findViewById(R.id.second);
        EditText EditText_name = dialog.findViewById(R.id.group_name);

        Public.SetFilter(EditText_name);

        Spinner_grade.setAdapter(Public.GetArrayAdapter(
                getApplicationContext(),
                getResources().getStringArray(R.array.grade)));
        Spinner_kindsOfJumpRope.setAdapter(Public.GetArrayAdapter(
                getApplicationContext(),
                getResources().getStringArray(R.array.kinds_of_jump_rope)));

        dialog.findViewById(R.id.add).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String groupName = Spinner_groups.getSelectedItem().toString();
                RecordItem record;
                record = new RecordItem(
                        EditText_name.getText().toString(),
                        Public.GetInt(EditText_count),
                        Public.GetInt(EditText_second),
                        Spinner_grade.getSelectedItemPosition(),
                        Spinner_kindsOfJumpRope.getSelectedItemPosition(),
                        System.currentTimeMillis(),
                        Public.NULL);
                Toast.makeText(
                        getApplicationContext(),
                        RecordDBManager.AddNewRecord(
                                groupName,
                                record,
                                getResources()),
                        Toast.LENGTH_LONG).show();
                InitRecord();
                dialog.dismiss();
            }
        });

        dialog.findViewById(R.id.cancel).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
    }

    public void OnSortSetting(View v){
        Intent intent = new Intent(getApplicationContext(), SortSetting.class);
        startActivity(intent);
    }

    public void OnExit(View v){
        finish();
    }

    @Override
    protected void onRestart(){
        super.onRestart();
        InitRecord();
    }
}
