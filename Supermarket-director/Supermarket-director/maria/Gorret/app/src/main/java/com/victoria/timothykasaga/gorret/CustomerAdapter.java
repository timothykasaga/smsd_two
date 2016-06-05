package com.victoria.timothykasaga.gorret;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by Leontymo on 4/27/2016.
 */
public class CustomerAdapter extends ArrayAdapter<Supermarket_product> {
    public CustomerAdapter(Context context, ArrayList<Supermarket_product> supermarket_products) {
        super(context,R.layout.custom_row, supermarket_products);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        LayoutInflater inflater = LayoutInflater.from(getContext());
        View view = inflater.inflate(R.layout.custom_row,parent,false);
        Supermarket_product supermarket_product = getItem(position);
        TextView txtSm = (TextView)view.findViewById(R.id.textSm);
        TextView txtPrice = (TextView)view.findViewById(R.id.textPrice);
        txtSm.setText(supermarket_product.supermarket_name+" "+supermarket_product.location);
        txtPrice.setText(String.valueOf(supermarket_product.unitcost));
        return view;
    }
}
