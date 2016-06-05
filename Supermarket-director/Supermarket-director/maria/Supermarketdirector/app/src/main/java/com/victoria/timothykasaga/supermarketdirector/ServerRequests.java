package com.victoria.timothykasaga.supermarketdirector;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Entity;
import android.net.Uri;
import android.os.AsyncTask;
import android.provider.Settings;

import org.apache.http.params.HttpParams;
import org.json.JSONArray;
import org.json.JSONObject;
import org.w3c.dom.NameList;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;


/**
 * Created by timothy kasaga on 1/5/2016.
 */
public class ServerRequests {
    String result ;
    ProgressDialog progressDialog;
    public static  final int CONNECTION_TIMEOUT = 15000;
    public static final  String SERVER ="http://timothykasaga.net16.net/";


    public ServerRequests(Context context) {
        progressDialog = new ProgressDialog(context);
        progressDialog.setCancelable(false);
        progressDialog.setTitle("Processing");
        progressDialog.setMessage("Please wait....");

    }

    public void storeDataInBackground(Contact contact){
        progressDialog.show();
        new StoreDataAsycTask(contact).execute();
    }
    public String fetchDataInBackground(Contact contact, GetUserCallBack getUserCallBack){
        progressDialog.show();
        new FetchDataAsyncTask(contact,getUserCallBack).execute();
        return result;
    }
    //Handle storing Storedetails

    public void storeDetailsInBackground(StoreDetails storeDetails){
        progressDialog.show();
        new StoreDetailsAsycTask(storeDetails).execute();
    }

    public void ReturnAllAddress(GetUserCallBack getUserCallBack){
        progressDialog.show();
        new GetAllAddressesAsyncTask(getUserCallBack).execute();

    }

    public void searchByLocation(String location, GetUserCallBack getUserCallBack) {
        progressDialog.show();
        new SearchByLocationAsyncTask(location,getUserCallBack).execute();
    }

    public class StoreDataAsycTask extends AsyncTask<Void,Void,Void>{
        Contact contact;

        public StoreDataAsycTask(Contact contact) {
            this.contact = contact;
        }

