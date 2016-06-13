package com.victoria.timothykasaga.gorret;

import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Objects;


public class Modify_second extends AppCompatActivity {
    Toolbar toolbar;
    Button btnSave;
    ListView myProductList;
    String adminName, supermarket_id;
    ArrayList<Product> arrayList_prodts;
    ArrayList<String> product_array;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.modify_second);
        initliaze();
        LocalDatabase localDatabase = new LocalDatabase(this);
        adminName = localDatabase.getLoggedInAdmin().username;
        supermarket_id = getIntent().getStringExtra("sm_id");
        ServerRequests serverRequests = new ServerRequests(Modify_second.this);
        serverRequests.returnProductList(Modify_second.this,adminName,supermarket_id);
        //Pick product list from extras
        //Pick admin and supermarket_id from extras
        //Set adapter for list from product list
        //Set onItemclick for list
        //Set onItemLongclick to delete product


    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_modify_second, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }
        if(id == R.id.btn_mod){
              if(arrayList_prodts.size() == 0){
                  Toast.makeText(getApplicationContext(),"Nothing",Toast.LENGTH_SHORT).show();
              }else{
                  Toast.makeText(getApplicationContext(),"Some thing "+arrayList_prodts.size(),Toast.LENGTH_SHORT).show();
                  ServerRequests serverRequests1 = new ServerRequests(Modify_second.this);
                  serverRequests1.updateProductList(supermarket_id,arrayList_prodts,Modify_second.this);
              }
            return true;
        }
        if (id == R.id.btn_add) {
            final Dialog dialog = new Dialog(Modify_second.this);
            dialog.setContentView(R.layout.mydial);
            dialog.setTitle("Edit item");
            final EditText editName = (EditText)dialog.findViewById(R.id.edtdPname);
            final EditText editCost = (EditText)dialog.findViewById(R.id.edtUnitCost);
            final EditText editUnits = (EditText)dialog.findViewById(R.id.edtUnits);
            final EditText editPdtId= (EditText)dialog.findViewById(R.id.edtPId);
            final EditText editSection= (EditText)dialog.findViewById(R.id.edtsection);

            // editName.setText(pname);
            Button cancel = (Button)dialog.findViewById(R.id.btndCancel);
            Button save = (Button)dialog.findViewById(R.id.btndSave);
            save.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if ((!Objects.equals(editName.getText().toString(), "")) && (!Objects.equals(editCost.getText().toString(), "")))
                    {
                        String name = editName.getText().toString();
                        double d = Float.parseFloat(editCost.getText().toString());
                        String units = editUnits.getText().toString();
                        String prodtId = editPdtId.getText().toString();
                        String section = editSection.getText().toString();
                        Product product = new Product(name,prodtId,units,d,section);
                        arrayList_prodts.add(product);
                        product_array.add(editName.getText().toString());
                        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(getApplicationContext(),
                                android.R.layout.simple_list_item_1, product_array);
                        myProductList.setAdapter(arrayAdapter);
                        dialog.dismiss();
                    }else{
                        Toast.makeText(getApplicationContext(), "Enter Product name and price", Toast.LENGTH_SHORT).show();
                    }
                }
            });

            cancel.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    dialog.dismiss();
                }
            });
            dialog.show();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public void initliaze() {
        myProductList = (ListView) findViewById(R.id.listView);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        //toolbar.setTitle("Product Details");
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Product Details");
        toolbar.setTitleTextColor(getResources().getColor(R.color.white));
        toolbar.setNavigationIcon(R.drawable.home);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Modify_second.this, MainActivity.class);
                startActivity(intent);
            }
        });

    }

    public void continueExecution(String s, final Modify_second modify_second) {
        Toast.makeText(modify_second.getApplicationContext(),s,Toast.LENGTH_SHORT).show();
        try {
            final ArrayList<Product> productArrayList = new ArrayList<>();
            final ArrayList<String> myproducts = new ArrayList<>();
            JSONArray jsonArray = new JSONArray(s);
            for (int i = 0; i <jsonArray.length() ; i++) {
                JSONObject jsonObject = jsonArray.getJSONObject(i);
                String pname = jsonObject.getString("p_name");
                String pid = jsonObject.getString("p_id");
                String pmeasure = jsonObject.getString("p_measure");
                String punitcost = jsonObject.getString("p_unitcost");
                String psection = jsonObject.getString("p_section");
                myproducts.add(jsonObject.getString("p_name"));
               productArrayList.add(new Product(pname,pid,pmeasure,Float.parseFloat(punitcost),psection));
            }

            ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(modify_second.getApplicationContext(),
                    android.R.layout.simple_list_item_1,myproducts);
            myProductList.setAdapter(arrayAdapter);
            myProductList.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
                @Override
                public boolean onItemLongClick(final AdapterView<?> adapterView, View view, final int i, long l) {

                    final AlertDialog.Builder alertDialog = new AlertDialog.Builder(Modify_second.this);
                    alertDialog.setMessage("Delete Product ?");
                    alertDialog.setNegativeButton("No", new DialogInterface.OnClickListener()
                    {
                        public void onClick(DialogInterface paramAnonymous2DialogInterface, int paramAnonymous2Int) {}
                    });
                    alertDialog.setPositiveButton("Yes", new DialogInterface.OnClickListener()
                    {
                        public void onClick(DialogInterface paramAnonymous2DialogInterface, int paramAnonymous2Int)
                        {
                            final String product = adapterView.getItemAtPosition(i).toString();
                            for (int k = 0; k < productArrayList.size(); k++) {
                                if ((productArrayList.get(k).name).equals(product)) {
                                    productArrayList.remove(k);
                                }
                            }
                            //remove old product name
                            myproducts.remove(product);
                            ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(getApplicationContext(),
                                    android.R.layout.simple_list_item_1, myproducts);
                            modify_second.myProductList.setAdapter(arrayAdapter);

                        }
                    });
                    alertDialog.show();

                    return false;

                }
            });
            myProductList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                    final String product = adapterView.getItemAtPosition(i).toString();
                    String pdt_name = null, pdt_id = null, pdt_measure = null, pdt_cost = null, pdt_sec = null;
                    for (int j = 0; j < productArrayList.size(); j++) {
                        if ((productArrayList.get(j).name).equals(product)) {
                            pdt_name = product;
                            pdt_id = productArrayList.get(j).prodt_id;
                            pdt_measure = productArrayList.get(j).units;
                            pdt_cost = String.valueOf(productArrayList.get(j).unit_cost);
                            pdt_sec = productArrayList.get(j).section_name;
                        }
                    }
                    final Dialog dialog = new Dialog(Modify_second.this);
                    dialog.setContentView(R.layout.mydial);
                    dialog.setTitle("Edit item");
                    final EditText editName = (EditText) dialog.findViewById(R.id.edtdPname);
                    final EditText editCost = (EditText) dialog.findViewById(R.id.edtUnitCost);
                    final EditText editUnits = (EditText) dialog.findViewById(R.id.edtUnits);
                    final EditText editPdtId = (EditText) dialog.findViewById(R.id.edtPId);
                    final EditText editSection = (EditText) dialog.findViewById(R.id.edtsection);
                    Button cancel = (Button) dialog.findViewById(R.id.btndCancel);
                    Button save = (Button) dialog.findViewById(R.id.btndSave);

                    editName.setText(pdt_name);
                    editUnits.setText(pdt_measure);
                    editPdtId.setText(pdt_id);
                    editSection.setText(pdt_sec);
                    editCost.setText(pdt_cost);

                    save.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            if ((!Objects.equals(editName.getText().toString(), "")) && (!Objects.equals(editCost.getText().toString(), ""))) {
                                //remove old details
                                for (int k = 0; k < productArrayList.size(); k++) {
                                    if ((productArrayList.get(k).name).equals(product)) {
                                        productArrayList.remove(k);
                                    }
                                }
                                //remove old product name
                                myproducts.remove(product);

                                String name = editName.getText().toString();
                                double d = Float.parseFloat(editCost.getText().toString());
                                String units = editUnits.getText().toString();
                                String prodtId = editPdtId.getText().toString();
                                String section = editSection.getText().toString();
                                Product newproduct = new Product(name, prodtId, units, d, section);
                                productArrayList.add(newproduct);
                                myproducts.add(editName.getText().toString());
                                ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(getApplicationContext(),
                                        android.R.layout.simple_list_item_1, myproducts);
                                modify_second.myProductList.setAdapter(arrayAdapter);
                                dialog.dismiss();
                            } else {
                                Toast.makeText(getApplicationContext(), "Enter Product name and price", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });

                    cancel.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            dialog.dismiss();
                        }
                    });

                    dialog.show();
                }
            });

            modify_second.arrayList_prodts = productArrayList;
            modify_second.product_array =
                    myproducts;

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void continueExecutionAfterUpdateProdtList(String s, Modify_second modify_second) {
      if(!s.equals("fail"+"\n")){
          Toast.makeText(modify_second.getApplicationContext(),"Changes successfully made",Toast.LENGTH_SHORT).show();
          Intent intent = new Intent(modify_second.getApplicationContext(),ModificationPage.class);
          startActivity(intent);
      }
        else{
          Toast.makeText(modify_second.getApplicationContext(),"Changes not made",Toast.LENGTH_SHORT).show();
      }


    }
}
