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
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;


public class Modify_first extends AppCompatActivity {
     private  String admin;
    private  String supermarket_id;
    EditText etSname, etSWeb, etSEmail,etSTel,etSDes;
    RadioGroup rgOption; RadioButton rbselested;
    Toolbar toolbar;
    Button proceed;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.modify_first);
        initliaze();
        admin = getIntent().getStringExtra("admin");
        supermarket_id = getIntent().getStringExtra("s_id");
        Toast.makeText(this,"Admin: "+admin+"\nSupermarket_id: "+supermarket_id,Toast.LENGTH_SHORT).show();
        //Pick Data from database where admin and supermarket_id
       ServerRequests serverRequests = new ServerRequests(Modify_first.this);
        serverRequests.retriveSupermarketDetails(admin,supermarket_id, Modify_first.this);
        //finish pick data
        proceed.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int checked_but = rgOption.getCheckedRadioButtonId();
                switch (checked_but){
                    case R.id.rbdontsave:{
                        Intent intent = new Intent(Modify_first.this,Modify_second.class);
                        //put Extras e.g admin supermarket_id and product list
                        intent.putExtra("sm_id",supermarket_id);
                        startActivity(intent);
                        break;
                    }
                    case R.id.rbsave:{
                        DetailsPack  detailsPack = new DetailsPack(etSname.getText().toString(),""
                                ,etSWeb.getText().toString(),etSEmail.getText().toString(),etSTel.getText().toString(),
                                etSDes.getText().toString(),"","",admin);
                        ServerRequests serverRequests1 = new ServerRequests(Modify_first.this);
                        serverRequests1.saveModifiedGeneralDetails(admin,supermarket_id,detailsPack,Modify_first.this);
                        break;
                    }
                    case R.id.rbcancel:{
                        Intent intent = new Intent(Modify_first.this,ModificationPage.class);
                        startActivity(intent);
                        break;
                    }

                }

            }
        });
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_modify_first, menu);
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

    public void initliaze() {
        etSname = (EditText) findViewById(R.id.edt_mod_sdname);
        etSWeb = (EditText) findViewById(R.id.edt_mod_sdweb);
        etSEmail = (EditText) findViewById(R.id.edt_mod_sdema);
        etSTel = (EditText) findViewById(R.id.edt_mod_sdtel);
        etSDes = (EditText) findViewById(R.id.edt_mod_sddes);
        proceed = (Button) findViewById(R.id.btn_proceed);
        rgOption = (RadioGroup) findViewById(R.id.rgSaveOption);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle("General Details");
        toolbar.setTitleTextColor(getResources().getColor(R.color.white));
        toolbar.setNavigationIcon(R.drawable.home);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Modify_first.this, MainActivity.class);
                startActivity(intent);
            }
        });
    }

    public void continueExecution(String s, Modify_first modify_first) {
    Toast.makeText(modify_first.getApplicationContext(),s,Toast.LENGTH_LONG).show();
     if(!s.equals("fail"+"\n")){
         try {
             JSONArray jsonArray = new JSONArray(s);
             for (int i = 0; i < jsonArray.length(); i++) {
                 JSONObject jsonObject = jsonArray.getJSONObject(i);
                 if(jsonObject.has("name")){
                     String name = jsonObject.getString("name");
                     modify_first.etSname.setText(name);
                 }
                 if(jsonObject.has("tel")){
                     String tel = jsonObject.getString("tel");
                     modify_first.etSTel.setText(tel);
                 }
                 if(jsonObject.has("web")){
                     String web = jsonObject.getString("web");
                     modify_first.etSWeb.setText(web);
                 }
                 if(jsonObject.has("email")){
                     String email = jsonObject.getString("email");
                     modify_first.etSEmail.setText(email);
                 }
                 if(jsonObject.has("slogan")){
                     String slogan = jsonObject.getString("slogan");
                     modify_first.etSDes.setText(slogan);
                 }
             }
         } catch (Exception e) {
             e.printStackTrace();
         }


     }
    }

    public void continueExecutionAfterSave(String s, Modify_first modify_first, String supermarket_id) {
        Toast.makeText(modify_first.getApplicationContext(),s,Toast.LENGTH_SHORT).show();
        Intent intent = new Intent(Modify_first.this,Modify_second.class);
        intent.putExtra("sm_id", supermarket_id);
        //put Extras e.g admin supermarket_id and product list
        startActivity(intent);
    }
}
