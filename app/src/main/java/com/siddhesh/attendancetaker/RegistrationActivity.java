package com.siddhesh.attendancetaker;

import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

public class RegistrationActivity extends AppCompatActivity
{

    boolean flag=true;
    int mYear,mMonth,mDay;
    EditText first,last,middle,mobno,nem,adhar,password,passwordconfirm;
    Spinner bloodgr,spdesignation;
    Spinner spgender;
    String adhar4=" ",adhar2=" ",adhar3=" ",fstname="",lstname="",mdname="",blood="",gender="",desig="",mob="",nemuktext="",birth="",adharnm="";
    int count=0;
    TextView sptvbgerror,sptvgenerror,sptvdesigerror,dob;
    String passwordstr;

    public void SetError(String errorMessage, Spinner spn, TextView tvInvisibleError)
    {
        View view = spn.getSelectedView();
        TextView tvListItem = (TextView)view;
        if(errorMessage != null)
        {
            tvListItem.setError(errorMessage);
            tvListItem.requestFocus();
            tvInvisibleError.requestFocus();
            tvInvisibleError.setError(errorMessage);
        }
        else
        {
            tvListItem.setError(null);
            tvInvisibleError.setError(null);
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
      //  Toast.makeText(getApplicationContext(),""+year,Toast.LENGTH_LONG).show();
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration);
        first=findViewById(R.id.firstnametxt);
        last=findViewById(R.id.lastnametxt);
        middle=findViewById(R.id.middlenametxt);
        dob=findViewById(R.id.dob);
        adhar=findViewById(R.id.adharno);
        mobno=findViewById(R.id.mobno);
        nem=findViewById(R.id.nemnuk);
        spdesignation=findViewById(R.id.spdesignation);
        spgender= findViewById(R.id.spgender);
        bloodgr=findViewById(R.id.spblood);
        sptvbgerror=findViewById(R.id.tvInvisibleErrorBlood);
        sptvgenerror= findViewById(R.id.tvInvisibleErrorGender);
        sptvdesigerror=findViewById(R.id.tvInvisibleErrorDesig);
        password=findViewById(R.id.passwordtxt);
        passwordconfirm=findViewById(R.id.confirmpasswordtxt);

        ArrayAdapter<CharSequence> spdgenderadopter = ArrayAdapter.createFromResource(this,
                R.array.spgender, android.R.layout.simple_gallery_item);
        spdgenderadopter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spgender.setAdapter(spdgenderadopter);
        spgender.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                spgender.setSelection(position);
                gender=spgender.getSelectedItem().toString();
                if(position!=0)
                {
                    SetError(null,spgender,sptvgenerror);
                }
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        ArrayAdapter<CharSequence> spdpostadopter = ArrayAdapter.createFromResource(this,
                    R.array.post, android.R.layout.simple_gallery_item);
        spdpostadopter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spdesignation.setAdapter(spdpostadopter);
        spdesignation.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                    spdesignation.setSelection(position);
                    desig=spdesignation.getSelectedItem().toString();
                    if(position!=0)
                    {
                        SetError(null,spgender,sptvgenerror);
                    }
                }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        ArrayAdapter<CharSequence> bloodAdapter = ArrayAdapter.createFromResource(this,R.array.bloodgp, android.R.layout.simple_gallery_item);
        bloodAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        bloodgr.setAdapter(bloodAdapter);
        bloodgr.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int i, long l) {
                bloodgr.setSelection(i);
                blood=bloodgr.getSelectedItem().toString();
                SetError(null,bloodgr,sptvbgerror);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent)
            {

            }
        });

        adhar.addTextChangedListener(new TextWatcher()
        {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s)
            {
                if (count <= adhar.getText().toString().length()
                        &&(adhar.getText().toString().length()==4
                        ||adhar.getText().toString().length()==9
                        )){
                    adhar.setText(adhar.getText().toString()+"-");
                    int pos = adhar.getText().length();
                    adhar.setSelection(pos);
                }
                count = adhar.getText().toString().length();
            }
        });

    }

    public void onPickDate(View v) {

        if (v == dob) {
            final Calendar cal = Calendar.getInstance();
            //cal.set(mYear,mMonth,mDay);
            mDay = cal.get(Calendar.DAY_OF_MONTH);
            mMonth = cal.get(Calendar.MONTH);
            mYear = cal.get(Calendar.YEAR);
            DatePickerDialog datePickerDialog = new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {
                @Override
                public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                    dob.setText(year + "-" + (month + 1) + "-" +dayOfMonth );
                }
            }, mYear, mMonth, mDay);

            datePickerDialog.show();
        }
    }

    public void validation()
    {
        int b=0,g=0,d=0,de=0;
        flag=true;
        b=bloodgr.getSelectedItemPosition();
        g=spgender.getSelectedItemPosition();
        d=spdesignation.getSelectedItemPosition();

        if(b==0)
        {
            flag=false;
            SetError(getString(R.string.selectbloodGroup),bloodgr,sptvbgerror);
        }
        if(g==0)
        {
            flag=false;
            SetError(getString(R.string.SelectGender),spgender,sptvgenerror);
        }
        if(d==0)
        {
            flag=false;
            SetError(getString(R.string.selectDesignation),spdesignation,sptvdesigerror);
        }
        if(first.getText().length()<1)
        {
            flag=false;
            first.setError(getString(R.string.firstNameReq));
            first.setNextFocusDownId(R.id.firstnametxt);
        }
        if(middle.getText().length()<1)
        {
            flag=false;
            middle.setError(getString(R.string.middleNameReq));
            middle.setNextFocusDownId(R.id.middlenametxt);
        }
        if(last.getText().length()<1)//Last Name
        {
            flag=false;
            last.setError(getString(R.string.lastNameReq));
            last.setNextFocusDownId(R.id.lastnametxt);
        }
        if(adhar.getText().length()!=14)
        {
            flag=false;
            adhar.setError(getString(R.string.invalidAdhar));
            adhar.setNextFocusDownId(R.id.adharno);
        }
        if (mobno.getText().length() < 10) {
            flag = false;
            mobno.setError(getString(R.string.enterValidNo));
            mobno.setNextFocusDownId(R.id.mobno);
        }
        if(nem.getText().length()<4)//Numbnuk Place
        {
            flag=false;
            nem.setError(getString(R.string.enterValidNemnukNo));
            nem.setNextFocusDownId(R.id.nemnuk);
        }
        if(!passwordconfirm.getText().toString().equals(password.getText().toString()))
        {
            flag=false;
            passwordconfirm.setError(getString(R.string.PasswordnotMatch));
        }
    }

    public void OnRegister(View View)
    {
        validation();
        if(adhar.getText().length()==14)
        {
            adhar2 = adhar.getText().toString().substring(0,4);
            adhar3 = adhar.getText().toString().substring(5,9);
            adhar4 = adhar.getText().toString().substring(10,14);
            Toast.makeText(getApplicationContext(),adhar2+" "+adhar3+" "+adhar4, Toast.LENGTH_LONG).show();
        }
        fstname=first.getText().toString();
        lstname=last.getText().toString();
        mdname=middle.getText().toString();
        birth=dob.getText().toString();
        adharnm=adhar2+adhar3+adhar4;
        mob=mobno.getText().toString();
        nemuktext=nem.getText().toString();
        passwordstr=password.getText().toString();
        if(flag)
        {
            final ProgressDialog progressDialog = new ProgressDialog(this);
            progressDialog.setMessage(getString(R.string.RegisteringUser));
            progressDialog.show();

            StringRequest stringRequest = new StringRequest(Request.Method.POST, DataBaseConnection.URL_REGISTER, new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    progressDialog.dismiss();
                    try {
                        JSONObject jsonObject = new JSONObject(response);
                        JSONObject js = jsonObject.getJSONObject("register");
                        //Toast.makeText(getApplicationContext(),js1.toString(),Toast.LENGTH_LONG).show();
                        String flag = js.getString("flag");
                        if(flag.equals("true")) {
                            Toast.makeText(getApplicationContext(),R.string.registerSuccess, Toast.LENGTH_LONG).show();
                            finish();
                        }
                        else {
                            Toast.makeText(getApplicationContext(),R.string.registerUnsuccess, Toast.LENGTH_LONG).show();
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    progressDialog.hide();
                    Toast.makeText(getApplicationContext(),error.getMessage(), Toast.LENGTH_LONG).show();
                }
            })
            {
                @Override
                protected Map<String, String> getParams() throws AuthFailureError {
                    Map<String, String> params = new HashMap<>();
                    params.put("firstName", fstname);
                    params.put("middleName", mdname);
                    params.put("lastName", lstname);
                    params.put("dob", birth);
                    params.put("aadharNumber", adharnm);
                    params.put("mobileNo", mob);
                    params.put("gender", gender);
                    params.put("bloodGroup", blood);
                    params.put("designation", desig);
                    params.put("nemnuk", nemuktext);
                    params.put("password",passwordstr);
                    return params;
                }
            };
            RequestQueue requestQueue = Volley.newRequestQueue(this);
            requestQueue.add(stringRequest);
        }
        else
        {
            Toast.makeText(getApplicationContext(),"Error", Toast.LENGTH_LONG).show();
        }
    }//End of OnRegister

    public void OnCancel(View View)
    {
        finish();
    }
}

