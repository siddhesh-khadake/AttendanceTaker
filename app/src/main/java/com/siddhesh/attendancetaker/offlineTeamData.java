package com.siddhesh.attendancetaker;

public class offlineTeamData {

    String memberid,name,designation,mobileno,aadhr;

    public  String getMemberId()
    {
        return memberid;
    }

    public  String getName()
    {
        return name;
    }

    public  String getDesignation()
    {
        return designation;
    }

    public  String getMobileno()
    {
        return mobileno;
    }

    public  String getAadhar()
    {
        return aadhr;
    }

    public void putMemberId(String str)
    {
        memberid=str;
    }

    public void putName(String str)
    {
        name=str;
    }

    public void putDesignation(String str)
    {
        designation=str;
    }

    public void putMobileno(String str)
    {
        mobileno=str;
    }

    public void putAadhar(String str)
    {
        aadhr=str;
    }
}
