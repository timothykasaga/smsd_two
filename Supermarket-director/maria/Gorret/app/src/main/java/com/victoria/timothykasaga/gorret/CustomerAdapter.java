package com.victoria.timothykasaga.gorret;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by Leontymo on 4/27/2016.
 */
public class CustomerAdapter extends ArrayAdapter<String> {

    public CustomerAdapter(Context context, String[] services) {
        super(context,R.layout.custom_row,services);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater layoutInflater = LayoutInflater.from(getContext());
        View customView = layoutInflater.inflate(R.layout.custom_row,parent,false);
        String singleService = getItem(position);
        TextView tvservice = (TextView) customView.findViewById(R.id.textService);
        ImageView img = (ImageView) customView.findViewById(R.id.Imageservice);

        tvservice.setText(singleService);
        if(singleService.equals("Locate supermarket")){
            img.setImageResource(R.drawable.locate_pt);
        }else if(singleService.equals("Locate product")){
            img.setImageResource(R.drawable.locate_sm);
        }else{
            img.setImageResource(R.drawable.compare_pr);
        }

        return  customView;
    }
}
