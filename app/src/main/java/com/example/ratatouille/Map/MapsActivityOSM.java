package com.example.ratatouille.Map;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.ratatouille.ClientChef.CalificationActivity;
import com.example.ratatouille.Class.Agree;
import com.example.ratatouille.Class.Solicitud;
import com.example.ratatouille.Class.UserChef;
import com.example.ratatouille.Class.UserClient;
import com.example.ratatouille.R;
import com.example.ratatouille.permissions.PermissionIds;
import com.example.ratatouille.permissions.PermissionsActions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.mapbox.android.core.permissions.PermissionsListener;
import com.mapbox.android.core.permissions.PermissionsManager;
import com.mapbox.geojson.Point;
import com.mapbox.mapboxsdk.Mapbox;
import com.mapbox.mapboxsdk.geometry.LatLng;
import com.mapbox.mapboxsdk.geometry.LatLngBounds;
import com.mapbox.mapboxsdk.location.LocationComponent;
import com.mapbox.mapboxsdk.location.modes.CameraMode;
import com.mapbox.mapboxsdk.maps.MapView;
import com.mapbox.mapboxsdk.maps.MapboxMap;
import com.mapbox.mapboxsdk.maps.OnMapReadyCallback;
import com.mapbox.mapboxsdk.maps.Style;
import com.mapbox.mapboxsdk.plugins.annotation.Symbol;
import com.mapbox.mapboxsdk.plugins.annotation.SymbolManager;
import com.mapbox.mapboxsdk.plugins.annotation.SymbolOptions;
import com.mapbox.mapboxsdk.plugins.localization.LocalizationPlugin;
import com.mapbox.mapboxsdk.plugins.localization.MapLocale;
import com.mapbox.mapboxsdk.plugins.markerview.MarkerView;
import com.mapbox.mapboxsdk.plugins.markerview.MarkerViewManager;

import java.util.ArrayList;
import java.util.List;

// classes to calculate a route
import com.mapbox.services.android.navigation.ui.v5.route.NavigationMapRoute;
import com.mapbox.services.android.navigation.v5.navigation.NavigationRoute;
import com.mapbox.api.directions.v5.models.DirectionsResponse;
import com.mapbox.api.directions.v5.models.DirectionsRoute;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import android.widget.Toast;


public class MapsActivityOSM extends AppCompatActivity {

    private static final String SOURCE_ID = "SOURCE_ID";
    private static final String MARKER_SOURCE = "markers-source";
    private static final String MARKER_STYLE_LAYER = "markers-style-layer";
    private static final String MARKER_IMAGE = "custom-marker";
    private static final LatLngBounds BOGOTA_BBOX = new LatLngBounds.Builder()
            .include(new LatLng(4.550024,  -74.127039))
            .include(new LatLng(4.766830,  -74.044250)).build();
    public static final MapLocale BOGOTAMAP = new MapLocale(MapLocale.SPANISH, BOGOTA_BBOX);

    // variables for adding location layer
    private PermissionsManager permissionsManager;
    private LocationComponent locationComponent;

    private MapboxMap mapboxMap1;

    // variables for calculating and drawing a route
    private DirectionsRoute currentRoute;
    private static final String TAG = "DirectionsActivity";
    private NavigationMapRoute navigationMapRoute;

    private MapView mapView;
    private MarkerViewManager markerViewManager;
    private MarkerView markerViewChef;

    private SymbolManager symbolManager;
    private List<Symbol> symbols = new ArrayList<>();
    SymbolOptions markerClient, markerChef;

