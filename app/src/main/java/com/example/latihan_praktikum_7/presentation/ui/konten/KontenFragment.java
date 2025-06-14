package com.example.latihan_praktikum_7.presentation.ui.konten;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.latihan_praktikum_7.R;
import com.example.latihan_praktikum_7.presentation.adapter.CatAdapter;
import com.example.latihan_praktikum_7.data.entity.Cat;
import com.example.latihan_praktikum_7.presentation.viewmodel.CatViewModel;

import java.util.ArrayList;
import java.util.List;

public class KontenFragment extends Fragment {
    private CatViewModel catViewModel;
    private EditText etName, etOrigin, etDescription;
    private Button btnSave, btnUpdate, btnDelete;
    private RecyclerView recyclerView;
    private CatAdapter adapter;
    private List<Cat> catList = new ArrayList<>();
    private int selectedAnimalId = -1;

    private EditText etSearch;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_konten, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        catViewModel = new ViewModelProvider(requireActivity()).get(CatViewModel.class);

        etName = view.findViewById(R.id.et_name);
        etOrigin = view.findViewById(R.id.et_origin);
        etDescription = view.findViewById(R.id.et_description);
        etSearch = view.findViewById(R.id.et_search);

        etSearch.addTextChangedListener(new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
                searchCats(s.toString());
        }

        @Override
        public void afterTextChanged(Editable s) {}
        });

        btnSave = view.findViewById(R.id.btn_save);
        btnUpdate = view.findViewById(R.id.btn_update);
        btnDelete = view.findViewById(R.id.btn_delete);
        recyclerView = view.findViewById(R.id.recycler_view);

        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        adapter = new CatAdapter(catList, animal -> {
            etName.setText(animal.getName());
            etOrigin.setText(animal.getOrigin());
            etDescription.setText(animal.getDescription());
            selectedAnimalId = animal.getId();
        });
        recyclerView.setAdapter(adapter);

        catViewModel.getCatList().observe(getViewLifecycleOwner(), list -> {
            catList.clear();
            catList.addAll(list);
            adapter.notifyDataSetChanged();
        });

        catViewModel.fetchCatsFromApi();

        btnSave.setOnClickListener(v -> {
            String name = etName.getText().toString();
            String origin = etOrigin.getText().toString();
            String description = etDescription.getText().toString();

            if (name.isEmpty() || origin.isEmpty() || description.isEmpty()) {
                Toast.makeText(getContext(), "Semua kolom harus diisi!", Toast.LENGTH_SHORT).show();
                return;
            }

            catViewModel.saveData(name, origin, description);
            clearInputFields();
        });

        btnUpdate.setOnClickListener(v -> {
            if (selectedAnimalId == -1) {
                Toast.makeText(getContext(), "Pilih data terlebih dahulu!", Toast.LENGTH_SHORT).show();
                return;
            }

            String name = etName.getText().toString();
            String origin = etOrigin.getText().toString();
            String description = etDescription.getText().toString();

            if (name.isEmpty() || origin.isEmpty() || description.isEmpty()) {
                Toast.makeText(getContext(), "Semua kolom harus diisi!", Toast.LENGTH_SHORT).show();
                return;
            }

            catViewModel.updateData(selectedAnimalId, name, origin, description);
            clearInputFields();
        });

        btnDelete.setOnClickListener(v -> {
            if (selectedAnimalId == -1) {
                Toast.makeText(getContext(), "Pilih data terlebih dahulu!", Toast.LENGTH_SHORT).show();
                return;
            }

            catViewModel.deleteData(selectedAnimalId);
            clearInputFields();
        });
    }

    private void searchCats(String keyword) {
        new Thread(() -> {
            List<Cat> results = catViewModel.searchCats(keyword);
            requireActivity().runOnUiThread(() -> {
                catList.clear();
                catList.addAll(results);
                adapter.notifyDataSetChanged();
            });
        }).start();
    }


    private void clearInputFields() {
        etName.setText("");
        etOrigin.setText("");
        etDescription.setText("");
        selectedAnimalId = -1;
    }
}
