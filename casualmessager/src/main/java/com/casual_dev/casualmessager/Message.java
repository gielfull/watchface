package com.casual_dev.casualmessager;

import java.util.Hashtable;

/**
 * Created by adamoutler on 11/10/14.
 */
public class Message {
    public static enum ITEMS{
        BACKGROUNDIMAGE, BOTTOMTEXTTAG, TOPTEXTTAG
    }
    Hashtable table = new Hashtable<>();

    public static Class getClassOfItem(ITEMS i){
        Class returnValue;
        switch (i){
            case BACKGROUNDIMAGE:return Object.class;
            case BOTTOMTEXTTAG: return String.class;
            case TOPTEXTTAG: return String.class;
            default: return String.class;
        }
    }

}