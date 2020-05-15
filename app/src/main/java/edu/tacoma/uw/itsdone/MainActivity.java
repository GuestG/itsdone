package edu.tacoma.uw.itsdone;

import androidx.appcompat.app.AppCompatActivity;

import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.util.Log;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;


public class MainActivity extends AppCompatActivity {
    private JSONObject mMemberJSON;
    public static final String mLogin = "Login";



    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        SharedPreferences sharedPref =getApplicationContext().getApplicationContext().
                getSharedPreferences("userInfo", 0);
        //check if the use is already logged in
        if (sharedPref.getBoolean(getString(R.string.signed_in), false)){
            login(sharedPref.getString(getString(R.string.username), null), sharedPref.getInt(getString(R.string.memberID), 0));
        }
    }


    /** Called when the user taps the GO! button */
    public void loginCheck(View view) {
        StringBuilder url = new StringBuilder(getString(R.string.login));
        String password = ((EditText) findViewById(R.id.editText)).getText().toString();
        String username = ((EditText) findViewById(R.id.editText2)).getText().toString();
        mMemberJSON = new JSONObject();
        try{
            mMemberJSON.put("username", username);
            mMemberJSON.put("password", password);
            new LoginAsyncTask().execute(url.toString());

        } catch (JSONException e){
            Toast.makeText(this,"Error with JSON creation on login: " +
                            e.getMessage(),
                    Toast.LENGTH_LONG).show();
        }

    }

    private class LoginAsyncTask extends AsyncTask<String, Void, String> {
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
                    Log.i(mLogin, mMemberJSON.toString());
                    wr.write(mMemberJSON.toString());
                    wr.flush();
                    wr.close();

                    InputStream content = urlConnection.getInputStream();

                    BufferedReader buffer = new BufferedReader(new InputStreamReader(content));
                    String s;
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
                Toast.makeText(getApplicationContext(), s, Toast.LENGTH_SHORT).show();
                return;
            }
            try {
                JSONObject jsonObject = new JSONObject(s);
                if (jsonObject.getBoolean("success")) {
                    login(jsonObject.getString("username"),
                            jsonObject.getInt("memberID"));

                }
                else {
                    Toast.makeText(getApplicationContext(), "Member couldn't be added: "
                                    + jsonObject.getString("error")
                            , Toast.LENGTH_LONG).show();
                    Log.e(mLogin, jsonObject.getString("error"));
                }
            } catch (JSONException e) {
                Toast.makeText(getApplicationContext(), "JSON Parsing error on Adding Member"
                                + e.getMessage()
                        , Toast.LENGTH_LONG).show();
                Log.e(mLogin, e.getMessage());
            }
        }
    }

    /** creates an account */
    public void createAccount(View view) {
        Intent intent = new Intent(this, CreateAccountActivity.class);
        //intent.putExtra(EXTRA_MESSAGE, username);
        startActivity(intent);
    }

    /**
     * logs the user in
     *
     * adds MemberID, Username, and sets Signed_in to true for the userInfo shared preference.
     * @param username the username
     * @param memberID the MemberID
     */
    public void login(String username, int memberID){

        SharedPreferences sharedPref =getApplicationContext().getApplicationContext().
                getSharedPreferences("userInfo", 0);
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putInt(getString(R.string.memberID), memberID);
        editor.putString(getString(R.string.username), username);
        editor.putBoolean(getString(R.string.signed_in), true);
        editor.apply();

        Intent intent = new Intent(this, JobListActivity.class);
        startActivity(intent);
        finish();
    }
}
