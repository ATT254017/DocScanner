package com.example.finalproject.ui.textRecognition;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class TextRecognitionViewModel extends ViewModel {

    private MutableLiveData<String> mText;

    public TextRecognitionViewModel() {
        mText = new MutableLiveData<>();
        mText.setValue("This is dashboard fragment");
    }

}