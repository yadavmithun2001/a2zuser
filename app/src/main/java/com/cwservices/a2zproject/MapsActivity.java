package com.cwservices.a2zproject;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.FragmentActivity;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.location.Location;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.cwservices.a2zproject.Adapters.PopupAdapter;
import com.cwservices.a2zproject.Models.HouseNameModel;
import com.cwservices.a2zproject.Models.LocationModel;
import com.cwservices.a2zproject.Models.NotificationModel;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CustomCap;
import com.google.android.gms.maps.model.Dash;
import com.google.android.gms.maps.model.Dot;
import com.google.android.gms.maps.model.Gap;
import com.google.android.gms.maps.model.JointType;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.cwservices.a2zproject.databinding.ActivityMapsBinding;
import com.google.android.gms.maps.model.PatternItem;
import com.google.android.gms.maps.model.PointOfInterest;
import com.google.android.gms.maps.model.Polygon;
import com.google.android.gms.maps.model.PolygonOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;
import com.google.android.gms.maps.model.RoundCap;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.shashank.sony.fancytoastlib.FancyToast;

import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.TimeZone;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback, GoogleMap.OnPolylineClickListener,GoogleMap.OnPolygonClickListener {

    private GoogleMap mMap;
    private ActivityMapsBinding binding;
    FirebaseDatabase database;
    LocationModel model,startingpoint,endpoint;
    ArrayList<LocationModel> list;
    ArrayList<HouseNameModel> list1;
    double latitude;
    double longitude;
    HashMap<String, Uri> images;
    LatLng vehicle,inroutepoints;
    Marker marker = null;
    boolean _first_time = true;
    ArrayList<LatLng> points,inroute;
    Location location1,location2;
    TextView estimatedtime;
    PolylineOptions polylineOptions;

    float distanceInMeters;
    int speedIs10MetersPerMinute = 15;
    float estimatedDriveTimeInMinutes;



    private static final int COLOR_BLACK_ARGB = 0xff000000;
    private static final int POLYLINE_STROKE_WIDTH_PX = 12;
    private static final int COLOR_WHITE_ARGB = 0xffffffff;
    private static final int COLOR_GREEN_ARGB = 0xff388E3C;
    private static final int COLOR_PURPLE_ARGB = 0xff81C784;
    private static final int COLOR_ORANGE_ARGB = 0xffF57F17;
    private static final int COLOR_BLUE_ARGB = 0xffF9A825;

    private static final int POLYGON_STROKE_WIDTH_PX = 8;
    private static final int PATTERN_DASH_LENGTH_PX = 20;
    private static final int PATTERN_GAP_LENGTH_PX = 20;
    private static final PatternItem DOT = new Dot();
    private static final PatternItem DASH = new Dash(PATTERN_DASH_LENGTH_PX);
    private static final PatternItem GAP = new Gap(PATTERN_GAP_LENGTH_PX);

    private static final List<PatternItem> PATTERN_POLYLINE_DOTTED =
            Arrays.asList(GAP, DOT);
    private static final List<PatternItem> PATTERN_POLYGON_ALPHA =
            Arrays.asList(GAP, DASH);
    private static final List<PatternItem> PATTERN_POLYGON_BETA =
            Arrays.asList(DOT, GAP, DASH, GAP);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityMapsBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        database = FirebaseDatabase.getInstance();
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        assert mapFragment != null;
        mapFragment.getMapAsync(this);
        ImageView back = findViewById(R.id.imageback);
        estimatedtime = findViewById(R.id.estimatedtime);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MapsActivity.this,HomePage.class);
                startActivity(intent);
                finish();
            }
        });


    }

    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @Override
    public void onMapReady(@NonNull GoogleMap googleMap) {
        mMap = googleMap;
        points = new ArrayList<LatLng>();
        inroute = new ArrayList<LatLng>();
        polylineOptions = new PolylineOptions().width(10).color(Color.BLUE).geodesic(true);
        location1 = new Location("");
        location2 = new Location("");




        database.getReference()
                .child("driverlocation")
                .child("driver1")
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        model = snapshot.getValue(LocationModel.class);
                        vehicle = new LatLng(model.getLatitude(),model.getLongitude());
                        points.add(vehicle);
                        location1.setLatitude(model.getLatitude());
                        location1.setLongitude(model.getLongitude());






                        PolylineOptions options2 = new PolylineOptions().width(15).color(Color.GREEN).geodesic(true);
                        for (int i = 0; i < points.size(); i++) {
                            LatLng point = points.get(i);
                            options2.add(point);
                        }
                        mMap.addPolyline(options2);

                        if (marker == null) {
                            MarkerOptions options = new MarkerOptions().position(vehicle)
                                    .title("Your WASTE Collection Vehicle Details")
                                    .snippet("Vehicle Number : DL1LY5349  |  Vehicle Type : Auto Tipper")
                                    .icon(BitmapFromVector(MapsActivity.this,R.drawable.carveh));
                            marker = mMap.addMarker(options);
                        }
                        else {
                            marker.setPosition(vehicle);
                        }
                        mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(vehicle,20));

                        location2.setLatitude(28.61522041);
                        location2.setLongitude(77.09382387);
                        distanceInMeters = location1.distanceTo(location2);
                        estimatedDriveTimeInMinutes = distanceInMeters / speedIs10MetersPerMinute;


                        if(distanceInMeters < 20){
                            Calendar cal = Calendar.getInstance(TimeZone.getTimeZone("GMT+1:00"));
                            Date currentLocalTime = cal.getTime();
                            DateFormat date1 = new SimpleDateFormat("HH:mm");
                            date1.setTimeZone(TimeZone.getTimeZone("GMT+5:30"));
                            String localTime = date1.format(currentLocalTime);
                            DateFormat df = new SimpleDateFormat("dd/MM/yyyy | HH:mm");
                            String crdate = df.format(Calendar.getInstance().getTime());
                            NotificationModel notificationModel = new NotificationModel(crdate,"Your area has been served "+localTime);

                            database.getReference()
                                    .child("notifications")
                                    .setValue(notificationModel);
                            FancyToast.makeText(MapsActivity.this,"Your area has been served", FancyToast.LENGTH_SHORT,FancyToast.SUCCESS,false).show();
                        }

                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });

        database.getReference()
                .child("routepoints")
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        for (DataSnapshot snapshot1 : snapshot.getChildren()){
                            startingpoint = snapshot1.getValue(LocationModel.class);
                            inroutepoints = new LatLng(startingpoint.getLatitude(),startingpoint.getLongitude());
                            inroute.add(inroutepoints);
                        }

                        PolylineOptions options3 = new PolylineOptions().width(10).color(Color.BLUE).geodesic(true);
                        for (int i = 0; i < inroute.size(); i++) {
                            LatLng point = inroute.get(i);
                            options3.add(point);
                        }
                        mMap.addPolyline(options3);


                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });





        Polyline polyline2 = mMap.addPolyline(new PolylineOptions()
                .clickable(true)
                .add(new LatLng(28.624869, 77.101426),
                        new LatLng(28.622401, 77.102541),
                        new LatLng(28.614770, 77.107344),
                        new LatLng(28.612979, 77.104917),
                        new LatLng(28.612432, 77.10454),
                        new LatLng(28.608588, 77.102353),
                        new LatLng(28.609324, 77.100528),
                        new LatLng(28.609124, 77.098824),
                        new LatLng(28.609123, 77.097049),
                        new LatLng(28.617658, 77.077939),
                        new LatLng(28.619350, 77.079089),
                        new LatLng(28.619688, 77.079356),
                        new LatLng(28.620568, 77.080531),
                        new LatLng(28.621061, 77.081501),
                        new LatLng(28.621590, 77.082988),
                        new LatLng(28.621484, 77.084459),
                        new LatLng(28.621932, 77.085984),
                        new LatLng(28.621704, 77.088890),
                        new LatLng(28.621596, 77.090647),
                        new LatLng(28.621929, 77.092625),
                        new LatLng(28.622356, 77.094155),
                        new LatLng(28.624722, 77.100466),
                        new LatLng(28.624863, 77.101423),
                        new LatLng(28.622356, 77.102568),
                        new LatLng(28.624869, 77.101426)
                ));
        polyline2.setTag("A");

        Polygon polygon1 = mMap.addPolygon(new PolygonOptions()
                .clickable(true)
                .add(new LatLng(28.624869, 77.101426),
                        new LatLng(28.622401, 77.102541),
                        new LatLng(28.614770, 77.107344),
                        new LatLng(28.612979, 77.104917),
                        new LatLng(28.612432, 77.10454),
                        new LatLng(28.608588, 77.102353),
                        new LatLng(28.609324, 77.100528),
                        new LatLng(28.609124, 77.098824),
                        new LatLng(28.609123, 77.097049),
                        new LatLng(28.617658, 77.077939),
                        new LatLng(28.619350, 77.079089),
                        new LatLng(28.619688, 77.079356),
                        new LatLng(28.620568, 77.080531),
                        new LatLng(28.621061, 77.081501),
                        new LatLng(28.621590, 77.082988),
                        new LatLng(28.621484, 77.084459),
                        new LatLng(28.621932, 77.085984),
                        new LatLng(28.621704, 77.088890),
                        new LatLng(28.621596, 77.090647),
                        new LatLng(28.621929, 77.092625),
                        new LatLng(28.622356, 77.094155),
                        new LatLng(28.624722, 77.100466),
                        new LatLng(28.624863, 77.101423),
                        new LatLng(28.622356, 77.102568),
                        new LatLng(28.624869, 77.101426)
                ));
        polygon1.setTag("alpha");
        stylePolygon(polygon1);
        stylePolyline(polyline2);
        mMap.setOnPolylineClickListener(this);
        mMap.setOnPolygonClickListener(this);


       images =new HashMap<String, Uri>();

        // Add a marker in Sydney and move the camera
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                list = new ArrayList<>();
               // list.add(new LocationModel(model.getLatitude(),model.getLongitude()));
                list.add(new LocationModel(28.61526753,77.09368609));
                list.add(new LocationModel(28.61597844,77.09619405));
                list.add(new LocationModel(28.61594033,77.09637765));
                list.add(new LocationModel(28.61525928,77.09376279));
                list.add(new LocationModel(28.61524638,77.09377061));
                list.add(new LocationModel(28.61522041,77.09382387));
                list.add(new LocationModel(28.61514303,77.09383244));
                list.add(new LocationModel(28.6150935,77.0938717));
                list.add(new LocationModel(28.61505352,77.09382514));
                list.add(new LocationModel(28.61487717,77.09385242));
                list.add(new LocationModel(28.61495883,77.09392163));
                list.add(new LocationModel(28.61495883,77.09385242));
                list.add(new LocationModel(28.61486341,77.09382814));
                list.add(new LocationModel(28.61528763,77.09583957));
                list.add(new LocationModel(28.61535219,77.09578301));

                list1 = new ArrayList<>();
                database.getReference()
                        .child("notifications")
                        .addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot snapshot) {
                                NotificationModel notificationModel = snapshot.getValue(NotificationModel.class);
                                if(notificationModel.getNotification().startsWith("Your")){
                                    list1.add(new HouseNameModel("sujata singh", R.drawable.icons8home487));
                                    list1.add(new HouseNameModel("Surendra mohan", R.drawable.icons8home487));
                                    list1.add(new HouseNameModel("Kavita raghav", R.drawable.icons8home487));
                                    list1.add(new HouseNameModel("Krishan kumar", R.drawable.icons8home487));
                                    list1.add(new HouseNameModel("Anjula", R.drawable.icons8home487));
                                    list1.add(new HouseNameModel("shree gopal", R.drawable.icons8home487));
                                    list1.add(new HouseNameModel("mohan katyal", R.drawable.icons8home487));
                                    list1.add(new HouseNameModel("gopal Malik", R.drawable.icons8home487));
                                    list1.add(new HouseNameModel("dipanshu", R.drawable.icons8home487));
                                    list1.add(new HouseNameModel("GAYTRI TRIPATHI", R.drawable.icons8home487));
                                    list1.add(new HouseNameModel("Shivam Pathak", R.drawable.icons8home487));
                                    list1.add(new HouseNameModel("Mukesh Goel", R.drawable.icons8home487));
                                    list1.add(new HouseNameModel("Meesam Rizvi", R.drawable.icons8home487));
                                    list1.add(new HouseNameModel("Kunal Tanwar", R.drawable.icons8home487));
                                    list1.add(new HouseNameModel("Manish Jha", R.drawable.icons8home487));
                                }else {
                                    list1.add(new HouseNameModel("sujata singh", R.drawable.icons8home48));
                                    list1.add(new HouseNameModel("Surendra mohan", R.drawable.icons8home48));
                                    list1.add(new HouseNameModel("Kavita raghav", R.drawable.icons8home48));
                                    list1.add(new HouseNameModel("Krishan kumar", R.drawable.icons8home48));
                                    list1.add(new HouseNameModel("Anjula", R.drawable.icons8home48));
                                    list1.add(new HouseNameModel("shree gopal", R.drawable.icons8home48));
                                    list1.add(new HouseNameModel("mohan katyal", R.drawable.icons8home48));
                                    list1.add(new HouseNameModel("gopal Malik", R.drawable.icons8home48));
                                    list1.add(new HouseNameModel("dipanshu", R.drawable.icons8home48));
                                    list1.add(new HouseNameModel("GAYTRI TRIPATHI", R.drawable.icons8home48));
                                    list1.add(new HouseNameModel("Shivam Pathak",R.drawable.icons8home48));
                                    list1.add(new HouseNameModel("Mukesh Goel",R.drawable.icons8home48));
                                    list1.add(new HouseNameModel("Meesam Rizvi",R.drawable.icons8home48));
                                    list1.add(new HouseNameModel("Kunal Tanwar",R.drawable.icons8home48));
                                    list1.add(new HouseNameModel("Manish Jha",R.drawable.icons8home48));
                                }

                                for (int i =0;i<list.size();i++){
                                    LatLng sydney = new LatLng(list.get(i).getLatitude(), list.get(i).getLongitude());
                                    mMap.addMarker(new MarkerOptions().position(sydney).title("ETA:- "+String.valueOf((int)estimatedDriveTimeInMinutes+i)+" min")
                                            .snippet(list1.get(i).getName())
                                            .icon(BitmapFromVector(getApplicationContext(), list1.get(i).getImage())));
                                    mMap.moveCamera(CameraUpdateFactory.newLatLng(sydney));
                                    mMap.animateCamera(CameraUpdateFactory.zoomTo(20.0f));
                                }
                            }



                            @Override
                            public void onCancelled(@NonNull DatabaseError error) {

                            }
                        });






              //  list1.add(new HouseNameModel("Your waste collection vehicle", R.drawable.icons8garbagetruck50));






            }
        },1000);
    }
    private void stylePolygon(Polygon polygon) {
        String type = "";
        // Get the data object stored with the polygon.
        if (polygon.getTag() != null) {
            type = polygon.getTag().toString();
        }

        List<PatternItem> pattern = null;
        int strokeColor = COLOR_BLACK_ARGB;
        int fillColor = COLOR_WHITE_ARGB;

        switch (type) {
            // If no type is given, allow the API to use the default.
            case "alpha":
                // Apply a stroke pattern to render a dashed line, and define
                pattern = PATTERN_POLYGON_ALPHA;
                strokeColor = COLOR_GREEN_ARGB;
                fillColor = COLOR_PURPLE_ARGB;
                break;
            case "beta":
                pattern = PATTERN_POLYGON_BETA;
                strokeColor = COLOR_ORANGE_ARGB;
                fillColor = COLOR_BLUE_ARGB;
                break;
        }

        polygon.setStrokePattern(pattern);
        polygon.setStrokeWidth(POLYGON_STROKE_WIDTH_PX);
        polygon.setStrokeColor(strokeColor);
        polygon.setFillColor(fillColor);
    }
    private void stylePolyline(Polyline polyline) {
        String type = "";
        // Get the data object stored with the polyline.
        if (polyline.getTag() != null) {
            type = polyline.getTag().toString();
        }

        switch (type) {
            // If no type is given, allow the API to use the default.
            case "A":
                // Use a custom bitmap as the cap at the start of the line.
                polyline.setStartCap(
                        new CustomCap(BitmapDescriptorFactory.fromResource(android.R.drawable.arrow_down_float),
                                10));
                break;
            case "B":
                // Use a round cap at the start of the line.
                polyline.setStartCap(new RoundCap());
                break;
        }

        polyline.setEndCap(new RoundCap());
        polyline.setWidth(POLYLINE_STROKE_WIDTH_PX);
        polyline.setColor(COLOR_BLACK_ARGB);
        polyline.setJointType(JointType.ROUND);
    }



    private BitmapDescriptor BitmapFromVector(Context context, int vectorResId) {
        // below line is use to generate a drawable.
        Drawable vectorDrawable = ContextCompat.getDrawable(context, vectorResId);
        vectorDrawable.setBounds(0, 0, vectorDrawable.getIntrinsicWidth(), vectorDrawable.getIntrinsicHeight());
        Bitmap bitmap = Bitmap.createBitmap(vectorDrawable.getIntrinsicWidth(), vectorDrawable.getIntrinsicHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);
        vectorDrawable.draw(canvas);
        return BitmapDescriptorFactory.fromBitmap(bitmap);
    }

    @Override
    public void onPolygonClick(@NonNull Polygon polygon) {

    }

    @Override
    public void onPolylineClick(@NonNull Polyline polyline) {
        if ((polyline.getPattern() == null) ||
                (!polyline.getPattern().contains(DOT))) {
            polyline.setPattern(PATTERN_POLYLINE_DOTTED);
            mMap.addMarker(new MarkerOptions().position(new LatLng(28.616676,77.096199)).title("Ward No - 16 Janak Puri"));
        } else {
            // The default pattern is a solid stroke.
            polyline.setPattern(null);

        }
    }
}