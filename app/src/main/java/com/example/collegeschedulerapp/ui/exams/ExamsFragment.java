package com.example.collegeschedulerapp.ui.exams;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.Switch;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;


import com.example.collegeschedulerapp.Adapter.ExamAdapter;
import com.example.collegeschedulerapp.BottomSheetDialog.CoursesDialogFragment;
import com.example.collegeschedulerapp.BottomSheetDialog.ExamsDialogFragment;
import com.example.collegeschedulerapp.R;
import com.example.collegeschedulerapp.RecyclerItemTouchHelper;
import com.example.collegeschedulerapp.databinding.FragmentExamsBinding;
import com.example.collegeschedulerapp.internalfiles.Course;
import com.example.collegeschedulerapp.internalfiles.Exam;
import com.example.collegeschedulerapp.internalfiles.RecyclerViewInterfaceExam;
import com.example.collegeschedulerapp.internalfiles.Task;
import com.example.collegeschedulerapp.ui.courses.CoursesFragment;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Collections;

public class ExamsFragment extends Fragment implements RecyclerViewInterfaceExam {

    private FragmentExamsBinding binding;
    private Switch filterSwitch;
    ItemTouchHelper itemTouchHelper;
    RecyclerView recyclerView;
    SwipeRefreshLayout refreshLayout;

    private ArrayList<Course> myCourses;
    private ArrayList<Exam> myExams;
    private ArrayList<Task> myTasks;


    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        ExamsViewModel assignmentViewModel =
                new ViewModelProvider(this).get(ExamsViewModel.class);

        binding = FragmentExamsBinding.inflate(inflater, container, false);
        filterSwitch = binding.getRoot().findViewById(R.id.exam_filter_switch);
        refreshLayout = binding.getRoot().findViewById(R.id.swipe_refresh_exam);
        View root = binding.getRoot();

        return root;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        // getting the employeelist

        loadData();
        ExamAdapter itemAdapter = new ExamAdapter(this, getContext(), myExams, myCourses, myTasks);


        // Set the LayoutManager that
        // this RecyclerView will use.
        recyclerView = view.findViewById(R.id.list_exams);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        itemTouchHelper = new ItemTouchHelper(new RecyclerItemTouchHelper(itemAdapter));
        itemTouchHelper.attachToRecyclerView(recyclerView);


        // adapter instance is set to the
        // recyclerview to inflate the items.
        recyclerView.setAdapter(itemAdapter);

        refreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                loadData();
                itemAdapter.updatemMyExams(myExams);
                recyclerView.setAdapter(itemAdapter);
                refreshLayout.setRefreshing(false);
            }
        });

        filterSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                loadData();
                itemAdapter.updatemMyExams(myExams);
                recyclerView.setAdapter(itemAdapter);
            }
        });

    }

    public static CoursesFragment newInstance()
    {
        return new CoursesFragment();
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


        myExams = new ArrayList<>();

        for (Course a: myCourses) {
            myExams.addAll(a.exams);
        }

        if (!filterSwitch.isChecked()) {
            filterSwitch.setText("Sort by Due Date");
            Collections.sort(myExams);
        } else {
            filterSwitch.setText("Sort by Course");
        }


    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

    @Override
    public void onClick(int position, String name, String course, String dueDateAndTime, String examLocation) {
        ExamsDialogFragment examsDialogFragment = new ExamsDialogFragment(getContext(), name, course,examLocation);
        examsDialogFragment.show(getActivity().getSupportFragmentManager(), "Naur!");

    }
}