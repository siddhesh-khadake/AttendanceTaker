package com.siddhesh.attendancetaker;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.List;

public class MyAttendanceAdapter extends BaseAdapter {

    Activity activity;
    private List<MyAttendanceItemData> itemList;
    LayoutInflater inflater;
    MyAttendanceItemData model;

    public MyAttendanceAdapter(Activity activity, List<MyAttendanceItemData> itemList )
    {
        this.activity = activity;
        this.itemList = itemList;
        inflater = activity.getLayoutInflater();
    }

    public int getCount() {
        return itemList.size();
    }

    public Object getItem(int position) {
        return itemList.get(position);
    }

    public long getItemId(int position) {
        return position;
    }

    public View getView(int i, View view, ViewGroup viewGroup)
    {
        ViewHolder holder = null;

        if (view == null)
        {
            view = inflater.inflate(R.layout.my_attendance_list_item, viewGroup, false);

            holder = new ViewHolder();

            holder.tvStatus = (TextView)view.findViewById(R.id.status);
            holder.tvDateTime = (TextView) view.findViewById(R.id.datetime);
            holder.tvLocation = view.findViewById(R.id.location);
            holder.tvCheckedBy = view.findViewById(R.id.checkedby);
            holder.ivImageStatus = view.findViewById(R.id.imagestatus);
            view.setTag(holder);
        }
        else
            holder = (ViewHolder)view.getTag();

        model = itemList.get(i);

        holder.tvStatus.setText(model.status);
        holder.tvDateTime.setText(model.datetime);
        holder.tvLocation.setText(model.location);
        holder.tvCheckedBy.setText(model.checkedby);

        if(model.status.equals("Present"))
        {
            holder.ivImageStatus.setBackgroundResource(R.drawable.background_green);
        }
        else
        {
            holder.ivImageStatus.setBackgroundResource(R.drawable.background_red);
        }
        return view;
    }

    class ViewHolder
    {
        TextView tvStatus,tvDateTime,tvLocation,tvCheckedBy;
        ImageView ivImageStatus;
    }
}