        @Override
        protected Void doInBackground(Void... params) {
            try {
                StringBuilder content = new StringBuilder("res");
               // URL url = new URL(SERVER+"registerUser.php");
                URL url = new URL("http://10.0.3.2/smsd/registerUser.php");
                HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
                urlConnection.setReadTimeout(CONNECTION_TIMEOUT);
                urlConnection.setConnectTimeout(CONNECTION_TIMEOUT);
                urlConnection.setDoInput(true);
                urlConnection.setDoOutput(true);

               Uri.Builder builder = new Uri.Builder().appendQueryParameter("Name",contact.name)
                        .appendQueryParameter("Email", contact.email)
                        .appendQueryParameter("UName", contact.username).
                                appendQueryParameter("Pass", contact.pass).appendQueryParameter("BisName",contact.bisname)
                        .appendQueryParameter("Tel", contact.tel);


                String query = builder.build().getEncodedQuery();

                OutputStream os = urlConnection.getOutputStream();
                BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(os,"UTF-8"));
                writer.write(query);
                writer.flush();
                writer.close();
                os.close();

                BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(urlConnection.getInputStream()));
                String line;
                // read from the urlconnection via the bufferedreader
                while ((line = bufferedReader.readLine()) != null)
                {
                    content.append(line + "\n");
                }
                bufferedReader.close();
               // result = content.toString();


            } catch (Exception e) {
                e.printStackTrace();
            }

            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            progressDialog.dismiss();
        }
    }

    public class FetchDataAsyncTask extends AsyncTask<Void,Void,String>{
        Contact contact;
        GetUserCallBack callBack;

        StringBuilder content1 = new StringBuilder("Res");
        public FetchDataAsyncTask(Contact contact, GetUserCallBack callBack) {
            this.contact = contact;
            this.callBack =callBack;
        }
        @Override
        protected String doInBackground(Void... params) {
                    String res = null;
            try {

             //URL url = new URL(SERVER+"retrive.php");
             URL url = new URL("http://10.0.3.2/smsd/retrive.php");
                URLConnection urlConnection = url.openConnection();

                urlConnection.setReadTimeout(CONNECTION_TIMEOUT);
                urlConnection.setConnectTimeout(CONNECTION_TIMEOUT);
                urlConnection.setDoInput(true);
                urlConnection.setDoOutput(true);

                Uri.Builder builder = new Uri.Builder().appendQueryParameter("UName", contact.username)
                        .appendQueryParameter("Pass", contact.pass);
                String query = builder.build().getEncodedQuery();

                OutputStream os = urlConnection.getOutputStream();
                BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(os,"UTF-8"));
                writer.write(query);
                writer.flush();
                writer.close();
                os.close();

                BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(urlConnection.getInputStream()));
                String line;
                // read from the urlconnection via the bufferedreader
                while ((line = bufferedReader.readLine()) != null)
                {
                    content1.append(line + "\n");
                }
                bufferedReader.close();
               res = content1.toString();
            } catch (Exception e) {
                e.printStackTrace();
                res = e.toString();
            }

            return res;
        }

        @Override
        protected void onPostExecute(String res) {
            progressDialog.dismiss();
            callBack.done(res);
            super.onPostExecute(res);


        }
    }

    public class StoreDetailsAsycTask extends AsyncTask<Void,Void,Void>{
        StoreDetails storeDetails;

        public StoreDetailsAsycTask(StoreDetails storeDetails) {
            this.storeDetails = storeDetails;
        }

        @Override
        protected Void doInBackground(Void... params) {
            try {
                StringBuilder content = new StringBuilder("res");
               // URL url = new URL(SERVER+"storeDetails.php");
                URL url = new URL("http://10.0.3.2/smsd/storeDetails.php");
                HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
                urlConnection.setReadTimeout(CONNECTION_TIMEOUT);
                urlConnection.setConnectTimeout(CONNECTION_TIMEOUT);
                urlConnection.setDoInput(true);
                urlConnection.setDoOutput(true);

                //store Products from arraylist
                JSONArray jsonArray = new JSONArray();
                for(int i=0;i<storeDetails.getSlatlng().length;i++){
                    jsonArray.put(storeDetails.getSlatlng()[i]);
                }
                String sjson = jsonArray.toString();

                String[] products = storeDetails.getSproducts().toArray(new String[storeDetails.getSproducts().size()]);
                JSONArray jsonArray1 = new JSONArray();
                for (int x = 0;x<products.length;x++){
                    jsonArray1.put(products[x]);
                }
                String sjson1 = jsonArray1.toString();

                //Store products and prices in difft arrays;
                String arrPt[] = new String[storeDetails.getSproducts().size()];
                String arrPr[] = new String[storeDetails.getSproducts().size()];

                for (int i = 0; i < 2; i++) {
                    for (int j = 0; j < storeDetails.getSproducts().size(); j++) {
                        if(i == 0){
                            arrPt[j] = storeDetails.getSpdtprice()[0][j];
                        }
                        if(i == 1){
                            arrPr[j] = storeDetails.getSpdtprice()[1][j];
                        }
                    }
                }

                JSONArray jsonArrayPrice = new JSONArray();
                for(int i=0;i<arrPr.length;i++){
                    jsonArrayPrice.put(arrPr[i]);
                }
                String sjsonPrice = jsonArray.toString();

                JSONArray jsonArrayProdt = new JSONArray();
                for(int i=0;i<arrPt.length;i++){
                    jsonArrayProdt.put(arrPt[i]);
                }
                String sjsonProdt = jsonArray.toString();

                JSONArray jsonArrayPP = new JSONArray();
                jsonArrayPP.put(sjsonProdt);
                jsonArrayPP.put(sjsonPrice);

                String sjsonPP = jsonArrayPP.toString();


              /*  JSONArray jsonArray2 = new JSONArray();
                for (int i = 0; i <2 ; i++) {
                    JSONArray jsonArray3 = new JSONArray();
                    for (int j = 0; j <storeDetails.getSpdtprice().length ; j++) {
                         jsonArray3.put(storeDetails.getSpdtprice()[i][j]);
                    }
                    jsonArray2.put(jsonArray3);
                }

                String sjson2 = jsonArray2.toString();*/
                //String sjson2 ="bbbbb";

                Uri.Builder builder = new Uri.Builder().appendQueryParameter("Name",storeDetails.getSname())
                        .appendQueryParameter("Location", storeDetails.getSlocation())
                        .appendQueryParameter("Website", storeDetails.getSwebsite())
                        .appendQueryParameter("Email", storeDetails.getSemail())
                        .appendQueryParameter("Tel", storeDetails.getSphone())
                        .appendQueryParameter("Desc", storeDetails.getSdesc())
                       // .appendQueryParameter("Latlng", storeDetails.getSlatlng().toString())
                        .appendQueryParameter("Latlng", sjson)
                       // .appendQueryParameter("Prodts", storeDetails.getSproducts().toString());
                        .appendQueryParameter("Prodts", sjson1);
                        //  .appendQueryParameter("Price", sjsonPP);


                String query = builder.build().getEncodedQuery();

                OutputStream os = urlConnection.getOutputStream();
                BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(os,"UTF-8"));
                writer.write(query);
                writer.flush();
                writer.close();
                os.close();

                BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(urlConnection.getInputStream()));
                String line;
                // read from the urlconnection via the bufferedreader
                while ((line = bufferedReader.readLine()) != null)
                {
                    content.append(line + "\n");
                }
                bufferedReader.close();
                // result = content.toString();


            } catch (Exception e) {
                e.printStackTrace();
            }

            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            progressDialog.dismiss();
        }
    }

    public class GetAllAddressesAsyncTask extends AsyncTask<Void,Void,String>{
        GetUserCallBack callBack;

        StringBuilder content1 = new StringBuilder();
        public GetAllAddressesAsyncTask(GetUserCallBack callBack) {
            this.callBack =callBack;
        }
        @Override
        protected String doInBackground(Void... params) {
            String res = null;
            try {

                //URL url = new URL(SERVER+"test.php");
               URL url = new URL("http://10.0.3.2/smsd/test.php");
                URLConnection urlConnection = url.openConnection();

                urlConnection.setReadTimeout(CONNECTION_TIMEOUT);
                urlConnection.setConnectTimeout(CONNECTION_TIMEOUT);
                urlConnection.setDoInput(true);
                urlConnection.setDoOutput(true);

              /*  Uri.Builder builder = new Uri.Builder();
                String query = builder.build().getEncodedQuery();

                OutputStream os = urlConnection.getOutputStream();
                BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(os,"UTF-8"));
                writer.write(query);
                writer.flush();
                writer.close();
                os.close();*/

                BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(urlConnection.getInputStream()));
                String line;
                // read from the urlconnection via the bufferedreader
                while ((line = bufferedReader.readLine()) != null)
                {
                    content1.append(line + "\n");
                }
                bufferedReader.close();
                res = content1.toString();
            } catch (Exception e) {
                e.printStackTrace();
                res = e.toString();
            }

            return res;
        }

        @Override
        protected void onPostExecute(String res) {
            progressDialog.dismiss();
            callBack.done(res);
            super.onPostExecute(res);


        }
    }

    public class SearchByLocationAsyncTask extends AsyncTask<Void,Void,String>{
        String location;
        GetUserCallBack callBack;

        StringBuilder content1 = new StringBuilder();
        public SearchByLocationAsyncTask(String location, GetUserCallBack callBack) {
            this.location = location;
            this.callBack =callBack;
        }
        @Override
        protected String doInBackground(Void... params) {
            String res = null;
            try {

                //URL url = new URL(SERVER+"searchLocations.php");
               URL url = new URL("http://10.0.3.2/smsd/searchLocations.php");
                URLConnection urlConnection = url.openConnection();

                urlConnection.setReadTimeout(CONNECTION_TIMEOUT);
                urlConnection.setConnectTimeout(CONNECTION_TIMEOUT);
                urlConnection.setDoInput(true);
                urlConnection.setDoOutput(true);

                Uri.Builder builder = new Uri.Builder().appendQueryParameter("Location", location);
                String query = builder.build().getEncodedQuery();

                OutputStream os = urlConnection.getOutputStream();
                BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(os,"UTF-8"));
                writer.write(query);
                writer.flush();
                writer.close();
                os.close();

                BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(urlConnection.getInputStream()));
                String line;
                // read from the urlconnection via the bufferedreader
                while ((line = bufferedReader.readLine()) != null)
                {
                    content1.append(line + "\n");
                }
                bufferedReader.close();
                res = content1.toString();
            } catch (Exception e) {
                e.printStackTrace();
                res = e.toString();
            }

            return res;
        }

        @Override
        protected void onPostExecute(String res) {
            progressDialog.dismiss();
            callBack.done(res);
            super.onPostExecute(res);


        }
    }
}
