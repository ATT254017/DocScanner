package com.example.finalproject;

import androidx.lifecycle.LiveData;

import java.util.List;

public class DocumentRepository {

    private DocumentDao documentDao;
    private static DocumentRepository instance;

    private DocumentRepository() {
        documentDao = DocumentDao.getInstance();
    }

    public static DocumentRepository getInstance(){
        if(instance == null)
            instance = new DocumentRepository();

        return instance;
    }

    public LiveData<List<Document>> getAllDocuments(){
        return documentDao.getAllDocuments();
    }

    public void insert(Document note) {
        documentDao.insert(note);
    }

    public void deleteAllDocuments(){
        documentDao.deleteAllDocuments();
    }


}
