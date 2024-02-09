package com.example.collegeschedulerapp.ui.courses;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;
import android.widget.ToggleButton;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.collegeschedulerapp.Adapter.CourseAdapter;
import com.example.collegeschedulerapp.Adapter.ExamAdapter;
import com.example.collegeschedulerapp.BottomSheetDialog.AssignmentsDialogFragment;
import com.example.collegeschedulerapp.BottomSheetDialog.CoursesDialogFragment;
import com.example.collegeschedulerapp.R;
import com.example.collegeschedulerapp.RecyclerItemTouchHelper;
import com.example.collegeschedulerapp.databinding.FragmentCoursesBinding;
import com.example.collegeschedulerapp.internalfiles.Course;
import com.example.collegeschedulerapp.internalfiles.RecyclerViewInterface;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;


/// FRAGMENT FOR CALENDER WEEK VIEW
public class CoursesFragment extends Fragment implements RecyclerViewInterface {

    private FragmentCoursesBinding binding;
    private ArrayList<Course> myCourses;
    RecyclerView recyclerView;
    ItemTouchHelper itemTouchHelper;
    CourseAdapter itemAdapter;

    ToggleButton sundayButton, mondayButton, tuesdayButton, wednesdayButton, thursdayButton, fridayButton, saturdayButton;
    String markedButtons;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        CoursesViewModel coursesViewModel =
                new ViewModelProvider(this).get(CoursesViewModel.class);

        binding = FragmentCoursesBinding.inflate(inflater, container, false);

        View root = binding.getRoot();

        return root;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        loadData();
        itemAdapter = new CourseAdapter(this, getContext(), myCourses, "");
        BottomNavigationView navView = binding.getRoot().findViewById(R.id.nav_view);
        sundayButton = binding.getRoot().findViewById(R.id.course_sun);
        mondayButton = binding.getRoot().findViewById(R.id.course_mon);
        tuesdayButton = binding.getRoot().findViewById(R.id.course_tue);
        wednesdayButton = binding.getRoot().findViewById(R.id.course_wed);
        thursdayButton = binding.getRoot().findViewById(R.id.course_thu);
        fridayButton = binding.getRoot().findViewById(R.id.course_fri);
        saturdayButton = binding.getRoot().findViewById(R.id.course_sat);




        // Set the LayoutManager that
        // this RecyclerView will use.
        recyclerView = view.findViewById(R.id.list_courses);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        itemTouchHelper = new ItemTouchHelper(new RecyclerItemTouchHelper(itemAdapter));
        itemTouchHelper.attachToRecyclerView(recyclerView);
        recyclerView.setAdapter(itemAdapter);

        sundayButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loadData();
                itemAdapter.updateFilteredCourse(myCourses,
                        sundayButton.isChecked() ? "Sun" : "");

                recyclerView.setAdapter(itemAdapter);

                mondayButton.setChecked(false);
                tuesdayButton.setChecked(false);
                wednesdayButton.setChecked(false);
                thursdayButton.setChecked(false);
                fridayButton .setChecked(false);
                saturdayButton.setChecked(false);
            }
        });

        mondayButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loadData();
                itemAdapter.updateFilteredCourse(myCourses,
                        mondayButton.isChecked() ? "Mon" : "");
                recyclerView.setAdapter(itemAdapter);
                sundayButton.setChecked(false);
                tuesdayButton.setChecked(false);
                wednesdayButton.setChecked(false);
                thursdayButton.setChecked(false);
                fridayButton .setChecked(false);
                saturdayButton.setChecked(false);
            }
        });


        tuesdayButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loadData();
                itemAdapter.updateFilteredCourse(myCourses,
                        tuesdayButton.isChecked() ? "Tue" : "");
                recyclerView.setAdapter(itemAdapter);
                sundayButton.setChecked(false);
                mondayButton.setChecked(false);
                wednesdayButton.setChecked(false);
                thursdayButton.setChecked(false);
                fridayButton .setChecked(false);
                saturdayButton.setChecked(false);
            }
        });
        wednesdayButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loadData();
                itemAdapter.updateFilteredCourse(myCourses,
                        wednesdayButton.isChecked() ? "Wed" : "");
                recyclerView.setAdapter(itemAdapter);
                sundayButton.setChecked(false);
                mondayButton.setChecked(false);
                tuesdayButton.setChecked(false);
                thursdayButton.setChecked(false);
                fridayButton .setChecked(false);
                saturdayButton.setChecked(false);
            }
        });
        thursdayButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loadData();
                itemAdapter.updateFilteredCourse(myCourses,
                        thursdayButton.isChecked() ? "Thu" : "");
                sundayButton.setChecked(false);
                mondayButton.setChecked(false);
                tuesdayButton.setChecked(false);
                wednesdayButton.setChecked(false);
                fridayButton .setChecked(false);
                saturdayButton.setChecked(false);
            }
        });
        fridayButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                loadData();
                itemAdapter.updateFilteredCourse(myCourses,
                        fridayButton.isChecked() ? "Fri" : "");
                fridayButton.setChecked(true);
                sundayButton.setChecked(false);
                mondayButton.setChecked(false);
                tuesdayButton.setChecked(false);
                wednesdayButton.setChecked(false);
                thursdayButton.setChecked(false);
                saturdayButton.setChecked(false);
            }
        });
        saturdayButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loadData();
                itemAdapter.updateFilteredCourse(myCourses,
                        saturdayButton.isChecked() ? "Sat" : "");
                sundayButton.setChecked(false);
                mondayButton.setChecked(false);
                tuesdayButton.setChecked(false);
                wednesdayButton.setChecked(false);
                thursdayButton .setChecked(false);
                fridayButton.setChecked(false);
            }
        });









    }
    private void saveData() {
        SharedPreferences sharedPreferences = getContext().getSharedPreferences("shared preferences", Context.MODE_PRIVATE);
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

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

    @Override
    public void onClick(int position, String name, String course, String dueDateAndTime) {
        CoursesDialogFragment coursesDialogFragment = new CoursesDialogFragment(getContext(),name);
        coursesDialogFragment.show(getActivity().getSupportFragmentManager(), "Noo!");

    }
}

