package ru.mirea.fedorov.dogguide.presentation;

import android.Manifest;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import java.io.File;

import ru.mirea.fedorov.dogguide.R;
import ru.mirea.fedorov.dogguide.databinding.FragmentRecognizeBinding;
import ru.mirea.fedorov.dogguide.domain.models.RecognitionResult;

public class RecognizeFragment extends Fragment {
    private FragmentRecognizeBinding binding;
    private RecognizeViewModel viewModel;
    private Uri cameraImageUri;

    private final ActivityResultLauncher<String> pickImageLauncher =
            registerForActivityResult(new ActivityResultContracts.GetContent(), uri -> {
                if (uri == null) {
                    return;
                }
                viewModel.setImageUri(uri.toString());
            });

    private final ActivityResultLauncher<String> requestCameraPermissionLauncher =
            registerForActivityResult(new ActivityResultContracts.RequestPermission(), granted -> {
                if (granted) {
                    launchCamera();
                } else {
                    Toast.makeText(requireContext(), R.string.camera_permission_denied, Toast.LENGTH_SHORT).show();
                }
            });

    private final ActivityResultLauncher<Uri> takePictureLauncher =
            registerForActivityResult(new ActivityResultContracts.TakePicture(), success -> {
                if (!Boolean.TRUE.equals(success) || cameraImageUri == null) {
                    return;
                }
                viewModel.setImageUri(cameraImageUri.toString());
            });

    @Nullable
    @Override
    public View onCreateView(
            @NonNull LayoutInflater inflater,
            @Nullable ViewGroup container,
            @Nullable Bundle savedInstanceState
    ) {
        binding = FragmentRecognizeBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        viewModel = new ViewModelProvider(
                requireActivity(),
                new DogGuideViewModelFactory(requireActivity().getApplication())
        ).get(RecognizeViewModel.class);

        viewModel.getImageUri().observe(getViewLifecycleOwner(), uri -> {
            if (uri == null) {
                return;
            }
            binding.imageViewPhoto.setImageURI(null);
            binding.imageViewPhoto.setImageURI(Uri.parse(uri));
        });
        viewModel.getLoading().observe(getViewLifecycleOwner(), loading -> {
            binding.progressBar.setVisibility(Boolean.TRUE.equals(loading) ? View.VISIBLE : View.GONE);
            binding.buttonRecognize.setEnabled(!Boolean.TRUE.equals(loading));
            if (Boolean.TRUE.equals(loading)) {
                binding.textViewResult.setText(R.string.recognizing);
                binding.buttonSaveResult.setVisibility(View.GONE);
            }
        });
        viewModel.getResult().observe(getViewLifecycleOwner(), this::renderResult);
        viewModel.getMessage().observe(getViewLifecycleOwner(), this::renderMessage);

        binding.buttonPickImage.setOnClickListener(v -> pickImageLauncher.launch("image/*"));
        binding.buttonTakePhoto.setOnClickListener(v -> onCameraClicked());
        binding.buttonRecognize.setOnClickListener(v -> {
            String uri = viewModel.getImageUri().getValue();
            if (uri == null) {
                Toast.makeText(requireContext(), R.string.pick_photo_first, Toast.LENGTH_SHORT).show();
                return;
            }
            viewModel.recognize(uri);
        });
        binding.buttonSaveResult.setOnClickListener(v -> viewModel.saveResult());
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

    private void onCameraClicked() {
        if (ContextCompat.checkSelfPermission(requireContext(), Manifest.permission.CAMERA)
                == PackageManager.PERMISSION_GRANTED) {
            launchCamera();
        } else {
            requestCameraPermissionLauncher.launch(Manifest.permission.CAMERA);
        }
    }

    private void launchCamera() {
        File file = new File(requireContext().getCacheDir(), "camera_capture.jpg");
        cameraImageUri = FileProvider.getUriForFile(
                requireContext(),
                requireContext().getPackageName() + ".fileprovider",
                file
        );
        takePictureLauncher.launch(cameraImageUri);
    }

    private void renderResult(RecognitionResult result) {
        if (Boolean.TRUE.equals(viewModel.getLoading().getValue())) {
            return;
        }
        if (result == null) {
            binding.buttonSaveResult.setVisibility(View.GONE);
            if (viewModel.getImageUri().getValue() != null
                    && binding.textViewResult.getText().toString().equals(getString(R.string.recognizing))) {
                binding.textViewResult.setText(R.string.recognize_failed);
            }
            return;
        }
        binding.textViewResult.setText(String.format(
                "Распознано: %s (%.0f%%)\n\n%s",
                result.getBreedName(),
                result.getConfidence() * 100,
                result.getTopSummary()
        ));
        binding.buttonSaveResult.setVisibility(View.VISIBLE);
    }

    private void renderMessage(String message) {
        if (message == null) {
            return;
        }
        if ("login_required".equals(message)) {
            Toast.makeText(requireContext(), R.string.login_required, Toast.LENGTH_SHORT).show();
        } else if ("not_in_catalog".equals(message)) {
            Toast.makeText(requireContext(), R.string.breed_not_in_catalog, Toast.LENGTH_SHORT).show();
        } else if (message.startsWith("saved:")) {
            Toast.makeText(
                    requireContext(),
                    "Сохранено в избранное: " + message.substring(6),
                    Toast.LENGTH_SHORT
            ).show();
        } else if ("already".equals(message)) {
            Toast.makeText(requireContext(), "Уже в избранном", Toast.LENGTH_SHORT).show();
        }
    }
}
