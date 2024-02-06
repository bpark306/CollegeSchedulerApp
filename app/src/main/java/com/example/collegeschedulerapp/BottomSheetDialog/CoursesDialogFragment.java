package com.example.collegeschedulerapp.BottomSheetDialog;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.TimePickerDialog;
import android.content.SharedPreferences;
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

import com.example.collegeschedulerapp.internalfiles.Course;
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
import androidx.core.view.ViewCompat;
import androidx.fragment.app.FragmentManager;


import com.example.collegeschedulerapp.R;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.google.android.material.shape.MaterialShapeDrawable;
import com.google.android.material.shape.ShapeAppearanceModel;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.jetbrains.annotations.NotNull;

import java.lang.reflect.Type;
import java.sql.Time;
import java.util.ArrayList;
import java.util.Arrays;

public class CoursesDialogFragment extends BottomSheetDialogFragment {


    ArrayList<Course> myCourses;

    private Button addCourseButton, cancelCourseButton, startTimeButton, endTimeButton, locationPickerButton;
    private TextInputLayout courseName, courseSection, courseInstructor;
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
        //bottom sheet round corners can be obtained but the while background appears to remove that we need to add this.

        loadData();

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

        courseName = rootView.findViewById(R.id.course_name);
        courseSection= rootView.findViewById(R.id.course_section);
        courseInstructor = rootView.findViewById(R.id.course_instructor);


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
                dismiss();
            }
        });

        addCourseButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                confirmInput(v);

            }
        });
        return rootView;


    }

    private void loadData() {

        SharedPreferences sharedPreferences = context.getSharedPreferences("shared preferences", Context.MODE_PRIVATE);
        Gson gson = new Gson();
        String json = sharedPreferences.getString("my courses", null);
        Type type = new TypeToken<ArrayList<Course>>() {}.getType();
        myCourses = gson.fromJson(json, type);

        if (myCourses == null) {
            myCourses = new ArrayList<>();
        }

    }




    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        bottomSheetBehavior = BottomSheetBehavior.from((View) view.getParent());
        bottomSheetBehavior.setState(BottomSheetBehavior.STATE_EXPANDED);

    }

    public boolean validateTextInput(TextInputLayout tiy) {
        String text = tiy.getEditText().getText().toString().trim();

        if(text.isEmpty()) {
            tiy.setError("Field can't be empty!");
            return false;
        } else {
            tiy.setError(null);
            tiy.setErrorEnabled(false);
            return true;
        }
    }


    public void confirmInput(View v) {
        if (validateTextInput(courseName) && validateTextInput(courseInstructor)) {
            Course newCourse = new Course(courseName.getEditText().getText().toString());
            myCourses.add(newCourse);
            saveData();
            dismiss();
        }
    }

    private void saveData() {
        SharedPreferences sharedPreferences = context.getSharedPreferences("shared preferences",Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        Gson gson = new Gson();
        String json = gson.toJson(myCourses);
        editor.putString("my courses", json);
        editor.apply();
    }







    public void TimePickerDialog(View v, Button button) {
        TimePickerDialog dialog = new TimePickerDialog(context, 3,new TimePickerDialog.OnTimeSetListener() {

            @Override
            public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                if (hourOfDay > 12) {
                    button.setText(String.format("%02d:%02d PM", hourOfDay% 12, minute));
                } else {
                    button.setText(String.format("%02d:%02d AM", hourOfDay, minute));
                }
            }}, 12, 00, false);


        dialog.show();
    }


}
