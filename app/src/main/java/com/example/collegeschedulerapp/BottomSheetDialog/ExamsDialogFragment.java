package com.example.collegeschedulerapp.BottomSheetDialog;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;
import android.widget.Toolbar;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.collegeschedulerapp.R;
import com.example.collegeschedulerapp.internalfiles.Assignment;
import com.example.collegeschedulerapp.internalfiles.Course;
import com.example.collegeschedulerapp.internalfiles.Exam;
import com.google.android.gms.common.api.Status;
import com.google.android.libraries.places.api.Places;
import com.google.android.libraries.places.api.model.AutocompletePrediction;
import com.google.android.libraries.places.api.model.Place;
import com.google.android.libraries.places.widget.AutocompleteSupportFragment;
import com.google.android.libraries.places.widget.listener.PlaceSelectionListener;
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


public class ExamsDialogFragment extends BottomSheetDialogFragment {
    ArrayList<Course> myCourses;
    Date examTimeDate;
    View rootView;
    private TextInputLayout examName, examCourse;
    Context context;
    Course selectedCourse;
    Place selectedPlace;
    TextView examTitle;
    Button examTimeButton, examDateButton, cancelNewExam, addNewExam;

    AutoCompleteTextView autoCompleteTextView;
    ArrayAdapter<Course> adapaterItem;
    String savedExamName, savedExamCourse, examDateAndTime;



    public ExamsDialogFragment(Context context) {
        this.context = context;
    }

    public ExamsDialogFragment(Context context, String savedAssignmentName, String savedAssignmentCourse, String dueDateAndTime) {
        this.context = context;
        this.savedExamName = savedAssignmentName;
        this.savedExamCourse = savedAssignmentCourse;
        this.examDateAndTime = dueDateAndTime;
    }
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        rootView = inflater.inflate(R.layout.exam_bottom_dialog, container, false);
        //bottom sheet round corners can be obtained but the while background appears to remove that we need to add this.
        loadData();

        Calendar cal = Calendar.getInstance();

        Places.initialize(context, "AIzaSyAhjsp0gs7vVtpCsrc5IHLBwT-835Fczu0");

        // Initialize the AutocompleteSupportFragment.
        // Initialize the AutocompleteSupportFragment.
        AutocompleteSupportFragment autocompleteFragment = (AutocompleteSupportFragment)
                getChildFragmentManager().findFragmentById(R.id.autocomplete_fragment_exam);

        // Specify the types of place data to return.
        assert autocompleteFragment != null;
        autocompleteFragment.setPlaceFields(Arrays.asList(Place.Field.ID, Place.Field.NAME));
        autocompleteFragment.setHint("Exam Location");

        // Set up a PlaceSelectionListener to handle the response.
        autocompleteFragment.setOnPlaceSelectedListener(new PlaceSelectionListener() {
            @Override
            public void onPlaceSelected(@NonNull Place place) {
                if (place != null) {
                    selectedPlace = place;
                    Toast.makeText(getContext(), selectedPlace.getName(), Toast.LENGTH_SHORT).show();
                }

                // TODO: Get info about the selected place.
                Log.i("TAG", "Place: " + place.getName() + ", " + place.getId());
            }


            @Override
            public void onError(@NonNull Status status) {
                // TODO: Handle the error.
                Log.i("TAG", "An error occurred: " + status);
            }
        });

        // Initialize the AutocompleteSupportFragment.




        examTimeDate = cal.getTime();

        examTimeDate.setYear(cal.get(Calendar.YEAR) + 1900);
        examTimeDate.setMonth(cal.get(Calendar.DAY_OF_MONTH));
        examTimeDate.setDate(cal.get(Calendar.DAY_OF_MONTH));

        SimpleDateFormat dateForm = new SimpleDateFormat("MM/dd/YY");
        SimpleDateFormat timeForm = new SimpleDateFormat("h:mm a");




        examTimeButton = rootView.findViewById(R.id.exam_time_button);
        examDateButton = rootView.findViewById(R.id.exam_date_button);

        examDateButton.setText(dateForm.format(examTimeDate));
        examTimeButton.setText(timeForm.format(examTimeDate));

        cancelNewExam = rootView.findViewById(R.id.cancel_exam_button);
        addNewExam = rootView.findViewById(R.id.add_exam_button);


        examName = rootView.findViewById(R.id.exam_name);
        examCourse = rootView.findViewById(R.id.assocciated_course_exam);
        examTitle = rootView.findViewById(R.id.exam_title);



        if (savedExamName != null || examDateAndTime != null || savedExamCourse != null) {
            addNewExam.setText("Edit");
            examTitle.setText("EDITING " + savedExamName);
        }



