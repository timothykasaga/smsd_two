package com.victoria.timothykasaga.gorret;


import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;


/**
 * A simple {@link Fragment} subclass.
 */
public class Compare_prices extends Fragment {
    BarChart barChart;
    ArrayList<String> arrayList = new ArrayList();
    private Button bCompare;
    private Button bget_prodt;
    int count = 0;
    private ListView listSelected;
    int num_of_sms_retd;
    String[][] pairs;
    String result;
    private ListView list_compare;
    private Spinner spinner;
    String[] supermkts;
    private EditText t_pdt_name;
    View view;
    List<String> productlist = null;
   ArrayList<String> selectedSupermarkets = null;
    public Compare_prices() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.compare_prices, container, false);
        initialize(view);
        //set click listener for getting supermarkets with product;
        bget_prodt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String product_name = t_pdt_name.getText().toString();
                if(!product_name.equals("")){
                    ServerRequests serverRequests = new ServerRequests(Compare_prices.this.getActivity());
                    serverRequests.getSupermarketWithProduct(product_name,Compare_prices.this);
                    bCompare.setEnabled(true);
                }else{
                    Toast.makeText(getActivity(),"Please enter product name",Toast.LENGTH_SHORT).show();
                }
            }
        });

        return view;
    }

    public void initialize(View view){
        spinner = ((Spinner)this.view.findViewById(R.id.spinChooseSm));
        String[] initial = {"No product entered"};
        spinner.setAdapter(new ArrayAdapter<String>(getActivity(),android.R.layout.simple_list_item_1,initial));
        listSelected = ((ListView)this.view.findViewById(R.id.list_selected));
       // list_compare = ((ListView)this.view.findViewById(R.id.list_compare));
        t_pdt_name = ((EditText)this.view.findViewById(R.id.txt_pdt_name));
        bget_prodt = ((Button)this.view.findViewById(R.id.btn_get_prodt));
        bCompare = ((Button)this.view.findViewById(R.id.btn_Compare));
        barChart = (BarChart) view.findViewById(R.id.mybargraph);


    }

    public void continueExecution(String s, Compare_prices compare_prices) {
      if(!s.equals("fail"+"\n")){
           // Toast.makeText(compare_prices.getActivity(),s,Toast.LENGTH_SHORT).show();
          try
          {
              final ArrayList<Supermarket_product> supermarket_products = new ArrayList<>();
              JSONArray paramAnonymousView = new JSONArray(s);
              int i = 0;
              //Put product details in arraylist of type supermarket_prodt
              while (i < paramAnonymousView.length())
              {
                  JSONObject localObject2 = paramAnonymousView.getJSONObject(i);
                  String supermarket_name = localObject2.getString("smname");
                  String supermarket_id = localObject2.getString("smid");
                  String product_name = localObject2.getString("pname");
                  double unitcost = localObject2.getDouble("unitcost");
                  String measure = localObject2.getString("measurement");
                  String product_id  = localObject2.getString("pid");
                  String section = localObject2.getString("section");
                  String location = localObject2.getString("location");
                  Supermarket_product product_details = new Supermarket_product(supermarket_name,supermarket_id,product_name,measure,product_id,section,unitcost,location);
                  supermarket_products.add(product_details);
                  i += 1;
              }
              //check whether empty
              if(!(supermarket_products.size() == 0)){
                  //Get supermarket names to populate spinner
                  productlist = new ArrayList<>();
                  for (int j = 0; j < supermarket_products.size(); j++) {
                        productlist.add(supermarket_products.get(j).supermarket_name+" "+supermarket_products.get(j).location);
                  }
                  productlist.add("All");

                    //Populate spinner
                  ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(getActivity(),android.R.layout.simple_list_item_1,productlist);
                  spinner.setAdapter(arrayAdapter);
                  bCompare.setEnabled(true);

                  //set onitem selected listener for spinner
                  selectedSupermarkets = new ArrayList<>();
                  spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                      @Override
                      public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                          if (productlist.size() == 0) {

                          }else if(adapterView.getSelectedItem().toString().equals("All"))
                          {
                                productlist.remove(productlist.size()-1);
                                productlist.toArray();
                               //Populate list with supermarkets
                              ArrayAdapter<String> adapter = new ArrayAdapter<String>(getActivity(),
                                      android.R.layout.simple_list_item_1, productlist);
                              listSelected.setAdapter(adapter);


                                  listSelected.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
                                      @Override
                                      public boolean onItemLongClick(final AdapterView<?> adapterView1, View view, final int position, long l) {
                                          final AlertDialog.Builder alertDialog = new AlertDialog.Builder(getActivity());
                                          alertDialog.setMessage("Delete Item ?");
                                          alertDialog.setNegativeButton("No", new DialogInterface.OnClickListener() {
                                              @Override
                                              public void onClick(DialogInterface dialogInterface, int i) {
                                              }
                                          });
                                          alertDialog.setPositiveButton("yes", new DialogInterface.OnClickListener() {
                                              @Override
                                              public void onClick(DialogInterface dialogInterface, int i) {
                                                  productlist.remove(adapterView1.getItemAtPosition(position).toString());

                                                  ListAdapter listAdapter = new ArrayAdapter<>(getActivity(), android.R.layout.simple_list_item_1, productlist);
                                                  listSelected.setAdapter(listAdapter);
                                              }
                                          });
                                          alertDialog.show();
                                          return false;
                                      }
                                  });


                          }else{
                              if(!selectedSupermarkets.contains(adapterView.getSelectedItem().toString()))
                              { selectedSupermarkets.add(adapterView.getSelectedItem().toString());
                                    ArrayAdapter<String> adapter = new ArrayAdapter<String>(getActivity(),
                                    android.R.layout.simple_list_item_1, selectedSupermarkets);
                                    listSelected.setAdapter(adapter);


                                  listSelected.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
                                      @Override
                                      public boolean onItemLongClick(final AdapterView<?> adapterView1, View view, final int position, long l) {
                                          final AlertDialog.Builder alertDialog = new AlertDialog.Builder(getActivity());
                                          alertDialog.setMessage("Delete Item ?");
                                          alertDialog.setNegativeButton("No", new DialogInterface.OnClickListener() {
                                              @Override
                                              public void onClick(DialogInterface dialogInterface, int i) {

                                              }
                                          });
                                          alertDialog.setPositiveButton("yes", new DialogInterface.OnClickListener() {
                                              @Override
                                              public void onClick(DialogInterface dialogInterface, int i) {
                                                  selectedSupermarkets.remove(adapterView1.getItemAtPosition(position).toString());

                                                  ListAdapter listAdapter = new ArrayAdapter<>(getActivity(), android.R.layout.simple_list_item_1, selectedSupermarkets);
                                                  listSelected.setAdapter(listAdapter);
                                              }
                                          });
                                          alertDialog.show();
                                          return false;
                                      }
                                  });
                              }
                             }
                      }

                      @Override
                      public void onNothingSelected(AdapterView<?> adapterView) {

                      }
                  });


                  //try to compare
                  //set click listener for comparing supermarkets with product;

                  bCompare.setOnClickListener(new View.OnClickListener() {
                      @Override
                      public void onClick(View view) {
                          final ArrayList<Supermarket_product> toCompare = new ArrayList<>();
                          final ArrayList<String> selectedlist = new ArrayList<>();
                          for(int x =0;x<listSelected.getCount();x++){
                              selectedlist.add(listSelected.getItemAtPosition(x).toString());
                          }


                          if(listSelected.getCount() != 0){
                          for (int counter = 0; counter < listSelected.getCount(); counter++) {
                              for (int counttwo = 0; counttwo < supermarket_products.size(); counttwo++) {
                                  if (listSelected.getItemAtPosition(counter).equals(supermarket_products.get(counttwo).supermarket_name
                                          + " " + supermarket_products.get(counttwo).location)) {
                                      toCompare.add(supermarket_products.get(counttwo));
                                  }
                              }
                          }
                          //check if selected is more than zero
                          //test code
                          Collections.sort(toCompare, new Supermarket_product.PriceComparator());
                          String xxx = "";

                              //try bargraph data
                              ArrayList<BarEntry> entries = new ArrayList<>();
                              ArrayList<String> labels = new ArrayList<String>();


                          for (int j = 0; j < toCompare.size(); j++) {
                              xxx = xxx + toCompare.get(j).supermarket_name + " " + toCompare.get(j).location + " "
                                      + toCompare.get(j).unitcost + "\n";
                              //Put data in bargrapgh data sets
                            entries.add(new BarEntry((float) toCompare.get(j).unitcost,j));
                            labels.add(toCompare.get(j).supermarket_name);

                          }
                              BarDataSet dataset = new BarDataSet(entries, "Product price");
                              BarData data = new BarData(labels, dataset);
                              barChart.setData(data);
                              barChart.setDescription("Price Comparison");

                              //end bargraph trial

                              Toast.makeText(getActivity(), "Touch graph area", Toast.LENGTH_LONG).show();

                         // Toast.makeText(getActivity(), xxx, Toast.LENGTH_LONG).show();
                            // Supermarket_product sp = toCompare.toArray();
                          /* ListAdapter listAdapter = new CustomerAdapter(getContext(),toCompare);
                           list_compare.setAdapter(listAdapter); */
                          //end test code
                      }else {
                              Toast.makeText(getActivity(), "Please select supermarkets", Toast.LENGTH_LONG).show();
                          }
                      }
                  });


              }else{
                  Toast.makeText(compare_prices.getActivity(),"No supermarket found please try other products",Toast.LENGTH_SHORT).show();
              }

          }
          catch (Exception paramAnonymousView)
          {
              paramAnonymousView.printStackTrace();

          }

      } else{
          Toast.makeText(compare_prices.getActivity(),s,Toast.LENGTH_SHORT).show();
      }
    }
}
