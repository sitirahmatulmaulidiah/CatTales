package com.example.latihan_praktikum_7.presentation.ui.profil;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.latihan_praktikum_7.R;
import com.example.latihan_praktikum_7.presentation.ui.login.LoginActivity;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.firebase.auth.FirebaseAuth;

public class ProfilFragment extends Fragment {

    private TextView tvEmail;
    private Button btnLogout;
    private GoogleSignInClient mGoogleSignInClient;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_profil, container, false);

        tvEmail = view.findViewById(R.id.tv_email);
        btnLogout = view.findViewById(R.id.btn_logout);

        // Ambil email dari SharedPreferences atau Google Sign-In
        String email = getEmailFromSharedPreferences();
        if (email == null) {
            email = getEmailFromGoogleAccount();
        }

        if (email != null) {
            tvEmail.setText(email);
        } else {
            tvEmail.setText("Email tidak ditemukan");
        }

        // Setup Google Sign-In client
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .build();
        mGoogleSignInClient = GoogleSignIn.getClient(requireActivity(), gso);

        // Tombol Logout
        btnLogout.setOnClickListener(v -> {
            // Logout dari Firebase
            FirebaseAuth.getInstance().signOut();
            // Logout dari Google Sign-In
            mGoogleSignInClient.signOut();

            // Clear SharedPreferences
            SharedPreferences prefs = requireActivity().getSharedPreferences("user_prefs", Context.MODE_PRIVATE);
            prefs.edit().clear().apply();

            // Pindah ke LoginActivity
            Intent intent = new Intent(requireActivity(), LoginActivity.class);
            startActivity(intent);
            requireActivity().finish();
        });

        return view;
    }

    private String getEmailFromSharedPreferences() {
        SharedPreferences prefs = requireActivity().getSharedPreferences("user_prefs", Context.MODE_PRIVATE);
        return prefs.getString("user_email", null);
    }

    private String getEmailFromGoogleAccount() {
        GoogleSignInAccount account = GoogleSignIn.getLastSignedInAccount(requireContext());
        if (account != null) {
            return account.getEmail();
        }
        return null;
    }
}
