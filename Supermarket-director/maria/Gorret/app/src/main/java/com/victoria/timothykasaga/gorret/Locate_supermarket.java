package com.victoria.timothykasaga.gorret;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListAdapter;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;
import com.google.maps.android.SphericalUtil;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class Locate_supermarket extends FragmentActivity {
    private String criteria;
    Button go;
    String result = null;
    Spinner selected_loc;
    Spinner spinner;
    Toolbar toolbar;
    //From -> the first coordinate from where we need to calculate the distance
    private double fromLongitude;
    private double fromLatitude;

    //To -> the second coordinate to where we need to calculate the distance
    private double toLongitude;
    private double toLatitude;
    ArrayList<DetailsPack> detailsPacks = new ArrayList<>();
    private GoogleMap mMap; // Might be null if Google Play services APK is not available.

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.locate_supermarket);
        initliaze();
        setUpMapIfNeeded();
        //Get All supermarekts
        ServerRequests serverRequests = new ServerRequests(Locate_supermarket.this);
        mMap.clear();
        serverRequests.searchAllSm(Locate_supermarket.this);

        //Get All supermarekts

    }

    @Override
    protected void onResume() {
        super.onResume();
        setUpMapIfNeeded();
    }

    /**
     * Sets up the map if it is possible to do so (i.e., the Google Play services APK is correctly
     * installed) and the map has not already been instantiated.. This will ensure that we only ever
     * call {@link #setUpMap()} once when {@link #mMap} is not null.
     * <p/>
     * If it isn't installed {@link SupportMapFragment} (and
     * {@link com.google.android.gms.maps.MapView MapView}) will show a prompt for the user to
     * install/update the Google Play services APK on their device.
     * <p/>
     * A user can return to this FragmentActivity after following the prompt and correctly
     * installing/updating/enabling the Google Play services. Since the FragmentActivity may not
     * have been completely destroyed during this process (it is likely that it would only be
     * stopped or paused), {@link #onCreate(Bundle)} may not be called again so we should call this
     * method in {@link #onResume()} to guarantee that it will be called.
     */
    private void setUpMapIfNeeded() {
        // Do a null check to confirm that we have not already instantiated the map.
        if (mMap == null) {
            // Try to obtain the map from the SupportMapFragment.
            mMap = ((SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map))
                    .getMap();
            // Check if we were successful in obtaining the map.
            if (mMap != null) {
                setUpMap();
            }
        }
    }

    /**
     * This is where we can add markers or lines, add listeners or move the camera. In this case, we
     * just add a marker near Africa.
     * <p/>
     * This should only be called once and when we are sure that {@link #mMap} is not null.
     */
    private void setUpMap() {
      //  mMap.addMarker(new MarkerOptions().position(new LatLng(0, 0)).title("Marker"));
        mMap.setInfoWindowAdapter(new GoogleMap.InfoWindowAdapter() {
            @Override
            public View getInfoWindow(Marker marker) {
                return null;
            }

            @Override
            public View getInfoContents(Marker marker) {
                View view =getLayoutInflater().inflate(R.layout.info, null);
                TextView localTextView1 = (TextView)view.findViewById(R.id.tvInfoName);
                TextView localTextView2 = (TextView)view.findViewById(R.id.tvInfoContact);
                localTextView1.setText(marker.getTitle());
                localTextView2.setText(marker.getSnippet());
                return view;

            }
        });
      LatLng lat = new LatLng(0.3476,32.5825);
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(lat,10f));
       mMap.setMyLocationEnabled(true);
       mMap.getUiSettings().setMyLocationButtonEnabled(true);
        mMap.getUiSettings().setZoomControlsEnabled(true);
        mMap.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener()
        {
            public boolean onMarkerClick(Marker paramAnonymousMarker)
            {
                Toast.makeText(Locate_supermarket.this.getApplicationContext(),

                        paramAnonymousMarker.getTitle(), Toast.LENGTH_SHORT).show();
                return false;
            }
        });
    }

    public void initliaze(){
        spinner = ((Spinner)findViewById(R.id.selLocSpin));

        selected_loc = (Spinner) findViewById(R.id.txt_selected_loc);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle("Locate supermarket");
        toolbar.setTitleTextColor(getResources().getColor(R.color.white));
        toolbar.setNavigationIcon(R.drawable.home);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Locate_supermarket.this,MainActivity.class);
                startActivity(intent);
            }
        });

    }

    public void continueExecution(String s, Locate_supermarket locate_supermarket) {

       //- Toast.makeText(locate_supermarket.getApplicationContext(),s,Toast.LENGTH_SHORT).show();
        if(!s.equals("Could not retrive information check input")){
           // Toast.makeText(locate_supermarket.getApplicationContext(),s,Toast.LENGTH_SHORT).show();


            if(s.equals("fail")){
                Toast.makeText(locate_supermarket.getApplicationContext(),s,Toast.LENGTH_SHORT).show();
            }else {

                try {
                    JSONArray paramAnonymousView = new JSONArray(s);
                    int i = 0;

                    while (i < paramAnonymousView.length()) {
                        JSONObject localObject2 = paramAnonymousView.getJSONObject(i);
                        double d1 = localObject2.getDouble("lat");
                        double d2 = localObject2.getDouble("log");
                        String localObject1 = String.valueOf(localObject2.getString("contact"));
                        String localObject3 = String.valueOf(localObject2.getString("sname"));
                        String location = String.valueOf(localObject2.getString("location"));


                        detailsPacks.add(new DetailsPack(localObject3,location,"","",localObject1,"",d1+"",d2+"",""));

                        i += 1;
                    }
                    spinner.setSelection(0);
                    spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                        @Override
                        public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                            ((TextView) adapterView.getChildAt(0)).setTextColor(Color.WHITE);
                            ((TextView) adapterView.getChildAt(0)).setTextSize(20);
                            switch (i){
                                case 0:{
                                    selected_loc.setEnabled(true);

                                    ArrayList<String> arrayList = new ArrayList<String>();
                                    if(detailsPacks.size() != 0){
                                        for(i = 0;i<detailsPacks.size();i++){
                                            if(!arrayList.contains(detailsPacks.get(i).getS_name()))
                                            arrayList.add(detailsPacks.get(i).getS_name());
                                        }
                                        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<>(getApplicationContext(),
                                                R.layout.support_simple_spinner_dropdown_item,arrayList);
                                        selected_loc.setAdapter(arrayAdapter);
                                    }
                                    selected_loc.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                                        @Override
                                        public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                                            ((TextView) adapterView.getChildAt(0)).setTextColor(Color.WHITE);
                                            ((TextView) adapterView.getChildAt(0)).setTextSize(20);
                                            mMap.clear();
                                            for(int j = 0;j<detailsPacks.size();j++){

                                                if(detailsPacks.get(j).getS_name().equals(adapterView.getItemAtPosition(i).toString())) {

                                                    LatLng latLng = new LatLng(Double.parseDouble(String.valueOf(detailsPacks.get(j).getD_lat())),
                                                            Double.parseDouble(String.valueOf(detailsPacks.get(j).getD_log())));
                                                    MarkerOptions markerOptions = new MarkerOptions().position(latLng).title(detailsPacks.get(j).getS_name());
                                                    markerOptions.snippet(detailsPacks.get(j).getS_location());
                                                    mMap.addMarker(markerOptions);
                                                    mMap.moveCamera(CameraUpdateFactory.newLatLng(latLng));
                                                }
                                            }





                                        }

                                        @Override
                                        public void onNothingSelected(AdapterView<?> adapterView) {

                                        }
                                    });
                                    break;
                                }
                                case 1:{
                                    selected_loc.setEnabled(true);
                                    ArrayList<String> arrayList = new ArrayList<String>();
                                    if(detailsPacks.size() != 0){
                                        for(i = 0;i<detailsPacks.size();i++){
                                            if(!arrayList.contains(detailsPacks.get(i).getS_location()))
                                            arrayList.add(detailsPacks.get(i).getS_location());
                                        }
                                        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<>(getApplicationContext(),
                                                R.layout.support_simple_spinner_dropdown_item,arrayList);
                                        selected_loc.setAdapter(arrayAdapter);
                                    }
                                    selected_loc.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                                        @Override
                                        public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                                            ((TextView) adapterView.getChildAt(0)).setTextColor(Color.WHITE);
                                            ((TextView) adapterView.getChildAt(0)).setTextSize(20);
                                            mMap.clear();
                                            for(int j = 0;j<detailsPacks.size();j++){

                                                if(detailsPacks.get(j).getS_location().equals(adapterView.getItemAtPosition(i).toString())) {

                                                    LatLng latLng = new LatLng(Double.parseDouble(String.valueOf(detailsPacks.get(j).getD_lat())),
                                                            Double.parseDouble(String.valueOf(detailsPacks.get(j).getD_log())));
                                                    MarkerOptions markerOptions = new MarkerOptions().position(latLng).title(detailsPacks.get(j).getS_name());
                                                    markerOptions.snippet(detailsPacks.get(j).getS_location());
                                                    mMap.addMarker(markerOptions);
                                                    mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng,15));
                                                }
                                            }





                                        }

                                        @Override
                                        public void onNothingSelected(AdapterView<?> adapterView) {

                                        }
                                    });
                                    break;
                                }
                                case 2:{
                                    selected_loc.setEnabled(false);
                                    mMap.clear();
                                    for(int j = 0;j<detailsPacks.size();j++){


                                            LatLng latLng = new LatLng(Double.parseDouble(String.valueOf(detailsPacks.get(j).getD_lat())),
                                                    Double.parseDouble(String.valueOf(detailsPacks.get(j).getD_log())));
                                            MarkerOptions markerOptions = new MarkerOptions().position(latLng).title(detailsPacks.get(j).getS_name());
                                            markerOptions.snippet(detailsPacks.get(j).getS_location());
                                            mMap.addMarker(markerOptions);
                                            mMap.moveCamera(CameraUpdateFactory.newLatLng(latLng));

                                    }
                                    break;
                                }
                                case 3:{
                                    mMap.clear();
                                    selected_loc.setEnabled(false);
                                    LatLng userLocation = getUserLocation();
                                    LatLng minLocatedSupermarketLocation = null;
                                    String nearestSupermarket = "";
                                    double minDistance = 0;
                                    int counter = 0;
                                    for(int x = 0;x<detailsPacks.size();x++){
                                        LatLng SupermarketLatLng = new LatLng(Double.parseDouble(String.valueOf(detailsPacks.get(x).getD_lat())),
                                                Double.parseDouble(String.valueOf(detailsPacks.get(x).getD_log())));

                                        //Calculating the distance in meters
                                        double distance = SphericalUtil.computeDistanceBetween(userLocation, SupermarketLatLng);
                                        if(counter == 0){
                                            minDistance = distance;
                                            minLocatedSupermarketLocation = SupermarketLatLng;
                                            nearestSupermarket = detailsPacks.get(x).getS_name();
                                        }else{
                                            if(distance < minDistance){
                                            minDistance = distance;
                                            minLocatedSupermarketLocation = SupermarketLatLng;
                                            nearestSupermarket = detailsPacks.get(x).getS_name();
                                            }
                                        }
                                        counter++;
                                    }

                                    Toast.makeText(Locate_supermarket.this,"Nearest supermarket: "+nearestSupermarket+
                                    "\nBy distance: "+minDistance+"\nAt Lat: "+minLocatedSupermarketLocation.latitude,Toast.LENGTH_LONG).show();
                                    //
                                    fromLatitude = userLocation.latitude;
                                    fromLongitude =userLocation.longitude;
                                    toLatitude = minLocatedSupermarketLocation.latitude;
                                    toLongitude = minLocatedSupermarketLocation.longitude;
                                    getDirection();
                                    break;
                                }
                            }
                           // criteria = adapterView.getItemAtPosition(i).toString();
                        }

                        @Override
                        public void onNothingSelected(AdapterView<?> adapterView) {

                        }
                    });


                } catch (Exception paramAnonymousView) {
                    paramAnonymousView.printStackTrace();

                }

            }


        }else{
            Toast.makeText(locate_supermarket.getApplicationContext(),s,Toast.LENGTH_SHORT).show();

        }
    }



    public LatLng getUserLocation(){
        LatLng latLng = null;
        MyGPSTracker gps;
        double latitude;
        double longitude;
        gps = new MyGPSTracker(Locate_supermarket.this);

        // check if GPS enabled
        if(gps.canGetLocation()){

            latitude = gps.getLatitude();
            longitude = gps.getLongitude();
            latLng = new LatLng(0.3476,32.5825);
            // \n is for new line
            Toast.makeText(getApplicationContext(), "Your Location is - \nLat: " + latitude + "\nLong: " + longitude, Toast.LENGTH_LONG).show();
        }else{
            // can't get location
            // GPS or Network is not enabled
            // Ask user to enable GPS/network in settings
            gps.showSettingsAlert();
        }

        return  latLng;
    }

    public String makeURL (double sourcelat, double sourcelog, double destlat, double destlog ){
        StringBuilder urlString = new StringBuilder();
        urlString.append("https://maps.googleapis.com/maps/api/directions/json");
        urlString.append("?origin=");// from
        urlString.append(Double.toString(sourcelat));
        urlString.append(",");
        urlString
                .append(Double.toString( sourcelog));
        urlString.append("&destination=");// to
        urlString
                .append(Double.toString( destlat));
        urlString.append(",");
        urlString.append(Double.toString(destlog));
        urlString.append("&sensor=false&mode=driving&alternatives=true");
        urlString.append("&key=AIzaSyCeka0OBJx-fTrPqaqsIpPd57NrxN1t8BU");
        return urlString.toString();
    }

    private void getDirection(){
        //Getting the URL
        String url = makeURL(fromLatitude, fromLongitude, toLatitude, toLongitude);

        //Showing a dialog till we get the route
        final ProgressDialog loading = ProgressDialog.show(this, "Getting Route", "Please wait...", false, false);

        //Creating a string request
        StringRequest stringRequest = new StringRequest(url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        loading.dismiss();
                        //Calling the method drawPath to draw the path
                        drawPath(response);
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        loading.dismiss();
                    }
                });

        //Adding the request to request queue
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);
    }


    //The parameter is the server response
    public void drawPath(String  result) {
        //Getting both the coordinates
        LatLng from = new LatLng(fromLatitude,fromLongitude);
        LatLng to = new LatLng(toLatitude,toLongitude);

        //Calculating the distance in meters
        Double distance = SphericalUtil.computeDistanceBetween(from, to);

        //Displaying the distance
        Toast.makeText(this,String.valueOf(distance+" Meters"),Toast.LENGTH_SHORT).show();


        try {
            //Parsing json
            final JSONObject json = new JSONObject(result);
            JSONArray routeArray = json.getJSONArray("routes");
            JSONObject routes = routeArray.getJSONObject(0);
            JSONObject overviewPolylines = routes.getJSONObject("overview_polyline");
            String encodedString = overviewPolylines.getString("points");
            List<LatLng> list = decodePoly(encodedString);
            Polyline line = mMap.addPolyline(new PolylineOptions()
                            .addAll(list)
                            .width(20)
                            .color(Color.RED)
                            .geodesic(true)
            );
            line.setVisible(true);
            LatLng lat = new LatLng(fromLatitude,fromLongitude);
            LatLng lat2 = new LatLng(toLatitude,toLongitude);
            mMap.addMarker(new MarkerOptions().position(lat).title("You").icon(BitmapDescriptorFactory.fromResource(R.drawable.user_image)));
            mMap.addMarker(new MarkerOptions().position(lat2).title("Nearest supermarket"));
            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(lat, 15f));

        }
        catch (JSONException e) {

        }
    }

    private List<LatLng> decodePoly(String encoded) {
        List<LatLng> poly = new ArrayList<LatLng>();
        int index = 0, len = encoded.length();
        int lat = 0, lng = 0;

        while (index < len) {
            int b, shift = 0, result = 0;
            do {
                b = encoded.charAt(index++) - 63;
                result |= (b & 0x1f) << shift;
                shift += 5;
            } while (b >= 0x20);
            int dlat = ((result & 1) != 0 ? ~(result >> 1) : (result >> 1));
            lat += dlat;

            shift = 0;
            result = 0;
            do {
                b = encoded.charAt(index++) - 63;
                result |= (b & 0x1f) << shift;
                shift += 5;
            } while (b >= 0x20);
            int dlng = ((result & 1) != 0 ? ~(result >> 1) : (result >> 1));
            lng += dlng;

            LatLng p = new LatLng( (((double) lat / 1E5)),
                    (((double) lng / 1E5) ));
            poly.add(p);
        }

        return poly;
    }

    public Bitmap getMarkerImage(){
        Bitmap.Config conf = Bitmap.Config.ARGB_8888;
        Bitmap bmp = Bitmap.createBitmap(80, 80, conf);
        Canvas canvas1 = new Canvas(bmp);

// paint defines the text color, stroke width and size
        Paint color = new Paint();
        color.setTextSize(35);
        color.setColor(Color.BLACK);

// modify canvas
        canvas1.drawBitmap(BitmapFactory.decodeResource(getResources(),
                R.drawable.user_icon), 0,0, color);
        canvas1.drawText("You", 30, 40, color);
        return  bmp;
    }

}
