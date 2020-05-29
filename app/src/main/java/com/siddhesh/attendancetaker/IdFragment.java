package com.siddhesh.attendancetaker;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Point;
import android.os.Bundle;
import android.util.Log;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.fragment.app.Fragment;

import com.google.zxing.WriterException;

import androidmads.library.qrgenearator.QRGContents;
import androidmads.library.qrgenearator.QRGEncoder;


public class IdFragment extends Fragment {

    TextView nameText,designationText,dobText,bloodgroupText;
    String firstName,lastName,middleName,designation,mobileno,dob,aadharNumber,bloodGroup,gender;
    String TAG = "GenerateQRCode";
    ImageView qrImage;
    String inputValue;

    QRGEncoder qrgEncoder;

    LinearLayout llScroll;
    public Bitmap bitmap;

    public IdFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
       View view=inflater.inflate(R.layout.fragment_id, container, false);

        firstName=PreferenceUtils.getFirstName(getActivity().getApplicationContext());
        middleName=PreferenceUtils.getMiddleName(getActivity().getApplicationContext());
        lastName=PreferenceUtils.getLastName(getActivity().getApplicationContext());
        bloodGroup=PreferenceUtils.getBloodGroup(getActivity().getApplicationContext());
        designation=PreferenceUtils.getDesignation(getActivity().getApplicationContext());
        gender=PreferenceUtils.getGender(getActivity().getApplicationContext());
        mobileno=PreferenceUtils.getMobileNo(getActivity().getApplicationContext());
        dob=PreferenceUtils.getDob(getActivity().getApplicationContext());
        aadharNumber=PreferenceUtils.getAadharNumber(getActivity().getApplicationContext());

        qrImage = view.findViewById(R.id.QR_Image);

        dobText=view.findViewById(R.id.dob);
        llScroll=view.findViewById(R.id.llScroll);
        nameText=view.findViewById(R.id.name);
        designationText=view.findViewById(R.id.designation);
        bloodgroupText=view.findViewById(R.id.bloodgroup);
        //image = (ImageView) view.findViewById(R.id.image);

        nameText.setText(firstName+" "+middleName+" "+lastName);
        designationText.setText(designation);
        bloodgroupText.setText(bloodGroup);
        dobText.setText(dob);
        getQR();

        return view;
    }

    public void getQR()
    {
        inputValue = PreferenceUtils.getAadharNumber(getContext());
        if (inputValue.length() > 0) {
            WindowManager manager = (WindowManager) getActivity().getSystemService(Context.WINDOW_SERVICE);
            Display display = manager.getDefaultDisplay();
            Point point = new Point();
            display.getSize(point);
            int width = point.x;
            int height = point.y;
            int smallerDimension = width < height ? width : height;
            smallerDimension = smallerDimension * 3 / 4;

            qrgEncoder = new QRGEncoder(
                    inputValue, null,
                    QRGContents.Type.TEXT,
                    smallerDimension);
            try {
                bitmap = qrgEncoder.encodeAsBitmap();
                qrImage.setImageBitmap(bitmap);
            } catch (WriterException e) {
                Log.v(TAG, e.toString());
            }
        } else {
            Toast.makeText(getContext(), "No Required", Toast.LENGTH_SHORT).show();

        }
    }

}
