package com.example.finalproject.ui.textRecognition;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.method.ScrollingMovementMethod;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;

import com.example.finalproject.R;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.ml.vision.FirebaseVision;
import com.google.firebase.ml.vision.common.FirebaseVisionImage;
import com.google.firebase.ml.vision.text.FirebaseVisionText;
import com.google.firebase.ml.vision.text.FirebaseVisionTextRecognizer;

import java.util.List;

import static android.app.Activity.RESULT_OK;

public class TextRecognitionFragment extends Fragment {

    private TextRecognitionViewModel textRecognitionViewModel;
    private Button captureImageButton, detectTextButton;
    private ImageView imageView;
    private TextView textView;
    private Bitmap imageBitmap;
    static final int REQUEST_IMAGE_CAPTURE = 1;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        textRecognitionViewModel =
                ViewModelProviders.of(this).get(TextRecognitionViewModel.class);
        View root = inflater.inflate(R.layout.fragment_text_recognition, container, false);

        captureImageButton = root.findViewById(R.id.capture_image);
        detectTextButton = root.findViewById(R.id.detect_text);
        imageView = root.findViewById(R.id.iv);
        textView = root.findViewById(R.id.text_output);

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
            }
        });

        return root;
    }

    private void dispatchTakePictureIntent() {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (takePictureIntent.resolveActivity(getActivity().getPackageManager()) != null) {
            startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK) {
            Bundle extras = data.getExtras();
            imageBitmap = (Bitmap) extras.get("data");
            imageView.setImageBitmap(imageBitmap);
        }
    }

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
                Toast.makeText(getContext(), "Error " + e.getMessage(), Toast.LENGTH_LONG);
                Log.e("Error: ", e.getMessage());
            }
        });
    }

    private void displayTextFromImageDetection(FirebaseVisionText firebaseVisionText) {
        List<FirebaseVisionText.TextBlock> blockList = firebaseVisionText.getTextBlocks();

        if(blockList.size() == 0) {
            Toast.makeText(getContext(), "No text found in image", Toast.LENGTH_LONG);
        } else {
            for(FirebaseVisionText.TextBlock textBlock : firebaseVisionText.getTextBlocks()) {
                String result = textBlock.getText();
                textView.append(result);
            }
        }
    }

}