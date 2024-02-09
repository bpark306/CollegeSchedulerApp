package com.example.collegeschedulerapp.ui.todo;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
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


import com.example.collegeschedulerapp.Adapter.TaskAdapter;
import com.example.collegeschedulerapp.BottomSheetDialog.ExamsDialogFragment;
import com.example.collegeschedulerapp.BottomSheetDialog.TasksDialogFragment;
import com.example.collegeschedulerapp.R;
import com.example.collegeschedulerapp.RecyclerItemTouchHelper;
import com.example.collegeschedulerapp.databinding.FragmentTodoBinding;
import com.example.collegeschedulerapp.internalfiles.Course;

import com.example.collegeschedulerapp.internalfiles.RecyclerViewInterfaceTask;
import com.example.collegeschedulerapp.internalfiles.Task;

import com.example.collegeschedulerapp.ui.todo.TodoViewModel;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Array;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

public class TodoFragment extends Fragment implements RecyclerViewInterfaceTask {

    private FragmentTodoBinding binding;

    private Switch filterSwitch, showCompleted;
    ItemTouchHelper itemTouchHelper;
    RecyclerView recyclerView;
    SwipeRefreshLayout refreshLayout;

    private ArrayList<Course> myCourses;
    private ArrayList<Task> myTasks;



    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        TodoViewModel todoViewModel  =
                new ViewModelProvider(this).get(TodoViewModel.class);

        binding = FragmentTodoBinding.inflate(inflater, container, false);
        filterSwitch = binding.getRoot().findViewById(R.id.todo_filter_switch);
        showCompleted= binding.getRoot().findViewById(R.id.todo_show_completed);
        refreshLayout = binding.getRoot().findViewById(R.id.swipe_refresh_task);
        showCompleted = binding.getRoot().findViewById(R.id.todo_show_completed);
        View root = binding.getRoot();

        return root;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        // getting the employeelist

        loadData();

        TaskAdapter itemAdapter = new TaskAdapter(this, getContext(), myCourses, myTasks);

        // Set the LayoutManager that
        // this RecyclerView will use.
        recyclerView = view.findViewById(R.id.list_tasks);
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
                itemAdapter.updateEmplist(myTasks);
                recyclerView.setAdapter(itemAdapter);
                refreshLayout.setRefreshing(false);
            }
        });

        filterSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                loadData();
                itemAdapter.updateEmplist(myTasks);
                recyclerView.setAdapter(itemAdapter);
            }
        });
        showCompleted.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                loadData();
                itemAdapter.updateFilteredList(myTasks, showCompleted.isChecked());
                recyclerView.setAdapter(itemAdapter);
            }
        });



    }

    public static TodoFragment newInstance()
    {
        return new TodoFragment();
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


        json = sharedPreferences.getString("my tasks", null);
        type = new TypeToken<ArrayList<Task>>() {}.getType();
        myTasks = gson.fromJson(json, type);


        if (myTasks == null) {
            myTasks = new ArrayList<>();
        }


        if (!showCompleted.isChecked()) {
            ArrayList<Task> nonCompletedTasks = new ArrayList<>();

            for (int i = 0; i < myTasks.size(); i++) {
                if (!myTasks.get(i).isCompleted()) {
                    nonCompletedTasks.add(myTasks.get(i));
                }
            }

            myTasks = nonCompletedTasks;
        }



        if (!filterSwitch.isChecked()) {
            filterSwitch.setText("Sort by Due Date");
            myTasks.sort(new sortByDate());
        } else {
            filterSwitch.setText("Sort by Course");
            myTasks.sort(new sortByCourse());
        }



    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

    @Override
    public void onClick(int position, String name, String course, String dueDateAndTime) {
        // Implement the edit task


        TasksDialogFragment tasksDialogFragment = new TasksDialogFragment(getContext(), name, course, dueDateAndTime);
        tasksDialogFragment.show(getActivity().getSupportFragmentManager(), "Naur!");
    }

    @Override
    public void updateTaskWithCheckBoxStatus(int position, String name, String course, CheckBox checkBox) {
        for (int i = 0; i < myTasks.size(); i++) {
            //Finds the task in the array of tasks
            if (myTasks.get(i).getName().equals(name))  {
                //Sets the task to completed
                myTasks.get(i).switchStatus(checkBox.isChecked());

                //Sets the task assignment to completed
                if (myTasks.get(i).returnAssignment() != null) {
                    myTasks.get(i).returnAssignment().setCompleted(checkBox.isChecked());
                    Toast.makeText(getContext(), "Task and Assignment Task status to " + checkBox.isChecked(), Toast.LENGTH_SHORT).show();
                }
                saveData();
            }
        }

    }

    private class sortByCourse implements Comparator<Task>

    {
        // override the compare() method
        public int compare(Task t1, Task t2)
        {
            if (t1.getCourse().equals(t2.getCourse()))
                return 0;
            else
                return t1.getCourse().name.compareTo(t2.getCourse().name);
        }
    }

    private class sortByDate implements Comparator<Task>

    {
        // override the compare() method
        public int compare(Task t1, Task t2)
        {
            return t1.getDueDateTime().compareTo(t2.getDueDateTime());
        }
    }

}

