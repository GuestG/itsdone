package edu.tacoma.uw.itsdone;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
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

/**
 * this is an activity that creates an account for the user
 *
 * @authors Trevor Peters, Gehry Guest
 * @version 1.0
 * @since 2020-05-13
 */
public class CreateAccountActivity extends AppCompatActivity {
    private JSONObject mMemberJSON;
    public static final String ADD_MEMBER = "ADD_MEMBER";

    /**
     * hides action bar and creates saved state, loading the activity_create_account.XML
     * for the activity.
     *
     * @param savedInstanceState
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ActionBar actionBar = getSupportActionBar();
        actionBar.hide();
        setContentView(R.layout.activity_create_account);
    }

    /**
     * adds the member to the database
     *
     * @param account account to be added
     */
    public void addMember(Account account) {
        StringBuilder url = new StringBuilder(getString(R.string.register));

        mMemberJSON = new JSONObject();
        try{
            mMemberJSON.put("first", account.getFirstName());
            mMemberJSON.put("last", account.getLastName());
            mMemberJSON.put("username", account.getUsername());
            mMemberJSON.put("email", account.getEmail());
            mMemberJSON.put("password", account.getPassword());
            new AddMemberAsyncTask().execute(url.toString());

        } catch (JSONException e){
            Toast.makeText(this,"Error with JSON creation on creating an account: " +
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
                    Log.i(ADD_MEMBER, mMemberJSON.toString());
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
                    Toast.makeText(getApplicationContext(), "Member Added successfully"
                            , Toast.LENGTH_SHORT).show();
                    finish();
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


    /**
     * creates the account and passes it to the AddMember method.
     *
     * @param view
     */
    public void createAccount(View view) {
        String password = ((EditText) findViewById(R.id.password)).getText().toString();
        String username = ((EditText) findViewById(R.id.username)).getText().toString();
        String firstname = ((EditText) findViewById(R.id.first_name)).getText().toString();
        String lastname = ((EditText) findViewById(R.id.last_name)).getText().toString();
        String email = ((EditText) findViewById(R.id.email)).getText().toString();

        Account account = new Account(firstname,lastname, username, email, password);
        addMember(account);

    }
}
