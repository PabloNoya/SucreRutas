package com.example.pablo.sucrerutas;

import android.Manifest;
import android.content.Intent;
import android.content.IntentSender;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.Location;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.chip.Chip;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.CompoundButton;

import com.google.android.gms.common.api.ResolvableApiException;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.LocationSettingsRequest;
import com.google.android.gms.location.LocationSettingsResponse;
import com.google.android.gms.location.SettingsClient;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PolylineOptions;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;

public class MapsActivity extends AppCompatActivity implements OnMapReadyCallback {
    private GoogleMap mMap;
    Chip chipIda, chipVuelta;
    String file, nombreLinea;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);

        Bundle bundle = getIntent().getExtras();
        nombreLinea = bundle.getString("nombreLinea", "Map");

        setTitle("Línea " + nombreLinea + ", ruta de ida");

        final SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (ContextCompat.checkSelfPermission(getApplicationContext(),
                        Manifest.permission.ACCESS_FINE_LOCATION)
                        == PackageManager.PERMISSION_GRANTED) {
                    mMap.setMyLocationEnabled(true);
                    createLocationRequest();
                } else {
                    ActivityCompat.requestPermissions(MapsActivity.this,
                            new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 123);
                }
            }
        });

        chipIda = findViewById(R.id.chipIda);
        chipIda.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked && mMap != null) {
                    setTitle("Línea " + nombreLinea + ", ruta de ida");

                    mMap.clear();
                    file = nombreLinea + "-ida.txt";
                    fillMap(file);
                }
            }
        });

        chipVuelta = findViewById(R.id.chipVuelta);
        chipVuelta.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked && mMap != null) {
                    setTitle("Línea " + nombreLinea + ", ruta de vuelta");

                    mMap.clear();
                    file = nombreLinea + "-vuelta.txt";
                    fillMap(file);
                }
            }
        });
    }

    ArrayList<LatLng> getRuta(String archivo) {
        ArrayList<LatLng> coordList = new ArrayList<>();
        try {
            InputStreamReader is = new InputStreamReader(getAssets().open(archivo));
            BufferedReader reader = new BufferedReader(is);
            String line;
            String[] coordinates;

            while ((line = reader.readLine()) != null) {
                coordinates = line.split(",");
                Float lat = Float.valueOf(coordinates[0]);
                Float lng = Float.valueOf(coordinates[1]);
                coordList.add(new LatLng(lat, lng));
            }
        } catch (IOException e) {
            setTitle("ERROR");
        }
        return coordList;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_search:
                Intent search = new Intent(getApplicationContext(), SearchActivity.class);
                startActivity(search);
                return true;

            case R.id.action_about:
                Intent about = new Intent(getApplicationContext(), AboutActivity.class);
                startActivity(about);
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        mMap.getUiSettings().setMyLocationButtonEnabled(false);
        LatLng sucre = new LatLng(-19.035, -65.259);
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(sucre, 14.2f));
        file = nombreLinea + "-ida.txt";
        fillMap(file);
    }

    void fillMap(String file){
        ArrayList<LatLng> ruta = getRuta(file);
        mMap.addPolyline(new PolylineOptions()
                .addAll(ruta)
                .width(5)
                .color(Color.BLUE));

        mMap.addMarker(new MarkerOptions().
                position(ruta.get(0)).
                icon(BitmapDescriptorFactory.fromAsset("marker_inicio.png"))
                .title("inicio"));
        mMap.addMarker(new MarkerOptions()
                .position(ruta.get(ruta.size()-1))
                .title("fin"));
    }

    private void createLocationRequest() {
        LocationRequest mLocationRequest = LocationRequest.create();
        mLocationRequest.setInterval(10000);
        mLocationRequest.setFastestInterval(500);
        mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);

        LocationSettingsRequest.Builder builder = new LocationSettingsRequest.Builder().
                addLocationRequest(mLocationRequest);

        SettingsClient client = LocationServices.getSettingsClient(getApplicationContext());
        Task<LocationSettingsResponse> task = client.checkLocationSettings(builder.build());
        task.addOnSuccessListener(this, new OnSuccessListener<LocationSettingsResponse>() {
            @Override
            public void onSuccess(LocationSettingsResponse locationSettingsResponse) {
                moveCameraToLocation();
            }
        });

        task.addOnFailureListener(this, new OnFailureListener() {
            private static final int REQUEST_CHECK_SETTINGS = 123;

            @Override
            public void onFailure(@NonNull Exception e) {
                if (e instanceof ResolvableApiException) {
                    try {
                        ResolvableApiException resolvable = (ResolvableApiException) e;
                        resolvable.startResolutionForResult(MapsActivity.this,
                                REQUEST_CHECK_SETTINGS);
                    } catch (IntentSender.SendIntentException e1) {
                        e1.printStackTrace();
                    }
                }
            }
        });
    }

    private void moveCameraToLocation() {
        FusedLocationProviderClient mFusedLocation = LocationServices.getFusedLocationProviderClient(MapsActivity.this);
        mFusedLocation.getLastLocation().addOnSuccessListener(this, new OnSuccessListener<Location>() {
            @Override
            public void onSuccess(Location location) {
                if (location != null) {
                    double lat = location.getLatitude();
                    double lng = location.getLongitude();
                    mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(lat, lng), 15));
                }
            }
        });
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        if (requestCode == 123) {
            if (permissions.length == 1 &&
                    permissions[0].equals(Manifest.permission.ACCESS_FINE_LOCATION) &&
                    grantResults[0] == PackageManager.PERMISSION_GRANTED &&
                    grantResults[0] == ContextCompat.checkSelfPermission(getApplicationContext(),
                            Manifest.permission.ACCESS_FINE_LOCATION)) {
                mMap.setMyLocationEnabled(true);
                createLocationRequest();
            }
        }
    }
}
