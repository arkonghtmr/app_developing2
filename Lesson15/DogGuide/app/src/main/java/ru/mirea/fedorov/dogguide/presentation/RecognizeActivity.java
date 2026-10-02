package ru.mirea.fedorov.dogguide.presentation;

import android.Manifest;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;
import androidx.lifecycle.ViewModelProvider;

import java.io.File;

import ru.mirea.fedorov.dogguide.R;
import ru.mirea.fedorov.dogguide.domain.models.RecognitionResult;

public class RecognizeActivity extends BaseActivity {
    private RecognizeViewModel viewModel;
    private ImageView imageViewPhoto;
    private TextView textViewResult;
    private ProgressBar progressBar;
    private Button buttonSaveResult;
    private Button buttonRecognize;
    private Uri selectedImage;
    private Uri cameraImageUri;

    private final ActivityResultLauncher<String> pickImageLauncher =
            registerForActivityResult(new ActivityResultContracts.GetContent(), uri -> {
                if (uri == null) {
                    return;
                }
                showSelectedImage(uri);
            });

    private final ActivityResultLauncher<String> requestCameraPermissionLauncher =
            registerForActivityResult(new ActivityResultContracts.RequestPermission(), granted -> {
                if (granted) {
                    launchCamera();
                } else {
                    Toast.makeText(this, R.string.camera_permission_denied, Toast.LENGTH_SHORT).show();
                }
            });

    private final ActivityResultLauncher<Uri> takePictureLauncher =
            registerForActivityResult(new ActivityResultContracts.TakePicture(), success -> {
                if (!Boolean.TRUE.equals(success) || cameraImageUri == null) {
                    return;
                }
                showSelectedImage(cameraImageUri);
            });

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recognize);
        setupChrome(true);
        setTitle(R.string.recognize);

        viewModel = new ViewModelProvider(
                this,
                new DogGuideViewModelFactory(getApplication())
        ).get(RecognizeViewModel.class);

        imageViewPhoto = findViewById(R.id.imageViewPhoto);
        textViewResult = findViewById(R.id.textViewResult);
        progressBar = findViewById(R.id.progressBar);
        buttonSaveResult = findViewById(R.id.buttonSaveResult);
        buttonRecognize = findViewById(R.id.buttonRecognize);

        viewModel.getLoading().observe(this, loading -> {
            progressBar.setVisibility(Boolean.TRUE.equals(loading) ? View.VISIBLE : View.GONE);
            buttonRecognize.setEnabled(!Boolean.TRUE.equals(loading));
            if (Boolean.TRUE.equals(loading)) {
                textViewResult.setText(R.string.recognizing);
                buttonSaveResult.setVisibility(View.GONE);
            }
        });
        viewModel.getResult().observe(this, this::renderResult);
        viewModel.getMessage().observe(this, this::renderMessage);

        findViewById(R.id.buttonPickImage).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                pickImageLauncher.launch("image/*");
            }
        });
        findViewById(R.id.buttonTakePhoto).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onCameraClicked();
            }
        });
        buttonRecognize.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (selectedImage == null) {
                    Toast.makeText(RecognizeActivity.this, R.string.pick_photo_first, Toast.LENGTH_SHORT).show();
                    return;
                }
                viewModel.recognize(selectedImage.toString());
            }
        });
        buttonSaveResult.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                viewModel.saveResult();
            }
        });
    }

    private void onCameraClicked() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA)
                == PackageManager.PERMISSION_GRANTED) {
            launchCamera();
        } else {
            requestCameraPermissionLauncher.launch(Manifest.permission.CAMERA);
        }
    }

    private void launchCamera() {
        File file = new File(getCacheDir(), "camera_capture.jpg");
        cameraImageUri = FileProvider.getUriForFile(
                this,
                getPackageName() + ".fileprovider",
                file
        );
        takePictureLauncher.launch(cameraImageUri);
    }

    private void showSelectedImage(Uri uri) {
        selectedImage = uri;
        buttonSaveResult.setVisibility(View.GONE);
        textViewResult.setText("");
        imageViewPhoto.setImageURI(null);
        imageViewPhoto.setImageURI(uri);
    }

    private void renderResult(RecognitionResult result) {
        if (Boolean.TRUE.equals(viewModel.getLoading().getValue())) {
            return;
        }
        if (result == null) {
            if (selectedImage != null && textViewResult.getText().toString().equals(getString(R.string.recognizing))) {
                textViewResult.setText(R.string.recognize_failed);
            }
            return;
        }
        textViewResult.setText(String.format(
                "Распознано: %s (%.0f%%)\n\n%s",
                result.getBreedName(),
                result.getConfidence() * 100,
                result.getTopSummary()
        ));
        buttonSaveResult.setVisibility(View.VISIBLE);
    }

    private void renderMessage(String message) {
        if (message == null) {
            return;
        }
        if ("login_required".equals(message)) {
            Toast.makeText(this, R.string.login_required, Toast.LENGTH_SHORT).show();
        } else if ("not_in_catalog".equals(message)) {
            Toast.makeText(this, R.string.breed_not_in_catalog, Toast.LENGTH_SHORT).show();
        } else if (message.startsWith("saved:")) {
            Toast.makeText(this, "Сохранено в избранное: " + message.substring(6), Toast.LENGTH_SHORT).show();
        } else if ("already".equals(message)) {
            Toast.makeText(this, "Уже в избранном", Toast.LENGTH_SHORT).show();
        }
    }
}
