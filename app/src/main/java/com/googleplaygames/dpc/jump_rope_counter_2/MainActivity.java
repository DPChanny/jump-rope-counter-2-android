package com.googleplaygames.dpc.jump_rope_counter_2;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

public class MainActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        RecordDBManager.db = (new RecordDBHelper(getApplicationContext())).getWritableDatabase();
        SettingDBManager.db = (new SettingDBHelper(getApplicationContext()).getWritableDatabase());

        Public.settingItem = SettingDBManager.GetSetting();
    }

    public void OnExit(View v){
        finish();
    }

    public void OnCounter(View v){
        Intent activity = new Intent(getApplicationContext(), Counter.class);
        startActivity(activity);
    }

    public void OnRecord(View v){
        Intent activity = new Intent(getApplicationContext(), Record.class);
        startActivity(activity);
    }

    @Override
    protected void onDestroy() {
        RecordDBManager.db.close();
        SettingDBManager.SaveSetting(Public.settingItem);
        SettingDBManager.db.close();
        super.onDestroy();
    }
}