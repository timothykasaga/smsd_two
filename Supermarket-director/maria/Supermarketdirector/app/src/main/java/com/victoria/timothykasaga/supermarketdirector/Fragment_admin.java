package com.victoria.timothykasaga.supermarketdirector;

/**
 * Created by Leontymo on 4/20/2016.
 */
        import android.app.Fragment;
        import android.app.FragmentManager;
        import android.app.FragmentTransaction;
        import android.content.Intent;
        import android.os.Bundle;
        import android.view.LayoutInflater;
        import android.view.View;
        import android.view.View.OnClickListener;
        import android.view.ViewGroup;
        import android.widget.EditText;
        import android.widget.TextView;
        import android.widget.Toast;
        import org.json.JSONException;
        import org.json.JSONObject;

public class Fragment_admin
        extends Fragment
{
    String ans;
    TextView bcreate;
    TextView bforgot;
    TextView btnLog;
    EditText eUName;
    EditText eUpass;

    public View onCreateView(LayoutInflater paramLayoutInflater, ViewGroup paramViewGroup, Bundle paramBundle)
    {
        paramLayoutInflater = paramLayoutInflater.inflate(2130968619, paramViewGroup, false);
        this.btnLog = ((TextView)paramLayoutInflater.findViewById(2131624107));
        this.bcreate = ((TextView)paramLayoutInflater.findViewById(2131624109));
        this.bforgot = ((TextView)paramLayoutInflater.findViewById(2131624108));
        this.eUName = ((EditText)paramLayoutInflater.findViewById(2131624105));
        this.eUpass = ((EditText)paramLayoutInflater.findViewById(2131624106));
        this.bcreate.setOnClickListener(new View.OnClickListener()
        {
            public void onClick(View paramAnonymousView)
            {
                paramAnonymousView = new Intent("com.timothykasaga.victoria.Create_ac");
                Fragment_admin.this.startActivity(paramAnonymousView);
            }
        });
        this.bforgot.setOnClickListener(new View.OnClickListener()
        {
            public void onClick(View paramAnonymousView)
            {
                Fragment_admin.this.getFragmentManager().beginTransaction().replace(2131624112, new Forgot_pass()).commit();
            }
        });
        this.btnLog.setOnClickListener(new View.OnClickListener()
        {
            public void onClick(View paramAnonymousView)
            {
                paramAnonymousView = Fragment_admin.this.eUName.getText().toString();
                Object localObject = Fragment_admin.this.eUpass.getText().toString();
                if ((paramAnonymousView.equals("")) || (((String)localObject).equals(""))) {
                    Toast.makeText(Fragment_admin.this.getActivity(), "Please insert username and password", 0).show();
                }
                for (;;)
                {
                    return;
                    paramAnonymousView = new Contact(paramAnonymousView, (String)localObject);
                    new ServerRequests(Fragment_admin.this.getActivity()).fetchDataInBackground(paramAnonymousView, new GetUserCallBack()
                    {
                        public void done(String paramAnonymous2String)
                        {
                            Fragment_admin.this.ans = paramAnonymous2String;
                        }
                    });
                    if (Fragment_admin.this.ans == null)
                    {
                        Toast.makeText(Fragment_admin.this.getActivity(), "Login failed check credentials", 0).show();
                        return;
                    }
                    if (Fragment_admin.this.ans.length() == 9)
                    {
                        Toast.makeText(Fragment_admin.this.getActivity(), "Login failed", 0).show();
                        return;
                    }
                    try
                    {
                        localObject = new JSONObject(Fragment_admin.this.ans);
                        try
                        {
                            if ((((JSONObject)localObject).has("uname")) && (((JSONObject)localObject).has("pass")))
                            {
                                String str1 = ((JSONObject)localObject).getString("uname");
                                String str2 = ((JSONObject)localObject).getString("pass");
                                int i = ((JSONObject)localObject).getInt("logintimes");
                                if ((!str1.equals(paramAnonymousView.username)) || (!str2.equals(paramAnonymousView.pass))) {
                                    break label323;
                                }
                                if (i != 0) {
                                    break label286;
                                }
                                localObject = new Intent(Fragment_admin.this.getActivity(), Add_sm.class);
                                ((Intent)localObject).putExtra("username", paramAnonymousView.username);
                                Fragment_admin.this.startActivity((Intent)localObject);
                                return;
                            }
                        }
                        catch (JSONException paramAnonymousView) {}
                    }
                    catch (JSONException paramAnonymousView)
                    {
                        for (;;) {}
                    }
                }
                paramAnonymousView.printStackTrace();
                return;
                label286:
                localObject = new Intent(Fragment_admin.this.getActivity(), ModificationPage.class);
                ((Intent)localObject).putExtra("username", paramAnonymousView.username);
                Fragment_admin.this.startActivity((Intent)localObject);
                return;
                label323:
                Toast.makeText(Fragment_admin.this.getActivity(), "Incorrect credentials", 0).show();
            }
        });
        return paramLayoutInflater;
    }
}

