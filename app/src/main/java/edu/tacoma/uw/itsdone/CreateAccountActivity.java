package edu.tacoma.uw.itsdone;

import androidx.appcompat.app.AppCompatActivity;

import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
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

public class CreateAccountActivity extends AppCompatActivity {
    private JSONObject mJobJSON;
    public static final String ADD_MEMBER = "ADD_MEMBER";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_account);
    }

    public void addMember(Account account) {
        StringBuilder url = new StringBuilder(getString(R.string.register));

        mJobJSON = new JSONObject();
        try{
            mJobJSON.put("first", account.getFirstName());
            mJobJSON.put("last", account.getLastName());
            mJobJSON.put("username", account.getUsername());
            mJobJSON.put("email", account.getEmail());
            mJobJSON.put("password", account.getPassword());
            new AddMemberAsyncTask().execute(url.toString());

        } catch (JSONException e){
            Toast.makeText(this,"Error with JSON creation on adding a job: " +
                            e.getMessage(),
                    Toast.LENGTH_LONG).show();
        }
    }

    private class AddMemberAsyncTask extends AsyncTask<String, Void, String> {
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
                    Log.i(ADD_MEMBER, mJobJSON.toString());
                    wr.write(mJobJSON.toString());
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
                    Toast.makeText(getApplicationContext(), "Member Added successfully"
                            , Toast.LENGTH_SHORT).show();
                }
                else {
                    Toast.makeText(getApplicationContext(), "Member couldn't be added: "
                                    + jsonObject.getString("error")
                            , Toast.LENGTH_LONG).show();
                    Log.e(ADD_MEMBER, jsonObject.getString("error"));
                }
            } catch (JSONException e) {
                Toast.makeText(getApplicationContext(), "JSON Parsing error on Adding Member"
                                + e.getMessage()
                        , Toast.LENGTH_LONG).show();
                Log.e(ADD_MEMBER, e.getMessage());
            }
        }
    }
    /** creates an account */
    public void createAccount(View view) {
        String toastString = "Account Created!";
        String password = ((EditText) findViewById(R.id.password)).getText().toString();
        String username = ((EditText) findViewById(R.id.username)).getText().toString();
        String firstname = ((EditText) findViewById(R.id.first_name)).getText().toString();
        String lastname = ((EditText) findViewById(R.id.last_name)).getText().toString();
        String email = ((EditText) findViewById(R.id.email)).getText().toString();

        Account account = new Account(firstname,lastname, username, email, password);
        addMember(account);

//        Toast toast = Toast.makeText( getApplicationContext(),
//                toastString, Toast.LENGTH_SHORT);
//        toast.show();

    }
}
