package com.siddhesh.attendancetaker;

public class offlineData
{
    String addhar,location,lattitude,longitude,status;

    public  String getAddhar()
    {
        return addhar;
    }

    public  String getLocation()
    {
        return location;
    }

    public  String getLattitude()
    {
        return lattitude;
    }

    public  String getLongitude()
    {
        return longitude;
    }

    public  String getStatus()
    {
        return status;
    }

    public void putAddhar(String str)
    {
       addhar=str;
    }

    public void putLocation(String str)
    {
        location=str;
    }

    public void putLattitude(String str)
    {
        lattitude=str;
    }

    public void putStatus(String str)
    {
        status=str;
    }

    public void putLongitude(String str)
    {
        longitude=str;
    }
}
