package com.example.bhawesh.mupyadmin;

import android.content.Intent;
import android.os.AsyncTask;
import android.renderscript.Sampler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicHeader;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

//implementing onclicklistener
public class MainActivity extends AppCompatActivity implements View.OnClickListener,AdapterView.OnItemSelectedListener {

    //View Objects
    private Button buttonScan;
    String selectedSpeaker;
    String scannedContent;
    //qr code scanner object
    private IntentIntegrator qrScan;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //View objects
        buttonScan = (Button) findViewById(R.id.buttonScan);

        //intializing scan object
        qrScan = new IntentIntegrator(this);

        //attaching onclick listener
        buttonScan.setOnClickListener(this);

        Spinner spinner = (Spinner) findViewById(R.id.spinner);

        // Spinner click listener
        spinner.setOnItemSelectedListener((AdapterView.OnItemSelectedListener) this);

        // Spinner Drop down elements
        List<String> categories = new ArrayList<String>();
        categories.add("anikmath");
        categories.add("abhikumr");
        categories.add("stepupan");
        categories.add("teamunip");
        categories.add("dheerajr");
        categories.add("danevans");
        categories.add("narenrav");
        categories.add("ayodanki");
        categories.add("soumyade");
        categories.add("saileshs");
        categories.add("kartmand");
        categories.add("yashklal");
        categories.add("kalbhoro");
        categories.add("anuragks");
        categories.add("ronaldas");
        categories.add("ankushan");
        categories.add("ashwmurl");
        categories.add("aneeshjo");
        categories.add("sidkotho");

        // Creating adapter for spinner
        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, categories);

        // Drop down layout style - list view with radio button
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        // attaching data adapter to spinner
        spinner.setAdapter(dataAdapter);

    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        // On selecting a spinner item
        selectedSpeaker = parent.getItemAtPosition(position).toString();

        // Showing selected spinner item
        Toast.makeText(parent.getContext(), "Selected: " + selectedSpeaker, Toast.LENGTH_LONG).show();
    }

    public void onNothingSelected(AdapterView<?> arg0) {
        // TODO Auto-generated method stub
    }


    //Getting the scan results
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        IntentResult result = IntentIntegrator.parseActivityResult(requestCode, resultCode, data);
        if (result != null) {
            //if qrcode has nothing in it
            if (result.getContents() == null) {
                Toast.makeText(this, "Result Not Found", Toast.LENGTH_LONG).show();
            } else {
                //if qr contains data
                try {
                    //converting the data to json
                    JSONObject obj = new JSONObject(result.getContents());
                    //setting values to textviews
//                    textViewName.setText(obj.getString("name"));
//                    textViewAddress.setText(obj.getString("address"));
                } catch (JSONException e) {
                    e.printStackTrace();
                    //if control comes here
                    //that means the encoded format not matches
                    //in this case you can display whatever data is available on the qrcode
                    //to a toast
                    scannedContent = result.getContents();
                    Toast.makeText(this, scannedContent, Toast.LENGTH_LONG).show();
                    new myTask().execute();
                    //postData(scannedContent, selectedSpeaker);
                }
            }
        } else {
            super.onActivityResult(requestCode, resultCode, data);
        }
    }

    @Override
    public void onClick(View view) {
        //initiating the qr code scan
        qrScan.initiateScan();
    }

//
// private static class MyTaskParams {
//        String qrContent, talk_id;
//
//        MyTaskParams(String qrContent, String talk_id) {
//            this.qrContent = qrContent;
//            this.talk_id = talk_id;
//        }
//    }
//
    private class myTask extends AsyncTask<Void, Void, Void> {

        //initiate vars
        public myTask() {
            super();
            //my params here
        }

        @Override
        protected Void doInBackground(Void... params) {
            String qrContent = scannedContent;
            String talk_id = selectedSpeaker;
            try {
                postData(qrContent, talk_id);
            //    Toast.makeText(getApplicationContext(), "Request Sent", Toast.LENGTH_SHORT).show();
                Log.d("HEY", "postData called success");
            }
            catch (Exception e){
             //   Toast.makeText(getApplicationContext(), e.toString(), Toast.LENGTH_SHORT).show();
                Log.d("HEY", "error" + e.toString());
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            //do stuff
        }
    }

    public void postData(String qrContent, String talk_id) {
        HttpClient httpclient = new DefaultHttpClient();
        // specify the URL you want to post to
        HttpPost httppost = new HttpPost("https://www.pypals.org/attendance");
        try {
            // create a list to store HTTP variables and their values
            List nameValuePairs = new ArrayList();
            // add an HTTP variable and value pair
            httppost.setHeader("PyPals-Authorization", "4JcngjcSJGNGKJFSNRKsdjd-nPFfjSFfGJGFsnfj");
            httppost.setHeader("Content-Type", "application/json");

            JSONObject jsonBody = new JSONObject();
                jsonBody.put("name", "BKL");
                jsonBody.put("college_id", qrContent);
                jsonBody.put("eventid", talk_id);

            JSONObject jsonObject = new JSONObject();
                jsonObject.put("Request body", jsonBody.toString());

            Log.d("HEY", "JSON : " + jsonBody.toString());
            StringEntity se = new StringEntity(jsonBody.toString());
            httppost.setEntity(se);


            // send the variable and value, in other words post, to the URL
            HttpResponse response = httpclient.execute(httppost);
            HttpEntity entity = response.getEntity();
            if(entity !=null){
                String responseString = EntityUtils.toString(entity, "UTF-8");
                //Toast.makeText(getApplicationContext(), responseString, Toast.LENGTH_SHORT).show();
                Log.d("HEY", "response : " + responseString);
            }
        } catch (ClientProtocolException e) {
            // process execption
        } catch (IOException e) {
            // process execption
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}