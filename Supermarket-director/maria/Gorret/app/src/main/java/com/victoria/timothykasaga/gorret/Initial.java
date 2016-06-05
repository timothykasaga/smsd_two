package com.victoria.timothykasaga.gorret;


import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListAdapter;
import android.widget.ListView;


/**
 * A simple {@link Fragment} subclass.
 */
public class Initial extends Fragment {
           Button bAdmin;

    public Initial() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.initial, container, false);
        bAdmin = (Button) view.findViewById(R.id.butadmin);
        String[] services = {"Locate supermarket","Locate product","Compare prices"};
        ListView listView = (ListView) view.findViewById(R.id.listViewServices);
        ListAdapter listAdapter = new CustomerAdapter(getActivity(),services);
        listView.setAdapter(listAdapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                if(i == 0){
                    Intent intent = new Intent(getActivity(),Locate_supermarket.class);
                    startActivity(intent);
                }else if(i == 1){

                    FragmentTransaction fragmentTransaction = getFragmentManager().beginTransaction();
                    fragmentTransaction.replace(R.id.main_container,new Locate_product());
                    fragmentTransaction.commit();
                   // getSupportActionBar().setTitle("Product location");
                }else{
                    FragmentTransaction fragmentTransaction = getFragmentManager().beginTransaction();
                    fragmentTransaction.replace(R.id.main_container,new Compare_prices());
                    fragmentTransaction.commit();
                }
            }
        });


        bAdmin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FragmentTransaction fragmentTransaction = getFragmentManager().beginTransaction();
                fragmentTransaction.replace(R.id.main_container,new Login());
                fragmentTransaction.commit();

            }
        });
        // Inflate the layout for this fragment
        return view;
    }


}
