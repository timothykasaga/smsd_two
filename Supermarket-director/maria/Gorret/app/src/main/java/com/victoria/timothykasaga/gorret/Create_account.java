package com.victoria.timothykasaga.gorret;

import android.content.Context;
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
import android.widget.Toast;


public class Create_account extends AppCompatActivity {
    Toolbar toolbar;
    Button bSub;
    EditText etConPass;
    EditText etEmail;
    EditText etName;
    EditText etPass;
    EditText etTel;
    EditText etUName;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.create_account);
        initialize();

        bSub.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String name = etName.getText().toString();
                String email = etEmail.getText().toString();
                String username = etUName.getText().toString();
                String userpass = etPass.getText().toString();
                String conpass = etConPass.getText().toString();
                String telno = etTel.getText().toString();
                //check for empty fields.
                if(name.equals("") || username.equals("") || userpass.equals("") || conpass.equals("")){
                    Toast.makeText(getApplicationContext(),"Please fill in Name, Username and Password fields",Toast.LENGTH_SHORT).show();
                }else{
                    //check for equal password values
                    if(!userpass.equals(conpass)){
                        Toast.makeText(getApplicationContext(),"Password fields do not match ",Toast.LENGTH_SHORT).show();
                    }else{
                        //Store data
                        Admin admin = new Admin(name,email,username,userpass,telno);
                        ServerRequests serverRequests = new ServerRequests(Create_account.this);
                        serverRequests.storeAdmin(admin, Create_account.this);

                    }

                }
            }
        });
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_create_account, menu);
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
        etName = ((EditText)findViewById(R.id.edtName));
        etEmail = ((EditText)findViewById(R.id.edtEmail));
        etUName = ((EditText)findViewById(R.id.edtUname));
        etPass = ((EditText)findViewById(R.id.edtPass));
        etConPass = ((EditText)findViewById(R.id.edtConPass));
         etTel = ((EditText)findViewById(R.id.edtTel));
        bSub = ((Button)findViewById(R.id.bSubmit));

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle("Registration");
        toolbar.setTitleTextColor(getResources().getColor(R.color.white));
        toolbar.setNavigationIcon(R.drawable.home);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Create_account.this,MainActivity.class);
                startActivity(intent);
            }
        });
    }

    public void continueExecution(String response, Create_account create_account){
       //Toast.makeText(create_account.getApplicationContext(),response,Toast.LENGTH_SHORT).show();
            if(response.equals("success"+ "\n")){
               Toast.makeText(create_account.getApplicationContext(),"User account created",Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(create_account.getApplicationContext().getApplicationContext(),MainActivity.class);
                startActivity(intent);
            }else{
               Toast.makeText(create_account.getApplicationContext(),"User account not saved username already exists",Toast.LENGTH_SHORT).show();
            }
    }
}
