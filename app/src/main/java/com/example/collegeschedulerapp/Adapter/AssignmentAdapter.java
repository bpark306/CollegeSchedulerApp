package com.example.collegeschedulerapp.Adapter;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.recyclerview.widget.RecyclerView;

import com.example.collegeschedulerapp.AssignmentsDialogFragment;
import com.example.collegeschedulerapp.CoursesDialogFragment;
import com.example.collegeschedulerapp.MainActivity;
import com.example.collegeschedulerapp.R;
import com.example.collegeschedulerapp.internalfiles.Assignment;

import java.util.ArrayList;

public class AssignmentAdapter extends RecyclerView.Adapter<AssignmentAdapter.MyViewHolder> {
    private ArrayList<Assignment> emplist;

    Context context;

    public AssignmentAdapter(Context context, ArrayList<Assignment> emplist) {
        this.context = context;
        this.emplist = emplist;
    }

    // This method creates a new ViewHolder object for each item in the RecyclerView
    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        // Inflate the layout for each item and return a new ViewHolder object
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.assignemnts_list, parent, false);

        return new MyViewHolder(itemView);
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
        holder.dueDateAndTime.setText("Due on " + currentEmp.getTimeDateDue());
        holder.course.setText(currentEmp.getCourseName());
    }

    // This class defines the ViewHolder object for each item in the RecyclerView
    public static class MyViewHolder extends RecyclerView.ViewHolder {
        private TextView name;
        private TextView course;
        private TextView dueDateAndTime;
        private CoursesDialogFragment coursesDialogFragment;


        public MyViewHolder(View itemView) {
            super(itemView);
            name = itemView.findViewById(R.id.assignment_name_list);
            course = itemView.findViewById(R.id.assignment_associated_course_list);
            dueDateAndTime = itemView.findViewById(R.id.due_date_and_time);
        }
    }
    public Context getContext() {
        return context;
    }

    public void deleteItem(int position) {
        emplist.remove(emplist.get(position));

        notifyItemRemoved(position);
    }



    public void editItem(int position) {

        notifyItemChanged(position);
        Toast.makeText(context, "Assignment Move to TodoList!", Toast.LENGTH_SHORT).show();

    }
}
