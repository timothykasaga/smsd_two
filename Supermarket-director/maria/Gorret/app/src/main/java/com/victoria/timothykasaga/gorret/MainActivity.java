package com.victoria.timothykasaga.gorret;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.NavigationView;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;


public class MainActivity extends AppCompatActivity {
       Toolbar toolbar;
    DrawerLayout drawerLayout;
    ActionBarDrawerToggle toggle;
    FragmentTransaction fragmentTransaction;
    NavigationView navigationView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initialize();
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.home, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
      /*  int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }  */

        return super.onOptionsItemSelected(item);
    }

    public void initialize(){
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        navigationView = (NavigationView) findViewById(R.id.navigationview);
        setSupportActionBar(toolbar);
        drawerLayout = (DrawerLayout) findViewById(R.id.drawerlayout);
        toggle = new ActionBarDrawerToggle(this,drawerLayout,toolbar,R.string.drawer_open,R.string.drawer_close);
        drawerLayout.setDrawerListener(toggle);
        fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.add(R.id.main_container,new Initial());
        fragmentTransaction.commit();
        getSupportActionBar().setTitle("Smart shopping mate");

        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(MenuItem item) {
                switch (item.getItemId()){
                    case R.id.nav_Administration:{
                        fragmentTransaction = getSupportFragmentManager().beginTransaction();
                        fragmentTransaction.replace(R.id.main_container, new Initial());
                        fragmentTransaction.commit();
                        getSupportActionBar().setTitle("Smart shopping mate");
                        item.setChecked(true);
                        drawerLayout.closeDrawer(Gravity.LEFT);
                        break;
                    }
                    case R.id.nav_Loc_Supermarket:{
                        Intent intent = new Intent(MainActivity.this,Locate_supermarket.class);
                        startActivity(intent);
                        item.setChecked(true);
                        drawerLayout.closeDrawer(Gravity.LEFT);
                        break;
                    }
                    case R.id.nav_Loc_Product:{
                        fragmentTransaction = getSupportFragmentManager().beginTransaction();
                        fragmentTransaction.replace(R.id.main_container,new Locate_product());
                        fragmentTransaction.commit();
                        getSupportActionBar().setTitle("Product location");
                        item.setChecked(true);
                        drawerLayout.closeDrawer(Gravity.LEFT);
                        break;
                    }
                    case R.id.nav_Compare:{
                        fragmentTransaction = getSupportFragmentManager().beginTransaction();
                        fragmentTransaction.replace(R.id.main_container,new Compare_prices());
                        fragmentTransaction.commit();
                        getSupportActionBar().setTitle("Price comparison");
                        item.setChecked(true);
                        drawerLayout.closeDrawer(Gravity.LEFT);
                        break;
                    }
                    case R.id.nav_Help:{
                        String data = readTextFile(getApplicationContext(), R.raw.help);
                       // item.setChecked(true);
                        drawerLayout.closeDrawer(Gravity.LEFT);
                        final Dialog dialog = new Dialog(MainActivity.this);
                        dialog.setContentView(R.layout.helpdialog);
                        dialog.setTitle("Smart shopping mate");
                        final TextView text = (TextView)dialog.findViewById(R.id.txtHelp);
                        text.setText(data);
                        Button cancel = (Button)dialog.findViewById(R.id.btnHelp);
                        cancel.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                dialog.dismiss();
                            }
                        });

                        dialog.show();
                       // Toast.makeText(getApplicationContext(),data,Toast.LENGTH_LONG).show();
                        break;
                    }
                    case R.id.nav_About:{
                        String data = readTextFile(getApplicationContext(), R.raw.aboutus);
                        //item.setChecked(true);
                        drawerLayout.closeDrawer(Gravity.LEFT);
                        final Dialog dialog = new Dialog(MainActivity.this);
                        dialog.setContentView(R.layout.helpdialog);
                        dialog.setTitle("Smart shopping mate");
                        final TextView text = (TextView)dialog.findViewById(R.id.txtHelp);
                        text.setText(data);
                        Button cancel = (Button)dialog.findViewById(R.id.btnHelp);
                        cancel.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                dialog.dismiss();
                            }
                        });
                        dialog.show();
                       // Toast.makeText(getApplicationContext(),data,Toast.LENGTH_LONG).show();
                        break;
                    }

                }
                return false;
            }
        });
    }

    @Override
    protected void onPostCreate(@Nullable Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        toggle.syncState();
    }

    public static String readTextFile(Context ctx, int resId)
    {
        InputStream inputStream = ctx.getResources().openRawResource(resId);

        InputStreamReader inputreader = new InputStreamReader(inputStream);
        BufferedReader bufferedreader = new BufferedReader(inputreader);
        String line;
        StringBuilder stringBuilder = new StringBuilder();
        try
        {
            while (( line = bufferedreader.readLine()) != null)
            {
                stringBuilder.append(line);
                stringBuilder.append('\n');
            }
        }
        catch (Exception e)
        {
            return null;
        }
        return stringBuilder.toString();
    }
}
