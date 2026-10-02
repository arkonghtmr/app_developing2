package ru.mirea.fedorov.fragmentapp;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

public class BlankFragment extends Fragment {
    public static final String TAG = "BlankFragment";

    @Nullable
    @Override
    public View onCreateView(
            @NonNull LayoutInflater inflater,
            @Nullable ViewGroup container,
            @Nullable Bundle savedInstanceState
    ) {
        return inflater.inflate(R.layout.fragment_blank, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        int number = requireArguments().getInt(MainActivity.KEY_STUDENT_NUMBER);
        Log.d(TAG, MainActivity.KEY_STUDENT_NUMBER + " = " + number);
        TextView textView = view.findViewById(R.id.textViewStudentNumber);
        textView.setText(getString(R.string.student_number, number));
    }
}
