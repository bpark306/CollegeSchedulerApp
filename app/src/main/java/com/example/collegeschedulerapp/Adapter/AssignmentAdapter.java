package com.example.collegeschedulerapp.Adapter;

import android.content.Context;
import android.content.SharedPreferences;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.widget.AppCompatImageButton;
import androidx.recyclerview.widget.RecyclerView;

import com.example.collegeschedulerapp.BottomSheetDialog.CoursesDialogFragment;
import com.example.collegeschedulerapp.R;
import com.example.collegeschedulerapp.internalfiles.Assignment;
import com.example.collegeschedulerapp.internalfiles.Course;
import com.example.collegeschedulerapp.internalfiles.RecyclerViewInterface;
import com.example.collegeschedulerapp.internalfiles.Task;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Collections;


public class AssignmentAdapter extends RecyclerView.Adapter<AssignmentAdapter.MyViewHolder>{
    private final RecyclerViewInterface recyclerViewInterface;
    private ArrayList<Assignment> emplist;
    private ArrayList<Course> myCourses;
    private ArrayList<Task> myTasks;

    private Context context;

    public AssignmentAdapter(RecyclerViewInterface recyclerViewInterface, Context context, ArrayList<Assignment> emplist,ArrayList<Course> myCourses,  ArrayList<Task> myTasks  ) {
        this.recyclerViewInterface = recyclerViewInterface;
        this.emplist = emplist;
        this.myCourses = myCourses;
        this.myTasks = myTasks;

        this.context = context;
    }



    // This method creates a new ViewHolder object for each item in the RecyclerView
    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        // Inflate the layout for each item and return a new ViewHolder object
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.assignemnts_list, parent, false);

        return new MyViewHolder(itemView, recyclerViewInterface);
    }

    // This method returns the total
    // number of items in the data set
    @Override
    public int getItemCount() {
        return emplist.size();
    }

    // This method binds the data to the ViewHolder object
    // for each item in the RecyclerView
    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        Assignment currentEmp = emplist.get(position);
        holder.name.setText(currentEmp.getName());
        holder.dueDateAndTime.setText(currentEmp.getTimeDateDueString());
        holder.course.setText(currentEmp.getCourse().name);
    }


    // This class defines the ViewHolder object for each item in the RecyclerView
    public static class MyViewHolder extends RecyclerView.ViewHolder {

        private TextView name;
        private TextView course;
        private TextView dueDateAndTime;
        private AppCompatImageButton dynamicButton;



        public MyViewHolder(View itemView, RecyclerViewInterface recyclerViewInterface) {
            super(itemView);
            name = itemView.findViewById(R.id.assignment_name_list);
            course = itemView.findViewById(R.id.assignment_associated_course_list);
            dueDateAndTime = itemView.findViewById(R.id.due_date_and_time);
            dynamicButton = itemView.findViewById(R.id.edit_assignment_button);

            dynamicButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (recyclerViewInterface != null) {
                        int position = getAdapterPosition();

                        if (position != RecyclerView.NO_POSITION) {
                            recyclerViewInterface.onClick(position, name.getText().toString(), course.getText().toString(), dueDateAndTime.getText().toString());
                        }
                    }
                }
            });
        }
    }
    public Context getContext() {
        return context;
    }

    public void deleteItem(int position) {
        removeAssignmentFromCourse(position);
        emplist.remove(emplist.get(position));
        notifyItemRemoved(position);
        saveData();
    }

    private void removeAssignmentFromCourse(int position) {
        for (int i = 0; i < myCourses.size(); i++) {
            if (emplist.get(position).getCourse().equals(myCourses.get(i))) {
                myCourses.get(i).assignments.remove(emplist.get(position));
                saveData();
            }
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



    public void sendToDo(int position) {
        myTasks.add(new Task(emplist.get(position)));
        notifyItemChanged(position);
        Toast.makeText(context, "Assignment Move to TodoList!", Toast.LENGTH_SHORT).show();
        saveData();
    }

    public void updateEmplist(ArrayList<Assignment> emplist) {
        this.emplist = emplist;
    }


}
