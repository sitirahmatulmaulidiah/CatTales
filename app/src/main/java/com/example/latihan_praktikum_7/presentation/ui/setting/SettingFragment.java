package com.example.latihan_praktikum_7.presentation.ui.setting;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.Switch;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.fragment.app.Fragment;

import com.example.latihan_praktikum_7.R;

public class SettingFragment extends Fragment {

    private static final String PREFS_NAME = "user_prefs";
    private static final String KEY_DARK_MODE = "dark_mode";

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_setting, container, false);

        Switch switchTheme = view.findViewById(R.id.switch_theme);

        // Load saved theme preference
        SharedPreferences prefs = requireContext().getSharedPreferences(PREFS_NAME, 0);
        boolean isDarkMode = prefs.getBoolean(KEY_DARK_MODE, false);
        switchTheme.setChecked(isDarkMode);

        // Set initial theme
        updateTheme(isDarkMode);

        // Listener toggle switch tema
        switchTheme.setOnCheckedChangeListener((CompoundButton buttonView, boolean isChecked) -> {
            updateTheme(isChecked);

            // Simpan pilihan tema ke SharedPreferences
            prefs.edit().putBoolean(KEY_DARK_MODE, isChecked).apply();
        });

        return view;
    }

    private void updateTheme(boolean darkMode) {
        if (darkMode) {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
        } else {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
        }
    }
}
