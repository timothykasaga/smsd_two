package com.victoria.timothykasaga.gorret;


import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.zxing.common.StringUtils;
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;

import org.json.JSONException;
import org.json.JSONObject;


/**
 * A simple {@link Fragment} subclass.
 */
public class Locate_product extends Fragment {
    String toast;
    String supermkt_id = "";
    String user_floor= "";
    String user_cell= "";
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
        final EditText pdt_name = (EditText) view.findViewById(R.id.edtproductname);
        Button bgetLoc = (Button) view.findViewById(R.id.btnScan);
        Button bfetchLoc = (Button) view.findViewById(R.id.btngetLoc);
        bfetchLoc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(user_cell.equals("") || user_floor.equals("") || supermkt_id.equals("")){
                    Toast.makeText(getActivity(),"Please scan QR code", Toast.LENGTH_SHORT).show();
                }else{

                    String pname = pdt_name.getText().toString();
                    if(pname.equals(""))
                    {
                        Toast.makeText(getActivity(),"Please Enter product name", Toast.LENGTH_SHORT).show();
                    }else{
                    ServerRequests serverRequests = new ServerRequests(getActivity());
                    serverRequests.getImageURLpath(Locate_product.this,supermkt_id,user_floor,user_cell,pname);
                    }

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
               // toast = "Scanned from fragment: " + result.getContents();
                String lines[] = result.getContents().split("\\r?\\n");
                supermkt_id = lines[0];
                user_floor = lines[1];
                user_cell = lines[2];
                toast = "Supermarket id: "+supermkt_id+"\n"+
                        "User floor: "+user_floor+"\n"+
                        "Cell id: "+user_cell;
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
    //  Toast.makeText(locate_product.getActivity(),s+"What we received",Toast.LENGTH_SHORT).show();
        if(s.equals("not existent"+"\n")){
            Toast.makeText(locate_product.getActivity(),"Product not found",Toast.LENGTH_SHORT).show();
        }else{
            int loader = R.drawable.home;
            try {
                JSONObject jsonObject = new JSONObject(s);
                if(jsonObject.has("path")){
                    final String path  = jsonObject.getString("path");
                    int prodt_floor = Integer.parseInt(jsonObject.getString("product_floor"));
                    int user_floor = Integer.parseInt(jsonObject.getString("user_floor"));
                    ImageLoader imgLoader = new ImageLoader(locate_product.getActivity());
                    imgLoader.DisplayImage("http://192.168.43.73:80/smsd_locations/testlocateuser/"+path, loader,
                            (ImageView) locate_product.getView().findViewById(R.id.floorimage));
                    if(prodt_floor == user_floor){
                        TextView txtpdt = (TextView) locate_product.getView().findViewById(R.id.pdt_floor);
                        TextView simillar = (TextView) locate_product.getView().findViewById(R.id.ur_txt);
                        TextView urpdt = (TextView) locate_product.getView().findViewById(R.id.txt_cur_floor);
                        TextView pdttxt = (TextView) locate_product.getView().findViewById(R.id.pdt_txt);
                        urpdt.setVisibility(View.INVISIBLE);
                        simillar.setText("Product is here");
                       // txtpdt.setText(String.valueOf(prodt_floor));
                        pdttxt.setVisibility(View.INVISIBLE);
                        txtpdt.setVisibility(View.INVISIBLE);
                    }else{
                        TextView txtpdt = (TextView) locate_product.getView().findViewById(R.id.pdt_floor);
                        TextView urpdt = (TextView) locate_product.getView().findViewById(R.id.txt_cur_floor);
                        TextView simillar = (TextView) locate_product.getView().findViewById(R.id.ur_txt);
                        TextView pdttxt = (TextView) locate_product.getView().findViewById(R.id.pdt_txt);
                        simillar.setText("Your floor no.");
                        txtpdt.setVisibility(View.VISIBLE);
                        pdttxt.setVisibility(View.VISIBLE);
                        urpdt.setVisibility(View.VISIBLE);
                        txtpdt.setText(String.valueOf(prodt_floor));
                        urpdt.setText(String.valueOf(user_floor));
                    }
                    ImageView img = (ImageView) locate_product.getView().findViewById(R.id.floorimage);
                    img.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            Intent intent = new Intent(getActivity(),Display_plan.class);
                            intent.putExtra("path",path);
                            startActivity(intent);
                        }
                    });


                }



            } catch (Exception e) {
                e.printStackTrace();
            }


        }





    }
}
