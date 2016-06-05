package com.victoria.timothykasaga.gorret;


import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;


/**
 * A simple {@link Fragment} subclass.
 */
public class Login extends Fragment {
       TextView blogin,bcreate,bforgot;
       View view;
        EditText eUName;
        EditText eUpass;
    public Login() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.login, container, false);
        initialize(view);
        // Inflate the layout for this fragment
        return view;
    }


    public void initialize(View view){
        blogin = (TextView) view.findViewById(R.id.btnLogin);
        bcreate = (TextView) view.findViewById(R.id.btnCreate);
        bforgot = (TextView) view.findViewById(R.id.btnfgPass);
       eUName = ((EditText)view.findViewById(R.id.edtAUName));
       eUpass = ((EditText)view.findViewById(R.id.edtAUpass));

        blogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String username = eUName.getText().toString();
                String userpassword = eUpass.getText().toString();
                //check whether password fields are equal
                if ((username.equals("")) || (userpassword.equals(""))) {
                    Toast.makeText(getActivity(), "Please insert username and password", Toast.LENGTH_SHORT).show();
                }
                else{
                    Admin admin = new Admin(username,userpassword);
                    ServerRequests serverRequests = new ServerRequests(getActivity());
                    serverRequests.authenticateAdmin(admin,Login.this);
                   // Intent intent = new Intent(getActivity(),Add_supermarket.class);
                   // startActivity(intent);
                }

            }
        });

        bcreate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(),Create_account.class);
                startActivity(intent);
            }
        });

        bforgot.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FragmentTransaction fragmentTransaction = getFragmentManager().beginTransaction();
                fragmentTransaction.replace(R.id.main_container,new Reset());
                fragmentTransaction.commit();
            }
        });
    }

    public void continueExecution(String response, Login login){
            if(response.equals("fail"+"\n")){
                Toast.makeText(login.getActivity(),"Incorrect credentials",Toast.LENGTH_SHORT).show();
            }else{
               // Toast.makeText(login.getActivity(),"User exists",Toast.LENGTH_SHORT).show();
                try
                {
                    JSONObject jsonObject = new JSONObject(response);
                    try
                    {
                        if ((jsonObject.has("uname")) && (jsonObject.has("pass")))
                        {
                            String username = jsonObject.getString("uname");
                            String userpassword  = jsonObject.getString("pass");
                            String name = jsonObject.getString("name");
                            String email  = jsonObject.getString("email");
                            String telno = jsonObject.getString("tel");

                            //set logged in user;
                            Admin admin = new Admin(name,email,username,userpassword,telno);
                            LocalDatabase localDatabase = new LocalDatabase(login.getActivity());
                            localDatabase.storeData(admin);
                            localDatabase.setuserLoggedIn(true);

                            int i = jsonObject.getInt("logintimes");

                            if (i != 0) {
                                login.eUName.setText("");
                                login.eUpass.setText("");
                                Intent intent = new Intent(login.getActivity(), ModificationPage.class);
                                intent.putExtra("username", username);
                                startActivity(intent);

                            }else{
                                login.eUName.setText("");
                                login.eUpass.setText("");
                                Intent intent = new Intent(login.getActivity(), Add_supermarket.class);
                                intent.putExtra("username", username);
                                startActivity(intent);

                            }

                        }
                    }
                    catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
                catch (JSONException e)
                {
                    e.printStackTrace();
                }
            }


    }
}
