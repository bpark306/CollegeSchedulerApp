package com.example.collegeschedulerapp.ui.assignments;

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
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;


import com.example.collegeschedulerapp.Adapter.AssignmentAdapter;
import com.example.collegeschedulerapp.BottomSheetDialog.AssignmentsDialogFragment;
import com.example.collegeschedulerapp.R;
import com.example.collegeschedulerapp.RecyclerItemTouchHelper;
import com.example.collegeschedulerapp.databinding.FragmentAssignmentsBinding;
import com.example.collegeschedulerapp.internalfiles.Assignment;
import com.example.collegeschedulerapp.internalfiles.Course;
import com.example.collegeschedulerapp.internalfiles.RecyclerViewInterface;
import com.example.collegeschedulerapp.internalfiles.Task;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;

public class AssignmentsFragment extends Fragment implements RecyclerViewInterface {

    private FragmentAssignmentsBinding binding;

    private Switch filterSwitch;
    ItemTouchHelper itemTouchHelper;
    RecyclerView recyclerView;
    SwipeRefreshLayout refreshLayout;

    private ArrayList<Course> myCourses;
    private ArrayList<Assignment> myAssignments;
    private ArrayList<Task> myTasks;


    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        AssignmentsViewModel todoViewModel =
                new ViewModelProvider(this).get(AssignmentsViewModel.class);

        binding = FragmentAssignmentsBinding.inflate(inflater, container, false);
        filterSwitch = binding.getRoot().findViewById(R.id.assignment_filter_switch);
        refreshLayout = binding.getRoot().findViewById(R.id.swipe_refresh);
        View root = binding.getRoot();

        return root;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        // getting the employeelist

        loadData();
        AssignmentAdapter itemAdapter = new AssignmentAdapter(this, getContext(), myAssignments, myCourses, myTasks);

        // Set the LayoutManager that
        // this RecyclerView will use.
        recyclerView = view.findViewById(R.id.list_assignments);
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
                itemAdapter.updateEmplist(myAssignments);
                recyclerView.setAdapter(itemAdapter);
                refreshLayout.setRefreshing(false);
            }
        });

        filterSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                loadData();
                itemAdapter.updateEmplist(myAssignments);
                recyclerView.setAdapter(itemAdapter);
            }
        });

    }

    public static AssignmentsFragment newInstance()
    {
        return new AssignmentsFragment();
    }

    private void saveData() {
        SharedPreferences sharedPreferences = getContext().getSharedPreferences("shared preferences",Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        Gson gson = new Gson();
        String json = gson.toJson(myCourses);
        editor.putString("my courses", json);
        //json = gson.toJson(myTasks);
        //editor.putString("my tasks", json);
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
        if (myTasks == null) {
            myTasks = new ArrayList<>();
        }

        myAssignments = new ArrayList<>();


        for (Course a : myCourses) {
            myAssignments.addAll(a.assignments);
        }
        if (!filterSwitch.isChecked()) {
            filterSwitch.setText("Sort by Due Date");
            Collections.sort(myAssignments);
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
    public void onClick(int position, String name, String course, String dueDateAndTime) {
        AssignmentsDialogFragment assignmentsDialogFragment = new AssignmentsDialogFragment(getContext(), name, course, dueDateAndTime);
        assignmentsDialogFragment.show(getActivity().getSupportFragmentManager(), "Yay!");

    }
}