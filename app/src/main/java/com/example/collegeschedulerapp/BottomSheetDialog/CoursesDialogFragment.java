package com.example.collegeschedulerapp.BottomSheetDialog;

import android.app.TimePickerDialog;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;
import android.widget.ToggleButton;

import com.example.collegeschedulerapp.internalfiles.Assignment;
import com.example.collegeschedulerapp.internalfiles.Course;
import com.example.collegeschedulerapp.internalfiles.Exam;
import com.google.android.libraries.places.api.Places;
import com.google.android.gms.common.api.Status;
import com.google.android.libraries.places.api.model.Place;
import com.google.android.libraries.places.widget.AutocompleteSupportFragment;
import com.google.android.libraries.places.widget.listener.PlaceSelectionListener;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import androidx.annotation.NonNull;

import com.google.android.material.bottomsheet.BottomSheetBehavior;

import android.content.Context;
import android.view.ViewGroup;
import android.widget.Toolbar;

import androidx.annotation.Nullable;


import com.example.collegeschedulerapp.R;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.google.android.material.textfield.TextInputLayout;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;

public class CoursesDialogFragment extends BottomSheetDialogFragment {


    ArrayList<Course> myCourses;
    Date startTimeDate, endTimeDate;
    Place selectedPlace;

    private Button addCourseButton, cancelCourseButton, startTimeButton, endTimeButton, locationPickerButton;
    private TextInputLayout courseName, courseSection, courseInstructor, courseRoom;
    Context context;
    BottomSheetBehavior<View> bottomSheetBehavior;
    String markedButtons;
    TextView dialogTitle;
    String savedName;

    View rootView;

    ToggleButton sundayButton, mondayButton, tuesdayButton, wednesdayButton, thursdayButton, fridayButton, saturdayButton;

