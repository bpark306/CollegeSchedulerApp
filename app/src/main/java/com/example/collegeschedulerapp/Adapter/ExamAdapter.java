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
import com.example.collegeschedulerapp.internalfiles.Exam;
import com.example.collegeschedulerapp.internalfiles.RecyclerViewInterface;
import com.example.collegeschedulerapp.internalfiles.RecyclerViewInterfaceExam;
import com.example.collegeschedulerapp.internalfiles.Task;
import com.google.gson.Gson;

import org.w3c.dom.Text;

import java.util.ArrayList;


public class ExamAdapter extends RecyclerView.Adapter<ExamAdapter.MyViewHolder>{
    private final RecyclerViewInterfaceExam recyclerViewInterfaceExam;
    private ArrayList<Exam> myExams;
    private ArrayList<Course> myCourses;
    private ArrayList<Task> myTasks;

    private Context context;

    public ExamAdapter(RecyclerViewInterfaceExam recyclerViewInterfaceExam, Context context, ArrayList<Exam> myExams, ArrayList<Course> myCourses, ArrayList<Task> myTasks  ) {
        this.recyclerViewInterfaceExam = recyclerViewInterfaceExam;
        this.myExams = myExams;
        this.myCourses = myCourses;
        this.myTasks = myTasks;

        this.context = context;
    }



    // This method creates a new ViewHolder object for each item in the RecyclerView
    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        // Inflate the layout for each item and return a new ViewHolder object
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.exams_list, parent, false);

        return new MyViewHolder(itemView, recyclerViewInterfaceExam);
    }

    // This method returns the total
    // number of items in the data set
    @Override
    public int getItemCount() {
        return myExams.size();
    }

    // This method binds the data to the ViewHolder object
    // for each item in the RecyclerView
    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        Exam currentExam = myExams.get(position);
        holder.name.setText(currentExam.getName());
        holder.dueDateAndTime.setText(currentExam.getTimeDateExamString());
        holder.course.setText(currentExam.getCourse().name);
        holder.examLocation.setText(currentExam.getLocation());
    }


    // This class defines the ViewHolder object for each item in the RecyclerView
    public static class MyViewHolder extends RecyclerView.ViewHolder {

        private TextView name;
        private TextView course;
        private TextView dueDateAndTime;
        private TextView examLocation;
        private AppCompatImageButton dynamicButton;



        public MyViewHolder(View itemView, RecyclerViewInterfaceExam recyclerViewInterfaceExam) {
            super(itemView);
            name = itemView.findViewById(R.id.exam_name_list);
            course = itemView.findViewById(R.id.exam_associated_course_list);
            dueDateAndTime = itemView.findViewById(R.id.exam_date_and_time);
            dynamicButton = itemView.findViewById(R.id.edit_exam_button);

            examLocation = itemView.findViewById(R.id.exam_location);
            dynamicButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (recyclerViewInterfaceExam != null) {
                        int position = getAdapterPosition();

                        if (position != RecyclerView.NO_POSITION) {
                            recyclerViewInterfaceExam.onClick(position, name.getText().toString(), course.getText().toString(), dueDateAndTime.getText().toString(), examLocation.getText().toString());
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
        removeExamFromCourse(position);
        myExams.remove(myExams.get(position));
        notifyItemRemoved(position);
        saveData();
    }

    private void removeExamFromCourse(int position) {
        for (int i = 0; i < myCourses.size(); i++) {
            if (myExams.get(position).getCourse().equals(myCourses.get(i))) {
                myCourses.get(i).exams.remove(myExams.get(position));
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
        myTasks.add(new Task(myExams.get(position)));
        notifyItemChanged(position);
        Toast.makeText(context, "Exam Moved to TodoList!", Toast.LENGTH_SHORT).show();
        saveData();
    }

    public void updatemMyExams(ArrayList<Exam> myExams) {
        this.myExams = myExams;
    }


}
