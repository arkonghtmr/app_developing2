package ru.mirea.fedorov.resultapifragmentapp;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.google.android.material.bottomsheet.BottomSheetDialogFragment;

public class BottomSheetFragment extends BottomSheetDialogFragment {
    private String pendingText;
    private TextView textViewResult;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getParentFragmentManager().setFragmentResultListener(
                DataFragment.REQUEST_KEY,
                this,
                (requestKey, bundle) -> {
                    pendingText = bundle.getString(DataFragment.BUNDLE_KEY);
                    bindResult();
                }
        );
    }

    @Nullable
    @Override
    public View onCreateView(
            @NonNull LayoutInflater inflater,
            @Nullable ViewGroup container,
            @Nullable Bundle savedInstanceState
    ) {
        return inflater.inflate(R.layout.fragment_bottom_sheet, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        textViewResult = view.findViewById(R.id.textViewResult);
        bindResult();
    }

    private void bindResult() {
        if (textViewResult == null) {
            return;
        }
        if (pendingText == null || pendingText.isEmpty()) {
            textViewResult.setText(R.string.bottom_sheet_empty);
        } else {
            textViewResult.setText(getString(R.string.received_prefix, pendingText));
        }
    }
}
