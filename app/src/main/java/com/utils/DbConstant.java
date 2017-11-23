package com.utils;

/**
 * Created by harpreetsingh on 21/11/17.
 */

public interface DbConstant {

    public static final String DbName="Db1";
    public static final int DbVersion=1;

    public static final  String T_Tbl1="T_tbl";
    public static final  String C_ID="C_id";
    public static final  String C_InvNo="C_Inv";
    public static final  String C_InvDate="C_InvDate";
    public static final  String C_InvAmt="C_InvAmt";

    public static final  String T_snap="T_snap";
    public static final  String C_SnapData="C_SnapData";

    public static final String Create_Table_Data= "Create table "+T_Tbl1+" (" + C_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
            +C_InvNo+" TEXT,"
            + C_InvDate+" TEXT,"
            +C_InvAmt+" TEXT)";


    public static final String Create_Table_Snap= "Create table "+T_snap+" (" + C_ID + " INTEGER,"
            +C_SnapData+" TEXT)";


}
