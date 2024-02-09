package com.example.collegeschedulerapp.internalfiles;

import android.widget.CheckBox;

public interface RecyclerViewInterfaceTask {

    public void onClick(int position, String name, String course, String dueDateAndTime);

    public void updateTaskWithCheckBoxStatus(int position, String name, String course, CheckBox checkBox);
}
