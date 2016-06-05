package com.victoria.timothykasaga.gorret;

import android.content.Intent;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import org.json.JSONArray;
import org.json.JSONObject;

public class Locate_supermarket extends FragmentActivity {
    private String criteria;
    Button go;
    String result = null;
    EditText selected_loc;
    Spinner spinner;
    Toolbar toolbar;
    private GoogleMap mMap; // Might be null if Google Play services APK is not available.

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.locate_supermarket);
        initliaze();
        setUpMapIfNeeded();
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                switch (i){
                    case 0:{
                        selected_loc.setEnabled(true);
                        break;
                    }
                    case 1:{
                        selected_loc.setEnabled(true);
                        break;
                    }
                    case 2:{
                        selected_loc.setEnabled(false);
                        break;
                    }
                }
                criteria = adapterView.getItemAtPosition(i).toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        go.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                switch (criteria){
                    case "Name":{
                        if(!selected_loc.getText().toString().equals("")){
                            String supermktname = selected_loc.getText().toString();
                            ServerRequests serverRequests = new ServerRequests(Locate_supermarket.this);
                            mMap.clear();
                            serverRequests.searchBySmName(supermktname,Locate_supermarket.this);

                        }else{
                            Toast.makeText(getApplicationContext(),"Please enter Supermarket name",Toast.LENGTH_SHORT).show();
                        }
                     break;
                    }
                    case "Location":{
                        if(!selected_loc.getText().toString().equals("")){
                        String location = selected_loc.getText().toString();
                        ServerRequests serverRequests = new ServerRequests(Locate_supermarket.this);
                        mMap.clear();
                        serverRequests.searchBySmLocation(location, Locate_supermarket.this);
                        }else{
                            Toast.makeText(getApplicationContext(),"Please enter location",Toast.LENGTH_SHORT).show();
                        }
                        break;
                    }
                    case "All":{
                        ServerRequests serverRequests = new ServerRequests(Locate_supermarket.this);
                        mMap.clear();
                        serverRequests.searchAllSm(Locate_supermarket.this);
                        break;
                    }
                }
            }
        });
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
        mMap.addMarker(new MarkerOptions().position(new LatLng(0, 0)).title("Marker"));
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

        mMap.moveCamera(CameraUpdateFactory.newLatLng(new LatLng(0,0)));
       mMap.setMyLocationEnabled(true);
       mMap.setTrafficEnabled(true);
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
        go = ((Button)findViewById(R.id.btn_go));
        selected_loc = ((EditText)findViewById(R.id.txt_selected_loc));
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
        if(!s.equals("fail")){
           // Toast.makeText(locate_supermarket.getApplicationContext(),s,Toast.LENGTH_SHORT).show();
            try
            {
                JSONArray paramAnonymousView = new JSONArray(s);
                int i = 0;
                while (i < paramAnonymousView.length())
                {
                    JSONObject localObject2 = paramAnonymousView.getJSONObject(i);
                    double d1 = localObject2.getDouble("lat");
                    double d2 = localObject2.getDouble("log");
                    String localObject1 = String.valueOf(localObject2.getString("contact"));
                    String localObject3 = String.valueOf(localObject2.getString("sname"));
                    LatLng latLng = new LatLng(Double.parseDouble(String.valueOf(d1)), Double.parseDouble(String.valueOf(d2)));
                    MarkerOptions markerOptions  = new MarkerOptions().position(latLng).title(localObject3);
                    markerOptions.snippet(localObject1);
                    mMap.addMarker(markerOptions);
                    mMap.moveCamera(CameraUpdateFactory.newLatLng(latLng));
                    i += 1;
                }

            }
            catch (Exception paramAnonymousView)
            {
                paramAnonymousView.printStackTrace();

            }
        }else{
            Toast.makeText(locate_supermarket.getApplicationContext(),s,Toast.LENGTH_SHORT).show();

        }
    }
}
