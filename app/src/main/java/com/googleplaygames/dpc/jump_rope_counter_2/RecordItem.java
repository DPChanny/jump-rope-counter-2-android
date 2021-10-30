package com.googleplaygames.dpc.jump_rope_counter_2;

import java.text.DecimalFormat;

public class RecordItem {
    public String name;
    public int count;
    public int elapsedTime;
    public int grade;
    public int kindsOfJumpRope;
    public long addTime;
    public int id;

    public RecordItem(
            String _name,
            int _count,
            int _second,
            int _grade,
            int _kindsOfJumpRope,
            long _addTime,
            int _id){
        name = _name;
        count = _count;
        elapsedTime = _second;
        grade = _grade;
        kindsOfJumpRope = _kindsOfJumpRope;
        addTime = _addTime;
        id = _id;
    }

    public String GetStringJPS(){
        return GetStringJPS(count, elapsedTime);
    }

    public static String GetStringJPS(int count, int elapsedTime){
        double jpsDouble = (double) count/(double)elapsedTime;
        if(Double.isNaN(jpsDouble) || Double.isInfinite(jpsDouble)){
            return "0";
        }else {
            DecimalFormat frmt = new DecimalFormat();
            frmt.setMaximumFractionDigits(2);
            return frmt.format(jpsDouble);
        }
    }

    public Double GetDoubleJPS(){
        return GetDoubleJPS(count, elapsedTime);
    }

    public Double GetDoubleJPS(int count, int elapsedTime){
        double jpsDouble = (double) count/(double)elapsedTime;
        if(Double.isNaN(jpsDouble) || Double.isInfinite(jpsDouble)){
            return 0.0;
        }else {
            return jpsDouble;
        }
    }
}
