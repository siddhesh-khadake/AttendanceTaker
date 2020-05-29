package com.siddhesh.attendancetaker;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
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

import java.util.HashMap;
import java.util.Map;

public class ChangePasswordFragment extends Fragment {
    EditText password,newpass,confirmpass;
    TextView warning;
    String flag;
    Button change;

    public ChangePasswordFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view=inflater.inflate(R.layout.fragment_change_password, container, false);

        password=view.findViewById(R.id.pass);
        newpass=view.findViewById(R.id.newpass);
        confirmpass=view.findViewById(R.id.confirmpass);
        warning=view.findViewById(R.id.warningtext);
        change = view.findViewById(R.id.changepass);
        change.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                validate();
            }
        });

        return view;
    }

    public void validate(){

        if(newpass.getText().toString().equals("")) {
            newpass.setError("Password Required");
        }
        else
        {
            if (confirmpass.getText().toString().equals(newpass.getText().toString())) {
                setPassword();
            } else {
                confirmpass.setError("password not matched");
            }
        }

    }

    public void setPassword()
    {

        StringRequest stringRequest = new StringRequest(Request.Method.POST, DataBaseConnection.URL_CHANGEPASSWORD, new Response.Listener<String>()
        {
            @Override
            public void onResponse(String response)
            {
                try
                {
                    JSONObject jsonObject = new JSONObject(response);
                    JSONObject js = jsonObject.getJSONObject("changepassword");
                    flag = js.getString("flag");
                    if (flag.equals("true"))
                    {
                        warning.setText("Password changed successfully");
                        clear();
                    }
                    else {
                        clear();
                        warning.setText("Current Password Doesn't Match");
                    }
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
                Toast.makeText(getActivity().getApplicationContext(),"Server Error:"+ error.getMessage(), Toast.LENGTH_LONG).show();
            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError
            {
                Map<String, String> params = new HashMap<>();
                params.put("memberId", PreferenceUtils.getMemberId(getActivity().getApplicationContext()));
                params.put("currentPassword",password.getText().toString());
                params.put("newPassword",newpass.getText().toString());
                return params;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(getActivity().getApplicationContext());
        requestQueue.add(stringRequest);
    }

    public void clear()
    {
        password.setText("");
        newpass.setText("");
        confirmpass.setText("");
    }
}
