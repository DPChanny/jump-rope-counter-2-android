package com.googleplaygames.dpc.jump_rope_counter_2;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

public class ContainSettingView  extends LinearLayout {

    private TextView TextView_selectedSetting;
    private Button Button_deleteSelectedSetting;

    public ContainSettingView(Context _context){
        super(_context);
        Init(_context);
    }

    public ContainSettingView(Context _context, AttributeSet _attrs){
        super(_context, _attrs);
        Init(_context);
    }

    private void Init(Context _context){
        LayoutInflater inflater = (LayoutInflater) _context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        inflater.inflate(R.layout.contain_setting_item, this, true);

        TextView_selectedSetting = findViewById(R.id.selected_setting);
        Button_deleteSelectedSetting = findViewById(R.id.delete_selected_setting);
    }

    public void SetSelectedContainSetting(String _selectedContainSetting){
        TextView_selectedSetting.setText(_selectedContainSetting);
    }

    public void SetDeleteSelectedContainSettingListener(OnClickListener _listener){
        Button_deleteSelectedSetting.setOnClickListener(_listener);
    }
}