        autoCompleteTextView = rootView.findViewById(R.id.autocomplete_course_exam);

        adapaterItem = new ArrayAdapter<Course>(context, R.layout.list_item, myCourses);

        autoCompleteTextView.setAdapter(adapaterItem);

        autoCompleteTextView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                selectedCourse = (Course) parent.getItemAtPosition(position);
            }
        });




        examTimeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TimePickerDialog(rootView, examTimeButton, examTimeDate);
            }
        });

        examDateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DatePickerDialog(v, examDateButton, examTimeDate);
            }
        });



        cancelNewExam.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });

        addNewExam.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                confirmInput(v);
            }
        });
        return rootView;
    }



    public void confirmInput(View v) {
        if (validateTextInput(examName)
                && validateTextInput(examCourse)
                && !alreadyContainsName(selectedCourse, examName)) {

            if (savedExamName != null || savedExamCourse != null || examDateAndTime != null) {
                //iterate thru ArrayList<Courses>
                for (int i = 0; i < myCourses.size(); i++) {

                    //Found specific course with matching name
                    if (myCourses.get(i).name.equals(savedExamCourse)) {

                        //Iterate thru ArrayList<Assignment
                        for(int j = 0; j < myCourses.get(i).exams.size(); j++) {

                            //Found specific assignment with matching name
                            if (myCourses.get(i).exams.get(j).getName().equals(savedExamName)) {
                                myCourses.get(i).exams.remove(j);

                            }
                        }
                    }
                }
            }

            selectedCourse.exams.add(new Exam(examName.getEditText().getText().toString(),
                    examTimeDate,
                    selectedCourse,
                    (selectedPlace == null) ? null : selectedPlace.getName()));

            saveData();
            dismiss();
        }
    }
    private void saveData() {
        SharedPreferences sharedPreferences = getContext().getSharedPreferences("shared preferences",Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        Gson gson = new Gson();
        String json = gson.toJson(myCourses);
        editor.putString("my courses", json);
        editor.apply();
    }


    private void loadData() {

        SharedPreferences sharedPreferences = getContext().getSharedPreferences("shared preferences", Context.MODE_PRIVATE);
        Gson gson = new Gson();
        String json = sharedPreferences.getString("my courses", null);
        Type type = new TypeToken<ArrayList<Course>>() {}.getType();
        myCourses = gson.fromJson(json, type);

        if (myCourses == null) {
            myCourses = new ArrayList<>();
        }
    }

    public boolean validateTextInput(TextInputLayout tiy) {
        String text = tiy.getEditText().getText().toString().trim();

        if(text.isEmpty()) {
            tiy.setError("Name field can't be empty!");
            return false;
        } else {
            tiy.setError(null);
            tiy.setErrorEnabled(false);
            return true;
        }
    }


    private boolean alreadyContainsName(Course course, TextInputLayout textInputLayout) {

        String text = textInputLayout.getEditText().getText().toString().trim();

        for (Course a: myCourses) {
            //intentionally, I want to see if these two courses refer to the same obj
            if (a == course) {
                for (Exam b: a.exams) {
                    if (b.getName().equalsIgnoreCase(text)) {
                        textInputLayout.setError("Cannot be the same name as an exam in the same course!");
                        return true;
                    }
                }
            }
        }

        return false;
    }




    public void TimePickerDialog(View v, Button button, Date dueTimeDate) {
        TimePickerDialog dialog = new TimePickerDialog(context, 3,new TimePickerDialog.OnTimeSetListener() {

            @Override
            public void onTimeSet(TimePicker view, int hourOfDay, int minute) {


                dueTimeDate.setHours(hourOfDay);
                dueTimeDate.setMinutes(minute);


                SimpleDateFormat timeForm = new SimpleDateFormat("H:mm a");
                button.setText(timeForm.format(dueTimeDate));

            }}, 23, 59, false);


        dialog.show();
    }

    public void DatePickerDialog(View v, Button button, Date dueTimeDate) {

        Calendar cal = Calendar.getInstance();
        DatePickerDialog dialog = new DatePickerDialog(context, new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {


                dueTimeDate.setYear(cal.get(Calendar.YEAR));
                dueTimeDate.setMonth(month);
                dueTimeDate.setDate(dayOfMonth);


                SimpleDateFormat dateForm = new SimpleDateFormat("MM/dd/YY");

                String date = dateForm.format(dueTimeDate);
                button.setText(date);


            }
        }, cal.get(Calendar.YEAR),cal.get(Calendar.MONTH), cal.get(Calendar.DAY_OF_MONTH));

        dialog.show();
    }

}
