package ru.mirea.fedorov.dogguide.data.ml;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;

import org.tensorflow.lite.support.image.TensorImage;
import org.tensorflow.lite.task.core.BaseOptions;
import org.tensorflow.lite.task.vision.classifier.Classifications;
import org.tensorflow.lite.task.vision.classifier.ImageClassifier;

import java.io.InputStream;
import java.util.List;
import java.util.Locale;

import ru.mirea.fedorov.dogguide.domain.models.RecognitionResult;

public class TfliteBreedClassifier {
    private static final String MODEL_FILE = "mobilenet_v1.tflite";

    private final Context context;
    private final DogCeoLabelMapper mapper;
    private ImageClassifier classifier;

    public TfliteBreedClassifier(Context context, DogCeoLabelMapper mapper) {
        this.context = context.getApplicationContext();
        this.mapper = mapper;
        try {
            ImageClassifier.ImageClassifierOptions options =
                    ImageClassifier.ImageClassifierOptions.builder()
                            .setBaseOptions(BaseOptions.builder().setNumThreads(4).build())
                            .setMaxResults(5)
                            .build();
            classifier = ImageClassifier.createFromFileAndOptions(
                    this.context,
                    MODEL_FILE,
                    options
            );
        } catch (Exception exception) {
            classifier = null;
        }
    }

    public RecognitionResult classify(String imageUri) {
        if (classifier == null || imageUri == null || imageUri.isEmpty()) {
            return null;
        }
        Bitmap bitmap = decodeBitmap(imageUri);
        if (bitmap == null) {
            return null;
        }
        try {
            TensorImage tensorImage = TensorImage.fromBitmap(bitmap);
            List<Classifications> results = classifier.classify(tensorImage);
            if (results == null || results.isEmpty() || results.get(0).getCategories().isEmpty()) {
                return null;
            }
            List<org.tensorflow.lite.support.label.Category> categories =
                    results.get(0).getCategories();
            org.tensorflow.lite.support.label.Category chosen = categories.get(0);
            for (org.tensorflow.lite.support.label.Category category : categories) {
                if (mapper.isDog(category.getLabel())) {
                    chosen = category;
                    break;
                }
            }
            chosen = disambiguateHusky(categories, chosen);

            StringBuilder topSummary = new StringBuilder("Топ модели ImageNet:");
            int limit = Math.min(3, categories.size());
            for (int i = 0; i < limit; i++) {
                org.tensorflow.lite.support.label.Category category = categories.get(i);
                topSummary.append('\n')
                        .append(i + 1)
                        .append(". ")
                        .append(category.getLabel())
                        .append(" — ")
                        .append(Math.round(category.getScore() * 100))
                        .append('%');
            }

            String label = chosen.getLabel();
            return new RecognitionResult(
                    mapper.toBreedId(label),
                    displayName(label),
                    chosen.getScore(),
                    topSummary.toString()
            );
        } finally {
            bitmap.recycle();
        }
    }

    private Bitmap decodeBitmap(String imageUri) {
        try (InputStream stream = context.getContentResolver().openInputStream(Uri.parse(imageUri))) {
            if (stream == null) {
                return null;
            }
            BitmapFactory.Options options = new BitmapFactory.Options();
            options.inPreferredConfig = Bitmap.Config.ARGB_8888;
            Bitmap bitmap = BitmapFactory.decodeStream(stream, null, options);
            if (bitmap == null) {
                return null;
            }
            int maxSide = Math.max(bitmap.getWidth(), bitmap.getHeight());
            if (maxSide > 1024) {
                float scale = 1024f / maxSide;
                Bitmap scaled = Bitmap.createScaledBitmap(
                        bitmap,
                        Math.round(bitmap.getWidth() * scale),
                        Math.round(bitmap.getHeight() * scale),
                        true
                );
                if (scaled != bitmap) {
                    bitmap.recycle();
                }
                return scaled;
            }
            return bitmap;
        } catch (Exception exception) {
            return null;
        }
    }

    private org.tensorflow.lite.support.label.Category disambiguateHusky(
            List<org.tensorflow.lite.support.label.Category> categories,
            org.tensorflow.lite.support.label.Category chosen
    ) {
        String chosenKey = chosen.getLabel().toLowerCase(Locale.ROOT);
        if (!chosenKey.contains("eskimo")) {
            return chosen;
        }
        for (org.tensorflow.lite.support.label.Category category : categories) {
            if (category.getLabel().toLowerCase(Locale.ROOT).contains("siberian husky")) {
                return category;
            }
        }
        return chosen;
    }

    private String displayName(String label) {
        if (label.toLowerCase(Locale.ROOT).contains("eskimo")) {
            return "Husky";
        }
        if (label == null || label.isEmpty()) {
            return "";
        }
        return Character.toUpperCase(label.charAt(0)) + label.substring(1);
    }
}
