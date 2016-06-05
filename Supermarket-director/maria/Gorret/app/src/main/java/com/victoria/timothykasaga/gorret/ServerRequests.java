package com.victoria.timothykasaga.gorret;

import android.app.ProgressDialog;
import android.content.Context;
import android.net.Uri;
import android.os.AsyncTask;

import org.json.JSONArray;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;

/**
 * Created by Leontymo on 4/21/2016.
 */
public class ServerRequests {
    public static final int CONNECTION_TIMEOUT = 15000;
    public static final int READ_TIMEOUT = 20000;
    public static final String SERVER2 = "http://10.0.2.2:80/smsd/";
    ProgressDialog progressDialog;
    Context context;
    public ServerRequests(Context paramContext)
    {   context = paramContext;
        progressDialog = new ProgressDialog(paramContext);
        progressDialog.setCancelable(false);
        progressDialog.setTitle("Processing");
        progressDialog.setMessage("Please wait....");
    }

    public void storeAdmin(Admin admin, Create_account create_account){
        progressDialog.show();
        new StoreAdminsAsyncTask(admin, create_account).execute();
    }

    public void authenticateAdmin(Admin admin, Login login){
        progressDialog.show();
        new FetchDataAsyncTask(admin,login).execute();
    }

    public void storeSupermktDetailsInBackgound(DetailsPack detailsPack,
                ArrayList<Product> productArrayList, Supermarket_details supermarket_details){
        progressDialog.show();
        new StoreSupermktdetailsAsyncTask(detailsPack,productArrayList,supermarket_details).execute();
    }

    public void searchBySmName(String name, Locate_supermarket locate_supermarket) {
        progressDialog.show();
        new searchBySmNameAsnycTasks(locate_supermarket,name).execute();
    }

    public void searchAllSm(Locate_supermarket locate_supermarket) {
        progressDialog.show();
        new searchAllSmAsnycTasks(locate_supermarket).execute();
    }

    public void searchBySmLocation(String location, Locate_supermarket locate_supermarket) {
        progressDialog.show();
        new searchBySmLocationAsnycTasks(locate_supermarket,location).execute();
    }

    public void getSupermarketWithProduct(String product_name, Compare_prices compare_prices) {
            progressDialog.show();
            new getAllSupermarketWithprodtAsnycTasks(compare_prices,product_name).execute();
    }

    public void getImageURLpath(Locate_product locate_product,String supermkt_id, String user_floor, String user_cell) {
            progressDialog.show();
            new getImageUrlAsnycTasks(locate_product,supermkt_id,user_floor,user_cell).execute();
    }

    //class fetchImageURl
    private class getImageUrlAsnycTasks extends AsyncTask<Void,Void,String>{
        Locate_product locate_product;
        String supermkt_id; String user_floor; String user_cell;

        private getImageUrlAsnycTasks(Locate_product locate_product, String supermkt_id, String user_floor, String user_cell) {
            this.locate_product = locate_product;
            this.supermkt_id = supermkt_id;
            this.user_floor = user_floor;
            this.user_cell = user_cell;
        }

        @Override
        protected String doInBackground(Void... voids) {
            String result = "";
            try {
                StringBuilder content = new StringBuilder();
                // URL url = new URL(SERVER+"registerUser.php");
                URL url = new URL("http://10.0.3.2/smsd_locations/mapUserLocation.php");
                HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
                urlConnection.setReadTimeout(CONNECTION_TIMEOUT);
                urlConnection.setConnectTimeout(CONNECTION_TIMEOUT);
                urlConnection.setDoInput(true);
                urlConnection.setDoOutput(true);

                Uri.Builder builder = new Uri.Builder().appendQueryParameter("Supermarketid",supermkt_id)
                        .appendQueryParameter("Userfloor",user_floor)
                        .appendQueryParameter("Usercell",user_cell);


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
                result = content.toString();


            } catch (Exception e) {
                e.printStackTrace();
            }
            return result;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            progressDialog.dismiss();
            if(!s.equals("")){
                locate_product.continueExecution(s,locate_product);
            }

        }
    }


    //here
       private class getAllSupermarketWithprodtAsnycTasks extends AsyncTask<Void,Void,String>{
           Compare_prices compare_prices;
           String productname;

           private getAllSupermarketWithprodtAsnycTasks(Compare_prices compare_prices, String productname) {
               this.compare_prices = compare_prices;
               this.productname = productname;
           }

