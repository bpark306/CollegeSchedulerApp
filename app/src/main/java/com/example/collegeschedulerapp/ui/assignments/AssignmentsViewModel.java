package com.example.collegeschedulerapp.ui.assignments;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class AssignmentsViewModel extends ViewModel {

    private final MutableLiveData<String> mText;

    public AssignmentsViewModel() {
        mText = new MutableLiveData<>();
        mText.setValue("This is Assignments fragment");
    }

    public LiveData<String> getText() {
        return mText;
    }
}