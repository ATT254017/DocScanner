package com.example.finalproject.ui.storage;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.finalproject.Document;
import com.example.finalproject.DocumentAdapter;
import com.example.finalproject.DocumentViewModel;
import com.example.finalproject.EditDocumentActivity;
import com.example.finalproject.R;

import java.util.List;

import static android.app.Activity.RESULT_OK;

public class StorageFragment extends Fragment {
    public static final int EDIT_DOCUMENT_REQUEST = 1;
    private DocumentViewModel documentViewModel;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        documentViewModel = ViewModelProviders.of(this).get(DocumentViewModel.class);

        View root = inflater.inflate(R.layout.fragment_storage, container, false);

        setHasOptionsMenu(true);

        RecyclerView recyclerView = root.findViewById(R.id.rv);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setHasFixedSize(true);

        final DocumentAdapter documentAdapter = new DocumentAdapter();
        recyclerView.setAdapter(documentAdapter);

        documentViewModel.getAllDocuments().observe(this, new Observer<List<Document>>() {
            @Override
            public void onChanged(List<Document> documents) {
                documentAdapter.submitList(documents);
            }
        });

        new ItemTouchHelper(new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.RIGHT) {
            @Override
            public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
                return false;
            }

            @Override
            public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
                documentViewModel.delete(documentAdapter.getDocumentAt(viewHolder.getAdapterPosition()));
                Toast.makeText(getContext(), "Document successfully deleted!", Toast.LENGTH_LONG).show();
            }
        }).attachToRecyclerView(recyclerView);

        documentAdapter.setOnItemClickListener(new DocumentAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(Document document) {
                Intent intent = new Intent(getActivity(), EditDocumentActivity.class);
                intent.putExtra(EditDocumentActivity.EXTRA_ID, document.getId());
                intent.putExtra(EditDocumentActivity.EXTRA_TITLE, document.getTitle());
                intent.putExtra(EditDocumentActivity.EXTRA_CONTENT, document.getContent());

                startActivityForResult(intent, EDIT_DOCUMENT_REQUEST);
            }
        });

        return root;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater menuInflater) {
        menuInflater.inflate(R.menu.storage_menu, menu);
        super.onCreateOptionsMenu(menu,menuInflater);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch(item.getItemId()) {
            case R.id.delete_all_documents_option:
                documentViewModel.deleteAllDocuments();
                Toast.makeText(getContext(), "All documents were deleted!", Toast.LENGTH_LONG);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode == EDIT_DOCUMENT_REQUEST && resultCode == RESULT_OK) {
            int id = data.getIntExtra(EditDocumentActivity.EXTRA_ID, -1);

            if(id == -1) {
                Toast.makeText(getContext(), "Document wasn't updated", Toast.LENGTH_LONG).show();
                return;
            }

            String title = data.getStringExtra(EditDocumentActivity.EXTRA_TITLE);
            String content = data.getStringExtra(EditDocumentActivity.EXTRA_CONTENT);

            Document document = new Document(title, content);
            document.setId(id);

            documentViewModel.update(document);
            Toast.makeText(getContext(), "Document successfully updated!", Toast.LENGTH_LONG).show();
        }
    }
}