           @Override
           protected String doInBackground(Void... voids) {
               String result = "";
               try {
                   StringBuilder content = new StringBuilder();
                   // URL url = new URL(SERVER+"registerUser.php");
                   URL url = new URL("http://10.0.3.2/smsd/getProductPrice.php");
                   HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
                   urlConnection.setReadTimeout(CONNECTION_TIMEOUT);
                   urlConnection.setConnectTimeout(CONNECTION_TIMEOUT);
                   urlConnection.setDoInput(true);
                   urlConnection.setDoOutput(true);

                   Uri.Builder builder = new Uri.Builder().appendQueryParameter("Product",productname);


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
                   result = content.toString();


               } catch (Exception e) {
                   e.printStackTrace();
               }
               return result;
           }

           @Override
           protected void onPostExecute(String s) {
               super.onPostExecute(s);
               progressDialog.dismiss();
               if(!s.equals("")){
                   compare_prices.continueExecution(s,compare_prices);
               }

           }
       }

    private class searchBySmLocationAsnycTasks extends AsyncTask<Void,Void,String>{
        Locate_supermarket locate_supermarket;
        String location;

        private searchBySmLocationAsnycTasks(Locate_supermarket locate_supermarket, String location) {
            this.locate_supermarket = locate_supermarket;
            this.location = location;
        }

        @Override
        protected String doInBackground(Void... voids) {
            String result = "";
            try {
                StringBuilder content = new StringBuilder();
                // URL url = new URL(SERVER+"registerUser.php");
                URL url = new URL("http://10.0.3.2/smsd/searchByLocation.php");
                HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
                urlConnection.setReadTimeout(CONNECTION_TIMEOUT);
                urlConnection.setConnectTimeout(CONNECTION_TIMEOUT);
                urlConnection.setDoInput(true);
                urlConnection.setDoOutput(true);

                Uri.Builder builder = new Uri.Builder().appendQueryParameter("Location",location);


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
                result = content.toString();


            } catch (Exception e) {
                e.printStackTrace();
            }
            return result;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            progressDialog.dismiss();
            if(!s.equals("")){
            locate_supermarket.continueExecution(s,locate_supermarket);
            }

        }
    }

    private class searchAllSmAsnycTasks extends AsyncTask<Void,Void,String>{
        Locate_supermarket locate_supermarket;


        private searchAllSmAsnycTasks(Locate_supermarket locate_supermarket) {
            this.locate_supermarket = locate_supermarket;

        }

