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

import com.example.collegeschedulerapp.R;
import com.example.collegeschedulerapp.internalfiles.Assignment;
import com.example.collegeschedulerapp.internalfiles.Course;
import com.example.collegeschedulerapp.internalfiles.RecyclerViewInterface;
import com.example.collegeschedulerapp.internalfiles.Task;
import com.google.android.material.internal.ClippableRoundedCornerLayout;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;


public class CourseAdapter extends RecyclerView.Adapter<CourseAdapter.MyViewHolder>{
    private final RecyclerViewInterface recyclerViewInterface;
    private ArrayList<Course> myCourses;
    private ArrayList<Course> filteredCourses;

    private Context context;

    public CourseAdapter(RecyclerViewInterface recyclerViewInterface, Context context, ArrayList<Course> myCourses, String filterDay) {
        this.recyclerViewInterface = recyclerViewInterface;
        this.context = context;
        this.filteredCourses = new ArrayList<>();

        this.myCourses = myCourses;

        for (int i = 0; i < myCourses.size();i++) {
            if (myCourses.get(i).getMeetingDays().contains(filterDay)) {
                this.filteredCourses.add(myCourses.get(i));
            }
        }
    }


    // This method creates a new ViewHolder object for each item in the RecyclerView
    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        // Inflate the layout for each item and return a new ViewHolder object
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.course_list, parent, false);

        return new MyViewHolder(itemView, recyclerViewInterface);
    }

    // This method returns the total
    // number of items in the data set
    @Override
    public int getItemCount() {
        return filteredCourses.size();
    }

    // This method binds the data to the ViewHolder object
    // for each item in the RecyclerView
    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        Course current = filteredCourses.get(position);
        holder.instructor.setText("Professor " + current.instructor);
        holder.courseName.setText(current.name);
        String formatedMeetingDays = "";
        if (current.getMeetingDays().contains("Sun")) {
            formatedMeetingDays +="Sun";
        }
        if (current.getMeetingDays().contains("Mon")) {
            formatedMeetingDays +="Mon";
        }
        if (current.getMeetingDays().contains("Tue")) {
            formatedMeetingDays +="Tue";
        }
        if (current.getMeetingDays().contains("Wed")) {
            formatedMeetingDays +="Wed";
        }
        if (current.getMeetingDays().contains("Thu")) {
            formatedMeetingDays +="Thu";
        }
        if (current.getMeetingDays().contains("Fri")) {
            formatedMeetingDays +="Fri";
        }
        if (current.getMeetingDays().contains("Sat")) {
            formatedMeetingDays +="Sat";
        }

        StringBuilder stringBuilder = new StringBuilder((formatedMeetingDays));
        int length = stringBuilder.length();
        for (int  i = 3; i < length; i+=4) {
            stringBuilder.insert(i,"-");
            length++;
        }

        holder.meetingTime.setText(stringBuilder.toString() +  " | "  + current.getTimeDateDueString());



        if (current.getLocation().equals("N/A")) {
            holder.meetingLocation.setText("Online");
        } else if (current.room == null) {

            holder.meetingLocation.setText("At " + current.getLocation());
        } else {
            holder.meetingLocation.setText("At " + current.getLocation() + " in Room " + current.room);
        }

    }


    // This class defines the ViewHolder object for each item in the RecyclerView
    public static class MyViewHolder extends RecyclerView.ViewHolder {

        private TextView instructor;
        private TextView courseName;
        private TextView meetingTime;
        private TextView meetingLocation;
        private AppCompatImageButton dynamicButton;



        public MyViewHolder(View itemView, RecyclerViewInterface recyclerViewInterface) {
            super(itemView);
            instructor = itemView.findViewById(R.id.course_instructor);
            courseName = itemView.findViewById(R.id.course_name_list);
            meetingTime = itemView.findViewById(R.id.course_meeting_time);
            meetingLocation = itemView.findViewById(R.id.meeting_location);
            dynamicButton = itemView.findViewById(R.id.edit_course_button);

            dynamicButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (recyclerViewInterface != null) {
                        int position = getAdapterPosition();

                        if (position != RecyclerView.NO_POSITION) {
                            recyclerViewInterface.onClick(position, courseName.getText().toString(), "", "");
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
        myCourses.remove(position);
        notifyItemRemoved(position);
        saveData();
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

        //Get Courses
        String json = sharedPreferences.getString("my courses", null);
        Type type = new TypeToken<ArrayList<Course>>() {}.getType();
        myCourses = gson.fromJson(json, type);

        if (myCourses == null) {
            myCourses = new ArrayList<>();
        }
    }



    public void updateFilteredCourse (ArrayList<Course> myCourses, String filterDay) {
        this.myCourses = myCourses;
        filteredCourses = new ArrayList<>();


        for (int i = 0; i < myCourses.size();i++) {
            if (myCourses.get(i).getMeetingDays().contains(filterDay)) {
                filteredCourses.add(myCourses.get(i));
                Toast.makeText(getContext(),filterDay + " is in " + myCourses.get(i).getMeetingDays() + ": "+ myCourses.get(i).getMeetingDays().contains(filterDay), Toast.LENGTH_SHORT).show();
            }
        }
    }


}
