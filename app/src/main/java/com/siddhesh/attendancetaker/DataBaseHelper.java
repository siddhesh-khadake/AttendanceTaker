package com.siddhesh.attendancetaker;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

public class DataBaseHelper extends SQLiteOpenHelper {

    SQLiteDatabase sqLiteDatabase;
    Context context;

    public DataBaseHelper(Context context)
    {
        super(context, "user", null, 4);
        this.context=context;
        sqLiteDatabase = this.getWritableDatabase();
    }

    @Override
    public void onCreate(SQLiteDatabase db)
    {
        db.execSQL("create table attendance(memberid text PRIMARY KEY,location text,lattitude text,longitude text,status text,sectorid text)");
        db.execSQL("create table teammembers(memberid text PRIMARY KEY,name text,designation text,mobileno text,aadhar text,sectorid text)");
        db.execSQL("create table status(memberid text PRIMARY KEY,status text,sectorid text)");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion)
    {
        db.execSQL("drop table if exists attendance");
        db.execSQL("drop table if exists teammembers");
        db.execSQL("drop table if exists status");
        onCreate(db);
    }

    public List<offlineTeamData> showTeamData(String sectorid)
    {
        Cursor cursor=getTeamData(sectorid);
        List<offlineTeamData> list=new ArrayList<offlineTeamData>();
        while(cursor.moveToNext())
        {
            offlineTeamData data = new offlineTeamData();
            data.putMemberId(cursor.getString(0));
            data.putName(cursor.getString(1));
            data.putDesignation(cursor.getString(2));
            data.putMobileno(cursor.getString(3));
            data.putAadhar(cursor.getString(4));
            list.add(data);
        }
        return list;
    }

    public Cursor getTeamData(String sectorid)
    {
        Cursor cursor= sqLiteDatabase.rawQuery("select * from teammembers where sectorid="+sectorid,null);
        return  cursor;
    }

    public long insertTeamData(String sectorid,String memberid,String name,String designation,String mobileno,String aadhar)
    {
        ContentValues contentValues = new ContentValues();
        contentValues.put("memberid",memberid);
        contentValues.put("name",name);
        contentValues.put("designation",designation);
        contentValues.put("mobileno",mobileno);
        contentValues.put("aadhar",aadhar);
        contentValues.put("sectorid",sectorid);
        //Toast.makeText(context,"Details:\nMemberID:"+memberid+"\nName:"+name+"\nDesignation:"+designation+"\nMobileNo:"+mobileno,Toast.LENGTH_LONG).show();
        long res=sqLiteDatabase.insert("teammembers",null,contentValues);
        /*
        if(res>0)
        {
            Toast.makeText(context,"Data Inserted",Toast.LENGTH_LONG).show();
        }
        else
        {
            Toast.makeText(context,"Data Not Inserted",Toast.LENGTH_LONG).show();
        }
        */
        return res;
    }

    public List showTeamMemberId(String sectorid)
    {
        List<String> id = new ArrayList<String>();
        Cursor cursor=getTeamMemberId(sectorid);
        while(!cursor.moveToNext())
        {
            String memberid=cursor.getString(0);
            id.add(memberid);
        }
        return id;
    }

    public Cursor getTeamMemberId(String sectorid)
    {
        Cursor cursor= sqLiteDatabase.rawQuery("select memberid from teammembers where sectorid="+sectorid,null);
        return  cursor;
    }


    public long insertStatus(String sectorid,String memberid,String status)
    {
        ContentValues contentValues = new ContentValues();
        contentValues.put("memberid",memberid);
        contentValues.put("status",status);
        contentValues.put("sectorid",sectorid);
        long res=sqLiteDatabase.insert("status",null,contentValues);
        if(res>0)
        {
            // Toast.makeText(context,"Inserted",Toast.LENGTH_LONG).show();
        }
        else
        {
            sqLiteDatabase.update("status",contentValues,"memberid="+memberid,null);
        }
        return res;
    }

    public int getStatus(String id)
    {
        int status=0;
        Cursor cursor= sqLiteDatabase.rawQuery("select status from status where memberid=" + id,null);
        while (cursor.moveToNext())
        {
            String str;
            str=cursor.getString(0);
            status=Integer.parseInt(str);
        }
        if(status==0)
        {
            status=3;
        }
        return status;
    }

    public void deleteStatus(String sectorid)
    {
        sqLiteDatabase.execSQL("delete from status where sectorid="+sectorid);
    }


    public void deleteTeamMember(String memberid)
    {
        long res=sqLiteDatabase.delete("teammembers","memberid=?",new String[]{memberid});
        /*
        if(res>0)
        {
            Toast.makeText(context,"Deleted 1 Record",Toast.LENGTH_LONG).show();
        }
        else
        {
            Toast.makeText(context,"Not Deleted",Toast.LENGTH_LONG).show();
        }
        */
    }


    public Cursor getAllTeamData()
    {
        Cursor cursor= sqLiteDatabase.rawQuery("select * from teammembers",null);
        return  cursor;
    }


    public List<offlineTeamData> showAllTeamData()
    {
        Cursor cursor=getAllTeamData();
        List<offlineTeamData> list=new ArrayList<offlineTeamData>();
        while(cursor.moveToNext())
        {
            offlineTeamData data = new offlineTeamData();
            data.putMemberId(cursor.getString(0));
            data.putName(cursor.getString(1));
            data.putDesignation(cursor.getString(2));
            data.putMobileno(cursor.getString(3));
            data.putAadhar(cursor.getString(4));
            list.add(data);
        }
        return list;
    }

    //Attendence
    public long insertData(String sectorid,String memberid,String location,String lattitude,String longitude,String status)
    {
        ContentValues contentValues = new ContentValues();
        contentValues.put("memberid",memberid);
        contentValues.put("location",location);
        contentValues.put("longitude",longitude);
        contentValues.put("lattitude",lattitude);
        contentValues.put("status",status);
        contentValues.put("sectorid",sectorid);
        long res=sqLiteDatabase.insert("attendance",null,contentValues);
        return res;
    }

    public Cursor getAllData()
    {
        Cursor cursor= sqLiteDatabase.rawQuery("select * from attendance",null);
        return  cursor;
    }

    public Cursor getData(String sectorid)
    {
        Cursor cursor= sqLiteDatabase.rawQuery("select * from attendance where sectorid="+sectorid,null);
        return  cursor;
    }

    public List<offlineData> showAllData()
    {
        Cursor cursor=getAllData();
        List<offlineData> list=new ArrayList<offlineData>();
        while(cursor.moveToNext())
        {
            offlineData data = new offlineData();
            data.putAddhar(cursor.getString(0));
            data.putLocation(cursor.getString(1));
            data.putLattitude(cursor.getString(2));
            data.putLongitude(cursor.getString(3));
            data.putStatus(cursor.getString(4));
            list.add(data);
        }
        return list;
    }


    public void deleteData(String memebrid)
    {
        long res=sqLiteDatabase.delete("attendance","memberid=?",new String[]{memebrid});
        /*
        if(res>0)
        {
            Toast.makeText(context,"Deleted 1 Record",Toast.LENGTH_LONG).show();
        }
        else
        {
            Toast.makeText(context,"Not Deleted",Toast.LENGTH_LONG).show();
        }
        */
    }

    public void deleteAll()
    {
        sqLiteDatabase.execSQL("delete from teammembers");
        sqLiteDatabase.execSQL("delete from sectormembers");
        sqLiteDatabase.execSQL("delete from status");
        sqLiteDatabase.execSQL("delete from sectorstatus");
    }

}
