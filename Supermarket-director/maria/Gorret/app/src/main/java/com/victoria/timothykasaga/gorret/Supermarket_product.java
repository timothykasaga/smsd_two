package com.victoria.timothykasaga.gorret;

import java.util.Comparator;

/**
 * Created by Leontymo on 4/25/2016.
 */
public class Supermarket_product{
    String supermarket_name, supermarket_id, product_name, measure, product_id, section, location;
    double unitcost;

    public double getUnitcost() {
        return unitcost;
    }

    public void setUnitcost(double unitcost) {
        this.unitcost = unitcost;
    }

    public Supermarket_product(String supermarket_name, String supermarket_id, String product_name, String measure,
                               String product_id, String section, double unitcost, String location) {
        this.supermarket_name = supermarket_name;
        this.supermarket_id = supermarket_id;
        this.product_name = product_name;
        this.measure = measure;
        this.product_id = product_id;
        this.section = section;
        this.unitcost = unitcost;
        this.location = location;
    }




    public static class PriceComparator implements Comparator<Supermarket_product>{
        @Override
        public int compare(Supermarket_product c1, Supermarket_product c2) {
            if(c1.getUnitcost() > c2.getUnitcost()){
                return  1;
            }
            else if(c1.getUnitcost() < c2.getUnitcost()){
                return  -1;
            }else
                return 0;
        }
    }




}


