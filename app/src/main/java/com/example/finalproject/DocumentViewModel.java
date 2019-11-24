package com.example.finalproject;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

import java.util.List;

public class DocumentViewModel extends ViewModel {

    private DocumentRepository repository;

    public DocumentViewModel() {
        repository = DocumentRepository.getInstance();
    }

    public LiveData<List<Document>> getAllNotes() {
        return repository.getAllDocuments();
    }

    public void insert(final Document doc) {
        repository.insert(doc);
    }

    public void deleteAllNotes() {
        repository.deleteAllDocuments();
    }

}