        @Override
        protected String doInBackground(Void... voids) {
            String result = "";
            try {
                StringBuilder content = new StringBuilder();
                // URL url = new URL(SERVER+"registerUser.php");
                URL url = new URL("http://10.0.3.2/smsd/searchAllSm.php");
                HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
                urlConnection.setReadTimeout(CONNECTION_TIMEOUT);
                urlConnection.setConnectTimeout(CONNECTION_TIMEOUT);
                urlConnection.setDoInput(true);
                urlConnection.setDoOutput(true);

                BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(urlConnection.getInputStream()));
                String line;
                // read from the urlconnection via the bufferedreader
                while ((line = bufferedReader.readLine()) != null)
                {
                    content.append(line + "\n");
                }
                bufferedReader.close();
                result = content.toString();


            } catch (Exception e) {
                e.printStackTrace();
            }
            return result;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            progressDialog.dismiss();
            if(!s.equals("")){
                locate_supermarket.continueExecution(s,locate_supermarket);
            }

        }
    }

    private class searchBySmNameAsnycTasks extends AsyncTask<Void,Void,String>{
        Locate_supermarket locate_supermarket;
        String name;

        private searchBySmNameAsnycTasks(Locate_supermarket locate_supermarket, String name) {
            this.locate_supermarket = locate_supermarket;
            this.name = name;
        }

        @Override
        protected String doInBackground(Void... voids) {
            String result = "";
            try {
                StringBuilder content = new StringBuilder();
                // URL url = new URL(SERVER+"registerUser.php");
                URL url = new URL("http://10.0.3.2/smsd/searchByName.php");
                HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
                urlConnection.setReadTimeout(CONNECTION_TIMEOUT);
                urlConnection.setConnectTimeout(CONNECTION_TIMEOUT);
                urlConnection.setDoInput(true);
                urlConnection.setDoOutput(true);

                Uri.Builder builder = new Uri.Builder().appendQueryParameter("Name",name);

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
                result = content.toString();


            } catch (Exception e) {
                e.printStackTrace();
            }
            return result;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            progressDialog.dismiss();
            if(!s.equals("")){
                locate_supermarket.continueExecution(s,locate_supermarket);
            }

        }
    }

    private class StoreAdminsAsyncTask extends AsyncTask<Void,Void,String>{
        Admin admin;
        Create_account create_account;

        private StoreAdminsAsyncTask(Admin admin, Create_account create_account) {
            this.admin = admin;
            this.create_account = create_account;
        }

        @Override
        protected String doInBackground(Void... voids) {
            String result = "";
            try {
                StringBuilder content = new StringBuilder();
                // URL url = new URL(SERVER+"registerUser.php");
                URL url = new URL("http://10.0.3.2/smsd/registerUser.php");
                HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
                urlConnection.setReadTimeout(CONNECTION_TIMEOUT);
                urlConnection.setConnectTimeout(CONNECTION_TIMEOUT);
                urlConnection.setDoInput(true);
                urlConnection.setDoOutput(true);

                Uri.Builder builder = new Uri.Builder().appendQueryParameter("Name",admin.name)
                        .appendQueryParameter("Email", admin.email)
                        .appendQueryParameter("UName", admin.username)
                        .appendQueryParameter("Pass", admin.pass)
                        .appendQueryParameter("Tel", admin.tel);


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
                result = content.toString();


            } catch (Exception e) {
                e.printStackTrace();
            }
            return result;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            progressDialog.dismiss();
            create_account.continueExecution(s,create_account);
        }
    }

    private class FetchDataAsyncTask extends AsyncTask<Void,Void,String>{
        Admin admin;
        Login login;

        StringBuilder content1 = new StringBuilder();
        public FetchDataAsyncTask(Admin admin, Login login) {
            this.admin = admin;
            this.login = login;
        }
        @Override
        protected String doInBackground(Void... params) {
            String res = "";
            try {
                URL url = new URL("http://10.0.3.2/smsd/retrive.php");
                URLConnection urlConnection = url.openConnection();

                urlConnection.setReadTimeout(CONNECTION_TIMEOUT);
                urlConnection.setConnectTimeout(CONNECTION_TIMEOUT);
                urlConnection.setDoInput(true);
                urlConnection.setDoOutput(true);

                Uri.Builder builder = new Uri.Builder().appendQueryParameter("UName", admin.username)
                        .appendQueryParameter("Pass", admin.pass);
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
            }

            return res;
        }

        @Override
        protected void onPostExecute(String res) {
            progressDialog.dismiss();
            super.onPostExecute(res);
            if(!res.equals("")){
            login.continueExecution(res,login);
            }

        }
    }

    private class StoreSupermktdetailsAsyncTask extends AsyncTask<Void,Void,String>{
         DetailsPack detailsPack;
         ArrayList<Product> productArrayList;
        Supermarket_details supermarket_details;

        private StoreSupermktdetailsAsyncTask(DetailsPack detailsPack, ArrayList<Product> productArrayList,Supermarket_details supermarket_details) {
            this.detailsPack = detailsPack;
            this.productArrayList = productArrayList;
            this.supermarket_details = supermarket_details;
        }

        @Override
        protected String doInBackground(Void... voids) {
            StringBuilder content = new StringBuilder();
            String result = "";
            try {
                JSONArray listArray = new JSONArray();
                Product product;
                for (int i = 0; i < productArrayList.size(); i++) {
                    product = productArrayList.get(i);
                    JSONArray prodtArray = new JSONArray();
                    prodtArray.put(0, product.name);
                    prodtArray.put(1, product.prodt_id);
                    prodtArray.put(2, product.unit_cost);
                    prodtArray.put(3, product.units);
                    prodtArray.put(4, product.section_name);
                    listArray.put(i,prodtArray);
                }
                String strProdtList = listArray.toString();

                // URL url = new URL(SERVER+"storeDetails.php");
                URL url = new URL("http://10.0.3.2/smsd/storeDetails.php");
                HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
                urlConnection.setReadTimeout(CONNECTION_TIMEOUT);
                urlConnection.setConnectTimeout(CONNECTION_TIMEOUT);
                urlConnection.setDoInput(true);
                urlConnection.setDoOutput(true);

                Uri.Builder builder = new Uri.Builder().appendQueryParameter("Name",detailsPack.getS_name())
                        .appendQueryParameter("Location", detailsPack.getS_location())
                        .appendQueryParameter("Website", detailsPack.getS_website())
                        .appendQueryParameter("Email", detailsPack.getS_email())
                        .appendQueryParameter("Tel", detailsPack.getS_phone())
                        .appendQueryParameter("Desc", detailsPack.getS_desc())
                         .appendQueryParameter("Lat", detailsPack.getD_lat())
                        .appendQueryParameter("Log", detailsPack.getD_log())
                         .appendQueryParameter("Admin", detailsPack.getAdmin())
                        .appendQueryParameter("Productlist", strProdtList);


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
                result = content.toString();


            }catch (Exception e){
                e.printStackTrace();
            }
            return result;
        }

        @Override
        protected void onPostExecute(String res) {
            progressDialog.dismiss();
            super.onPostExecute(res);
            if(!res.equals("")){
                supermarket_details.continueExecution(res,supermarket_details);
            }
        }
    }

}
