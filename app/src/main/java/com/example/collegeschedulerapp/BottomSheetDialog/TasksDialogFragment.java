package com.example.collegeschedulerapp.BottomSheetDialog;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.SharedPreferences;
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

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.collegeschedulerapp.R;
import com.example.collegeschedulerapp.internalfiles.Assignment;
import com.example.collegeschedulerapp.internalfiles.Course;
import com.example.collegeschedulerapp.internalfiles.Task;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.google.android.material.textfield.TextInputLayout;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;


public class TasksDialogFragment extends BottomSheetDialogFragment {
    ArrayList<Task> myTasks;
    ArrayList<Course> myCourses;
    Date dueTimeDate;

    View rootView;
    private TextInputLayout taskName, taskCourse;

    Context context;
    Course selectedCourse;
    TextView taskTitle;
    Button dueTimeButton, dueDateButton, cancelNewTask, addNewTask;

    AutoCompleteTextView autoCompleteTextView;
    ArrayAdapter<Course> adapaterItem;
    String savedTaskName, savedTaskCourse, dueDateAndTime;


    public TasksDialogFragment(Context context) {
        this.context = context;
    }

    public TasksDialogFragment(Context context, String savedTaskName, String savedTaskCourse, String dueDateAndTime) {
        this.context = context;
        this.savedTaskName = savedTaskName;
        this.savedTaskCourse = savedTaskCourse;
        this.dueDateAndTime = dueDateAndTime;
    }
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        rootView = inflater.inflate(R.layout.task_bottom_dialog, container, false);
        //bottom sheet round corners can be obtained but the while background appears to remove that we need to add this.
        loadData();

        Calendar cal = Calendar.getInstance();

        dueTimeDate = cal.getTime();

        dueTimeDate.setYear(cal.get(Calendar.YEAR) + 1900);
        dueTimeDate.setMonth(cal.get(Calendar.DAY_OF_MONTH));
        dueTimeDate.setDate(cal.get(Calendar.DAY_OF_MONTH));

        SimpleDateFormat dateForm = new SimpleDateFormat("MM/dd/YY");
        SimpleDateFormat timeForm = new SimpleDateFormat("h:mm a");




        dueTimeButton = rootView.findViewById(R.id.due_time_button_task);
        dueDateButton = rootView.findViewById(R.id.due_date_button_task);


        dueDateButton.setText(dateForm.format(dueTimeDate));
        dueTimeButton.setText(timeForm.format(dueTimeDate));



        cancelNewTask = rootView.findViewById(R.id.cancel_task_button);
        addNewTask = rootView.findViewById(R.id.add_task_button);


        taskName = rootView.findViewById(R.id.task_name);
        taskCourse = rootView.findViewById(R.id.assocciated_course_task);
        taskTitle = rootView.findViewById(R.id.task_title);



        if (savedTaskName != null || dueDateAndTime != null || savedTaskCourse != null) {
            addNewTask.setText("Edit");
            taskTitle.setText("EDITING " + savedTaskName);
        }



        autoCompleteTextView = rootView.findViewById(R.id.autocomplete_course_task);

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



        cancelNewTask.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });

        addNewTask.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                confirmInput(v);
            }
        });
        return rootView;
    }



    public void confirmInput(View v) {
        if (validateTextInput(taskName)
                && validateTextInput(taskCourse)
                && !alreadyContainsName(selectedCourse, taskName)) {

            if (savedTaskName != null || savedTaskCourse != null || dueDateAndTime != null) {
                //iterate thru ArrayList<Courses>
                for (int i = 0; i < myTasks.size(); i++) {

                    //Found specific assignment with matching name
                    if (myTasks.get(i).getName().equals(savedTaskName) && myTasks.get(i).getCourse().equals(savedTaskCourse)) {
                        myTasks.remove(i);
                    }
                }
            }

            myTasks.add(new Task(taskName.getEditText().getText().toString(),
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
        String json = gson.toJson(myTasks);
        editor.putString("my tasks", json);
        editor.apply();

        json = gson.toJson(myCourses);
        editor.putString("my courses", json);
        editor.apply();
    }


    private void loadData() {

        SharedPreferences sharedPreferences = getContext().getSharedPreferences("shared preferences", Context.MODE_PRIVATE);
        Gson gson = new Gson();
        String json = sharedPreferences.getString("my tasks", null);
        Type type = new TypeToken<ArrayList<Task>>() {}.getType();
        myTasks = gson.fromJson(json, type);

        if (myTasks == null) {
            myTasks = new ArrayList<>();

        }
        json = sharedPreferences.getString("my courses", null);
        type = new TypeToken<ArrayList<Course>>() {}.getType();
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

        for (Task task: myTasks) {
            //intentionally, I want to see if these two courses refer to the same obj
            if (task.getName().equals(text)) {
                return true;
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


                dueTimeDate.setYear(year);
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
