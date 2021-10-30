package com.googleplaygames.dpc.jump_rope_counter_2;

import android.content.Context;
import android.view.LayoutInflater;
import android.widget.LinearLayout;
import android.widget.TextView;

public class RecordItemView extends LinearLayout {

    public RecordItemView(Context _context, RecordItem _record, int _index){
        super(_context);
        Init(_context, _record, _index);
    }

    private void Init(Context _context, RecordItem _record, int _index){
        LayoutInflater inflater = (LayoutInflater) _context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        if(Public.settingItem.recordSortStyle == SettingItem.RECORD_SORT_STYLE_SIMPLE){
            inflater.inflate(R.layout.record_item_sort_style_simple, this, true);
            ((TextView)findViewById(R.id.number)).setText(Integer.toString(_index + 1));
        }
        else if(Public.settingItem.recordSortStyle == SettingItem.RECORD_SORT_STYLE_DETAIL){
            inflater.inflate(R.layout.record_item_sort_style_detail, this, true);
            ((TextView)findViewById(R.id.grade)).setText(
                    getResources().getStringArray(R.array.grade_simple)[_record.grade]);
            ((TextView)findViewById(R.id.kinds_of_jump_rope)).setText(
                    getResources().getStringArray(R.array.kinds_of_jump_rope)[_record.kindsOfJumpRope]);
            ((TextView)findViewById(R.id.jps)).setText(_record.GetStringJPS());
            ((TextView)findViewById(R.id.add_time)).setText(Public.GetDate(_record.addTime));
        }

        ((TextView)findViewById(R.id.name)).setText(_record.name);
        ((TextView)findViewById(R.id.count)).setText(Integer.toString(_record.count));
        ((TextView)findViewById(R.id.second)).setText(Integer.toString(_record.elapsedTime));
    }
}
