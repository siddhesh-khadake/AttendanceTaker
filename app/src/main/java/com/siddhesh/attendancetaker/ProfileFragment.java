package com.siddhesh.attendancetaker;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

public class ProfileFragment extends Fragment {

    TextView nameText,designationText,mobileNoText,dobText,aadharNumberText,bloodgroupText,genderText,placeOfPostingText;
    String firstName,lastName,middleName,designation,mobileno,dob,aadharNumber,bloodGroup,gender,nemnuk;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState)
    {
        View view = inflater.inflate(R.layout.fragment_profile,container,false);

        firstName=PreferenceUtils.getFirstName(getActivity().getApplicationContext());
        middleName=PreferenceUtils.getMiddleName(getActivity().getApplicationContext());
        lastName=PreferenceUtils.getLastName(getActivity().getApplicationContext());
        bloodGroup=PreferenceUtils.getBloodGroup(getActivity().getApplicationContext());
        designation=PreferenceUtils.getDesignation(getActivity().getApplicationContext());
        gender=PreferenceUtils.getGender(getActivity().getApplicationContext());
        mobileno=PreferenceUtils.getMobileNo(getActivity().getApplicationContext());
        dob=PreferenceUtils.getDob(getActivity().getApplicationContext());
        nemnuk=PreferenceUtils.getNemnuk(getActivity().getApplicationContext());
        aadharNumber=PreferenceUtils.getAadharNumber(getActivity().getApplicationContext());

        nameText=view.findViewById(R.id.admin_name);
        designationText=view.findViewById(R.id.admin_designation);
        bloodgroupText=view.findViewById(R.id.bloodgroup);
        mobileNoText=view.findViewById(R.id.mobno);
        genderText=view.findViewById(R.id.gender);
        dobText=view.findViewById(R.id.dob);
        placeOfPostingText=view.findViewById(R.id.placeofposting);
        aadharNumberText=view.findViewById(R.id.adharno);

        nameText.setText(firstName+" "+middleName+" "+lastName);
        designationText.setText(designation);
        bloodgroupText.setText(bloodGroup);
        mobileNoText.setText(mobileno);
        genderText.setText(gender);
        dobText.setText(dob);
        placeOfPostingText.setText(nemnuk);
        aadharNumberText.setText(aadharNumber);

        return view;
    }
}
