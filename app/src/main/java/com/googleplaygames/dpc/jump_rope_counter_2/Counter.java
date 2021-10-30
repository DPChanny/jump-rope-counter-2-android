package com.googleplaygames.dpc.jump_rope_counter_2;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.Window;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

public class Counter extends AppCompatActivity {
    private boolean isRunning = false;

    private boolean isTimer = false;

    private final MessageHandler handler = new MessageHandler();

    private int timerTime;
    private int elapsedTime;
    private int count;

    private TextView TextView_elapsedTime;
    private TextView TextView_count;
    private TextView TextView_jps;
    private EditText EditText_time;
    private CheckBox CheckBox_isTimer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_counter);

        TextView_elapsedTime = findViewById(R.id.elapsed_time);
        TextView_count = findViewById(R.id.count);
        TextView_jps = findViewById(R.id.jps);
        EditText_time = findViewById(R.id.time);
        CheckBox_isTimer = findViewById(R.id.is_timer);

        SetIsRunningEnabled(isRunning);
        SetIsTimerEnabled(isTimer);

        View view = findViewById(R.id.view);
        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(isRunning){
                    TextView_count.setText(Integer.toString(++count));
                }
            }
        });

        CheckBox_isTimer.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                SetIsTimerEnabled(isChecked);
            }
        });
    }

    public void OnExit(View v){
        finish();
    }

    public void OnStart(View v){
        TextView_count.setText("0");
        TextView_jps.setText("0");
        SetIsRunningEnabled(true);

        elapsedTime = 0;
        count = 0;
        if(isTimer){
            timerTime = Public.GetInt(EditText_time);
        }
        (new Thread(new Timer())).start();
    }

    public void OnAddRecord(View v)
    {
        Dialog dialog;
        dialog = new Dialog(this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.add_record_dialog_counter);
        dialog.show();

        Spinner Spinner_kindsOfJumpRope = dialog.findViewById(R.id.kinds_of_jump_rope);
        Spinner Spinner_grade = dialog.findViewById(R.id.grade);
        Spinner Spinner_groups = dialog.findViewById(R.id.groups);
        EditText EditText_count = dialog.findViewById(R.id.count);
        EditText EditText_second = dialog.findViewById(R.id.second);
        EditText EditText_name = dialog.findViewById(R.id.group_name);

        Public.SetFilter(EditText_name);

        Spinner_groups.setAdapter(
                Public.GetArrayAdapter(
                        getApplicationContext(),
                        RecordDBManager.GetGroups().toArray()));

        if(Spinner_groups.getSelectedItem() != null){
            Spinner_grade.setAdapter(Public.GetArrayAdapter(
                    getApplicationContext(),
                    getResources().getStringArray(R.array.grade)));
            Spinner_kindsOfJumpRope.setAdapter(Public.GetArrayAdapter(
                    getApplicationContext(),
                    getResources().getStringArray(R.array.kinds_of_jump_rope)));

            EditText_second.setText(Integer.toString(elapsedTime));
            EditText_count.setText(TextView_count.getText().toString());

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
                    dialog.dismiss();
                }
            });

            dialog.findViewById(R.id.cancel).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    dialog.dismiss();
                }
            });
        }else {
            Toast.makeText(getApplicationContext(), getResources().getString(R.string.group_does_not_exists), Toast.LENGTH_LONG).show();
            dialog.dismiss();
        }
    }

    public void OnStop(View v){
        SetIsRunningEnabled(false);
    }

    private void SetIsRunningEnabled(boolean _isEnabled){
        isRunning = _isEnabled;
        findViewById(R.id.start_button).setEnabled(!_isEnabled);
        findViewById(R.id.stop_button).setEnabled(_isEnabled);
        findViewById(R.id.exit_button).setEnabled(!_isEnabled);
        findViewById(R.id.add_button).setEnabled(!_isEnabled);
        findViewById(R.id.is_timer).setEnabled(!_isEnabled);
        findViewById(R.id.decrease).setEnabled(_isEnabled);
        findViewById(R.id.view).setEnabled(_isEnabled);
        EditText_time.setEnabled(!_isEnabled);
    }

    private void SetIsTimerEnabled(boolean _isEnabled)
    {
        isTimer = _isEnabled;
        CheckBox_isTimer.setChecked(isTimer);
        EditText_time.setEnabled(isTimer);
    }

    public void OnDecrease(View view) {
        if(count > 0){
            TextView_count.setText(Integer.toString(--count));
        }
    }

    public class Timer implements Runnable
    {
        @Override
        public void run(){
            while(isRunning){
                SetElapsedTimeMessage(Integer.toString(elapsedTime));
                if(isTimer){
                    if(timerTime-elapsedTime <= 0){
                        StopMessage();
                    }
                }

                SetJPSMessage(RecordItem.GetStringJPS(count, elapsedTime));

                try {
                    Thread.sleep(1000);
                }catch (Exception ignored){

                }

                if(isRunning){
                    elapsedTime++;
                }
            }
        }

        private void SetElapsedTimeMessage(String _message) {
            Message message = handler.obtainMessage();
            Bundle _bundle = new Bundle();
            _bundle.putString("work", "SetElapsedTimeMessage");
            _bundle.putString("value", _message);
            message.setData(_bundle);
            handler.sendMessage(message);
        }

        private void SetJPSMessage(String _message){
            Message message = handler.obtainMessage();
            Bundle _bundle = new Bundle();
            _bundle.putString("work", "SetJPSMessage");
            _bundle.putString("value", _message);
            message.setData(_bundle);
            handler.sendMessage(message);
        }

        private void StopMessage() {
            Message message = handler.obtainMessage();
            Bundle _bundle = new Bundle();
            _bundle.putString("work", "StopMessage");
            message.setData(_bundle);
            handler.sendMessage(message);
        }
    }

    private void StopAlarm(){
        MediaPlayer.create(getApplicationContext(), R.raw.alarm).start();
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(getResources().getString(R.string.alarm));
        builder.setMessage(getResources().getString(R.string.time_is_out));
        builder.setIcon(android.R.drawable.ic_dialog_alert);
        builder.setPositiveButton(getResources().getString(R.string.confirm), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        });
        AlertDialog dialog = builder.create();
        dialog.show();
    }

    public class MessageHandler extends Handler {
        @Override
        public void handleMessage(@NonNull Message msg) {
            super.handleMessage(msg);
            Bundle bundle = msg.getData();

            if(bundle.getString("work").equals("SetElapsedTimeMessage")){
                TextView_elapsedTime.setText(bundle.getString("value"));
            }
            if(bundle.getString("work").equals("StopMessage")){
                SetIsRunningEnabled(false);
                StopAlarm();
            }
            if(bundle.getString("work").equals("SetJPSMessage")){
                TextView_jps.setText(bundle.getString("value"));
            }
        }
    }
}