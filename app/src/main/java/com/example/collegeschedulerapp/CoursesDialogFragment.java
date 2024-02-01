package com.example.collegeschedulerapp;

import android.app.Dialog;
import android.app.TimePickerDialog;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.DrawableContainer;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.TimePicker;
import android.widget.Toast;
import android.widget.ToggleButton;
import com.google.android.libraries.places.api.Places;
import com.google.android.gms.common.api.Status;
import com.google.android.libraries.places.api.Places;
import com.google.android.libraries.places.api.model.Place;
import com.google.android.libraries.places.widget.AutocompleteSupportFragment;
import com.google.android.libraries.places.widget.listener.PlaceSelectionListener;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import androidx.annotation.NonNull;

import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.android.material.bottomsheet.BottomSheetDialog;

import android.content.Context;
import android.content.res.Resources;
import android.view.ViewGroup;

import androidx.annotation.Nullable;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.fragment.app.FragmentManager;


import com.example.collegeschedulerapp.R;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;

import java.util.Arrays;

public class CoursesDialogFragment extends BottomSheetDialogFragment {

    private BottomSheetDialog courseDialog;

    private Button addCourseButton, cancelCourseButton, startTimeButton, endTimeButton, locationPickerButton;
    int startHour, startMinute, endHour, endMinute;
    Context context;
    BottomSheetBehavior<View> bottomSheetBehavior;


    View rootView;

    ToggleButton tD, tL, tM, tMi, tJ, tV, tS;

    public CoursesDialogFragment(Context context) {
        this.context = context;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.course_bottom_dialog, container, false);


        Places.initialize(context, "AIzaSyAhjsp0gs7vVtpCsrc5IHLBwT-835Fczu0");

        BottomNavigationView navView = rootView.findViewById(R.id.nav_view);
        tD = (ToggleButton) rootView.findViewById(R.id.tD);
        tL = (ToggleButton) rootView.findViewById(R.id.tL);
        tM = (ToggleButton) rootView.findViewById(R.id.tM);
        tMi = (ToggleButton) rootView.findViewById(R.id.tMi);
        tJ = (ToggleButton) rootView.findViewById(R.id.tJ);
        tV = (ToggleButton) rootView.findViewById(R.id.tV);
        tS = (ToggleButton) rootView.findViewById(R.id.tS);
        String markedButtons= "";


        addCourseButton = rootView.findViewById(R.id.add_course_button);
        cancelCourseButton = rootView.findViewById(R.id.cancel_course_button);

        startTimeButton = rootView.findViewById(R.id.start_time_button);
        endTimeButton = rootView.findViewById(R.id.end_time_button);


        // Initialize the AutocompleteSupportFragment.
        AutocompleteSupportFragment autocompleteFragment = (AutocompleteSupportFragment)
                getChildFragmentManager().findFragmentById(R.id.autocomplete_fragment);

        // Specify the types of place data to return.
        assert autocompleteFragment != null;
        autocompleteFragment.setPlaceFields(Arrays.asList(Place.Field.ID, Place.Field.NAME));
        autocompleteFragment.setHint("Lecture Location");

        // Set up a PlaceSelectionListener to handle the response.
        autocompleteFragment.setOnPlaceSelectedListener(new PlaceSelectionListener() {
            @Override
            public void onPlaceSelected(@NonNull Place place) {
                // TODO: Get info about the selected place.
                Log.i("TAG", "Place: " + place.getName() + ", " + place.getId());
            }


            @Override
            public void onError(@NonNull Status status) {
                // TODO: Handle the error.
                Log.i("TAG", "An error occurred: " + status);
            }
        });








        //Event Handlers
        startTimeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TimePickerDialog(rootView, startTimeButton);
            }
        });

        endTimeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TimePickerDialog(rootView, endTimeButton);
            }
        });
        cancelCourseButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                courseDialog.dismiss();
            }
        });


        return rootView;
    }



    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        bottomSheetBehavior = BottomSheetBehavior.from((View) view.getParent());

        bottomSheetBehavior.setState(BottomSheetBehavior.STATE_EXPANDED);

    }




    public void TimePickerDialog(View v, Button button) {
        TimePickerDialog dialog = new TimePickerDialog(context, 2,new TimePickerDialog.OnTimeSetListener() {

            @Override
            public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                button.setText(String.format("%02d:%02d", hourOfDay, minute));
            }}, 12, 00, false);


        dialog.show();
    }


}
