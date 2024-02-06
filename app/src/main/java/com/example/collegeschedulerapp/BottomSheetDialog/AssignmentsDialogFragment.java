package com.example.collegeschedulerapp.BottomSheetDialog;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
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

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.collegeschedulerapp.R;
import com.example.collegeschedulerapp.internalfiles.Assignment;
import com.example.collegeschedulerapp.internalfiles.Course;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.google.android.material.textfield.TextInputLayout;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;


public class AssignmentsDialogFragment extends BottomSheetDialogFragment {
    ArrayList<Course> myCourses;
    Date dueTimeDate;

    View rootView;
    private TextInputLayout assignmentName, assignmentCourse;

    Context context;
    Course selectedCourse;
    TextView assignmentTitle;
    Button dueTimeButton, dueDateButton, cancelNewAssignment, addNewAssignment;

    AutoCompleteTextView autoCompleteTextView;
    ArrayAdapter<Course> adapaterItem;
    String savedAssignmentName, savedAssignmentCourse, dueDateAndTime;


    public AssignmentsDialogFragment(Context context) {
        this.context = context;
    }

    public AssignmentsDialogFragment(Context context, String savedAssignmentName, String savedAssignmentCourse, String dueDateAndTime) {
        this.context = context;
        this.savedAssignmentName = savedAssignmentName;
        this.savedAssignmentCourse = savedAssignmentCourse;
        this.dueDateAndTime = dueDateAndTime;
    }
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        rootView = inflater.inflate(R.layout.assignment_bottom_dialog, container, false);
        //bottom sheet round corners can be obtained but the while background appears to remove that we need to add this.
        loadData();

        Calendar cal = Calendar.getInstance();

        dueTimeDate = cal.getTime();

        dueTimeDate.setYear(cal.get(Calendar.YEAR) + 1900);
        dueTimeDate.setMonth(cal.get(Calendar.DAY_OF_MONTH));
        dueTimeDate.setDate(cal.get(Calendar.DAY_OF_MONTH));

        SimpleDateFormat dateForm = new SimpleDateFormat("MM/dd/YY");
        SimpleDateFormat timeForm = new SimpleDateFormat("h:mm a");




        dueTimeButton = rootView.findViewById(R.id.due_time_button);
        dueDateButton = rootView.findViewById(R.id.due_date_button);


        dueDateButton.setText(dateForm.format(dueTimeDate));
        dueTimeButton.setText(timeForm.format(dueTimeDate));



        cancelNewAssignment = rootView.findViewById(R.id.cancel_assignment_button);
        addNewAssignment = rootView.findViewById(R.id.add_assignment_button);


        assignmentName = rootView.findViewById(R.id.assignment_name);
        assignmentCourse = rootView.findViewById(R.id.assocciated_course);
        assignmentTitle = rootView.findViewById(R.id.assignment_title);



        if (savedAssignmentName != null || dueDateAndTime != null || savedAssignmentCourse != null) {
            addNewAssignment.setText("Edit");
            assignmentTitle.setText("EDITING " + savedAssignmentName);
        }



        autoCompleteTextView = rootView.findViewById(R.id.autocomplete_course);

        adapaterItem = new ArrayAdapter<Course>(context, R.layout.list_item, myCourses);

        autoCompleteTextView.setAdapter(adapaterItem);

        autoCompleteTextView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                selectedCourse = (Course) parent.getItemAtPosition(position);
            }
        });


        dueTimeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TimePickerDialog(rootView, dueTimeButton, dueTimeDate);
            }
        });

        dueDateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DatePickerDialog(v, dueDateButton, dueTimeDate);
            }
        });



        cancelNewAssignment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });

        addNewAssignment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                confirmInput(v);
            }
        });
        return rootView;
    }



    public void confirmInput(View v) {
        if (validateTextInput(assignmentName)
                && validateTextInput(assignmentCourse)
                && !alreadyContainsName(selectedCourse, assignmentName)) {

            if (savedAssignmentName != null || savedAssignmentCourse != null || dueDateAndTime != null) {
                //iterate thru ArrayList<Courses>
                for (int i = 0; i < myCourses.size(); i++) {

                    //Found specific course with matching name
                    if (myCourses.get(i).name.equals(savedAssignmentCourse)) {

                        //Iterate thru ArrayList<Assignment
                        for(int j = 0; j < myCourses.get(i).assignments.size(); j++) {

                            //Found specific assignment with matching name
                            if (myCourses.get(i).assignments.get(j).getName().equals(savedAssignmentName)) {
                                myCourses.get(i).assignments.remove(j);

                            }
                        }
                    }
                }
            }

            selectedCourse.assignments.add(new Assignment(assignmentName.getEditText().getText().toString(),
                    dueTimeDate,
                    false,
                    selectedCourse));

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
                for (Assignment b: a.assignments) {
                    if (b.getName().equalsIgnoreCase(text)) {
                        textInputLayout.setError("Cannot be the same name as an assignment in the same course!");
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
