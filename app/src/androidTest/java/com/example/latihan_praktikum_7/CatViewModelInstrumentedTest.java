package com.example.latihan_praktikum_7;

import android.content.Context;

import androidx.arch.core.executor.testing.InstantTaskExecutorRule;
import androidx.lifecycle.Observer;
import androidx.test.core.app.ApplicationProvider;
import androidx.test.ext.junit.runners.AndroidJUnit4;

import com.example.latihan_praktikum_7.data.entity.Cat;
import com.example.latihan_praktikum_7.presentation.viewmodel.CatViewModel;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.List;

import static org.junit.Assert.*;

@RunWith(AndroidJUnit4.class)
public class CatViewModelInstrumentedTest {

    private CatViewModel viewModel;
    private Context context;

    @Rule
    public InstantTaskExecutorRule instantTaskExecutorRule = new InstantTaskExecutorRule();

    @Before
    public void setUp() {
        context = ApplicationProvider.getApplicationContext();
        viewModel = new CatViewModel(ApplicationProvider.getApplicationContext());
    }

    @After
    public void tearDown() {
        // bisa kosong atau clear database kalau perlu
    }

    @Test
    public void testSaveAndLoadCat() {
        // Tambahkan data
        viewModel.saveData("British Shorthair", "United Kingdom", "Calm and affectionate");

        // Ambil data setelah disimpan
        List<Cat> cats = viewModel.getCatList().getValue();

        // Karena load async, tambahkan observer langsung
        viewModel.getCatList().observeForever(new Observer<List<Cat>>() {
            @Override
            public void onChanged(List<Cat> cats) {
                // Harusnya tidak null
                assertNotNull(cats);
                assertTrue(cats.size() > 0);

                // Data pertama sesuai
                Cat firstCat = cats.get(0);
                assertEquals("British Shorthair", firstCat.getName());

                // Hapus observer biar tidak leak
                viewModel.getCatList().removeObserver(this);
            }
        });

        // Load data dari database
        viewModel.loadCatsFromDatabase();
    }
}
