package com.victoria.timothykasaga.gorret;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.telecom.Call;
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

import java.util.ArrayList;
import java.util.Objects;


public class Product_list extends AppCompatActivity {
    Toolbar toolbar;
    ArrayList<String> arrayList = new ArrayList();
    final Context context = this;
    DetailsPack detailsPack;
    ListView list;
    ArrayList<Product> prodtArray = new ArrayList();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.product_list);
        initialize();
        detailsPack = getIntent().getParcelableExtra("com.timothykasaga.victoria.pack");

        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                String pname = adapterView.getItemAtPosition(i).toString();
                final Dialog dialog = new Dialog(Product_list.this);
                dialog.setContentView(R.layout.mydial);
                dialog.setTitle("Edit item");
                final EditText editName = (EditText)dialog.findViewById(R.id.edtdPname);
                final EditText editCost = (EditText)dialog.findViewById(R.id.edtUnitCost);
                final EditText editUnits = (EditText)dialog.findViewById(R.id.edtUnits);
                final EditText editPdtId= (EditText)dialog.findViewById(R.id.edtPId);
                final EditText editSection= (EditText)dialog.findViewById(R.id.edtsection);

                editName.setText(pname);
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
                            prodtArray.add(product);
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

            }
        });

        list.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener()
        {
            public boolean onItemLongClick(final AdapterView<?> paramAnonymousAdapterView, View paramAnonymousView, final int paramAnonymousInt, long paramAnonymousLong)
            {
                AlertDialog.Builder alertDialog = new AlertDialog.Builder(Product_list.this.context);
                alertDialog.setMessage("Delete Item ?");
                alertDialog.setNegativeButton("No", new DialogInterface.OnClickListener()
                {
                    public void onClick(DialogInterface paramAnonymous2DialogInterface, int paramAnonymous2Int) {}
                });
                alertDialog.setPositiveButton("Yes", new DialogInterface.OnClickListener()
                {
                    public void onClick(DialogInterface paramAnonymous2DialogInterface, int paramAnonymous2Int)
                    {
                        arrayList.remove(paramAnonymousAdapterView.getItemAtPosition(paramAnonymousInt).toString());
                        ListAdapter listAdapter = new ArrayAdapter<>(getApplicationContext(), android.R.layout.simple_list_item_1, arrayList);
                        Product_list.this.list.setAdapter(listAdapter);
                    }
                });
                alertDialog.show();
                return false;
            }
        });

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_product_list, menu);
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
        if(id == R.id.savebtn){
            Intent intent;
            if (getIntent().getStringExtra("flag").equals("modify")) {
                intent = new Intent(Product_list.this, ModificationPage.class);

            }
            else{
                intent = new Intent(Product_list.this, Supermarket_details.class);
            }
            intent.putParcelableArrayListExtra("com.timothykasaga.victoria.prodts", prodtArray);
            intent.putExtra("com.timothykasaga.victoria.returnedpack", detailsPack);
            intent.putExtra("flags", "list");
            startActivity(intent);
        }
        if(id == R.id.addbtn){
            final Dialog dialog = new Dialog(Product_list.this);
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
                        prodtArray.add(product);
                        arrayList.add(editName.getText().toString());
                        ListAdapter arrayAdapter = new ArrayAdapter<>(getApplicationContext(),
                                android.R.layout.simple_list_item_1, arrayList);
                        list.setAdapter(arrayAdapter);
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
        }

        return super.onOptionsItemSelected(item);
    }

    public void initialize(){
        list = ((ListView)findViewById(R.id.list_prodts));
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Product list");
        toolbar.setTitleTextColor(getResources().getColor(R.color.white));
        toolbar.setNavigationIcon(R.drawable.home);

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Product_list.this,MainActivity.class);
                startActivity(intent);
            }
        });
    }
}
