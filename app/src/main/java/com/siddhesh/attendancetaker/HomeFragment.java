package com.siddhesh.attendancetaker;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.Handler;
import android.provider.Settings;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static android.app.Activity.RESULT_OK;

public class HomeFragment extends Fragment {

    ListView listView;
    private static final int REQUEST_LOCATION = 1;
    MarkAttendence markAttendence;
    ConnectionDetection cd;
    DataBaseHelper helper;
    String checkedBy;
    String selectedSectorId = "1";
    List<AttendenceTeamUserModel> users;
    AttendenceTeamCustomAdapter adapter;
    String[] memid;
    String mid,mname,mdesignation,mmobileno,maadhar;
    String checkedid;
    Button markAttendance;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState)
    {
        View view = inflater.inflate(R.layout.fragment_home,container,false);

        listView= view.findViewById(R.id.listView);
        helper=new DataBaseHelper(getContext());
        checkedBy=PreferenceUtils.getMemberId(getContext());
        ActivityCompat.requestPermissions(getActivity(), new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION}, REQUEST_LOCATION);
        cd= new ConnectionDetection(getContext());
        markAttendence = new MarkAttendence(getContext());
        markAttendance = view.findViewById(R.id.btnMarkAttendens);

        markAttendance.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getContext(), ScanningActivity.class);
                intent.putExtra("checkedBy",checkedBy);
                startActivityForResult(intent, 999);
            }
        });

        LocationManager locationManager = (LocationManager) getContext().getSystemService(Context.LOCATION_SERVICE);
        if (!locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER))
        {
            buildAlertMessageNoGps();
        }

        getTeam(selectedSectorId);
        checkAttendance(selectedSectorId);

        return view;
    }

    protected void buildAlertMessageNoGps()
    {
        final AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setMessage("Please Turn ON your GPS Connection")
                .setCancelable(false)
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    public void onClick(final DialogInterface dialog, final int id) {
                        startActivity(new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS));
                    }
                })
                .setNegativeButton("No", new DialogInterface.OnClickListener() {
                    public void onClick(final DialogInterface dialog, final int id) {
                        dialog.cancel();
                    }
                });
        final AlertDialog alert = builder.create();
        alert.show();
    }

    public void getTeam(String id)
    {
        Log.d("home","In GetTeam");
        Cursor cursor = helper.getTeamData(id);
        if (!cursor.moveToNext())
        {
            //Toast.makeText(getApplicationContext(),"GetDataFromServer",Toast.LENGTH_LONG).show();
            getDataFromServer();
        } else {
            //Toast.makeText(getApplicationContext(),"LoadTeam",Toast.LENGTH_LONG).show();
            loadTeam();
        }
    }

    public void createList(String[] id,String[] fullname,String[] designation,String[] mobileNo,int[] status)
    {
        users = new ArrayList<>();
        for (int i = 0; i < fullname.length; i++)
        {
            users.add(new AttendenceTeamUserModel(id[i],fullname[i],designation[i],mobileNo[i],status[i]));
        }
        adapter = new AttendenceTeamCustomAdapter(getActivity(), users);
        listView.setAdapter(adapter);

        Date c = Calendar.getInstance().getTime();
        SimpleDateFormat df = new SimpleDateFormat("dd-mm-yyyy");

        String today = df.format(c);
        String date;
        if((date = PreferenceUtils.getDate(getContext())) == null) {
            PreferenceUtils.saveDate(today,getContext());
        }
        else {
            if(!date.equals(today)) {
                        helper.deleteStatus(selectedSectorId);
                        PreferenceUtils.saveDate(today,getContext());
                        updateTeam();
            }
        }
    }

    public void checkAttendance(String id)
    {
        updateTeam();
    }

    public void loadTeam()
    {
        Log.d("home","In loadTeam");
        List<offlineTeamData> list;
        offlineTeamData data;
        list=helper.showTeamData(selectedSectorId);
        //Toast.makeText(getApplicationContext(),list.toString(),Toast.LENGTH_LONG).show();
        int size=list.size();
        //Toast.makeText(getApplicationContext(),"Size:"+size,Toast.LENGTH_LONG).show();
        String[] id=new String[size];
        String[] fullname=new String[size];
        String[] designation=new String[size];
        String[] mobileNo=new String[size];
        String[] aadhar=new String[size];
        int[] status=new int[size];
        for(int i=0;i<size;i++)
        {
            data=list.get(i);
            id[i]=data.getMemberId();
            fullname[i]=data.getName();
            designation[i]=data.getDesignation();
            mobileNo[i]=data.getMobileno();
            aadhar[i]=data.getAadhar();
            status[i]=helper.getStatus(id[i]);
        }
        createList(id,fullname,designation,mobileNo,status);
        getMembers();
    }

    public void getDataFromServer()
    {
        Log.d("home","In getDataFromServer");
        StringRequest stringRequest = new StringRequest(Request.Method.POST, DataBaseConnection.URL_MYTEAM, new Response.Listener<String>()
        {
            @Override
            public void onResponse(String response)
            {
                try
                {
                    Log.d("home","In Responce GetDataFromServer");
                    JSONObject jsonObject = new JSONObject(response);
                    JSONArray js = jsonObject.getJSONArray("myteam");
                    Toast.makeText(getContext(),"Data:"+js.toString(),Toast.LENGTH_LONG).show();
                    JSONObject test = js.getJSONObject(0);
                    //Toast.makeText(getApplicationContext(),js.toString(),Toast.LENGTH_LONG).show();
                    String flag = test.getString("flag");
                    String[] name=new String[js.length()];
                    String[] desg=new String[js.length()];
                    String[] mob=new String[js.length()];
                    String[] d=new String[js.length()];
                    String[] aadhar=new String[js.length()];
                    if (flag.equals("true")) {
                        for (int i = 0; i < js.length(); i++) {
                            JSONObject js1 = js.getJSONObject(i);
                            d[i] = js1.getString("memberId");
                            name[i] = js1.getString("name");
                            desg[i] = js1.getString("designation");
                            mob[i] = js1.getString("mobileNo");
                            aadhar[i] = js1.getString("aadharNumber");
                            //designationId[i] = Integer.parseInt(js1.getString("designationid"));
                            //Toast.makeText(getApplicationContext(),d[i]+"\n"+name[i]+"\n"+desg[i],Toast.LENGTH_LONG).show();
                            helper.insertTeamData(selectedSectorId, d[i], name[i], desg[i], mob[i], aadhar[i]);
                        }
                        //Toast.makeText(getApplicationContext(), "GetData:LoadTeam", Toast.LENGTH_LONG).show();
                    } else {
                        Toast.makeText(getContext(), "No Data Found", Toast.LENGTH_LONG).show();
                    }
                    loadTeam();
                } catch (JSONException e)
                {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener()
        {
            @Override
            public void onErrorResponse(VolleyError error)
            {
                Toast.makeText(getContext(),"Server Error:"+ error.getMessage(), Toast.LENGTH_LONG).show();
            }
        })
        {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("sectorId", selectedSectorId);
                return params;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(getContext());
        //Toast.makeText(getApplicationContext(),"Request Sent",Toast.LENGTH_LONG).show();
        requestQueue.add(stringRequest);
    }

    public void getMembers()
    {
        Log.d("home","In getMembers");
        StringRequest stringRequest = new StringRequest(Request.Method.POST, DataBaseConnection.URL_MEMBERS, new Response.Listener<String>()
        {
            @Override
            public void onResponse(String response)
            {
                try
                {
                    Log.d("home","In Responce getMembers");
                    JSONObject jsonObject = new JSONObject(response);
                    JSONArray js = jsonObject.getJSONArray("myteam");
                    String flag= (String) js.get(0);
                    memid=new String[js.length() - 1];
                    if(flag.equals("true"))
                    {
                        for (int i = 0; i < js.length() - 1; i++)
                        {
                            memid[i] = (String) js.get(i+1);
                        }
                    }
                    else
                    {
                        Toast.makeText(getContext(),"No Data Found",Toast.LENGTH_LONG).show();
                    }
                    updateList();
                } catch (JSONException e)
                {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener()
        {
            @Override
            public void onErrorResponse(VolleyError error)
            {
                Toast.makeText(getContext(),"Server Error:"+ error.getMessage(), Toast.LENGTH_LONG).show();
            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("sectorId",selectedSectorId);
                return params;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(getContext());
        //Toast.makeText(getApplicationContext(),"Request Sent",Toast.LENGTH_LONG).show();
        requestQueue.add(stringRequest);
    }

    public void updateList()
    {
        Log.d("home","In Update List");
        boolean memflag=false,idflag=false;
        List test=helper.showTeamMemberId(selectedSectorId);
        String[] id = new String[test.size()];
        for(int i=0;i<test.size();i++)
        {
            id[i]=test.get(i).toString();
        }

        for(int i=0;i<id.length;i++)
        {
            for(int j=0;j<memid.length;i++)
            {
                if (id[i].equals(memid[j]))
                {
                    memflag=true;
                    break;
                }
            }
            if(!memflag)
            {
                helper.deleteTeamMember(id[i]);
            }
            memflag=false;
        }

        for(int i=0;i<memid.length;i++)
        {
            for(int j=0;j<id.length;i++)
            {
                if(memid[i].equals(id[j]))
                {
                    idflag=true;
                }
            }
            if(!idflag)
            {
                fatchMembers(memid[i]);
            }
            idflag=false;
        }
        updateTeam();
    }

    public void fatchMembers(final String id)
    {
        StringRequest stringRequest = new StringRequest(Request.Method.POST, DataBaseConnection.URL_MEMBERDETAILS, new Response.Listener<String>()
        {
            @Override
            public void onResponse(String response)
            {
                try
                {
                    JSONObject jsonObject = new JSONObject(response);
                    JSONObject js = jsonObject.getJSONObject("myteam");
                    //Toast.makeText(getApplicationContext(),js.toString(),Toast.LENGTH_LONG).show();
                    String flag = js.getString("flag");
                    if(flag.equals("true"))
                    {
                        mid= js.getString("memberId");
                        mname= js.getString("name");
                        mdesignation = js.getString("designation");
                        mmobileno = js.getString("mobileNo");
                        maadhar = js.getString("aadharNumber");
                    }
                    else
                    {
                        Toast.makeText(getContext(),"No Data Found",Toast.LENGTH_LONG).show();
                    }
                    helper.insertTeamData(selectedSectorId,mid,mname,mdesignation,mmobileno,maadhar);
                }
                catch (JSONException e)
                {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener()
        {
            @Override
            public void onErrorResponse(VolleyError error)
            {
                Toast.makeText(getContext(),"Server Error:"+ error.getMessage(), Toast.LENGTH_LONG).show();
            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("memberId",id);
                return params;
            }
        };
        Log.d("home","Memberid:+" + id);
        RequestQueue requestQueue = Volley.newRequestQueue(getContext());
        //Toast.makeText(getApplicationContext(),"Request Sent",Toast.LENGTH_LONG).show();
        requestQueue.add(stringRequest);
    }

    public void updateTeam()
    {
        List<offlineTeamData> list;
        offlineTeamData data;
        list=helper.showTeamData(selectedSectorId);
        //Toast.makeText(getApplicationContext(),list.toString(),Toast.LENGTH_LONG).show();
        int size=list.size();
        //Toast.makeText(getApplicationContext(),"Size:"+size,Toast.LENGTH_LONG).show();
        String[] id=new String[size];
        String[] fullname=new String[size];
        String[] designation=new String[size];
        String[] mobileNo=new String[size];
        int[] status=new int[size];
        for(int i=0;i<size;i++)
        {
            data=list.get(i);
            id[i]=data.getMemberId();
            fullname[i]=data.getName();
            designation[i]=data.getDesignation();
            mobileNo[i]=data.getMobileno();
            status[i]=helper.getStatus(id[i]);
        }
        createList(id,fullname,designation,mobileNo,status);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == 999 && resultCode == RESULT_OK)
        {
            checkedid = data.getStringExtra("checkedid");
            String[] memid, aadhar;
            List<offlineTeamData> list;
            list = helper.showAllTeamData();
            aadhar = new String[list.size()];
            memid = new String[list.size()];
            for (int i = 0; i < list.size(); i++)
            {
                offlineTeamData offteam = list.get(i);
                aadhar[i] = offteam.getAadhar();
                memid[i] = offteam.getMemberId();
            }
            for (int i = 0; i < list.size(); i++)
            {
                if (aadhar[i].equals(checkedid))
                {
                    GPSTracker gpsTracker=new GPSTracker(getActivity());
                    String memberid=memid[i];
                    String address=gpsTracker.getAddress();
                    String lattitude=gpsTracker.getLattitude();
                    String longitude=gpsTracker.getLongitude();
                    if(cd.isConnected())
                    {
                        markAttendence.onlineAtttendance(memberid,address,lattitude,longitude,"Present");
                    }
                    else
                    {
                        offlineAttendance(memberid,lattitude,longitude,address);
                    }
                    helper.insertStatus(selectedSectorId,memid[i], "1");
                    updateTeam();
                    break;
                }
            }
        }
    }

    public void offlineAttendance(String addhar,String lattitude,String longitude,String address) {
        long res = helper.insertData(selectedSectorId, addhar, address, lattitude, longitude, "Present");
        if (res > 0) {
            Toast.makeText(getContext(), getString(R.string.displayAttendanceMsgSuccess), Toast.LENGTH_LONG).show();
        } else {
            Toast.makeText(getContext(), getString(R.string.displayMsgAttendanceError), Toast.LENGTH_LONG).show();
        }
    }

}
