package com.example.latihan_praktikum_7.presentation.ui.home;

import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.navigation.NavController;
import androidx.navigation.fragment.NavHostFragment;
import androidx.navigation.ui.NavigationUI;

import com.example.latihan_praktikum_7.R;
import com.example.latihan_praktikum_7.util.NotificationHelper;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.util.Random;

public class MainActivity extends AppCompatActivity {

    // 1️⃣ Deklarasi global sensor
    private SensorManager sensorManager;
    private Sensor accelerometer;
    private float lastX, lastY, lastZ;
    private long lastShakeTime = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Inisialisasi Bottom Navigation
        BottomNavigationView bottomNav = findViewById(R.id.bottom_navigation);
        NavHostFragment navHostFragment = (NavHostFragment) getSupportFragmentManager()
                .findFragmentById(R.id.nav_host_fragment);
        NavController navController = navHostFragment.getNavController();
        NavigationUI.setupWithNavController(bottomNav, navController);

        // 2️⃣ Inisialisasi Notification Channel
        NotificationHelper.createNotificationChannel(this);

        // Inisialisasi Sensor
        sensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        if (sensorManager != null) {
            accelerometer = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        }
    }

    // 3️⃣ Daftarkan sensor listener di onResume
    @Override
    protected void onResume() {
        super.onResume();
        if (accelerometer != null) {
            sensorManager.registerListener(sensorEventListener, accelerometer, SensorManager.SENSOR_DELAY_NORMAL);
        }
    }

    // Unregister listener saat onPause
    @Override
    protected void onPause() {
        super.onPause();
        sensorManager.unregisterListener(sensorEventListener);
    }

    // 4️⃣ SensorEventListener
    private final SensorEventListener sensorEventListener = new SensorEventListener() {
        @Override
        public void onSensorChanged(SensorEvent event) {
            float x = event.values[0];
            float y = event.values[1];
            float z = event.values[2];

            float deltaX = Math.abs(lastX - x);
            float deltaY = Math.abs(lastY - y);
            float deltaZ = Math.abs(lastZ - z);

            if ((deltaX > 8 || deltaY > 8 || deltaZ > 8)) {
                long currentTime = System.currentTimeMillis();
                if (currentTime - lastShakeTime > 2000) { // cooldown 2 detik
                    lastShakeTime = currentTime;

                    // Tampilkan notifikasi fakta kucing
                    String[] facts = {
                            "Meow~ Si Mpus suka digendong? Coba cek asal-usulnya di aplikasi!",
                            "Tahukah kamu? Kucing tidur 16 jam sehari.",
                            "Kucing bisa mendengar suara hingga 64 kHz."
                    };
                    int randomIndex = new Random().nextInt(facts.length);

                    NotificationHelper.showNotification(MainActivity.this, "Fakta Kucing!", facts[randomIndex]);
                }
            }

            lastX = x;
            lastY = y;
            lastZ = z;
        }

        @Override
        public void onAccuracyChanged(Sensor sensor, int accuracy) {
            // do nothing
        }
    };
}
