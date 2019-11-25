package com.example.finalproject.ui.textRecognition;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.method.ScrollingMovementMethod;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;

import com.example.finalproject.Document;
import com.example.finalproject.DocumentViewModel;
import com.example.finalproject.R;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.ml.vision.FirebaseVision;
import com.google.firebase.ml.vision.common.FirebaseVisionImage;
import com.google.firebase.ml.vision.text.FirebaseVisionText;
import com.google.firebase.ml.vision.text.FirebaseVisionTextRecognizer;

import java.io.ByteArrayOutputStream;
import java.util.List;

import static android.app.Activity.RESULT_OK;

public class TextRecognitionFragment extends Fragment {

    private DocumentViewModel documentViewModel;
    private Button captureImageButton, detectTextButton;
    private FloatingActionButton saveDocumentButton;
    private EditText editText;
    private ImageView imageView;
    private TextView textView;
    private Bitmap imageBitmap;
    static final int REQUEST_IMAGE_CAPTURE = 1;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        documentViewModel = ViewModelProviders.of(this).get(DocumentViewModel.class);

        View root = inflater.inflate(R.layout.fragment_text_recognition, container, false);

        captureImageButton = root.findViewById(R.id.capture_image);
        detectTextButton = root.findViewById(R.id.detect_text);
        imageView = root.findViewById(R.id.iv);
        textView = root.findViewById(R.id.text_output);
        saveDocumentButton = root.findViewById(R.id.floatingActionButton);
        editText = root.findViewById(R.id.et_title);

        textView.setMovementMethod(new ScrollingMovementMethod());

        captureImageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dispatchTakePictureIntent();
                textView.setText("");
            }
        });

        detectTextButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                detectTextFromImage();
                textView.setText("");
            }
        });

        saveDocumentButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveDocument();
            }
        });

        return root;
    }

    // Save recognized text to local storage
    public void saveDocument() {
        if(editText.getText().toString().trim().isEmpty()) {
            Toast.makeText(getContext(), "Please add a TITLE to save your document!", Toast.LENGTH_LONG).show();
            Log.e("From save doc", "Doc added without title set");
        } else {
            documentViewModel.insert(new Document(editText.getText().toString(), textView.getText().toString()));
            Toast.makeText(getContext(), "Document successfully added to storage!", Toast.LENGTH_LONG).show();
            Log.i("From save doc", "Doc added successfully");
        }
    }

    // Call camera intent
    private void dispatchTakePictureIntent() {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (takePictureIntent.resolveActivity(getActivity().getPackageManager()) != null) {
            startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);
        }
    }

    // Get picture taken as Bitmap
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK) {
            Bundle extras = data.getExtras();
            imageBitmap = (Bitmap) extras.get("data");
            imageView.setImageBitmap(imageBitmap);
        }
    }

    // Input picture taken into a FirebaseVisionImage and process it
    private void detectTextFromImage() {
        FirebaseVisionImage fireBaseVisionImage = FirebaseVisionImage.fromBitmap(imageBitmap);
        FirebaseVisionTextRecognizer firebaseVisionTextRecognizer = FirebaseVision.getInstance().getCloudTextRecognizer();
        firebaseVisionTextRecognizer.processImage(fireBaseVisionImage).addOnSuccessListener(new OnSuccessListener<FirebaseVisionText>() {
            @Override
            public void onSuccess(FirebaseVisionText firebaseVisionText) {
                displayTextFromImageDetection(firebaseVisionText);
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(getContext(), "Error " + e.getMessage(), Toast.LENGTH_LONG).show();
                Log.e("Error: ", e.getMessage());
            }
        });
    }

    // Display recognized text in the TextView
    private void displayTextFromImageDetection(FirebaseVisionText firebaseVisionText) {
        List<FirebaseVisionText.TextBlock> blockList = firebaseVisionText.getTextBlocks();

        if(blockList.size() == 0) {
            Toast.makeText(getContext(), "No text found in image", Toast.LENGTH_LONG).show();
        } else {
            for(FirebaseVisionText.TextBlock textBlock : firebaseVisionText.getTextBlocks()) {
                String result = textBlock.getText();
                textView.append(result);
            }
        }
    }

}