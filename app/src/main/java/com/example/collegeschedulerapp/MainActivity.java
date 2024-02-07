package com.example.collegeschedulerapp;

import static android.app.PendingIntent.getActivity;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Rect;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MotionEvent;
import android.view.View;
import android.widget.TextView;

import com.example.collegeschedulerapp.BottomSheetDialog.AssignmentsDialogFragment;
import com.example.collegeschedulerapp.BottomSheetDialog.CoursesDialogFragment;
import com.example.collegeschedulerapp.BottomSheetDialog.ExamsDialogFragment;
import com.example.collegeschedulerapp.internalfiles.Course;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import com.example.collegeschedulerapp.databinding.ActivityMainBinding;
import com.example.collegeschedulerapp.internalfiles.RecyclerViewInterfaceExam;
import com.google.android.material.floatingactionbutton.ExtendedFloatingActionButton;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;

import jp.wasabeef.blurry.Blurry;

public class MainActivity extends AppCompatActivity {


    private ActivityMainBinding binding;

    private ExtendedFloatingActionButton addButton;
    private FloatingActionButton courseButton,examButton, assignmentButton;
    private TextView courseButtonText, examButtonText, assignmentButtonText;
    private boolean isAllFABVisble;

    ArrayList<Course> myCourses;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        getSupportActionBar().hide();


        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        AppBarConfiguration appBarConfiguration = new AppBarConfiguration.Builder(
                R.id.navigation_todo,R.id.navigation_assignments, R.id.navigation_exams, R.id.navigation_courses)
                .build();
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_activity_main);
        NavigationUI.setupActionBarWithNavController(this, navController, appBarConfiguration);
        NavigationUI.setupWithNavController(binding.navView, navController);


        myCourses = new ArrayList<>(10);



        // FLoating Button Code
        addButton = findViewById(R.id.add_btn);
        courseButton = findViewById(R.id.add_course);
        examButton = findViewById(R.id.add_exam);
        assignmentButton = findViewById(R.id.add_assignment);

        courseButtonText = findViewById(R.id.add_course_text);
        examButtonText = findViewById(R.id.add_exam_text);
        assignmentButtonText = findViewById(R.id.add_assignment_text);

        courseButton.setVisibility(View.GONE);
        examButton.setVisibility(View.GONE);
        assignmentButton.setVisibility(View.GONE);

        courseButtonText.setVisibility(View.GONE);
        examButtonText.setVisibility(View.GONE);
        assignmentButtonText.setVisibility(View.GONE);

        isAllFABVisble = false;
        addButton.shrink();





        courseButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addButton.callOnClick();
                CoursesDialogFragment courseDialogFragmenet = new CoursesDialogFragment(MainActivity.this);
                courseDialogFragmenet.show(getSupportFragmentManager(), "Yipiee");

            }
        });

        assignmentButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addButton.callOnClick();
                AssignmentsDialogFragment assignmentsDialogFragment = new AssignmentsDialogFragment(MainActivity.this);
                assignmentsDialogFragment.show(getSupportFragmentManager(), "Hiya");
            }
        });

        examButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addButton.callOnClick();
                ExamsDialogFragment examsDialogFragment = new ExamsDialogFragment(MainActivity.this);
                examsDialogFragment.show(getSupportFragmentManager(), "Nooo");
            }
        });

        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isAllFABVisble) {
                    courseButton.show();
                    examButton.show();
                    assignmentButton.show();

                    courseButtonText.setVisibility(View.VISIBLE);
                    examButtonText.setVisibility(View.VISIBLE);
                    assignmentButtonText.setVisibility(View.VISIBLE);

                    addButton.extend();
                } else {
                    addButton.shrink();
                    courseButton.setVisibility(View.GONE);
                    examButton.setVisibility(View.GONE);
                    assignmentButton.setVisibility(View.GONE);

                    courseButtonText.setVisibility(View.GONE);
                    examButtonText.setVisibility(View.GONE);
                    assignmentButtonText.setVisibility(View.GONE);

                }
                isAllFABVisble = !isAllFABVisble;
            }
        });
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent event){
        if (event.getAction() == MotionEvent.ACTION_DOWN) {
            if (addButton.isExtended()) {

                Rect outCourse = new Rect();
                Rect outAssignment = new Rect();
                Rect outExam = new Rect();

                courseButton.getGlobalVisibleRect(outCourse);
                assignmentButton.getGlobalVisibleRect(outAssignment);
                examButton.getGlobalVisibleRect(outExam);


                if(!outCourse.contains((int)event.getRawX(), (int)event.getRawY())
                        && !outAssignment.contains((int)event.getRawX(), (int)event.getRawY())
                        && !outExam.contains((int)event.getRawX(), (int)event.getRawY())) {

                    addButton.callOnClick();
                    return false;
                }
            }
        }
        return super.dispatchTouchEvent(event);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.filter_by_menu, menu);
        return true;
    }
}