package com.example.latihan_praktikum_7.presentation.viewmodel;

import android.app.Application;
import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MutableLiveData;

import com.example.latihan_praktikum_7.data.database.DatabaseContract;
import com.example.latihan_praktikum_7.data.database.DatabaseHelper;
import com.example.latihan_praktikum_7.data.entity.Cat;
import com.example.latihan_praktikum_7.data.network.ApiService;
import com.example.latihan_praktikum_7.data.network.RetrofitClient;
import com.example.latihan_praktikum_7.util.NotificationHelper;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CatViewModel extends AndroidViewModel {
    private final DatabaseHelper dbHelper;
    private final MutableLiveData<List<Cat>> catList = new MutableLiveData<>();

    public CatViewModel(@NonNull Application application) {
        super(application);
        dbHelper = new DatabaseHelper(application);
        fetchCatsFromApi();
    }

    public MutableLiveData<List<Cat>> getCatList() {
        return catList;
    }

    public void fetchCatsFromApi() {
        ApiService apiService = RetrofitClient.getClient();
        Call<List<Cat>> call = apiService.getCats();

        call.enqueue(new Callback<List<Cat>>() {
            @Override
            public void onResponse(Call<List<Cat>> call, Response<List<Cat>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    saveAnimalsToDatabase(response.body());
                }
            }

            @Override
            public void onFailure(Call<List<Cat>> call, Throwable t) {
                Log.e("API_ERROR", "Failed to fetch data: " + t.getMessage());
            }
        });
    }

    public void saveAnimalsToDatabase(List<Cat> cats) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        db.execSQL("DELETE FROM " + DatabaseContract.CatEntry.TABLE_NAME);

        for (Cat cat : cats) {
            db.execSQL("INSERT INTO " + DatabaseContract.CatEntry.TABLE_NAME +
                            " (" + DatabaseContract.CatEntry.COLUMN_ID + ", " +
                            DatabaseContract.CatEntry.COLUMN_NAME + ", " +
                            DatabaseContract.CatEntry.COLUMN_ORIGIN + ", " +
                            DatabaseContract.CatEntry.COLUMN_DESCRIPTION + ") VALUES (?, ?, ?, ?)",
                    new Object[]{cat.getId(), cat.getName(), cat.getOrigin(), cat.getDescription()});
        }

        loadCatsFromDatabase();
    }

    public void loadCatsFromDatabase() {
        List<Cat> cats = new ArrayList<>();
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM " + DatabaseContract.CatEntry.TABLE_NAME +
                " ORDER BY id DESC", null);

        while (cursor.moveToNext()) {
            cats.add(new Cat(
                    cursor.getInt(0),
                    cursor.getString(1),
                    cursor.getString(2),
                    cursor.getString(3)
            ));
        }
        cursor.close();
        catList.postValue(cats);
    }

    public void saveData(String name, String origin, String habitat) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(DatabaseContract.CatEntry.COLUMN_NAME, name);
        values.put(DatabaseContract.CatEntry.COLUMN_ORIGIN, origin);
        values.put(DatabaseContract.CatEntry.COLUMN_DESCRIPTION, habitat);

        long result = db.insert(DatabaseContract.CatEntry.TABLE_NAME, null, values);
        loadCatsFromDatabase();

        // Tambahkan notifikasi setelah insert berhasil
        if (result != -1) {
            NotificationHelper.showNotification(
                    getApplication(),
                    "Data Baru Ditambahkan",
                    "Kucing bernama " + name + " berhasil disimpan."
            );
        }
    }

    public void updateData(int id, String name, String origin, String habitat) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(DatabaseContract.CatEntry.COLUMN_NAME, name);
        values.put(DatabaseContract.CatEntry.COLUMN_ORIGIN, origin);
        values.put(DatabaseContract.CatEntry.COLUMN_DESCRIPTION, habitat);

        int rowsAffected = db.update(DatabaseContract.CatEntry.TABLE_NAME, values,
                DatabaseContract.CatEntry.COLUMN_ID + "=?",
                new String[]{String.valueOf(id)});
        loadCatsFromDatabase();

        // Notifikasi setelah update berhasil
        if (rowsAffected > 0) {
            NotificationHelper.showNotification(
                    getApplication(),
                    "Data Diperbarui",
                    "Kucing dengan nama " + name + " berhasil diperbarui."
            );
        }
    }

    public void deleteData(int id) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        int rowsDeleted = db.delete(DatabaseContract.CatEntry.TABLE_NAME,
                DatabaseContract.CatEntry.COLUMN_ID + "=?",
                new String[]{String.valueOf(id)});
        loadCatsFromDatabase();

        // Notifikasi setelah hapus berhasil
        if (rowsDeleted > 0) {
            NotificationHelper.showNotification(
                    getApplication(),
                    "Data Dihapus",
                    "Data kucing berhasil dihapus."
            );
        }
    }


    public List<Cat> searchCats(String keyword) {
        return dbHelper.searchCats(keyword);
    }
}
