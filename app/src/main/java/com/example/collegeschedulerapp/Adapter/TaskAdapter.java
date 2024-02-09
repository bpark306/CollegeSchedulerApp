package com.example.collegeschedulerapp.Adapter;

import android.content.Context;
import android.content.SharedPreferences;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;

import androidx.appcompat.widget.AppCompatImageButton;
import androidx.recyclerview.widget.RecyclerView;

import com.example.collegeschedulerapp.R;
import com.example.collegeschedulerapp.internalfiles.Course;
import com.example.collegeschedulerapp.internalfiles.RecyclerViewInterfaceTask;
import com.example.collegeschedulerapp.internalfiles.Task;
import com.google.gson.Gson;

import java.util.ArrayList;


public class TaskAdapter extends RecyclerView.Adapter<TaskAdapter.MyViewHolder>{
    private final RecyclerViewInterfaceTask recyclerViewInterfaceTask;

    private ArrayList<Course> myCourses;
    private ArrayList<Task> myTasks;


    private Context context;

    public TaskAdapter(RecyclerViewInterfaceTask recyclerViewInterfaceTask, Context context, ArrayList<Course> myCourses, ArrayList<Task> myTasks) {
        this.recyclerViewInterfaceTask = recyclerViewInterfaceTask;
        this.myCourses = myCourses;
        this.myTasks = myTasks;

        this.context = context;
    }



    // This method creates a new ViewHolder object for each item in the RecyclerView
    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        // Inflate the layout for each item and return a new ViewHolder object
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.task_list, parent, false);

        return new MyViewHolder(itemView, recyclerViewInterfaceTask);
    }

    // This method returns the total
    // number of items in the data set
    @Override
    public int getItemCount() {
        return myTasks.size();
    }

    // This method binds the data to the ViewHolder object
    // for each item in the RecyclerView
    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        Task currentTask = myTasks.get(position);
        holder.name.setText(currentTask.getName());
        holder.course.setText(currentTask.getCourse().name);
        holder.dueDateAndTime.setText(currentTask.getTimeDateDueString());
        holder.checkBox.setChecked(currentTask.isCompleted());
    }


    // This class defines the ViewHolder object for each item in the RecyclerView
    public static class MyViewHolder extends RecyclerView.ViewHolder {

        private TextView name;
        private TextView course;
        private TextView dueDateAndTime;
        private AppCompatImageButton dynamicButton;

        private CheckBox checkBox;



        public MyViewHolder(View itemView, RecyclerViewInterfaceTask recyclerViewInterfaceTask) {
            super(itemView);

            name = itemView.findViewById(R.id.task_name_list);
            course = itemView.findViewById(R.id.task_associated_course_list);
            dueDateAndTime = itemView.findViewById(R.id.task_date_and_time);
            dynamicButton = itemView.findViewById(R.id.edit_task_button);
            checkBox = itemView.findViewById(R.id.checkBox);

            checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    recyclerViewInterfaceTask.updateTaskWithCheckBoxStatus(getAdapterPosition(), name.getText().toString(), course.getText().toString(), checkBox);
                }
            });
            dynamicButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (recyclerViewInterfaceTask != null) {
                        int position = getAdapterPosition();

                        if (position != RecyclerView.NO_POSITION) {
                            //recyclerViewInterfaceTask.onClick(position, name.getText().toString(), course.getText().toString(), dueDateAndTime.getText().toString());
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
        myTasks.remove(myTasks.get(position));
        notifyItemRemoved(position);
        saveData();
    }

    private void updateTaskStatus(int position, CheckBox checkBox) {
        for (int i = 0; i < myCourses.size(); i++) {
            if (myTasks.get(position).getCourse().equals(myCourses.get(i))) {
                for(int j = 0; j < myCourses.get(i).assignments.size(); j++){
                    if (myCourses.get(i).assignments.get(j).getName().equals(myTasks.get(position).getName())) {
                        myTasks.get(position).switchStatus(checkBox.isChecked());
                        saveData();
                    }
                }
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

        json = gson.toJson(myTasks);
        editor.putString("my tasks", json);
        editor.apply();
    }



    public void updateEmplist(ArrayList<Task> myTasks) {
        this.myTasks = myTasks;
    }

    public void updateFilteredList(ArrayList<Task> myTasks, boolean requestCompleted) {
        ArrayList<Task> filteredCompleted = new  ArrayList<>();
        for (int i = 0; i < myTasks.size(); i++) {
            if (myTasks.get(i).isCompleted()) {
                filteredCompleted.add(myTasks.get(i));
            }
        }

        myTasks = filteredCompleted;
    }




}
