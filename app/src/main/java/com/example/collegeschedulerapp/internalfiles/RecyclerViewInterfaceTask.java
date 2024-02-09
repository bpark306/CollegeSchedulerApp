package com.example.collegeschedulerapp.internalfiles;

public interface RecyclerViewInterfaceTask {

    public void onClick(int position, String name, String course, String dueDateAndTime);
    public void toggleCompletedStatus(int position, String name, String course, boolean bool);
}
