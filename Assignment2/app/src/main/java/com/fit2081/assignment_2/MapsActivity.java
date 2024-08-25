package com.fit2081.assignment_2;

import androidx.fragment.app.FragmentActivity;

import android.location.Address;
import android.location.Geocoder;
import android.os.Build;
import android.os.Bundle;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.fit2081.assignment_2.databinding.ActivityMapsBinding;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mapLocation;
    private ActivityMapsBinding binding;

    public String countryToFocus;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityMapsBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        // obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        // retrieve eventLocation from intent extras
        countryToFocus = getIntent().getStringExtra("eventLocation");
        if (countryToFocus != null) {
            findCountryMoveCamera();
        }
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mapLocation = googleMap;

    }

    private void findCountryMoveCamera() {
        Geocoder geocoder = new Geocoder(this, Locale.getDefault());

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            /*
             * countryToFocus: String value, any string we want to search
             * maxResults: how many results to return if search was successful
             * successCallback method: if results are found, this method will be executed
             * runs in a background thread
             */
            geocoder.getFromLocationName(countryToFocus, 1, addresses -> {
                // if there are results, this condition would return true
                if (!addresses.isEmpty()) {
                    // run on UI thread as the user interface will update once set map location
                    runOnUiThread(() -> {
                        // define new LatLng variable using the first address from list of addresses
                        LatLng newAddressLocation = new LatLng(
                                addresses.get(0).getLatitude(),
                                addresses.get(0).getLongitude()
                        );

                        // move the camera to the middle of the country
                        mapLocation.moveCamera(CameraUpdateFactory.newLatLng(newAddressLocation));

                        // add a new Marker with title as the countryToFocus
                        mapLocation.addMarker(
                                new MarkerOptions()
                                        .position(newAddressLocation)
                                        .title(countryToFocus)
                        );

                        // set zoom level to 10
                        mapLocation.animateCamera(CameraUpdateFactory.zoomTo(10));

                        // adjust the camera to ensure the marker appears in the middle of the screen
                        mapLocation.getUiSettings().setScrollGesturesEnabled(false);
                        mapLocation.moveCamera(CameraUpdateFactory.newLatLngZoom(newAddressLocation, 10));

                        // add click listener to the map
                        mapLocation.setOnMapClickListener(latLng -> {
                            // perform reverse geocoding to get the address information for the clicked location
                            Geocoder reverseGeocoder = new Geocoder(MapsActivity.this, Locale.getDefault());
                            try {
                                List<Address> addressesList = reverseGeocoder.getFromLocation(latLng.latitude, latLng.longitude, 1);
                                if (!addressesList.isEmpty()) {
                                    Address address = addressesList.get(0);
                                    String countryName = address.getCountryName();
                                    // show a Toast message with the name of the country
                                    Toast.makeText(MapsActivity.this, "Clicked on: " + countryName, Toast.LENGTH_SHORT).show();
                                } else {
                                    // show a Toast message if the address is not found
                                    Toast.makeText(MapsActivity.this, "Country not found on clicked location", Toast.LENGTH_SHORT).show();
                                }
                            } catch (IOException e) {
                                e.printStackTrace();
                                // show a Toast message if an error occurs during reverse geocoding
                                Toast.makeText(MapsActivity.this, "Error: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                            }
                        });
                    });
                } else {
                    // show a Toast message if the address is not found
                    runOnUiThread(() -> {
                        Toast.makeText(MapsActivity.this, "Category address not found", Toast.LENGTH_SHORT).show();

                        // set Malaysia as the default location
                        LatLng malaysiaLocation = new LatLng(4.2105, 101.9758);

                        mapLocation.moveCamera(CameraUpdateFactory.newLatLng(malaysiaLocation));
                        mapLocation.addMarker(new MarkerOptions().position(malaysiaLocation).title("Malaysia"));
                        mapLocation.animateCamera(CameraUpdateFactory.zoomTo(10));
                    });
                }
            });
        }
    }



}