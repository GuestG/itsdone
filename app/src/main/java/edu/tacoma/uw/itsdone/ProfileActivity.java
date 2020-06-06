package edu.tacoma.uw.itsdone;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.drawable.ColorDrawable;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * The central hub for all things about the current users profile!!
 *
 * @author Trevor Peters
 * @version 1.0
 * @since 5/15/2020
 *
 */
public class ProfileActivity extends AppCompatActivity {
    private JSONObject mMemberOutJSON;
    private JSONObject mMemberInJSON;
    private String mUsername;
    private String mAccount = "ACCOUNT";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setTitle("Profile");
        SharedPreferences sharedPref =getApplicationContext().getApplicationContext().
                getSharedPreferences("userInfo", 0);
        mUsername = getIntent().getStringExtra("username");
        if(checkNetwork()) {
            getAccount();
        }
        setupRepButtons();
    }

    /**
     * logs the user out. sets the userInfo shared preference to default values
     * @param view
     */
    public void logout(View view){

        SharedPreferences sharedPref =getApplicationContext().getApplicationContext().
                getSharedPreferences("userInfo", 0);
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putInt("memberID", 0);
        editor.putString("username", "");
        editor.putBoolean("signed in", false);
        editor.commit();
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
        finish();
    }

    /**
     * get the account info from the database
     */
    public void getAccount() {
        StringBuilder url = new StringBuilder(getString(R.string.account));
        mMemberOutJSON = new JSONObject();

        try{
            mMemberOutJSON.put("username", mUsername);
            new AccountAsyncTask().execute(url.toString());
        } catch (JSONException e){
            Toast.makeText(this,"Error with JSON creation on login: " +
                            e.getMessage(),
                    Toast.LENGTH_LONG).show();
        }

    }

    /**
     * gets the First and last names, email, and username from the database
     */
    private class AccountAsyncTask extends AsyncTask<String, Void, String> {
        @Override
        protected String doInBackground(String... urls) {
            String response = "";
            HttpURLConnection urlConnection = null;
            for (String url : urls) {
                try {
                    URL urlObject = new URL(url);
                    urlConnection = (HttpURLConnection) urlObject.openConnection();
                    urlConnection.setRequestMethod("POST");
                    urlConnection.setRequestProperty("Content-Type", "application/json");
                    urlConnection.setDoOutput(true);
                    OutputStreamWriter wr =
                            new OutputStreamWriter(urlConnection.getOutputStream());

                    // For Debugging
                    Log.i(mAccount, mMemberOutJSON.toString());
                    wr.write(mMemberOutJSON.toString());
                    wr.flush();
                    wr.close();

                    InputStream content = urlConnection.getInputStream();

                    BufferedReader buffer = new BufferedReader(new InputStreamReader(content));
                    String s = "";
                    while ((s = buffer.readLine()) != null) {
                        response += s;
                    }

                } catch (Exception e) {
                    response = "Unable to add the new Member, Reason: "
                            + e.getMessage();
                } finally {
                    if (urlConnection != null)
                        urlConnection.disconnect();
                }
            }
            return response;
        }

        @Override
        protected void onPostExecute(String s) {
            if (s.startsWith("Unable to add the new Member")) {
                Toast.makeText(getApplicationContext(), s, Toast.LENGTH_LONG).show();
                return;
            }
            try {
                JSONObject jsonObject = new JSONObject(s);
                mMemberInJSON = jsonObject;
                populateTextViews();
            } catch (JSONException e) {
//                Toast.makeText(getApplicationContext(), "JSON Parsing error on Adding Member"
//                                + e.getMessage()
//                        , Toast.LENGTH_LONG).show();
//                Log.e(mAccount, e.getMessage());
            }
        }
    }

    /**
     * populates the TextViews
     * @throws JSONException
     */
    private void populateTextViews() throws JSONException {
        TextView emailText = findViewById(R.id.email);
        TextView firstText = findViewById(R.id.first_name);
        TextView lastText = findViewById(R.id.last_name);
        TextView usernameText = findViewById(R.id.username);
        TextView account_rep = findViewById(R.id.account_rep);
        emailText.setText(mMemberInJSON.getString("email"));
        firstText.setText(mMemberInJSON.getString("firstname"));
        lastText.setText(mMemberInJSON.getString("lastname"));
        usernameText.setText(mMemberInJSON.getString("username"));
        account_rep.setText("Reputation: " + mMemberInJSON.getString("rating"));
    }

    /**
     * sets up the rep buttons
     */
    private void setupRepButtons(){
        Button upRep = findViewById(R.id.increaseRepButton);
        Button downRep = findViewById(R.id.decreaseRepButton);

        upRep.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                editRep(1);
            }
        });

        downRep.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                editRep(-1);
            }
        });
    }

    /**
     * edits the rep of the user selected
     * @param rep the amount of rep given
     */
    private void editRep(int rep){
        if (!checkNetwork()){
            return;
        }
        SharedPreferences sharedPref =getApplicationContext().getApplicationContext().
                getSharedPreferences("userInfo", 0);

        StringBuilder url = new StringBuilder(getString(R.string.edit_reputation));
        mMemberOutJSON = new JSONObject();
        if (((TextView) findViewById(R.id.username)).getText().toString().equals(
                sharedPref.getString(getString(R.string.username), null))){
               Toast.makeText(getApplicationContext(), "Cannot edit your own rep. ", Toast.LENGTH_LONG).show();

        }
        try{
            mMemberOutJSON.put("username", mUsername);
            mMemberOutJSON.put("rate", rep);
            new AccountAsyncTask().execute(url.toString());
        } catch (JSONException e){
            Toast.makeText(this,"Error with JSON creation on login: " +
                            e.getMessage(),
                    Toast.LENGTH_LONG).show();
        }
    }

    /**
     * checks if there is an active network connection
     */
    private boolean checkNetwork(){
        ConnectivityManager connMgr = (ConnectivityManager)
                getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();
        if (networkInfo != null && networkInfo.isConnected()) {
            return true;
        }
        Toast.makeText(this,
                "No network connection available. Please Try again later",
                Toast.LENGTH_LONG).show();
        return false;
    }
}
