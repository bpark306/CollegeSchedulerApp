package com.example.collegeschedulerapp.ui.exams;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class ExamsViewModel extends ViewModel {

    private final MutableLiveData<String> mText;

    public ExamsViewModel() {
        mText = new MutableLiveData<>();
        mText.setValue("This is Exams fragment");
    }

    public LiveData<String> getText() {
        return mText;
    }
}