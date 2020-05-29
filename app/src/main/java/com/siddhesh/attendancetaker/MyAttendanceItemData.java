package com.siddhesh.attendancetaker;

public class MyAttendanceItemData {

    public String status,location,datetime,checkedby;

    public MyAttendanceItemData(String status,String datetime,String location,String checkedby)
    {
        this.checkedby=checkedby;
        this.datetime=datetime;
        this.location=location;
        this.status=status;
    }

}