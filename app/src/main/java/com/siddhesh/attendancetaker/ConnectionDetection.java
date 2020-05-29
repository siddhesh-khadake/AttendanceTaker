package com.siddhesh.attendancetaker;

import android.app.Service;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

public class ConnectionDetection {

    Context context;

    public ConnectionDetection(Context context) {

        this.context=context;

    }
    public Boolean isConnected()
    {
        ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Service.CONNECTIVITY_SERVICE);

        if(cm != null){

            NetworkInfo info=cm.getActiveNetworkInfo();
            if(info != null)
            {
                if(info.getState() == NetworkInfo.State.CONNECTED){
                    return true;
                }
                else
                {
                    return false;
                }
            }
        }
        return false;
    }
}
