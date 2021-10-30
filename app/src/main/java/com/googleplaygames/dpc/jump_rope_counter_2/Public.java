package com.googleplaygames.dpc.jump_rope_counter_2;

import android.content.Context;
import android.text.InputFilter;
import android.text.Spanned;
import android.widget.ArrayAdapter;
import android.widget.EditText;

import java.text.SimpleDateFormat;
import java.util.Date;

public class Public {
    public static SettingItem settingItem;

    public static int NULL = 0;

    public static int GetInt(EditText _editText){
        String resultStr = _editText.getText().toString();
        int result;
        if (!resultStr.isEmpty()){
            result = Integer.parseInt(resultStr);
        }else {
            _editText.setText("0");
            result = 0;
        }
        return result;
    }

    public static void SetFilter(EditText _editText){
        _editText.setFilters(new InputFilter[]{new InputFilter() {
            @Override
            public CharSequence filter(CharSequence source, int start, int end, Spanned dest, int dstart, int dend) {
                for (int i = start; i < end; i++) {
                    if(source.charAt(i) == "'".charAt(0)){
                        return "";
                    }
                }
                return null;
            }
        }});
    }

    public static <T> ArrayAdapter<T> GetArrayAdapter(Context _context, T[] _array){
        ArrayAdapter<T> adapter = new ArrayAdapter<T>(
                _context,
                android.R.layout.simple_spinner_item,
                _array);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        return adapter;
    }

    public static String GetDate(long milliSec){
        return (new SimpleDateFormat("yyyy/MM/dd hh:mm:ss")).format(new Date(milliSec)) + " UTC";
    }
}
