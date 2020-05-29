package com.siddhesh.attendancetaker;

import android.content.Context;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MarkAttendence
{
    Context context;
    String checkedby;
    DataBaseHelper helper;
    ConnectionDetection cd;

    public MarkAttendence(Context context)
    {
        this.context=context;
        checkedby = PreferenceUtils.getMemberId(context);
        helper=new DataBaseHelper(context);
        cd=new ConnectionDetection(context);
    }

    public void offlineToOnline()
    {
        offlineData data;
        List<offlineData> list;
        list = helper.showAllData();
        for (int i = 0; i < list.size(); i++)
        {
            if (cd.isConnected())
            {
                data = list.get(i);
                offlineAtttendance(data.getAddhar(), data.getLocation(), data.getLattitude(), data.getLongitude(),data.getStatus());
            }
            }
    }

    public void onlineAtttendance(final String memberId, final String location, final String lattitude, final String longitude,final String status)
    {
        //Toast.makeText(context,"memberId:"+memberId+"\nStatus:"+status+"\nLocation:"+location+"\nLattitude:"+lattitude+"\nLongitude:"+longitude+"\nCheckedId:"+checkedby,Toast.LENGTH_LONG).show();
        StringRequest stringRequest = new StringRequest(Request.Method.POST, DataBaseConnection.URL_ATTENDENCE, new Response.Listener<String>()
        {
            @Override
            public void onResponse(String response)
            {
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    JSONObject js = jsonObject.getJSONObject("attendance");
                    //Toast.makeText(getApplicationContext(),js1.toString(),Toast.LENGTH_LONG).show();
                    String flag = js.getString("flag");
                    if(flag.equals("true"))
                    {
                        Toast.makeText(context,"Attendance Marked Successfully..",Toast.LENGTH_LONG).show();
                    }
                    else
                    {
                        Toast.makeText(context,"Attendance Not Marked..",Toast.LENGTH_LONG).show();
                    }
                }
                catch (Exception e) {
                    e.printStackTrace();
                    Toast.makeText(context,"Server Error: " + e.toString(), Toast.LENGTH_LONG).show();
                }
            }
        }, new Response.ErrorListener()
        {
            @Override
            public void onErrorResponse(VolleyError error)
            {
                //Toast.makeText(context,error.getMessage(), Toast.LENGTH_LONG).show();
            }
        })
        {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("memberId",memberId);
                params.put("checkedBy",checkedby);
                params.put("location",location);
                params.put("latitude",lattitude);
                params.put("longitude",longitude);
                params.put("status",status);
                return params;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(context);
        requestQueue.add(stringRequest);
    }

    public void offlineAtttendance(final String memberId,final String location, final String lattitude, final String longitude,final String status)
    {
        //Toast.makeText(context,"MemberId:"+memberId+"\nStatus:"+status+"\nLocation:"+location+"\nLattitude:"+lattitude+"\nLongitude:"+longitude+"\nCheckedId:"+checkedby,Toast.LENGTH_LONG).show();
        StringRequest stringRequest = new StringRequest(Request.Method.POST, DataBaseConnection.URL_ATTENDENCE, new Response.Listener<String>()
        {
            @Override
            public void onResponse(String response)
            {
                String msg=null;
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    JSONObject js = jsonObject.getJSONObject("attendance");
                    //Toast.makeText(getApplicationContext(),js1.toString(),Toast.LENGTH_LONG).show();
                    String flag = js.getString("flag");
                    String memberId = js.getString("memberId");
                    if(flag.equals("true"))
                    {
                        //Toast.makeText(context,"Attendance Marked Successfully..",Toast.LENGTH_LONG).show();
                        helper.deleteData(memberId);
                    }
                    else
                    {
                        //Toast.makeText(context,"Attendance Not Marked..",Toast.LENGTH_LONG).show();
                    }

                }
                catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener()
        {
            @Override
            public void onErrorResponse(VolleyError error)
            {
                //Toast.makeText(context,error.getMessage(), Toast.LENGTH_LONG).show();
            }
        })
        {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("memberId",memberId);
                params.put("checkedBy",checkedby);
                params.put("location",location);
                params.put("latitude",lattitude);
                params.put("longitude",longitude);
                params.put("status",status);
                return params;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(context);
        requestQueue.add(stringRequest);
    }

}
