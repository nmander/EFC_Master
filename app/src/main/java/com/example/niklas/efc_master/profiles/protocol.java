package com.example.niklas.efc_master.profiles;

public class protocol
{
    public static byte PHONE_PROTOCOL_NBR = 3;
    public static byte PHONE_PROTOCOL_BYTES = 4;
    public static byte BTN_TOOL_SELECT = 1;
    public static byte TOOL_BLADE = 0;
    public static byte TOOL_BLOWER = 1;
    public static byte TOOL_EDGER = 2;
    public static byte TOOL_POLE_SAW = 3;
    public static byte TOOL_TILLER = 4;
    public static byte TOOL_STRING = 5;
    public static String[] TOOL_STR = {"BLADE ATTACHMENT","BLOWER ATTACHMENT","EDGER ATTACHMENT","POLE SAW ATTACHMENT","TILLER ATTACHMENT","STRING ATTACHMENT"};
    public static byte BTN_TRIM_MODE = 2;
    public static byte NORMAL_TRIM = 0;
    public static byte LITE_TRIM = 1;
    public static byte BTN_STOP = 3;
    public static byte STOP_OFF = 0;
    public static byte STOP_ON = 1;
    public static byte BTN_CLEAR_CODE = 4;
    public static byte RESET_CODE = 1;
    public static byte BTN_STATS_REQUEST = 5;
    public static byte REQUEST_STATS = 1;
     public static byte BTN_RESET_OIL = 6;
    public static byte RESET_OIL_COUNTER = 1;
    public static byte TPS_PART_THROTTLE = 0;
    public static byte TPS_IDLE = 1;
    public static byte TPS_WOT = 2;
    public static byte PHONE_EPOCH_PROTOCOL_NBR = 4;
    public static byte PHONE_EPOCH_PROTOCOL_BYTES = 7;
    public static byte EPOCH_ID_LAST_RUN = 0;
    public static byte EPOCH_ID_OIL_CHANGE = 1;
}
