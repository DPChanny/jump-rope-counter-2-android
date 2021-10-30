package com.googleplaygames.dpc.jump_rope_counter_2;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.Window;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

public class RecordDetail extends AppCompatActivity {

    private String groupName;
    private int position;

    TextView name;
    TextView count;
    TextView second;
    TextView grade;
    TextView kindsOfJumpRope;
    TextView jps;
    TextView group;
    TextView addTime;
    TextView averageCount;
    TextView averageElapsedTime;
    TextView averageJPS;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_record_detail);

        groupName = getIntent().getStringExtra("groupName");
        position = getIntent().getIntExtra("position", 0);

        name = findViewById(R.id.group_name);
        count = findViewById(R.id.count);
        second = findViewById(R.id.second);
        grade = findViewById(R.id.grade);
        kindsOfJumpRope = findViewById(R.id.kinds_of_jump_rope);
        jps = findViewById(R.id.jps);
        group = findViewById(R.id.group);
        addTime = findViewById(R.id.add_time);
        averageCount = findViewById(R.id.average_count);
        averageElapsedTime = findViewById(R.id.average_elapsed_time);
        averageJPS = findViewById(R.id.average_jps);

        Init();
    }

    public void Init(){
        RecordItem record = RecordDBManager.GetRecords(groupName).get(position);

        jps.setText(record.GetStringJPS());
        name.setText(record.name);
        count.setText(Integer.toString(record.count));
        second.setText(Integer.toString(record.elapsedTime));
        grade.setText(getResources().getStringArray(R.array.grade)[record.grade]);
        kindsOfJumpRope.setText(getResources().getStringArray(R.array.kinds_of_jump_rope)[record.kindsOfJumpRope]);
        group.setText(groupName);
        addTime.setText(Public.GetDate(record.addTime));
        averageCount.setText(RecordDBManager.GetGroupAverageCount(groupName));
        averageElapsedTime.setText(RecordDBManager.GetGroupAverageElapsedTime(groupName));
        averageJPS.setText(RecordDBManager.GetGroupAverageJPS(groupName));
    }

    public void OnExit(View v){
        finish();
    }

    public void OnDeleteRecord(View v){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(getResources().getString(R.string.alarm));
        builder.setMessage(getResources().getString(R.string.delete_alarm));
        builder.setIcon(android.R.drawable.ic_dialog_alert);
        builder.setPositiveButton(getResources().getString(R.string.yes), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                RecordDBManager.DeleteRecord(groupName, RecordDBManager.GetRecords(groupName).get(position).id);
                finish();
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

    public void OnEditRecord(View v){
        Dialog dialog;
        dialog = new Dialog(this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.edit_record_dialog);
        dialog.show();

        Spinner Spinner_kindsOfJumpRope = dialog.findViewById(R.id.kinds_of_jump_rope);
        Spinner Spinner_grade = dialog.findViewById(R.id.grade);
        EditText EditText_count = dialog.findViewById(R.id.count);
        EditText EditText_second = dialog.findViewById(R.id.second);
        EditText EditText_name = dialog.findViewById(R.id.group_name);
        Spinner Spinner_groups = dialog.findViewById(R.id.groups);

        Public.SetFilter(EditText_name);

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(
                getApplicationContext(),
                android.R.layout.simple_spinner_item,
                RecordDBManager.GetGroups());

        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        Spinner_groups.setAdapter(adapter);

        RecordItem record = RecordDBManager.GetRecords(groupName).get(position);

        EditText_count.setText(Integer.toString(record.count));
        EditText_second.setText(Integer.toString(record.elapsedTime));
        EditText_name.setText(record.name);

        new Handler().postDelayed(new Runnable() {
            public void run() {
                Spinner_groups.setSelection(RecordDBManager.GetGroups().indexOf(groupName));
                Spinner_kindsOfJumpRope.setSelection(record.kindsOfJumpRope);
                Spinner_grade.setSelection(record.grade);
            }
        }, 100);

        ArrayAdapter<String> gradeAdapter = new ArrayAdapter<String>(getApplicationContext(),
                android.R.layout.simple_spinner_item, getResources().getStringArray(R.array.grade));
        ArrayAdapter<String> kindsOfJumpRopeAdapter = new ArrayAdapter<String>(getApplicationContext(),
                android.R.layout.simple_spinner_item, getResources().getStringArray(R.array.kinds_of_jump_rope));
        gradeAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        kindsOfJumpRopeAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        Spinner_grade.setAdapter(gradeAdapter);
        Spinner_kindsOfJumpRope.setAdapter(kindsOfJumpRopeAdapter);

        dialog.findViewById(R.id.edit).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                RecordItem record;
                record = new RecordItem(
                        EditText_name.getText().toString(),
                        Public.GetInt(EditText_count),
                        Public.GetInt(EditText_second),
                        Spinner_grade.getSelectedItemPosition(),
                        Spinner_kindsOfJumpRope.getSelectedItemPosition(),
                        RecordDBManager.GetRecords(groupName).get(position).addTime,
                        RecordDBManager.GetRecords(groupName).get(position).id);
                Toast.makeText(
                        getApplicationContext(),
                        RecordDBManager.EditRecord(
                                groupName,
                                Spinner_groups.getSelectedItem().toString(),
                                record, getResources()),
                        Toast.LENGTH_LONG).show();
                if(!groupName.matches(Spinner_groups.getSelectedItem().toString())){
                    groupName = Spinner_groups.getSelectedItem().toString();
                    position = RecordDBManager.GetRecords(groupName).size() - 1;
                }
                Init();
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
}