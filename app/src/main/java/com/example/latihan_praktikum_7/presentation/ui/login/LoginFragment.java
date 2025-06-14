package com.example.latihan_praktikum_7.presentation.ui.login;

import android.content.Context;
import android.os.Bundle;
import android.view.*;
import android.widget.*;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.latihan_praktikum_7.R;

public class LoginFragment extends Fragment {

    private EditText emailInput, passwordInput;
    private LoginActivity loginActivity;

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        if (context instanceof LoginActivity) {
            loginActivity = (LoginActivity) context;
        }
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_login, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        emailInput = view.findViewById(R.id.email_input);
        passwordInput = view.findViewById(R.id.password_input);
        Button emailLoginBtn = view.findViewById(R.id.email_login_btn);
        Button googleLoginBtn = view.findViewById(R.id.btn_sign_in);
        TextView toRegister = view.findViewById(R.id.to_register);

        emailLoginBtn.setOnClickListener(v -> {
            String email = emailInput.getText().toString();
            String password = passwordInput.getText().toString();

            loginActivity.signInWithEmail(email, password); // Panggil fungsi dari activity
        });

        googleLoginBtn.setOnClickListener(v -> {
            loginActivity.signInFromFragment(); // Panggil fungsi dari activity
        });

        toRegister.setOnClickListener(v -> requireActivity().getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.login_fragment_container, new RegisterFragment())
                .addToBackStack(null)
                .commit());
    }
}
