package com.example.finalproject;

import android.app.Application;
import android.os.AsyncTask;

import androidx.lifecycle.LiveData;

import java.util.List;

public class DocumentRepository {

    private DocumentDao documentDao;
    private static DocumentRepository instance;
    private LiveData<List<Document>> allNotes;

    private DocumentRepository(Application application){
        DocumentDatabase database = DocumentDatabase.getInstance(application);
        documentDao = database.documentDao();
        allNotes = documentDao.getAllDocuments();
    }

    public static synchronized DocumentRepository getInstance(Application application){
        if(instance == null)
            instance = new DocumentRepository(application);

        return instance;
    }

    public LiveData<List<Document>> getAllDocuments(){
        return allNotes;
    }

    public void insert(Document doc) {
        new InsertDocumentAsync(documentDao).execute(doc);
    }

    public void update(Document doc) {
        new UpdateDocumentAsync(documentDao).execute(doc);
    }

    public void delete(Document doc) {
        new DeleteDocumentAsync(documentDao).execute(doc);
    }

    public void deleteAllDocuments(){
        new DeleteAllDocumentsAsync(documentDao).execute();
    }


    private static class InsertDocumentAsync extends AsyncTask<Document,Void,Void> {
        private DocumentDao noteDao;

        private InsertDocumentAsync(DocumentDao documentDao) {
            this.noteDao = documentDao;
        }

        @Override
        protected Void doInBackground(Document... documents) {
            noteDao.insert(documents[0]);
            return null;
        }
    }

    private static class UpdateDocumentAsync extends AsyncTask<Document,Void,Void> {
        private DocumentDao noteDao;

        private UpdateDocumentAsync(DocumentDao documentDao) {
            this.noteDao = documentDao;
        }

        @Override
        protected Void doInBackground(Document... documents) {
            noteDao.update(documents[0]);
            return null;
        }
    }

    private static class DeleteDocumentAsync extends AsyncTask<Document,Void,Void> {
        private DocumentDao noteDao;

        private DeleteDocumentAsync(DocumentDao documentDao) {
            this.noteDao = documentDao;
        }

        @Override
        protected Void doInBackground(Document... documents) {
            noteDao.delete(documents[0]);
            return null;
        }
    }

    private static class DeleteAllDocumentsAsync extends AsyncTask<Void,Void,Void> {
        private DocumentDao documentDao;

        private DeleteAllDocumentsAsync(DocumentDao documentDao) {
            this.documentDao = documentDao;
        }

        @Override
        protected Void doInBackground(Void... voids) {
            documentDao.deleteAllDocuments();
            return null;
        }
    }

}
