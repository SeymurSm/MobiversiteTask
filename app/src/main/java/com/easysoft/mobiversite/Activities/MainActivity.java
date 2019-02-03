package com.easysoft.mobiversite.Activities;

import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.AppCompatButton;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ListView;
import android.widget.Toast;

import com.easysoft.mobiversite.Adapters.ListAdapterYemeks;
import com.easysoft.mobiversite.R;
import com.easysoft.mobiversite.Utils.TinyDB;
import com.easysoft.mobiversite.Models.Yemek;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.DateFormatSymbols;
import java.util.ArrayList;
import java.util.Locale;

import javax.net.ssl.HttpsURLConnection;

public class MainActivity extends AppCompatActivity {
    ListView listView;
    ListAdapterYemeks adapter;
    TinyDB tinyDB;
    String current ;
    public static String currency;
    String url = "https://steplinuxdiag318.blob.core.windows.net/mobiversite/restaurant.json";
    ArrayList<Yemek> yemekFiles;

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
                case R.id.navigation_dashboard:
                  //  mTextMessage.setText(R.string.title_dashboard);
                    return true;
                case R.id.navigation_exit:
                    //Showing exit dialog
                    showExitDialog();
                    return true;
            }
            return false;
        }
    };


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        setCurrency();

        BottomNavigationView navigation = (BottomNavigationView) findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);

        yemekFiles = new ArrayList<Yemek>();
        tinyDB = new TinyDB(getApplicationContext());
        listView = (ListView) findViewById(R.id.commandsList);


        /**
         * Body of the thread to get JSON data
         */

        Thread thread = new Thread(new Runnable(){
            @Override
            public void run(){
                String jo = getJSON(url);
                try {
                    //Reading JSON array
                    JSONArray mainObject = new JSONArray(jo);

                    //Iterating on idexes of the retrieved array to get separate objects
                    for(int i=0; i<mainObject.length(); i++){
                        JSONObject json_data = mainObject.getJSONObject(i);
                        String  uniName = json_data.getString("restaurantName");
                        String uniURL = json_data.getString("foodIngredients");
                        System.out.println(uniName + " " + uniURL);

                        //Creating new list item
                        Yemek y = new Yemek();
                        y.setYear(json_data.getString("date"));
                        y.setMonth(getMonth(Integer.parseInt(json_data.getString("month"))));
                        y.setRestaurantName(json_data.getString("restaurantName"));
                        y.setFoodIngredients(json_data.getString("foodIngredients"));
                        y.setState(json_data.getString("state"));
                        y.setFoodPrice(json_data.getDouble("foodPrice"));
                        y.setSummary(json_data.getString("restaurantName"));
                        JSONObject detail = json_data.getJSONObject("detail");
                        y.setSummary(detail.getString("summary"));
                        y.setSummaryPrice(detail.getDouble("summaryPrice"));
                        yemekFiles.add(y);


                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                //Creating adapter to set retrieved data on the list
                                adapter = new ListAdapterYemeks(getApplicationContext(), yemekFiles, new ListAdapterYemeks.ICallback() {
                                    @Override
                                    public void onExtraEvent(int position) {

                                    }
                                });
                                listView.setAdapter(adapter);
                            }
                        });
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
        thread.start();



    }



    /**
     * Shows the custom dialog for an exit operation.
     */
    private void showExitDialog(){
        LayoutInflater inflater = MainActivity.this.getLayoutInflater();

        View layout = inflater.inflate(R.layout.exit_dialog, null);
        final AlertDialog alertExit = new AlertDialog.Builder(MainActivity.this)
                .setView(layout)
                .show();
        AppCompatButton cancel = (AppCompatButton)layout.findViewById(R.id.btnCancel);
        AppCompatButton exit = (AppCompatButton)layout.findViewById(R.id.btnExit);
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alertExit.dismiss();
            }
        });
        exit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Reseting user login data on shared preferences
                tinyDB.putString("USERNAME","");
                tinyDB.putString("PASSWORD","");
                tinyDB.putBoolean("REMEMBER", false);
                finish();
            }
        });
        //Removing default background of the dialog
        alertExit.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.parseColor("#00000000")));
    }


    /**
     * Setting currency by using language information
     */
    private void setCurrency(){
        current = Locale.getDefault().getLanguage();
        if(current.equals("tr"))
            currency = " TL";
        else if(current.equals("en")){
            currency = " $";
            Toast.makeText(this, "English set edildi", Toast.LENGTH_SHORT).show();
        }
    }



    /**
     * Getting JSON string from provided url
     */
    public static String getJSON(String url) {
        HttpsURLConnection con = null;
        try {
            URL u = new URL(url);
            con = (HttpsURLConnection) u.openConnection();
            con.connect();

            BufferedReader br = new BufferedReader(new InputStreamReader(con.getInputStream()));
            StringBuilder sb = new StringBuilder();
            String line;
            while ((line = br.readLine()) != null) {
                sb.append(line + "\n");
            }
            br.close();
            return sb.toString();


        } catch (MalformedURLException ex) {
            ex.printStackTrace();
        } catch (IOException ex) {
            ex.printStackTrace();
        } finally {
            if (con != null) {
                try {
                    con.disconnect();
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            }
        }
        return null;
    }


    /**
     * Returns (month-1) th month name
     */
    public String getMonth(int month) {
        return new DateFormatSymbols().getMonths()[month-1];
    }

}
