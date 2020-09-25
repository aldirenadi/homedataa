package com.aldi.dacari.Activity;

import android.app.Activity;
import android.content.Intent;
import android.location.Location;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.aldi.dacari.R;
import com.mapbox.api.directions.v5.models.DirectionsResponse;
import com.mapbox.api.directions.v5.models.DirectionsRoute;
import com.mapbox.geojson.Point;
import com.mapbox.mapboxsdk.Mapbox;
import com.mapbox.mapboxsdk.annotations.Marker;
import com.mapbox.mapboxsdk.camera.CameraUpdateFactory;
import com.mapbox.mapboxsdk.geometry.LatLng;
import com.mapbox.mapboxsdk.maps.MapView;
import com.mapbox.mapboxsdk.maps.MapboxMap;
import com.mapbox.mapboxsdk.maps.OnMapReadyCallback;
import com.mapbox.mapboxsdk.plugins.locationlayer.LocationLayerMode;
import com.mapbox.mapboxsdk.plugins.locationlayer.LocationLayerPlugin;
import com.mapbox.services.android.navigation.ui.v5.NavigationLauncher;
import com.mapbox.services.android.navigation.ui.v5.NavigationViewOptions;
import com.mapbox.services.android.navigation.ui.v5.route.NavigationMapRoute;
import com.mapbox.services.android.navigation.v5.navigation.NavigationRoute;
import com.mapbox.services.android.telemetry.location.LocationEngine;
import com.mapbox.services.android.telemetry.location.LocationEngineListener;
import com.mapbox.services.android.telemetry.location.LocationEnginePriority;
import com.mapbox.services.android.telemetry.location.LocationEngineProvider;
import com.mapbox.services.android.telemetry.permissions.PermissionsListener;
import com.mapbox.services.android.telemetry.permissions.PermissionsManager;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class NavMapsHomeActivity extends AppCompatActivity implements LocationEngineListener, PermissionsListener {

    private MapView mapView;
    private MapboxMap map;
    private PermissionsManager permissionsManager;
    private LocationLayerPlugin locationPlugin;
    private LocationEngine locationEngine;
    private Location originLocation;
    private Marker destinationMarker;
    private LatLng originCoord;
    private LatLng destinationCoord;

    private Button button;

    private ImageButton ibSearch;

    // variables for calculating and drawing a route
    private Point originPosition;
    private Point destinationPosition;
    private DirectionsRoute currentRoute;
    private static final String TAG = "DirectionsActivity";
    private NavigationMapRoute navigationMapRoute;

    private double destlng, destlat;
    final static int REQUEST_CODE=100;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_nav_maps_home);

        Mapbox.getInstance(this, getString(R.string.access_token));
        setContentView(R.layout.activity_nav_maps_home);
        mapView = (MapView) findViewById(R.id.mapView);
        mapView.onCreate(savedInstanceState);

        mapView.getMapAsync(new OnMapReadyCallback() {
            @Override
            public void onMapReady(final MapboxMap mapboxMap) {
                map = mapboxMap;
                enableLocationPlugin();

                originCoord = new LatLng(originLocation.getLatitude(), originLocation.getLongitude());
                originPosition = Point.fromLngLat(originCoord.getLongitude(), originCoord.getLatitude());

                button.setEnabled(true);
                button.setBackgroundResource(R.color.mapbox_navigation_route_alternative_color);
                button.setVisibility(View.INVISIBLE);
            }
        });

        button = findViewById(R.id.startButton);
        button.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Point origin = originPosition;
                Point destination = destinationPosition;

                boolean simulateRoute = true;
                NavigationViewOptions options = NavigationViewOptions.builder()
                        .origin(origin)
                        .destination(destination)
                        .shouldSimulateRoute(simulateRoute)
                        .build();

                NavigationLauncher.startNavigation(NavMapsHomeActivity.this, options);
            }
        });
    }
    public void git(View v) {
        Intent intent=new Intent(this,NavMapsListActivity.class);
        startActivityForResult(intent,REQUEST_CODE);
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent returnIntent) {
        super.onActivityResult(requestCode, resultCode, returnIntent);
        if(requestCode==REQUEST_CODE)
            if(resultCode== Activity.RESULT_OK){
                String lat=returnIntent.getStringExtra("lat");
                String lng=returnIntent.getStringExtra("lng");
                destlng=Double.parseDouble(lng);
                destlat=Double.parseDouble(lat);
                destinationCoord = new LatLng(destlat,destlng);
                destinationPosition = Point.fromLngLat(destinationCoord.getLongitude(), destinationCoord.getLatitude());
                getRoute(originPosition,destinationPosition);
                button.setVisibility(View.VISIBLE);
                //Toast.makeText(getApplicationContext(),lng,Toast.LENGTH_LONG).show();
            }
    }
    @SuppressWarnings( {"MissingPermission"})
    private void enableLocationPlugin() {
        // Check if permissions are enabled and if not request
        if (PermissionsManager.areLocationPermissionsGranted(this)) {
            // Create an instance of LOST location engine
            initializeLocationEngine();

            locationPlugin = new LocationLayerPlugin(mapView, map, locationEngine);
            locationPlugin.setLocationLayerEnabled(LocationLayerMode.TRACKING);
        } else {
            permissionsManager = new PermissionsManager(this);
            permissionsManager.requestLocationPermissions(this);
        }
    }
    @SuppressWarnings( {"MissingPermission"})
    public void tıkla(View v){
        initializeLocationEngine();
    }
    private void initializeLocationEngine() {
        locationEngine = new LocationEngineProvider(this).obtainBestLocationEngineAvailable();
        locationEngine.setPriority(LocationEnginePriority.HIGH_ACCURACY);
        locationEngine.activate();

        Location lastLocation = locationEngine.getLastLocation();

        if (lastLocation != null) {
            originLocation = lastLocation;
            setCameraPosition(lastLocation);
        } else {
            locationEngine.addLocationEngineListener(this);
        }
    }
    private void setCameraPosition(Location location) {
        map.animateCamera(CameraUpdateFactory.newLatLngZoom(
                new LatLng(location.getLatitude(), location.getLongitude()), 13));
    }
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        permissionsManager.onRequestPermissionsResult(requestCode, permissions, grantResults);

    }
    private void getRoute(Point origin, Point destination) {
        if(destination!=null){
            NavigationRoute.builder()
                    .accessToken(Mapbox.getAccessToken())
                    .origin(origin)
                    .destination(destination)
                    .build()
                    .getRoute(new Callback<DirectionsResponse>() {
                        @Override
                        public void onResponse(Call<DirectionsResponse> call, Response<DirectionsResponse> response) {
                            // You can get the generic HTTP info about the response
                            Log.d(TAG, "Response code: " + response.code());
                            if (response.body() == null) {
                                Log.e(TAG, "No routes found, make sure you set the right user and access token.");
                                return;
                            } else if (response.body().routes().size() < 1) {
                                Log.e(TAG, "No routes found");
                                return;
                            }
                            currentRoute = response.body().routes().get(0);
                            // Draw the route on the map
                            if (navigationMapRoute != null) {
                                navigationMapRoute.removeRoute();
                            } else {
                                navigationMapRoute = new NavigationMapRoute(null, mapView, map, R.style.NavigationMapRoute);
                            }
                            navigationMapRoute.addRoute(currentRoute);
                        }
                        @Override
                        public void onFailure(Call<DirectionsResponse> call, Throwable throwable) {
                            Log.e(TAG, "Error: " + throwable.getMessage());
                        }
                    });
        }
    }
    @Override
    @SuppressWarnings( {"MissingPermission"})
    protected void onStart() {
        super.onStart();
        if (locationEngine != null) {
            locationEngine.requestLocationUpdates();
        }
        if (locationPlugin != null) {
            locationPlugin.onStart();
        }
        mapView.onStart();
    }
    @Override
    protected void onStop() {
        super.onStop();
        if (locationEngine != null) {
            locationEngine.removeLocationUpdates();
        }
        if (locationPlugin != null) {
            locationPlugin.onStop();
        }
        mapView.onStop();
    }
    @Override
    protected void onDestroy() {
        super.onDestroy();
        mapView.onDestroy();
        if (locationEngine != null) {
            locationEngine.deactivate();
        }
    }
    @Override
    public void onLowMemory() {
        super.onLowMemory();
        mapView.onLowMemory();
    }
    @Override
    protected void onResume() {
        super.onResume();
        mapView.onResume();
    }
    @Override
    protected void onPause() {
        super.onPause();
        mapView.onPause();
    }
    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        mapView.onSaveInstanceState(outState);
    }
    @Override
    public void onConnected() {
    }
    @Override
    public void onLocationChanged(Location location) {
    }
    @Override
    public void onExplanationNeeded(List<String> permissionsToExplain) {
    }
    @Override
    public void onPermissionResult(boolean granted) {
    }
}