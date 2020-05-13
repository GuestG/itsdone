package edu.tacoma.uw.itsdone;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
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

import edu.tacoma.uw.itsdone.model.Account;

public class MainActivity extends AppCompatActivity {
    private JSONObject mMemberJSON;
    public static final String mLogin = "Login";


    public static final String EXTRA_MESSAGE = "Not Entirely sure what this is for"; //TODO

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
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

    public void login(String username, int memberID){
        //TODO username & MemberID can accessed here

        SharedPreferences sharedPref =getApplicationContext().getApplicationContext().
                getSharedPreferences("userInfo", 0);
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putInt("memberID", memberID);
        editor.putString("username", username);
        editor.commit();

        Intent intent = new Intent(this, JobListActivity.class);
        //intent.putExtra(EXTRA_MESSAGE, username);
        startActivity(intent);
        finish();
    }
}
