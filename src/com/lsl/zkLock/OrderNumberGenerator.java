package com.lsl.zkLock;

import java.text.SimpleDateFormat;
import java.util.Date;

public class OrderNumberGenerator {

    private static int count;

    public String getNumber(){
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd-HH-mm-ss");
        return simpleDateFormat.format(new Date())+"-"+ ++count;
    }
}