    public CoursesDialogFragment(Context context) {
        this.context = context;
    }
    public CoursesDialogFragment(Context context, String savedName) {
        this.context = context;
        this.savedName = savedName;
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

        dialogTitle = rootView.findViewById(R.id.course_btm_dialog_title);

        BottomNavigationView navView = rootView.findViewById(R.id.nav_view);
        sundayButton = (ToggleButton) rootView.findViewById(R.id.tD);
        mondayButton = (ToggleButton) rootView.findViewById(R.id.tL);
        tuesdayButton = (ToggleButton) rootView.findViewById(R.id.tM);
        wednesdayButton = (ToggleButton) rootView.findViewById(R.id.tMi);
        thursdayButton = (ToggleButton) rootView.findViewById(R.id.tJ);
        fridayButton = (ToggleButton) rootView.findViewById(R.id.tV);
        saturdayButton = (ToggleButton) rootView.findViewById(R.id.tS);
        markedButtons= "";

        courseName = rootView.findViewById(R.id.course_name);
        courseSection= rootView.findViewById(R.id.course_section);
        courseInstructor = rootView.findViewById(R.id.course_instructor);
        courseRoom = rootView.findViewById(R.id.room_number);

        addCourseButton = rootView.findViewById(R.id.add_course_button);
        cancelCourseButton = rootView.findViewById(R.id.cancel_course_button);

        startTimeButton = rootView.findViewById(R.id.start_time_button);
        endTimeButton = rootView.findViewById(R.id.end_time_button);



        Calendar cal = Calendar.getInstance();

        startTimeDate = cal.getTime();

        startTimeDate.setMinutes(cal.get(Calendar.MINUTE));
        startTimeDate.setHours(cal.get(Calendar.HOUR));

        endTimeDate = cal.getTime();

        endTimeDate.setMinutes(cal.get(Calendar.MINUTE));
        endTimeDate.setHours(cal.get(Calendar.HOUR) + 1);



        SimpleDateFormat dateForm = new SimpleDateFormat("MM/dd/YY");
        SimpleDateFormat timeForm = new SimpleDateFormat("h:mm a");


        startTimeButton.setText(timeForm.format(startTimeDate));
        endTimeButton.setText(timeForm.format(endTimeDate));



        if (savedName != null) {
            dialogTitle.setText("EDITING " + savedName);
            addCourseButton.setText("Edit");
        }


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
                selectedPlace = place;
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
                TimePickerDialog(rootView, startTimeButton, startTimeDate);
            }
        });

        endTimeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TimePickerDialog(rootView, endTimeButton, endTimeDate);
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
        if (validateTextInput(courseName)
                && !alreadyContainsCourse(courseName)
                && validateTextInput(courseInstructor)) {

            String meetingTimes = "";
            if (sundayButton.isChecked()) {
                meetingTimes += "Sun";
            }
            if (mondayButton.isChecked()) {
                meetingTimes += "Mon";
            }
            if (tuesdayButton.isChecked()) {
                meetingTimes += "Tues";
            }
            if (wednesdayButton.isChecked()) {
                meetingTimes += "Wed";
            }
            if (thursdayButton.isChecked()) {
                meetingTimes += "Thu";
            }
            if (fridayButton.isChecked()) {
                meetingTimes += "Fri";
            }
            if (saturdayButton.isChecked()) {
                meetingTimes += "Sat";
            }
            Toast.makeText(context, meetingTimes, Toast.LENGTH_SHORT).show();

            String name = courseName.getEditText().getText().toString();
            String instructor = courseInstructor.getEditText().getText().toString();

            if (savedName != null) {
                for (Course a: myCourses) {
                    if (a.name.equals(savedName)) {

                        a.setName(name);
                        a.setInstructor(instructor);
                        a.setMeetingDays(meetingTimes);
                        a.setStartDateTime(startTimeDate);
                        a.setEndDateTime(endTimeDate);
                        if (!courseRoom.getEditText().getText().toString().isEmpty()) {
                            a.setRoom(courseRoom.getEditText().getText().toString());
                        }
                        if (!courseSection.getEditText().getText().toString().isEmpty()) {
                            a.setSection(courseSection.getEditText().getText().toString());
                        }
                        if (selectedPlace != null) {
                            a.setLocation(selectedPlace.getName());
                        }


                        for (Assignment b: a.assignments) {
                            b.setCourse(new Course(name));
                        }
                        for (Exam b: a.exams) {
                            b.setCourse(new Course(name));
                        }
                    }
                }


            } else {
                Course newCourse = new Course(courseName.getEditText().getText().toString(),
                        courseInstructor.getEditText().getText().toString(),
                        meetingTimes,
                        startTimeDate,
                        endTimeDate);

                if (selectedPlace != null) {
                    newCourse.setLocation(selectedPlace.getName());
                }
                if (!courseSection.getEditText().getText().toString().isEmpty()) {
                    newCourse.setSection(courseSection.getEditText().getText().toString());
                }
                if (!courseRoom.getEditText().getText().toString().isEmpty()) {
                    newCourse.setRoom(courseRoom.getEditText().getText().toString());
                }

                myCourses.add(newCourse);
            }
            saveData();
            dismiss();
        }
    }

    private boolean alreadyContainsCourse(TextInputLayout textInputLayout) {

        String text = textInputLayout.getEditText().getText().toString().trim();

        for (Course a: myCourses) {
            //intentionally, I want to see if these two courses refer to the same obj
            if (a.name.equalsIgnoreCase(text)) {
                textInputLayout.setError("Cannot make another course in the same!");
                return true;
            }
        }

        return false;
    }

    private void saveData() {
        SharedPreferences sharedPreferences = context.getSharedPreferences("shared preferences",Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        Gson gson = new Gson();
        String json = gson.toJson(myCourses);
        editor.putString("my courses", json);
        editor.apply();
    }







    public void TimePickerDialog(View v, Button button, Date date) {
        TimePickerDialog dialog = new TimePickerDialog(context, 3,new TimePickerDialog.OnTimeSetListener() {

            @Override
            public void onTimeSet(TimePicker view, int hourOfDay, int minute) {


                date.setHours(hourOfDay);
                date.setMinutes(minute);


                SimpleDateFormat timeForm = new SimpleDateFormat("H:mm a");
                button.setText(timeForm.format(date));

            }}, 12, 00, false);


        dialog.show();
    }


}
