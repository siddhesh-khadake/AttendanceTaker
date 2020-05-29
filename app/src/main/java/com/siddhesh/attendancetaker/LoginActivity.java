package com.siddhesh.attendancetaker;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.os.Bundle;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
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

import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.Objects;

public class LoginActivity extends AppCompatActivity
{
    LinearLayout l2,l1;

    String username,password;
    EditText usertext,passtext;
    TextView warn;
    String flag = "false";
    Animation uptodown,downtotop;
    ProgressDialog progressDialog;
    int loginid;
    ImageView logo;
    String lang="";

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        Objects.requireNonNull(getSupportActionBar()).hide();
        usertext = findViewById(R.id.etLoginUsername);
        passtext = findViewById(R.id.etLoginPassword);
        warn = findViewById(R.id.warningtext);
        l2 = (LinearLayout) findViewById(R.id.LayoutInput);
        l1= (LinearLayout) findViewById(R.id.LayoutImage);
        uptodown = AnimationUtils.loadAnimation(this,R.anim.uptodown);
        downtotop = AnimationUtils.loadAnimation(this,R.anim.downtotop);
        logo=findViewById(R.id.firstAnim);

        l2.setAnimation(downtotop);
        l1.setAnimation(uptodown);
        loadLocale();

        switch (lang)
        {
            case "en":
                logo.setBackgroundResource(R.drawable.englisttext);
                break;
            case "":
                logo.setBackgroundResource(R.drawable.englisttext);
                break;
            default:
                logo.setBackgroundResource(R.drawable.marathitext1);
                break;
        }

        if (PreferenceUtils.getUsername(this) != null )
        {
            loginid=Integer.parseInt(PreferenceUtils.getLoginId(getApplicationContext()));
            loginByDesignation(loginid);
        }

        Button chnglng = findViewById(R.id.btnlang);
        chnglng.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showChangeLanguageDialoge();
            }
        });
    }

    private void showChangeLanguageDialoge()
    {
        final String[] lang = {"English", "मराठी"};
        AlertDialog.Builder lngbuild = new AlertDialog.Builder(LoginActivity.this);
        lngbuild.setTitle(R.string.lang);
        lngbuild.setSingleChoiceItems(lang, -1, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int i) {
                if (i == 0) {
                    setLocale("en");
                    recreate();
                } else if (i == 1) {
                    setLocale("mr");
                    recreate();
                }

                dialog.dismiss();
            }
        });
        AlertDialog aldialog = lngbuild.create();
        aldialog.show();
    }

    private void setLocale(String lngs) {
        Locale locale = new Locale(lngs);
        Locale.setDefault(locale);
        Configuration cnf = new Configuration();
        cnf.locale = locale;
        getBaseContext().getResources().updateConfiguration(cnf, getBaseContext().getResources().getDisplayMetrics());
        SharedPreferences.Editor editor = getSharedPreferences("Settings", MODE_PRIVATE).edit();
        editor.putString("My Lang", lngs);
        editor.apply();
        lang=lngs;
    }

    public void loadLocale()
    {
        SharedPreferences prefs = getSharedPreferences("Settings", Activity.MODE_PRIVATE);
        String lan = prefs.getString("My Lang", "");
        setLocale(lan);
    }

    public void loginByDesignation(int loginid)
    {
        Intent intent;
        username = usertext.getText().toString();
        password = passtext.getText().toString();

        if(loginid == 1)
        {
            intent = new Intent(getApplicationContext(),OfficerMainActivity.class);
        }
        else if(loginid == 2)
        {
            intent = new Intent(getApplicationContext(),UserMainActivity.class);
        }
        else {
            intent = new Intent(getApplicationContext(),MainActivity.class);
        }
        startActivity(intent);
        finish();
    }

    public void onChange(View view)
    {
    }

    public void validateLogin()
    {
        StringRequest stringRequest = new StringRequest(Request.Method.POST, DataBaseConnection.URL_ROLE, new Response.Listener<String>()
        {
            @Override
            public void onResponse(String response)
            {
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    JSONObject js = jsonObject.getJSONObject("logindata");
                    //Toast.makeText(getApplicationContext(),js1.toString(),Toast.LENGTH_LONG).show();
                    flag = js.getString("flag");
                    if(flag.equals("true"))
                    {
                        PreferenceUtils.saveDetails(
                                js.getString("memberId"),
                                js.getString("firstName"),
                                js.getString("middleName"),
                                js.getString("lastName"),
                                js.getString("dob"),
                                js.getString("aadharNumber"),
                                js.getString("bloodGroup"),
                                js.getString("gender"),
                                js.getString("mobileNo"),
                                js.getString("posting"),
                                js.getString("loginId"),
                                js.getString("designation"),
                                getApplicationContext());

                        loginid=Integer.parseInt(js.getString("loginId"));

                        PreferenceUtils.saveUsername(username, getApplicationContext());
                        PreferenceUtils.savePassword(password, getApplicationContext());

                        progressDialog.dismiss();

                        loginByDesignation(loginid);
                    }
                    else
                    {
                        progressDialog.dismiss();
                        warn.setText(getString(R.string.errorinvalidlogin));
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
                Toast.makeText(getApplicationContext(),"Server Error:"+ error.getMessage(), Toast.LENGTH_LONG).show();
            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError
            {
                Map<String, String> params = new HashMap<>();
                params.put("mobileNo", username);
                params.put("password", password);
                return params;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);
    }

    public void onRecover(View view)
    {
        buildAlertMessage();
    }

    public void buildAlertMessage()
    {
        final AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage(getString(R.string.recover_msg))
                .setCancelable(false)
                .setNegativeButton("OK", new DialogInterface.OnClickListener() {
                    public void onClick(final DialogInterface dialog, final int id) {
                        dialog.cancel();
                        finishActivity(RESULT_CANCELED);
                    }
                });
        final AlertDialog alert = builder.create();
        alert.show();
    }

    public void onClickLogin(View view) {
        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage(getString(R.string.signin));
        progressDialog.show();
        warn.setText("");
        username = usertext.getText().toString();
        password = passtext.getText().toString();
        validateLogin();
    }

    public void onClickRegister(View view) {
        Intent intent = new Intent(getApplicationContext(),RegistrationActivity.class);
        startActivity(intent);
    }

}
