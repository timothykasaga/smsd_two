package com.victoria.timothykasaga.gorret;

import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.ArrayList;


public class ModificationPage extends AppCompatActivity {

    private Toolbar toolbar;
    private  Spinner spinner;
    Button bReturnAdd;
    Button bManageSel;
    TextView txtLoggedUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.modification_page);
        initliaze();
        LocalDatabase localDatabase = new LocalDatabase(this);
        final String user = localDatabase.getLoggedInAdmin().username;
        txtLoggedUser.setText(user);
        ServerRequests serverRequests = new ServerRequests(ModificationPage.this);
        serverRequests.retriveAdminSupermktIds(user, ModificationPage.this);

        bReturnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ModificationPage.this, Add_supermarket.class);
                startActivity(intent);
            }
        });

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_modification_page, menu);
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

    public void initliaze(){
      bReturnAdd = (Button) findViewById(R.id.btnReturnAdd);
        bManageSel = (Button) findViewById(R.id.btnManageSel);
        txtLoggedUser = (TextView) findViewById(R.id.txtLoggedInUser);
        spinner = (Spinner) findViewById(R.id.spinner_man_sm);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle("Manage your supermarkets");
        toolbar.setTitleTextColor(getResources().getColor(R.color.white));
        toolbar.setNavigationIcon(R.drawable.home);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ModificationPage.this,MainActivity.class);
                startActivity(intent);
            }
        });

    }

    public void continueExecution(String s, final ModificationPage modificationPage) {
        Toast.makeText(modificationPage.getApplicationContext(),s,Toast.LENGTH_LONG).show();
        if(!s.equals("fail"+"\n")){
            try {
                JSONArray jsonArray = new JSONArray(s);
                String[] mySmIds = new String[jsonArray.length()];
                for (int i = 0; i <jsonArray.length() ; i++) {
                    mySmIds[i] = jsonArray.get(i).toString();
                }

                ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(getApplicationContext(),R.layout.support_simple_spinner_dropdown_item,mySmIds);
                spinner.setAdapter(arrayAdapter);
                bManageSel.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        String supermarket_id = spinner.getSelectedItem().toString();
                        LocalDatabase localDatabase = new LocalDatabase(modificationPage.getApplicationContext());
                        final String user = localDatabase.getLoggedInAdmin().username;
                        Intent intent = new Intent(ModificationPage.this,Modify_first.class);
                        intent.putExtra("s_id",supermarket_id);
                        intent.putExtra("admin",user);
                        startActivity(intent);
                    }
                });


            } catch (Exception e) {
                e.printStackTrace();
            }
        }


     }
}
