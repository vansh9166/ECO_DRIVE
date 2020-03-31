package com.example.ecodrive5;

import android.Manifest;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
//import android.support.annotation.NonNull;
//import android.support.v4.app.ActivityCompat;
//import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
//import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;


import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.FragmentActivity;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.List;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback, LocationListener, GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener {

    //widgets
    private EditText SourceTF, DestinationTF;
    private Button StartRideTF;
    private Spinner spinner;
    private ArrayAdapter<CharSequence> adapter;
    private Spinner spinner2;
    private ArrayAdapter<CharSequence> adapter2;
    private Spinner spinner10;
    private ArrayAdapter<CharSequence> adapter10;
    private Spinner spinner9;
    private ArrayAdapter<CharSequence> adapter9;
    private Spinner spinner11;
    private ArrayAdapter<CharSequence> adapter11;

    //Variables
    private static final String TAG = "CKKK";
    private GoogleMap mMap;
    private SupportMapFragment mapFragment;
    private static boolean mLocationPermissionGranted = false;
    private Location mLastKnownLocation;
    private LatLng mStartLocation, mEndLocation, searchLocation;
    private static final int PERMISSIONS_REQUEST_ACCESS_CODE = 12345;
    private static final float DEFAULT_ZOOM = 15f;
    private FusedLocationProviderClient mFusedLocationProviderClient;
    private String Vehicle_TypeTF, Fuel_TypeTF, Journey_Time_Hour_S, Journey_Time_Min_S, Journey_Time_TimeZone_S;
    private FirebaseDatabase database;
    private DatabaseReference myRef;
    private FirebaseUser user;
    private boolean Source = false, Destination = false;
    private float array[] = new float[10];
    private String Address=null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);

        SourceTF = findViewById(R.id.sourceF);
        DestinationTF = findViewById(R.id.destinationF);
        StartRideTF = findViewById(R.id.start_rideF);
        user = FirebaseAuth.getInstance().getCurrentUser();
        database = FirebaseDatabase.getInstance();

        getLocationPermission();
        BottomSheetViews();
        StartRide();
    }


    @Override
    public void onConnected(Bundle bundle) {
        //  mPlaceArrayAdapter.setGoogleApiClient(mGoogleApiClient);
        Log.i(TAG, "Google Places API connected.");

    }

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {
      /*  Log.e(TAG, "Google Places API connection failed with error code: "
                + connectionResult.getErrorCode());

        Toast.makeText(this,
                "Google Places API connection failed with error code:" +
                        connectionResult.getErrorCode(),
                Toast.LENGTH_LONG).show();*/
    }

    @Override
    public void onConnectionSuspended(int i) {
     /*   mPlaceArrayAdapter.setGoogleApiClient(null);
        Log.e(TAG, "Google Places API connection suspended.");*/
    }

    private void StartRide() {
        StartRideTF.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    if (!SourceTF.getText().toString().trim().equals("") & !DestinationTF.getText().toString().trim().equals("")) {

                        myRef = database.getReference("UserDatabase").child(user.getUid()).child("Bookings");
                        UserDatabase ud = new UserDatabase();
                        ud.setSource(SourceTF.getText().toString());
                        ud.setDestination(DestinationTF.getText().toString());
                        ud.setVehicle(Vehicle_TypeTF);
                        ud.setFuel(Fuel_TypeTF);
                        ud.setTime(Journey_Time_Hour_S + ":" + Journey_Time_Min_S + "" + Journey_Time_TimeZone_S);
                        myRef.setValue(ud);
                        Toast.makeText(MapsActivity.this, "Ride Booked", Toast.LENGTH_SHORT).show();

                    } else {
                        SourceTF.setError("Enter Source");
                        DestinationTF.setError("Enter Destination");
                    }
                } catch (Exception e) {
                    Log.e(TAG, "onClick: " + e);
                }

            }
        });
    }

    private void BottomSheetViews() {

        spinner11 = findViewById(R.id.spinner11);
        adapter11 = ArrayAdapter.createFromResource(this, R.array.minutes, android.R.layout.simple_spinner_item);
        adapter11.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner11.setAdapter(adapter11);
        spinner11.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int a, long b) {
                Journey_Time_Min_S = parent.getItemAtPosition(a).toString();
                //     Toast.makeText(getBaseContext(), parent.getItemAtPosition(a)+ " selected",Toast.LENGTH_LONG).show();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        spinner10 = findViewById(R.id.spinner10);
        adapter10 = ArrayAdapter.createFromResource(this, R.array.TimeZones, android.R.layout.simple_spinner_item);
        adapter10.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner10.setAdapter(adapter10);
        spinner10.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int a, long b) {

                Journey_Time_TimeZone_S = parent.getItemAtPosition(a).toString();
                //  Toast.makeText(getBaseContext(), parent.getItemAtPosition(a)+ " selected",Toast.LENGTH_LONG).show();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
            }
        });

        spinner9 = findViewById(R.id.spinner9);
        adapter9 = ArrayAdapter.createFromResource(this, R.array.hours, android.R.layout.simple_spinner_item);
        adapter9.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner9.setAdapter(adapter9);
        spinner9.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int a, long b) {

                Journey_Time_Hour_S = parent.getItemAtPosition(a).toString();

                //  Toast.makeText(getBaseContext(), parent.getItemAtPosition(a)+ " selected",Toast.LENGTH_LONG).show();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
            }
        });

        spinner = findViewById(R.id.spinner);
        adapter = ArrayAdapter.createFromResource(this, R.array.vehicle_types, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                Vehicle_TypeTF = parent.getItemAtPosition(position).toString();
                //      Toast.makeText(getBaseContext(), parent.getItemAtPosition(position)+ " selected",Toast.LENGTH_LONG).show();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        spinner2 = findViewById(R.id.spinner2);
        adapter2 = ArrayAdapter.createFromResource(this, R.array.fuel_types, android.R.layout.simple_spinner_item);
        adapter2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner2.setAdapter(adapter2);
        spinner2.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int i, long l) {

                Fuel_TypeTF = parent.getItemAtPosition(i).toString();
                //      Toast.makeText(getBaseContext(), parent.getItemAtPosition(i)+ " selected",Toast.LENGTH_LONG).show();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
    }

    private void moveCamera(LatLng latLng, float DEFAULT_ZOOM, String title) {

        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, DEFAULT_ZOOM));
        MarkerOptions options = new MarkerOptions().position(latLng).title(title);
        mMap.addMarker(options);

    }

    private void getLocationPermission() {

        String[] permission = {android.Manifest.permission.ACCESS_FINE_LOCATION, android.Manifest.permission.ACCESS_COARSE_LOCATION};

        if (ContextCompat.checkSelfPermission(this.getApplicationContext(), android.Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            if (ContextCompat.checkSelfPermission(this.getApplicationContext(), Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
                Log.i(TAG, "getLocationPermission: i am here");
                mLocationPermissionGranted = true;
                initMap();
            } else {
                ActivityCompat.requestPermissions(this, permission, PERMISSIONS_REQUEST_ACCESS_CODE);
            }
        } else {
            ActivityCompat.requestPermissions(this, permission, PERMISSIONS_REQUEST_ACCESS_CODE);
        }

    }

    private void initMap() {
        mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(MapsActivity.this);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String permissions[], @NonNull int[] grantResults) {
        mLocationPermissionGranted = false;
        switch (requestCode) {
            case PERMISSIONS_REQUEST_ACCESS_CODE: {
                if (grantResults.length > 0) {
                    for (int i = 0; i < grantResults.length; i++) {
                        if (grantResults[i] != PackageManager.PERMISSION_GRANTED) {

                            mLocationPermissionGranted = false;
                            Log.i(TAG, "onRequestPermissionsResult: " + mLocationPermissionGranted + ": " + grantResults[i]);
                            return;
                        }
                    }
                    mLocationPermissionGranted = true;
                    Log.i("ck", "onRequestPermissionsResult: " + mLocationPermissionGranted);
                    initMap();
                }
            }
        }
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        Toast.makeText(MapsActivity.this, "map is ready", Toast.LENGTH_SHORT).show();
        if (mLocationPermissionGranted) {
            getDeviceLocation();
            Log.i(TAG, "onMapReady: i need to know the truth");
            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED &&
                    ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                return;
            }
            mMap.setMyLocationEnabled(true);
            setAddressOnTouch();
            //onTouchSetAddMarker();
            init();
        }
    }

    private void getDeviceLocation() {

        mFusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);

        try {
            if (mLocationPermissionGranted) {
                Task locationResult = mFusedLocationProviderClient.getLastLocation();
                locationResult.addOnCompleteListener(this, new OnCompleteListener() {
                    @Override
                    public void onComplete(@NonNull Task task) {
                        if (task.isSuccessful()) {
                            Log.i(TAG, "onComplete: ");
                            mLastKnownLocation = (Location) task.getResult();
                            Log.i(TAG, "onComplete: "+mLastKnownLocation.getLongitude()+" : "+ mLastKnownLocation.getLatitude());
                            moveCamera(new LatLng(mLastKnownLocation.getLatitude(), mLastKnownLocation.getLongitude()), DEFAULT_ZOOM, "My Location");
                        } else {
                            Log.d(TAG, "Current location is null. Using defaults.");
                            Log.e(TAG, "Exception: %s", task.getException());
                           /* mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(mDefaultLocation, DEFAULT_ZOOM));
                           // mMap.getUiSettings().setMyLocationButtonEnabled(false);*/
                        }
                    }
                });
            }
        } catch (SecurityException e) {
            Log.e("Exception: %s", e.getMessage());
        }
    }

    private void onTouchSetAddMarker() {
        mMap.setOnMapClickListener(new GoogleMap.OnMapClickListener() {
            @Override
            public void onMapClick(LatLng point) {
                mMap.clear();
                Address = GeoLocateViaLatLng(point);
                Log.i(TAG, "onMapClick: "+Address);
                MarkerOptions options = new MarkerOptions();
                if (Source) {
                    options.position(point).title("Source :"+Address);
                    SourceTF.setText(Address);
                } else if (Destination) {
                    options.position(point).title("Destination :"+Address);
                    DestinationTF.setText(Address);
                }
                mMap.addMarker(options);
            }
        });

    }

    private void setAddressOnTouch(){
        SourceTF.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean b) {
                // SourceTF.setText(adddress);
                Source=true;
                Destination=false;
                onTouchSetAddMarker();
                Log.i("sk", "onFocusChange: " + b + " : ");
            }
        });

        DestinationTF.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean b) {
                Destination=true;
                Source=false;
                onTouchSetAddMarker();
                //DestinationTF.setText(adddress);
                Log.i("skk", "onFocusChange: " + b + " : ");
            }
        });
    }

    private String GeoLocateViaLatLng(LatLng points) {
        /*
        String state = addresses.get(0).getAdminArea();
        String country = addresses.get(0).getCountryName();
        String postalCode = addresses.get(0).getPostalCode();
        String knownName = addresses.get(0).getFeatureName();*/

        Geocoder geocoder = new Geocoder(MapsActivity.this);
        List<Address> list = new ArrayList<>();

        try {
            list = geocoder.getFromLocation(points.latitude, points.longitude, 1);
        } catch (Exception e) {
            Log.e(TAG, "geolocation Exception " + e.getMessage());
        }

        if (list.size() > 0) {
            String address = list.get(0).getAddressLine(0); // If any additional address line present than only, check with max available address lines by getMaxAddressLineIndex()
            String city = list.get(0).getLocality();
            Log.d(TAG, "geoLocate: " + address + ":" + city);
            return address;
        } else {
            return null;
        }
    }

    private void init() {

        SourceTF.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int actionID, KeyEvent keyEvent) {
                if (actionID == EditorInfo.IME_ACTION_SEARCH
                        || actionID == EditorInfo.IME_ACTION_DONE
                        || keyEvent.getAction() == keyEvent.ACTION_DOWN
                        || keyEvent.getAction() == keyEvent.KEYCODE_ENTER) {

                    if (geoLocateViaString(SourceTF.getText().toString().trim()) != null && !SourceTF.getText().toString().trim().isEmpty()) {
                        mStartLocation = geoLocateViaString(SourceTF.getText().toString().trim());
                    } else {
                        SourceTF.setError("Enter Correct Location");
                    }
                }
                return false;
            }
        });

        DestinationTF.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int actionID, KeyEvent keyEvent) {
                if (actionID == EditorInfo.IME_ACTION_SEARCH
                        || actionID == EditorInfo.IME_ACTION_DONE
                        || keyEvent.getAction() == keyEvent.ACTION_DOWN
                        || keyEvent.getAction() == keyEvent.KEYCODE_ENTER) {

                    if (geoLocateViaString(DestinationTF.getText().toString().trim()) != null && !DestinationTF.getText().toString().trim().isEmpty()) {
                        mEndLocation = geoLocateViaString(DestinationTF.getText().toString().trim());
                        if (mStartLocation != null) {
                            Log.i(TAG, "onEditorAction: ");
                            CalculateDistance(mStartLocation, mEndLocation);
                        } else {
                            SourceTF.setError("Enter Correct Location");
                        }
                    } else {
                        DestinationTF.setError("Enter Correct Location");
                    }
                }
                return false;
            }
        });
    }

    void CalculateDistance(LatLng SourceLocation,LatLng EndLocation){
        Location.distanceBetween(SourceLocation.latitude,SourceLocation.latitude,EndLocation.latitude,EndLocation.latitude,array);
        MarkerOptions options = new MarkerOptions();
        Log.i(TAG, "CalculateDistance: "+array[0]);
        options.position(EndLocation).title("Distance "+(float)array[0]/1000+"km");
        mMap.addMarker(options);
    }

    private LatLng geoLocateViaString(String SearchSource) {

        Geocoder geocoder = new Geocoder(MapsActivity.this);
        List<Address>  list = new ArrayList<>();

        try{
            list = geocoder.getFromLocationName(SearchSource,1);
        }
        catch (Exception e){
            Log.e(TAG,"geolocation Exception " + e.getMessage());
        }

        if(list.size() > 0){
            Address address = list.get(0);
            Log.d(TAG, "geoLocate: "+ address.toString());
            searchLocation = new LatLng(address.getLatitude(),address.getLongitude());
            Log.i(TAG, "geoLocate: "+searchLocation.latitude+":"+searchLocation.longitude);
            moveCamera(searchLocation,DEFAULT_ZOOM,address.getAddressLine(0));
            return searchLocation;
        }
        else{
            return null;
        }
    }


    @Override
    public void onLocationChanged(Location location) {
    }


    @Override
    public void onStatusChanged(String s, int i, Bundle bundle) {

    }

    @Override
    public void onProviderEnabled(String s) {

    }

    @Override
    public void onProviderDisabled(String s) {

    }
}
