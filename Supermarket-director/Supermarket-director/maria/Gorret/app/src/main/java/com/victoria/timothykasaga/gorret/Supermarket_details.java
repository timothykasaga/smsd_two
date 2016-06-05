package com.victoria.timothykasaga.gorret;

import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;


public class Supermarket_details extends AppCompatActivity {
    Toolbar toolbar;
    TextView bAddProdt;
    String admin;
    Button btsubdet;
    private int countclick = 0;
    EditText esddes;
    EditText esdema;
    EditText esdloc;
    EditText esdname;
    EditText esdpname;
    EditText esdpprice;
    EditText esdtel;
    EditText esdweb;
    double lat = 12;
    double log =13;
    ArrayList<Product> productArrayList;
    private String s_desc;
    private String s_email;
    private float[] s_latlng = new float[2];
    private String s_location;
    private String s_name;
    String[][] s_pdtprice;
    private String s_phone;
    ArrayList<String> s_prc = new ArrayList();
    ArrayList<String> s_products = new ArrayList();
    ArrayList<String> s_prt = new ArrayList();
    private String s_website;
    TextView txtLoc;
    TextView txtLoc1;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.supermarket_details);
        initialize();

       // Bundle bundle = getIntent().getExtras();
        //String flagdata = ;
        if(getIntent().getStringExtra("flags").equals("add")){
            //get product details
            LocalDatabase localDatabase = new LocalDatabase(this);
            admin = localDatabase.getLoggedInAdmin().username;
            Toast.makeText(getApplicationContext(), "From Add Username : " + this.admin+"\n"+localDatabase.getLocation().latitude, Toast.LENGTH_SHORT).show();
            lat = localDatabase.getLocation().latitude;
            log = localDatabase.getLocation().longitude;
            txtLoc.setText(String.valueOf(lat));
            txtLoc1.setText(String.valueOf(log));
        }else{
            btsubdet.setEnabled(true);
            LocalDatabase localDatabase = new LocalDatabase(this);
            DetailsPack detailsPack = getIntent().getParcelableExtra("com.timothykasaga.victoria.returnedpack");
            productArrayList = getIntent().getParcelableArrayListExtra("com.timothykasaga.victoria.prodts");
            esdname.setText(detailsPack.getS_name());
            esdloc.setText(detailsPack.getS_location());
            esdweb.setText(detailsPack.getS_website());
            esdema.setText(detailsPack.getS_email());
            esdtel.setText(detailsPack.getS_phone());
            esddes.setText(detailsPack.getS_desc());
            txtLoc.setText(detailsPack.getD_lat());
            txtLoc1.setText(detailsPack.getD_log());

            String prodts = "";
            if(productArrayList.size() != 0){
                bAddProdt.setTextColor(getResources().getColor(R.color.red));
                countclick++;
                int i = 0;
                while (i < productArrayList.size()){
                    prodts = prodts+ productArrayList.get(i).name+ ": "+productArrayList.get(i).unit_cost+"\n";
                    i++;
                }
            }else{
                prodts = "No products";
            }
            Toast.makeText(getApplicationContext(), "From Product list Username : " + detailsPack.getAdmin()+"\n"+
                    localDatabase.getLocation().latitude + "\nProducts: "+prodts, Toast.LENGTH_SHORT).show();

        }

        bAddProdt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DetailsPack detailsPack = readDetails();
                if(detailsPack.getS_name().equals("")){
                    Toast.makeText(getApplicationContext(),"Enter supermarket name",Toast.LENGTH_SHORT).show();
                }else {
                    Intent intent = new Intent(Supermarket_details.this,Product_list.class);
                    intent.putExtra("com.timothykasaga.victoria.pack",detailsPack);
                    intent.putExtra("flag","details");
                    startActivity(intent);
                }

            }
        });

        btsubdet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DetailsPack detailsPack = readDetails();
                //Must send both product list and detailsPack
                if(countclick != 0){
                    ServerRequests serverRequests = new ServerRequests(Supermarket_details.this);
                    serverRequests.storeSupermktDetailsInBackgound(detailsPack,productArrayList,Supermarket_details.this);
                    Toast.makeText(getApplicationContext(),"Products added",Toast.LENGTH_SHORT).show();

                }
            }
        });

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_supermarket_details, menu);
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

        return super.onOptionsItemSelected(item);
    }

    public void initialize(){
        esdname = ((EditText)findViewById(R.id.edtsdname));
        esdloc = ((EditText)findViewById(R.id.edtsdloc));
        esdweb = ((EditText)findViewById(R.id.edtsdweb));
        esdema = ((EditText)findViewById(R.id.edtsdema));
        esdtel = ((EditText)findViewById(R.id.edtsdtel));
        esddes = ((EditText)findViewById(R.id.edtsddes));
        btsubdet = ((Button)findViewById(R.id.btnSubDetails));
        txtLoc = ((TextView)findViewById(R.id.txtLoc));
        txtLoc1 = ((TextView)findViewById(R.id.txtLoc1));
        bAddProdt = (TextView) findViewById(R.id.btnAddProdt);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle("Supermarket details");
        toolbar.setTitleTextColor(getResources().getColor(R.color.white));
        toolbar.setNavigationIcon(R.drawable.home);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Supermarket_details.this,MainActivity.class);
                startActivity(intent);
            }
        });



    }

    public DetailsPack readDetails(){
        DetailsPack detailsPack = null;
        String sname = esdname.getText().toString();
        String location = esdloc.getText().toString().toUpperCase();
        String sweb = esdweb.getText().toString();
        String sema= esdema.getText().toString();
        String stel = esdtel.getText().toString();
        String sdes = esddes.getText().toString();
        String lat = txtLoc.getText().toString();
        String log = txtLoc1.getText().toString();
        detailsPack  = new DetailsPack(sname,location,sweb,sema,stel,sdes,lat,log,
                new LocalDatabase(this).getLoggedInAdmin().username);
        return detailsPack;
    }

    public void continueExecution(String res, Supermarket_details supermarket_details) {
        if(res.equals("success"+"\n")){
            Toast.makeText(supermarket_details.getApplicationContext(),"Data saved server response: "+res,Toast.LENGTH_SHORT).show();
        }else{
            Toast.makeText(supermarket_details.getApplicationContext(),"Data not saved server response: "+res,Toast.LENGTH_LONG).show();

        }
    }
}
