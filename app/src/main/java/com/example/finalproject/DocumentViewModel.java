package com.example.finalproject;

import android.app.Application;

import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import java.util.List;

public class DocumentViewModel extends AndroidViewModel {

    private DocumentRepository repository;

    public DocumentViewModel(Application app) {
        super(app);
        repository = DocumentRepository.getInstance(app);
    }

    public LiveData<List<Document>> getAllDocuments() {
        return repository.getAllDocuments();
    }

    public void insert(final Document doc) {
        repository.insert(doc);
    }

    public void update(Document doc) {
        repository.update(doc);
    }

    public void delete(Document doc) {
        repository.delete(doc);
    }

    public void deleteAllDocuments() {
        repository.deleteAllDocuments();
    }

}
