package com.muratalarcin.lokasyonkullanimi;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Build;
import android.os.Bundle;
import android.widget.Toast;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.Task;
import com.muratalarcin.lokasyonkullanimi.databinding.ActivityMainBinding;

public class MainActivity extends AppCompatActivity {
    private ActivityMainBinding binding;
    private int izinKontrol = 0;
    private FusedLocationProviderClient flpc;
    private Task<Location> locationTask;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        flpc = LocationServices.getFusedLocationProviderClient(this);

        binding.buttonKonum.setOnClickListener(view -> {
            izinKontrol = ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION);//manifest seçerken android seç
            if (izinKontrol == PackageManager.PERMISSION_GRANTED) {//izin onaylanırsa
                locationTask = flpc.getLastLocation();//son konum
                konumBilgisiAl();
            }else {//onaylanmazsa
                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 100);//burda , ile ayırıp diğer izinleri isteyebilirsin
            }
        });

    }

    public void konumBilgisiAl() {
        locationTask.addOnSuccessListener(location -> {
            if (location != null) {
                binding.textViewKonum.setText(location.getLatitude() + " - " + location.getLongitude());
            }else {
                binding.textViewKonum.setText("Hata Oluştu");
            }
        });
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == 100) {
            izinKontrol = ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION);//manifest seçerken android seç
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Toast.makeText(getApplicationContext(), "İzin onaylandı", Toast.LENGTH_SHORT).show();
                locationTask = flpc.getLastLocation();//son konum
                konumBilgisiAl();
            }else {
                Toast.makeText(getApplicationContext(), "İzin reddedildi", Toast.LENGTH_SHORT).show();
            }
        }
    }
}