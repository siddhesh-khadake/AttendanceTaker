package com.siddhesh.attendancetaker;

public class AttendenceTeamUserModel
{
    int status;
    String userName,designation,mobno,id;
    String subsectorid;

    public AttendenceTeamUserModel(String id,String userName,String designation,String mobno,int status)
    {
        this.id=id;
        this.userName = userName;
        this.designation=designation;
        this.mobno=mobno;
        this.status=status;
    }

    public AttendenceTeamUserModel(String id,String userName,String designation,String mobno,int status,String subsectorid)
    {
        this.id=id;
        this.userName = userName;
        this.designation=designation;
        this.mobno=mobno;
        this.status=status;
        this.subsectorid=subsectorid;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }
}

