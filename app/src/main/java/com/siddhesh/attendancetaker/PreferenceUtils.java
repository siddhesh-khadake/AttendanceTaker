package com.siddhesh.attendancetaker;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;


public class PreferenceUtils
{
    public PreferenceUtils()
    {
    }

    public static boolean saveUsername(String username, Context context)
    {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
        SharedPreferences.Editor prefsEditor = prefs.edit();
        prefsEditor.putString(Constants.KEY_USERNAME, username);
        prefsEditor.apply();
        return true;
    }

    public static String getUsername(Context context)
    {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
        return prefs.getString(Constants.KEY_USERNAME, null);
    }

    public static boolean savePassword(String password, Context context)
    {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
        SharedPreferences.Editor prefsEditor = prefs.edit();
        prefsEditor.putString(Constants.KEY_PASSWORD, password);
        prefsEditor.apply();
        return true;
    }

    public static String getPassword(Context context)
    {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
        return prefs.getString(Constants.KEY_PASSWORD, null);
    }

    public static boolean saveDetails(String memberId,String firstName,String middleName, String lastName,String dob,
                                      String aadharNumber,String bloodGroup,String gender,String mobileNo,String nemnuk,
                                      String loginId,String designation, Context context)
    {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
        SharedPreferences.Editor prefsEditor = prefs.edit();
        prefsEditor.putString(Constants.KEY_MEMBERID, memberId);
        prefsEditor.putString(Constants.KEY_FIRSTNAME, firstName);
        prefsEditor.putString(Constants.KEY_MIDDLENAME,middleName);
        prefsEditor.putString(Constants.KEY_LASTNAME,lastName);
        prefsEditor.putString(Constants.KEY_DOB,dob);
        prefsEditor.putString(Constants.KEY_AADHARNUMBER,aadharNumber);
        prefsEditor.putString(Constants.KEY_BLOODGROUP,bloodGroup);
        prefsEditor.putString(Constants.KEY_GENDER,gender);
        prefsEditor.putString(Constants.KEY_MOBILENO,mobileNo);
        prefsEditor.putString(Constants.KEY_NEMNUK,nemnuk);
        prefsEditor.putString(Constants.KEY_LOGINID,loginId);
        prefsEditor.putString(Constants.KEY_DESIGNATION,designation);
        prefsEditor.apply();
        return true;
    }

    public static String getMemberId(Context context)
    {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
        return prefs.getString(Constants.KEY_MEMBERID, null);
    }

    public static String getFirstName(Context context)
    {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
        return prefs.getString(Constants.KEY_FIRSTNAME, null);
    }

    public static String getMiddleName(Context context)
    {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
        return prefs.getString(Constants.KEY_MIDDLENAME, null);
    }

    public static String getLastName(Context context)
    {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
        return prefs.getString(Constants.KEY_LASTNAME, null);
    }

    public static String getDob(Context context)
    {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
        return prefs.getString(Constants.KEY_DOB, null);
    }

    public static String getAadharNumber(Context context)
    {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
        return prefs.getString(Constants.KEY_AADHARNUMBER, null);
    }

    public static String getBloodGroup(Context context)
    {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
        return prefs.getString(Constants.KEY_BLOODGROUP, null);
    }

    public static String getMobileNo(Context context)
    {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
        return prefs.getString(Constants.KEY_MOBILENO, null);
    }

    public static String getGender(Context context)
    {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
        return prefs.getString(Constants.KEY_GENDER, null);
    }

    public static String getNemnuk(Context context)
    {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
        return prefs.getString(Constants.KEY_NEMNUK, null);
    }

    public static String getLoginId(Context context)
    {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
        return prefs.getString(Constants.KEY_LOGINID, null);
    }

    public static String getDesignation(Context context)
    {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
        return prefs.getString(Constants.KEY_DESIGNATION, null);
    }


    public static String getDate(Context context) {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
        return prefs.getString(Constants.KEY_DATE,null);
    }

    public static boolean saveDate(String date, Context context) {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
        SharedPreferences.Editor prefsEditor = prefs.edit();
        prefsEditor.putString(Constants.KEY_DATE, date);
        prefsEditor.apply();
        return true;
    }

    public static boolean saveSectorId(String id, Context context)
    {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
        SharedPreferences.Editor prefsEditor = prefs.edit();
        prefsEditor.putString(Constants.KEY_SECTORID, id);
        prefsEditor.apply();
        return true;
    }

    public static String getSectorId(Context context)
    {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
        return prefs.getString(Constants.KEY_SECTORID, null);
    }

    public static boolean clear(Context context)
    {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
        SharedPreferences.Editor prefsEditor = prefs.edit();

        prefsEditor.putString(Constants.KEY_SECTORCOHEAD, null);
        prefsEditor.putString(Constants.KEY_SECTORHEAD, null);
        prefsEditor.putString(Constants.KEY_SECTORNAME, null);
        prefsEditor.putString(Constants.KEY_SECTORID, null);

        prefsEditor.putString(Constants.KEY_AADHARNUMBER, null);
        prefsEditor.putString(Constants.KEY_BLOODGROUP, null);
        prefsEditor.putString(Constants.KEY_DESIGNATION, null);
        prefsEditor.putString(Constants.KEY_LOGINID, null);
        prefsEditor.putString(Constants.KEY_DOB, null);
        prefsEditor.putString(Constants.KEY_MIDDLENAME, null);
        prefsEditor.putString(Constants.KEY_LASTNAME, null);
        prefsEditor.putString(Constants.KEY_USERNAME, null);
        prefsEditor.putString(Constants.KEY_PASSWORD, null);
        prefsEditor.putString(Constants.KEY_MEMBERID, null);
        prefsEditor.putString(Constants.KEY_MOBILENO, null);
        prefsEditor.putString(Constants.KEY_NEMNUK, null);

        prefsEditor.apply();
        return true;
    }

}
