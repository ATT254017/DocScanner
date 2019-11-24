package com.example.finalproject;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import java.util.ArrayList;
import java.util.List;

public class DocumentDao {

    private MutableLiveData<List<Document>> allDocuments;
    private static DocumentDao instance;

    private DocumentDao() {
        allDocuments = new MutableLiveData<>();
        List<Document> newList = new ArrayList<>();
        allDocuments.setValue(newList);
    }

    public static DocumentDao getInstance() {
        if(instance == null) {
            instance = new DocumentDao();
        }

        return instance;
    }

    public LiveData<List<Document>> getAllDocuments() {
        return allDocuments;
    }

    public void insert(Document doc) {
        List<Document> currentNotes = allDocuments.getValue();
        currentNotes.add(doc);
        allDocuments.setValue(currentNotes);
    }

    public void deleteAllDocuments() {
        List<Document> newList = new ArrayList<>();
        allDocuments.setValue(newList);
    }

}