    private static final double RADIUS_OF_EARTH_KM = 6372.795;
    Agree acu;
    String idChef, idCliente;
    LatLng Chef = null, Cliente = null;
    Button Fin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Mapbox.getInstance(this, getString(R.string.access_token_OSM));
        setContentView(R.layout.activity_maps_osm);
        Fin = (Button)findViewById(R.id.Finalizar);
        mapView = (MapView) findViewById(R.id.mapView);
        mapView.onCreate(savedInstanceState);
        mapView.getMapAsync(new OnMapReadyCallback() {
            @Override
            public void onMapReady(@NonNull MapboxMap mapboxMap) {

                mapboxMap.setStyle(Style.MAPBOX_STREETS, new Style.OnStyleLoaded() {
                    @Override
                    public void onStyleLoaded(@NonNull Style style) {
                        mapboxMap1 = mapboxMap;
                        symbolManager = new SymbolManager(mapView, mapboxMap, style);
                        symbolManager.setIconAllowOverlap(true);  //your choice t/f
                        symbolManager.setTextAllowOverlap(true);  //your choice t/f
                        Bitmap bmChef = BitmapFactory.decodeResource(getResources(), R.drawable.chef);
                        mapboxMap.getStyle().addImage("chefMarker",bmChef);
                        Bitmap bmCliente = BitmapFactory.decodeResource(getResources(), R.drawable.client);
                        mapboxMap.getStyle().addImage("clienteMarker",bmCliente);
                        // Map is set up and the style has loaded. Now you can add data or make other map adjustments
                        LocalizationPlugin localizationPlugin = new LocalizationPlugin(mapView, mapboxMap, style);
                        markerViewManager = new MarkerViewManager(mapView, mapboxMap);
                        try {
                            localizationPlugin.matchMapLanguageWithDeviceDefault();
                            localizationPlugin.setCameraToLocaleCountry(BOGOTAMAP,0);
                            MarkerViewManager markerViewManager = new MarkerViewManager(mapView, mapboxMap);
                        } catch (RuntimeException exception) {
                            Log.i("MAP Lang", exception.toString());
                        }
                        enableLocationComponent(style);
                    }
                });

            }
        });
        acu = (Agree) getIntent().getSerializableExtra("Agreement");
        String solicitud = acu.getSolicitudId();
        Query querySolicitud = FirebaseDatabase.getInstance().getReference("solicitud").orderByChild("idSolicitud").equalTo(solicitud);
        querySolicitud.addListenerForSingleValueEvent(valueEventListener);
        PermissionsActions.askPermission(this, PermissionIds.REQUEST_LOCATION);
        Fin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(view.getContext(), CalificationActivity.class);
                Bundle bund = new Bundle();
                bund.putSerializable("solicitud",acu);
                overridePendingTransition(android.R.anim.fade_in,android.R.anim.fade_out);
                intent.putExtras(bund);
                startActivity(intent);
            }
        });
    }

    @SuppressWarnings( {"MissingPermission"})
    private void enableLocationComponent(@NonNull Style loadedMapStyle) {
        // Check if permissions are enabled and if not request
        if (PermissionsManager.areLocationPermissionsGranted(this)) {
            // Activate the MapboxMap LocationComponent to show user location
            // Adding in LocationComponentOptions is also an optional parameter
            locationComponent = mapboxMap1.getLocationComponent();
            locationComponent.activateLocationComponent(this, loadedMapStyle);
            locationComponent.setLocationComponentEnabled(true);
            // Set the component's camera mode
            locationComponent.setCameraMode(CameraMode.TRACKING);
        } else {
            permissionsManager = new PermissionsManager(new PermissionsListener() {
                @Override
                public void onExplanationNeeded(List<String> permissionsToExplain) {
                    Toast.makeText(getBaseContext(), getString(R.string.user_location_permission_explanation), Toast.LENGTH_LONG).show();
                }

                @Override
                public void onPermissionResult(boolean granted) {
                    if (granted) {
                        enableLocationComponent(mapboxMap1.getStyle());
                    } else {
                        Toast.makeText(getBaseContext(), getString(R.string.user_location_permission_not_granted), Toast.LENGTH_LONG).show();
                    }
                }
            });
            permissionsManager.requestLocationPermissions(this);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        permissionsManager.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    @Override
    public void onStart() {
        super.onStart();
        mapView.onStart();
    }

    @Override
    public void onResume() {
        super.onResume();
        mapView.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();
        mapView.onPause();
    }

    @Override
    public void onStop() {
        super.onStop();
        mapView.onStop();
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
        mapView.onLowMemory();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mapView.onDestroy();
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        mapView.onSaveInstanceState(outState);
    }

    ValueEventListener valueEventListener = new ValueEventListener() {
        @Override
        public void onDataChange(DataSnapshot dataSnapshot) {

            if (dataSnapshot.exists())
            {
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    Solicitud s = snapshot.getValue(Solicitud.class);
                    idChef = s.getIdChef();
                    idCliente = s.getIdCliente();
                    Query queryCliente = FirebaseDatabase.getInstance().getReference("userClient").orderByChild("userId").equalTo(idCliente);
                    queryCliente.addListenerForSingleValueEvent(valueEventListenerCliente);
                    Query queryChef = FirebaseDatabase.getInstance().getReference("userChef").orderByChild("userId").equalTo(idChef);
                    queryChef.addListenerForSingleValueEvent(valueEventListenerChef);
                }
            }

        }

        @Override
        public void onCancelled(DatabaseError databaseError) {

        }
    };

    ValueEventListener valueEventListenerCliente = new ValueEventListener() {
        @Override
        public void onDataChange(DataSnapshot dataSnapshot) {

            if (dataSnapshot.exists())
            {
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    UserClient s = snapshot.getValue(UserClient.class);
                    Cliente = new LatLng(s.getLat(), s.getLongi());
                    if(mapView != null){
                        markerClient = new SymbolOptions()
                                .withLatLng(Cliente)
                                .withIconImage("clienteMarker")
                                //set the below attributes according to your requirements
                                .withIconSize(0.4f)
                                .withIconOffset(new Float[] {0f,-1.5f})
                                .withTextField(s.getName())
                                .withTextHaloColor("rgba(255, 255, 255, 255)")
                                .withTextHaloWidth(5.0f)
                                .withTextAnchor("bottom")
                                .withTextOffset(new Float[] {0f, 1.5f})
                                .withDraggable(false)
                                .withTextSize(12f);
                        symbolManager.create(markerClient);
                    }
                    if(Chef != null)
                        generarRuta();
                }
            }

        }

        @Override
        public void onCancelled(DatabaseError databaseError) {

        }
    };

    ValueEventListener valueEventListenerChef = new ValueEventListener() {
        @Override
        public void onDataChange(DataSnapshot dataSnapshot) {

            if (dataSnapshot.exists())
            {
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    UserChef s = snapshot.getValue(UserChef.class);
                    Chef = new LatLng(s.getLat(), s.getLongi());
                    if(mapView != null) {
                        markerChef = new SymbolOptions()
                                .withLatLng(Chef)
                                .withIconImage("chefMarker")
                                //set the below attributes according to your requirements
                                .withIconSize(0.4f)
                                .withIconOffset(new Float[] {0f,-1.5f})
                                .withTextField(s.getName())
                                .withTextHaloColor("rgba(255, 255, 255, 255)")
                                .withTextHaloWidth(5.0f)
                                .withTextAnchor("bottom")
                                .withTextOffset(new Float[] {0f, 1.5f})
                                .withDraggable(false)
                                .withTextSize(12f);
                        symbolManager.create(markerChef);
                    }
                    if(Cliente != null)
                        generarRuta();
                }
            }

        }

        @Override
        public void onCancelled(DatabaseError databaseError) {

        }
    };

    private void generarRuta() {
        Point destinationPoint = Point.fromLngLat(Cliente.getLongitude(), Cliente.getLatitude());
        Point originPoint = Point.fromLngLat(Chef.getLongitude(),Chef.getLatitude());
        getRoute(originPoint,destinationPoint);
    }
    private void getRoute(Point origin, Point destination) {
        NavigationRoute.builder(this)
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
                            navigationMapRoute = new NavigationMapRoute(null, mapView, mapboxMap1, R.style.NavigationMapRoute);
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
