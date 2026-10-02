package ru.mirea.fedorov.resultapifragmentapp;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

public class DataFragment extends Fragment {
    public static final String REQUEST_KEY = "requestKey";
    public static final String BUNDLE_KEY = "key";

    private FragmentListener listener;

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        if (context instanceof FragmentListener) {
            listener = (FragmentListener) context;
        }
    }

    @Nullable
    @Override
    public View onCreateView(
            @NonNull LayoutInflater inflater,
            @Nullable ViewGroup container,
            @Nullable Bundle savedInstanceState
    ) {
        return inflater.inflate(R.layout.fragment_data, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        EditText editTextInfo = view.findViewById(R.id.editTextInfo);
        view.findViewById(R.id.buttonOpenBottomSheet).setOnClickListener(v -> {
            String text = editTextInfo.getText().toString();
            Bundle result = new Bundle();
            result.putString(BUNDLE_KEY, text);
            getChildFragmentManager().setFragmentResult(REQUEST_KEY, result);
            if (listener != null) {
                listener.sendResult(text);
            }
            BottomSheetFragment bottomSheet = new BottomSheetFragment();
            bottomSheet.show(getChildFragmentManager(), "ModalBottomSheet");
        });
    }

    @Override
    public void onDetach() {
        super.onDetach();
        listener = null;
    }
}
