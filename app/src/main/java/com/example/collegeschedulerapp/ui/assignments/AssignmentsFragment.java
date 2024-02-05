package com.example.collegeschedulerapp.ui.assignments;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ScrollView;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;


import com.example.collegeschedulerapp.Adapter.AssignmentAdapter;
import com.example.collegeschedulerapp.AssignmentsDialogFragment;
import com.example.collegeschedulerapp.R;
import com.example.collegeschedulerapp.RecyclerItemTouchHelper;
import com.example.collegeschedulerapp.databinding.FragmentAssignmentsBinding;
import com.example.collegeschedulerapp.databinding.FragmentTodoBinding;
import com.example.collegeschedulerapp.internalfiles.Assignment;
import com.example.collegeschedulerapp.internalfiles.Course;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

public class AssignmentsFragment extends Fragment {

    private FragmentAssignmentsBinding binding;
    private RecyclerView assignmentRecycler;
    private AssignmentAdapter assignmentAdapter;
    private Switch filterSwitch;

    private ArrayList<Assignment> myAssignments;
    private ArrayList<Course> myCourses;
    private boolean sortByCourse = false;


    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        AssignmentsViewModel todoViewModel =
                new ViewModelProvider(this).get(AssignmentsViewModel.class);

        binding = FragmentAssignmentsBinding.inflate(inflater, container, false);
        filterSwitch = binding.getRoot().findViewById(R.id.assignment_filter_switch);
        loadAssignments();

        if (filterSwitch.isActivated()) {
            filterSwitch.setText("Filter By Due Date");

        } else {
            filterSwitch.setText("Filter By Course");
        }


        View root = binding.getRoot();






        return root;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        // getting the employeelist






        AssignmentAdapter itemAdapter = new AssignmentAdapter(getContext(), myAssignments);

        // Set the LayoutManager that
        // this RecyclerView will use.
        RecyclerView recyclerView
                = view.findViewById(R.id.list_assignments);
        recyclerView.setLayoutManager(
                new LinearLayoutManager(getContext()));

        ItemTouchHelper itemTouchHelper = new
                ItemTouchHelper(new RecyclerItemTouchHelper(itemAdapter));
        itemTouchHelper.attachToRecyclerView(recyclerView);


        filterSwitch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (filterSwitch.isActivated()) {
                    filterSwitch.setText("Filter By Due Date");

                } else {
                    filterSwitch.setText("Filter By Course");
                }
            }
        });

        // adapter instance is set to the
        // recyclerview to inflate the items.
        recyclerView.setAdapter(itemAdapter);

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

    private void loadAssignments() {
        loadData();

        myAssignments = new ArrayList<>();

        for (Course a : myCourses) {
            myAssignments.addAll(a.assignments);
        }
        if (filterSwitch.isActivated()) {
            Collections.sort(myAssignments);
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();

        binding = null;
    }
}