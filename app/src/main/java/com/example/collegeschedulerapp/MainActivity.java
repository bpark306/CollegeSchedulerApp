package com.example.collegeschedulerapp;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import com.example.collegeschedulerapp.databinding.ActivityMainBinding;
import com.google.android.material.floatingactionbutton.ExtendedFloatingActionButton;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

public class MainActivity extends AppCompatActivity {

    private ActivityMainBinding binding;

    private ExtendedFloatingActionButton addButton;

    private FloatingActionButton courseButton;
    private FloatingActionButton examButton;
    private FloatingActionButton assignmentButton;




    private TextView courseButtonText;
    private TextView examButtonText;

    private TextView assignmentButtonText;


    private boolean isAllFABVisble;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        BottomNavigationView navView = findViewById(R.id.nav_view);
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        AppBarConfiguration appBarConfiguration = new AppBarConfiguration.Builder(
                R.id.navigation_todo,R.id.navigation_assignments, R.id.navigation_exams, R.id.navigation_courses)
                .build();
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_activity_main);
        NavigationUI.setupActionBarWithNavController(this, navController, appBarConfiguration);
        NavigationUI.setupWithNavController(binding.navView, navController);


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

                    courseButton.setVisibility(View.GONE);
                    examButton.setVisibility(View.GONE);
                    assignmentButton.setVisibility(View.GONE);

                    courseButtonText.setVisibility(View.GONE);
                    examButtonText.setVisibility(View.GONE);
                    assignmentButtonText.setVisibility(View.GONE);

                    addButton.shrink();
                }
                isAllFABVisble = !isAllFABVisble;
            }
        });





    }

    private void showToast(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();

    }

}