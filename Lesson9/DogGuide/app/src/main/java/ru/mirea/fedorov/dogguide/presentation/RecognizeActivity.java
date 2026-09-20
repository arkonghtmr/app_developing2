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

import java.io.File;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import ru.mirea.fedorov.dogguide.DogGuideApp;
import ru.mirea.fedorov.dogguide.R;
import ru.mirea.fedorov.dogguide.domain.models.Breed;
import ru.mirea.fedorov.dogguide.domain.models.RecognitionResult;
import ru.mirea.fedorov.dogguide.domain.usecases.AddBreedToFavoritesUseCase;
import ru.mirea.fedorov.dogguide.domain.usecases.GetBreedDetailsUseCase;
import ru.mirea.fedorov.dogguide.domain.usecases.GetCurrentUserUseCase;
import ru.mirea.fedorov.dogguide.domain.usecases.RecognizeBreedUseCase;

public class RecognizeActivity extends BaseActivity {
    private ImageView imageViewPhoto;
    private TextView textViewResult;
    private ProgressBar progressBar;
    private Button buttonSaveResult;
    private Button buttonRecognize;
    private Uri selectedImage;
    private Uri cameraImageUri;
    private RecognitionResult lastResult;

    private RecognizeBreedUseCase recognizeBreedUseCase;
    private GetBreedDetailsUseCase getBreedDetailsUseCase;
    private GetCurrentUserUseCase getCurrentUserUseCase;
    private AddBreedToFavoritesUseCase addBreedToFavoritesUseCase;
    private final ExecutorService executor = Executors.newSingleThreadExecutor();

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

        DogGuideApp app = (DogGuideApp) getApplication();
        recognizeBreedUseCase = new RecognizeBreedUseCase(app.getRecognitionRepository());
        getBreedDetailsUseCase = new GetBreedDetailsUseCase(app.getBreedRepository());
        getCurrentUserUseCase = new GetCurrentUserUseCase(app.getUserRepository());
        addBreedToFavoritesUseCase = new AddBreedToFavoritesUseCase(
                app.getFavoriteRepository(),
                app.getUserRepository()
        );

        imageViewPhoto = findViewById(R.id.imageViewPhoto);
        textViewResult = findViewById(R.id.textViewResult);
        progressBar = findViewById(R.id.progressBar);
        buttonSaveResult = findViewById(R.id.buttonSaveResult);
        buttonRecognize = findViewById(R.id.buttonRecognize);

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
                recognize();
            }
        });
        buttonSaveResult.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                saveResult();
            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        executor.shutdownNow();
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
        lastResult = null;
        buttonSaveResult.setVisibility(View.GONE);
        textViewResult.setText("");
        imageViewPhoto.setImageURI(null);
        imageViewPhoto.setImageURI(uri);
    }

    private void recognize() {
        if (selectedImage == null) {
            Toast.makeText(this, R.string.pick_photo_first, Toast.LENGTH_SHORT).show();
            return;
        }
        progressBar.setVisibility(View.VISIBLE);
        buttonRecognize.setEnabled(false);
        textViewResult.setText(R.string.recognizing);
        buttonSaveResult.setVisibility(View.GONE);
        String uri = selectedImage.toString();
        executor.execute(() -> {
            RecognitionResult result = recognizeBreedUseCase.execute(uri);
            runOnUiThread(() -> {
                if (isDestroyed()) {
                    return;
                }
                progressBar.setVisibility(View.GONE);
                buttonRecognize.setEnabled(true);
                if (result == null) {
                    textViewResult.setText(R.string.recognize_failed);
                    return;
                }
                lastResult = result;
                textViewResult.setText(String.format(
                        "Распознано: %s (%.0f%%)\n\n%s",
                        result.getBreedName(),
                        result.getConfidence() * 100,
                        result.getTopSummary()
                ));
                buttonSaveResult.setVisibility(View.VISIBLE);
            });
        });
    }

    private void saveResult() {
        if (getCurrentUserUseCase.execute() == null) {
            Toast.makeText(this, R.string.login_required, Toast.LENGTH_SHORT).show();
            return;
        }
        if (lastResult == null) {
            return;
        }
        executor.execute(() -> {
            Breed breed = getBreedDetailsUseCase.execute(lastResult.getBreedId());
            runOnUiThread(() -> {
                if (isDestroyed()) {
                    return;
                }
                if (breed == null) {
                    Toast.makeText(this, R.string.breed_not_in_catalog, Toast.LENGTH_SHORT).show();
                    return;
                }
                boolean added = addBreedToFavoritesUseCase.execute(breed);
                Toast.makeText(
                        this,
                        added ? "Сохранено в избранное: " + breed.getName() : "Уже в избранном",
                        Toast.LENGTH_SHORT
                ).show();
            });
        });
    }
}
