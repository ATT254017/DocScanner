package com.example.finalproject;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.Toast;

public class EditDocumentActivity extends AppCompatActivity {
    public static final String EXTRA_ID = "com.example.finalproject.EXTRA_ID";
    public static final String EXTRA_TITLE = "com.example.finalproject.EXTRA_TITLE";
    public static final String EXTRA_CONTENT = "com.example.finalproject.EXTRA_CONTENT";

    private EditText editTextTitle;
    private EditText editTextContent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_document);

        editTextTitle = findViewById(R.id.edit_title);
        editTextContent = findViewById(R.id.edit_content);

        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_close);

        Intent intent = getIntent();
        editTextTitle.setText(intent.getStringExtra(EXTRA_TITLE));
        editTextContent.setText(intent.getStringExtra(EXTRA_CONTENT));

        setTitle("Edit Document");
    }

    private void saveDocument() {
        String title = editTextTitle.getText().toString();
        String content = editTextContent.getText().toString();

        if(title.trim().isEmpty() || content.trim().isEmpty()) {
            Toast.makeText(this, "Please insert a title or content texts", Toast.LENGTH_LONG).show();
            return;
        }

        Intent data = new Intent();
        data.putExtra(EXTRA_TITLE, title);
        data.putExtra(EXTRA_CONTENT, content);

        int id = getIntent().getIntExtra(EXTRA_ID, -1);
        if(id != -1) {
            data.putExtra(EXTRA_ID, id);
        }

        setResult(RESULT_OK, data);
        finish();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.edit_document_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch(item.getItemId()) {
            case R.id.save_document:
                saveDocument();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}
