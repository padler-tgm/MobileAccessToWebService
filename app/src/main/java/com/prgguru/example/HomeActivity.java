package com.prgguru.example;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.TextHttpResponseHandler;

import org.apache.http.Header;
import org.apache.http.entity.StringEntity;
import org.apache.http.message.BasicHeader;
import org.apache.http.protocol.HTTP;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.web.client.RestTemplate;

import java.io.UnsupportedEncodingException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * Home Screen Activity
 * http://spring.io/guides/gs/rest-service/
 */
public class HomeActivity extends Activity {

    ProgressDialog prgDialog;
    TableLayout table;
    String email;
    String input;

    TableRow tr;
    TextView textview;
    TextView textview1;
    TextView textview2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            email = extras.getString("email");
        }
        //Displays Home Screen
        setContentView(R.layout.home);
        table = (TableLayout) findViewById(R.id.table);
        TableRow tr = new TableRow(this);
        tr.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.FILL_PARENT, TableRow.LayoutParams.WRAP_CONTENT));

        TextView textview = new TextView(this);
        textview.setText("Datum");
        textview.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.FILL_PARENT, TableRow.LayoutParams.WRAP_CONTENT));
        tr.addView(textview);
        TextView textview1 = new TextView(this);
        textview1.setText("Email");
        textview1.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.FILL_PARENT, TableRow.LayoutParams.WRAP_CONTENT));
        tr.addView(textview1);
        TextView textview2 = new TextView(this);
        textview2.setText("Message");
        textview2.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.FILL_PARENT, TableRow.LayoutParams.WRAP_CONTENT));
        tr.addView(textview2);
        table.addView(tr, new TableLayout.LayoutParams(TableLayout.LayoutParams.FILL_PARENT, TableLayout.LayoutParams.WRAP_CONTENT));
        //textview.getTextColors(R.color.)
        //textview.setTextColor(Color.YELLOW);
        prgDialog = new ProgressDialog(this);
        // Set Progress Dialog Text
        prgDialog.setMessage("Please wait...");
        // Set Cancelable as False
        prgDialog.setCancelable(false);
    }

    /**
     * Method gets triggered when Login button is clicked
     *
     * @param view
     */
    public void logoutUser(View view) {
        // Get Email Edit View Value
        JSONObject params = new JSONObject();
        // When Email Edit View and Password Edit View have values other than Null
        try {
            // Put Http parameter username with value of Email Edit View control
            params.put("email", email);
            // Invoke RESTful Web Service with Http parameters
            logout(params);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    /**
     * Method that performs RESTful webservice invocations
     *
     * @param params
     */
    private void logout(JSONObject params) {
        // Show Progress Dialog
        prgDialog.show();
        // Make RESTful webservice call using AsyncHttpClient object
        final AsyncHttpClient client = new AsyncHttpClient();
        StringEntity request = null;
        try {
            request = new StringEntity(params.toString());
            request.setContentType(new BasicHeader(HTTP.CONTENT_TYPE, "application/json"));
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        client.post(this.getApplicationContext(), "http://10.0.2.2:8080/logout", request, "application/json", new TextHttpResponseHandler() {

            // When the response returned by REST has Http response code other than '200'
            @Override
            public void onFailure(int statusCode, Header[] headers, String responseBody, Throwable error) {
                // Hide Progress Dialog
                prgDialog.hide();
                // When Http response code is '403'
                if (statusCode == 401) {
                    Toast.makeText(getApplicationContext(), responseBody, Toast.LENGTH_LONG).show();
                    navigatetoLoginActivity();
                }
                // When Http response code other than 403
                else {
                    Toast.makeText(getApplicationContext(), "Unexpected Error occcured! [Most common Error: Device might not be connected to Internet or remote server is not up and running]", Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onSuccess(int statusCode, Header[] headers, String responseBody) {
                prgDialog.hide();
                // When Http response code is '200'
                if (statusCode == 200) {
                    Toast.makeText(getApplicationContext(), responseBody, Toast.LENGTH_LONG).show();
                    // Navigate to Home screen
                    navigatetoLoginActivity();
                }

            }
        });
    }

    /**
     * Method that performs RESTful webservice invocations
     *
     * @param params
     */
    public void checkSession(JSONObject params) {
        // Show Progress Dialog
        System.out.println("CHECKSESSION");
        //prgDialog.show();??????????????????????????????????????????????SOLLTE NICHT WEGGELASSEN WERDEN
        // Make RESTful webservice call using AsyncHttpClient object
        final AsyncHttpClient client = new AsyncHttpClient();
        StringEntity request = null;
        try {
            request = new StringEntity(params.toString());
            request.setContentType(new BasicHeader(HTTP.CONTENT_TYPE, "application/json"));
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        client.post(this.getApplicationContext(), "http://10.0.2.2:8080/login", request, "application/json", new TextHttpResponseHandler() {
            //client.get("http://192.168.43.17:9999/useraccount/login/dologin",params ,new AsyncHttpResponseHandler() {
            // When the response returned by REST has Http response code '200'

            // When the response returned by REST has Http response code other than '200'
            @Override
            public void onFailure(int statusCode, Header[] headers, String responseBody, Throwable error) {
                // Hide Progress Dialog
                prgDialog.hide();
                System.out.println(statusCode);
                if (statusCode == 401) {
                    Toast.makeText(getApplicationContext(), responseBody, Toast.LENGTH_LONG).show();
                    navigatetoLoginActivity();
                }
                // When Http response code other than 403
                else {
                    Toast.makeText(getApplicationContext(), "Unexpected Error occcured! [Most common Error: Device might not be connected to Internet or remote server is not up and running]", Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onSuccess(int statusCode, Header[] headers, String responseBody) {
                prgDialog.hide();
                // When Http response code is '200'
                if (statusCode == 200) {}

            }
        });
    }

    public void sendMessage(View view) {
        EditText field = (EditText) findViewById(R.id.message);
        input = field.getText().toString();
        new HttpRequestTask().execute();
        JSONObject params = new JSONObject();
        // When Email Edit View and Password Edit View have values other than Null
        try {
            // Put Http parameter username with value of Email Edit View control
            params.put("email", email);
            // Invoke RESTful Web Service with Http parameters
            checkSession(params);


            tr = new TableRow(this);
            tr.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.FILL_PARENT, TableRow.LayoutParams.WRAP_CONTENT));
            //DATE
            textview = new TextView(this);

            //EMAIL
            textview1 = new TextView(this);

            //CONTENT
            textview2 = new TextView(this);
        } catch (JSONException e) {
            e.printStackTrace();
        }

    }

    /**
     * Method which navigates from Login Activity to Home Activity
     */
    public void navigatetoLoginActivity() {
        Intent homeIntent = new Intent(getApplicationContext(), LoginActivity.class);
        homeIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(homeIntent);
    }


    private class HttpRequestTask extends AsyncTask<Void, Void, com.prgguru.example.Message> {
        @Override
        /**
         * SENDET AN SERVER DAS OBJECT MESSAGE
         */
        protected com.prgguru.example.Message doInBackground(Void... params) {
            try {
                com.prgguru.example.Message message = new com.prgguru.example.Message();
                message.setEmail(email);
                message.setContent(input);
                DateFormat df = new SimpleDateFormat("MM/dd/yyyy HH:mm:ss");
                Date today = Calendar.getInstance().getTime();
                String reportDate = df.format(today);
                message.setDate(reportDate);
                final String url = "http://10.0.2.2:8080/greeting";
                RestTemplate restTemplate = new RestTemplate();
                restTemplate.getMessageConverters().add(new MappingJackson2HttpMessageConverter());
                com.prgguru.example.Message reply = restTemplate.postForObject(url, message, com.prgguru.example.Message.class);
                return reply;
            } catch (Exception e) {
                Log.e("MainActivity", e.getMessage(), e);
            }

            return null;
        }

        @Override
        protected void onPostExecute(com.prgguru.example.Message greeting) {
            //TextView greetingIdText = (TextView) findViewById(R.id.id_value);
            //TextView greetingContentText = (TextView) findViewById(R.id.content_value);
            System.out.println(greeting.getEmail() + " " + greeting.getContent());

            //DATE
            DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
            Date date = new Date();
            textview.setText(dateFormat.format(date));
            textview.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.FILL_PARENT, TableRow.LayoutParams.WRAP_CONTENT));
            tr.addView(textview);

            //EMAIL
            textview1.setText(greeting.getEmail());
            textview1.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.FILL_PARENT, TableRow.LayoutParams.WRAP_CONTENT));
            tr.addView(textview1);

            //MESSAGE
            textview2.setText(greeting.getContent());
            textview2.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.FILL_PARENT, TableRow.LayoutParams.WRAP_CONTENT));
            tr.addView(textview2);

            //ANZEIGEN
            table.addView(tr, new TableLayout.LayoutParams(TableLayout.LayoutParams.FILL_PARENT, TableLayout.LayoutParams.WRAP_CONTENT));

            //greetingIdText.setText(greeting.getId());
            //greetingContentText.setText(greeting.getContent());
        }

    }

}
