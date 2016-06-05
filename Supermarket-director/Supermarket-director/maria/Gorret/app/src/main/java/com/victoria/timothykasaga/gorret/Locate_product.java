package com.victoria.timothykasaga.gorret;


import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;

import org.json.JSONException;
import org.json.JSONObject;


/**
 * A simple {@link Fragment} subclass.
 */
public class Locate_product extends Fragment {
    String toast;
    String supermkt_id = "x";
    String user_floor= "x";
    String user_cell= "x";
    public Locate_product() {
        // Required empty public constructor

    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        displayToast();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.locate_product, container, false);
        ImageView imageView = (ImageView)view.findViewById(R.id.floorimage);
        Button bgetLoc = (Button) view.findViewById(R.id.btnScan);
        Button bfetchLoc = (Button) view.findViewById(R.id.btngetLoc);
        bfetchLoc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(user_cell.equals("") || user_floor.equals("") || supermkt_id.equals("")){
                    Toast.makeText(getActivity(),"Please scan QR code", Toast.LENGTH_SHORT).show();
                }else{
                    ServerRequests serverRequests = new ServerRequests(getActivity());
                    serverRequests.getImageURLpath(Locate_product.this,supermkt_id,user_floor,user_cell);
                }
            }
        });
        bgetLoc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                scanFromFragment();

            }
        });
        return view;

    }

    public void scanFromFragment() {
        IntentIntegrator.forSupportFragment(this).initiateScan();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        IntentResult result = IntentIntegrator.parseActivityResult(requestCode, resultCode, data);
        if(result != null) {

            if(result.getContents() == null) {
                toast = "Cancelled from fragment";
                displayToast();
               // Toast.makeText(getActivity(),toast,Toast.LENGTH_SHORT).show();
            } else {
                toast = "Scanned from fragment: " + result.getContents();
                displayToast();
               //Toast.makeText(getActivity(),toast,Toast.LENGTH_SHORT).show();
            }

            // At this point we may or may not have a reference to the activity

        } else {
        // This is important, otherwise the result will not be passed to the fragment
        super.onActivityResult(requestCode, resultCode, data);
    }
    }

    private void displayToast() {
        if(getActivity() != null && toast != null) {
            Toast.makeText(getActivity(), toast, Toast.LENGTH_LONG).show();
            toast = null;
        }
    }


    public void continueExecution(String s, Locate_product locate_product) {
        String filename;
        try {
            JSONObject jsonObject = new JSONObject(s);
           // if(jsonObject.)
        } catch (Exception e) {
            e.printStackTrace();
        }

        Toast.makeText(locate_product.getActivity(),s,Toast.LENGTH_SHORT).show();
        int loader = R.drawable.home;
        ImageLoader imgLoader = new ImageLoader(locate_product.getActivity());
        imgLoader.DisplayImage("http://10.0.3.2/smsd_locations/testlocateuser/"+s, loader,
                (ImageView) locate_product.getView().findViewById(R.id.floorimage));


    }
}
