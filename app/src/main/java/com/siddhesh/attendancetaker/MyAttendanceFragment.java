package com.siddhesh.attendancetaker;


import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridView;
import android.widget.ListView;
import android.widget.Toast;

import androidx.fragment.app.Fragment;

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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MyAttendanceFragment extends Fragment {

    ListView attendanceList;
    MyAttendanceAdapter m_gridviewAdapter;
    List<MyAttendanceItemData> itemList;

    String flag;
    String[] status,datetime,location,checkedby;

    public MyAttendanceFragment()
    {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view=inflater.inflate(R.layout.fragment_my_attendance, container, false);

        attendanceList=view.findViewById(R.id.attendancelist);

        itemList = new ArrayList<MyAttendanceItemData>();

        getDetails();

        return view;
    }

    public void getDetails()
    {
        StringRequest stringRequest = new StringRequest(Request.Method.POST, DataBaseConnection.URL_MYATTENDANCE, new Response.Listener<String>()
        {
            @Override
            public void onResponse(String response)
            {
                try
                {
                    JSONObject jsonObject = new JSONObject(response);
                    JSONArray js = jsonObject.getJSONArray("myattendance");
                    JSONObject test = js.getJSONObject(0);
                    flag = test.getString("flag");
                    status=new String[js.length()];
                    location=new String[js.length()];
                    datetime=new String[js.length()];
                    checkedby=new String[js.length()];

                    if(flag.equals("true"))
                    {
                        for (int i = 0; i < js.length(); i++)
                        {
                            JSONObject js1 = js.getJSONObject(i);
                            status[i] = js1.getString("status");
                            location[i] = js1.getString("location");
                            datetime[i] = js1.getString("dateTime");
                            checkedby[i] = js1.getString("checkedBy");
                        }
                        createList();
                    }
                    else
                    {
                        Toast.makeText(getActivity().getApplicationContext(),"No Data Found",Toast.LENGTH_LONG).show();
                    }
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
                Toast.makeText(getActivity().getApplicationContext(),"Server Error:"+ error.getMessage(), Toast.LENGTH_LONG).show();
            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("memberId", PreferenceUtils.getMemberId(getContext()));
                return params;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(getActivity().getApplicationContext());
        requestQueue.add(stringRequest);
    }

    public void createList()
    {
        for (int i=0;i<status.length;i++)
        {
            MyAttendanceItemData item = new MyAttendanceItemData(status[i],datetime[i],location[i],checkedby[i]);
            itemList.add(item);
        }

        //3. create adapter
        m_gridviewAdapter = new MyAttendanceAdapter(getActivity(), itemList);

        //4. add adapter to gridview
        attendanceList.setAdapter(m_gridviewAdapter);
    }
}
