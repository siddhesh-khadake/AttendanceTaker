package com.siddhesh.attendancetaker;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.cardview.widget.CardView;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class AttendenceTeamCustomAdapter extends BaseAdapter {

    Activity activity;
    List<AttendenceTeamUserModel> users;
    LayoutInflater inflater;

    ConnectionDetection cd;
    MarkAttendence markAttendence;
    DataBaseHelper helper;
    AttendenceTeamUserModel model;
    String abscentRemark;
    int spinnerPosition=0;
    Calendar calendar = Calendar.getInstance();
    SimpleDateFormat sdf =  new SimpleDateFormat("yyyy-mm-dd");
    SimpleDateFormat format = new SimpleDateFormat("hh:mm:ss");
    String memberId,date,time,checkedBy,lattitude,longitude,address;
    int status;
    String selectedSectorId = "1";

    public AttendenceTeamCustomAdapter(Activity activity)
    {
        this.activity = activity;
    }

    public AttendenceTeamCustomAdapter(Activity activity, List<AttendenceTeamUserModel> users)
    {
        this.activity   = activity;
        this.users      = users;
        inflater        = activity.getLayoutInflater();
        cd = new ConnectionDetection(activity.getApplicationContext());
        markAttendence = new MarkAttendence(activity.getApplicationContext());
        helper = new DataBaseHelper(activity.getApplicationContext());
    }


    @Override
    public int getCount() {
        return users.size();
    }

    @Override
    public Object getItem(int i) {
        return i;
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(final int i, View view, ViewGroup viewGroup)
    {

        final ViewHolder holder;

        if (view == null)
        {
            view = inflater.inflate(R.layout.attendence_team_list_view_item, viewGroup, false);

            holder = new ViewHolder();
            holder.tvUserName = (TextView)view.findViewById(R.id.tv_user_name);
            holder.tvMobNo = view.findViewById(R.id.tv_mobno);
            holder.tvDesignation = view.findViewById(R.id.tv_designation);
            holder.tvStatus = view.findViewById(R.id.tv_status);
            holder.linearLayout=view.findViewById(R.id.relative);
            holder.abscent = view.findViewById(R.id.abscent);
            holder.call = view.findViewById(R.id.call);
            holder.remarkList = view.findViewById(R.id.remarkList);
            holder.cardView = view.findViewById(R.id.cardView);
            view.setTag(holder);
        }
        else
            holder = (ViewHolder)view.getTag();

        model = users.get(i);

        holder.tvUserName.setText(model.getUserName());
        holder.tvMobNo.setText(model.mobno);
        holder.tvDesignation.setText(model.designation);
        holder.remarkList.setSelection(spinnerPosition);

        if (model.status==1)
        {
            holder.cardView.setBackgroundColor(Color.GREEN);
            holder.tvStatus.setText(activity.getString(R.string.present));
            holder.tvStatus.setTextColor(Color.WHITE);
        }
        else if(model.status==2)
        {
            holder.cardView.setBackgroundColor(Color.RED);
            holder.tvStatus.setText(activity.getString(R.string.absent));
            holder.tvStatus.setTextColor(Color.WHITE);
        }
        else
        {
            holder.cardView.setBackgroundColor(Color.WHITE);
            holder.remarkList.setVisibility(View.VISIBLE);
        }

        ArrayAdapter<CharSequence> remarkAdapter = ArrayAdapter.createFromResource(activity.getApplicationContext(), R.array.remarkList, android.R.layout.simple_gallery_item);
        // Specify the layout to use when the list of choices appears
        remarkAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        // Apply the adapter to the spinner
        holder.remarkList.setAdapter(remarkAdapter);

        holder.remarkList.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener()
        {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id)
            {
                holder.remarkList.setSelection(position);
                status=position;
                spinnerPosition=position;
                if(position==0)
                {
                    abscentRemark="Absent";
                }
                else if(position==1)
                {
                    abscentRemark="On Leave";
                }else
                {
                    abscentRemark="Not Present At The Moment";
                }
                //abscentRemark=holder.remarkList.getSelectedItem().toString();
                //Toast.makeText(activity.getApplicationContext(),"Remark:"+remark,Toast.LENGTH_LONG).show();
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent)
            {

            }
        });

        holder.abscent.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                //Toast.makeText(activity.getApplicationContext(),"Abscent Marked\nRemark:"+abscentRemark,Toast.LENGTH_LONG).show();
                model=users.get(i);
                memberId=model.id;
                checkedBy= PreferenceUtils.getMemberId(activity.getApplicationContext());
                time = format.format(calendar.getTime());
                date=sdf.format(new Date());
                GPSTracker gpsTracker=new GPSTracker(activity);
                address=gpsTracker.getAddress();
                lattitude=gpsTracker.getLattitude();
                longitude=gpsTracker.getLongitude();

                final AlertDialog.Builder builder = new AlertDialog.Builder(v.getContext());
                builder.setMessage("Do you really want to mark "+ model.userName+" as "+abscentRemark+"?")
                        .setCancelable(false)
                        .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            public void onClick(final DialogInterface dialog, final int id)
                            {
                                if(status==3)
                                {
                                    model.status=1;
                                }
                                else
                                {
                                    model.status=2;
                                }
                                DataBaseHelper helper=new DataBaseHelper(activity.getApplicationContext());
                                helper.insertStatus(selectedSectorId,model.id,""+model.status);
                                users.set(i,model);
                                updateRecords(users);
                                if(cd.isConnected())
                                {
                                    if(address==null)
                                    {
                                        address="Not Available";
                                    }
                                    //Toast.makeText(getApplicationContext(),"Network NOT Avaliable",Toast.LENGTH_SHORT).show();
                                    markAttendence.onlineAtttendance(memberId,address,lattitude,longitude,abscentRemark);
                                }
                                else
                                {
                                    //Toast.makeText(getApplicationContext(),"Network Avaliable",Toast.LENGTH_SHORT).show();
                                    offlineAttendance();
                                }
                                dialog.cancel();
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
        });

        holder.call.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String call=model.mobno;
                Intent i = new Intent(Intent.ACTION_DIAL);
                i.setData(Uri.parse("tel:"+call));
                activity.startActivity(i);
            }
        });
        return view;
    }

    public void offlineAttendance()
    {
        long res=helper.insertData(selectedSectorId,memberId,address,lattitude,longitude,abscentRemark);
        if(res>0)
        {
            Toast.makeText(activity.getApplicationContext(),activity.getString(R.string.displayAttendanceMsgSuccess),Toast.LENGTH_LONG).show();
        }
        else
        {
            Toast.makeText(activity.getApplicationContext(),activity.getString(R.string.displayMsgAttendanceError),Toast.LENGTH_LONG).show();
        }
    }

    public void updateRecords(List<AttendenceTeamUserModel> users)
    {
        this.users = users;
        notifyDataSetChanged();
    }

    class ViewHolder
    {
        Spinner remarkList;
        Button abscent,call;
        TextView tvUserName,tvMobNo,tvDesignation,tvStatus;
        LinearLayout linearLayout;
        CardView cardView;
    }
}

