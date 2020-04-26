package edu.tacoma.uw.itsdone;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    public static final String EXTRA_MESSAGE = "Not Entirely sure what this is for"; //TODO

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

    }

    /** Called when the user taps the GO! button */
    public void login(View view) {
        Intent intent = new Intent(this, FindJobsActivity.class);
        String password = ((EditText) findViewById(R.id.editText)).getText().toString();
        String username = ((EditText) findViewById(R.id.editText2)).getText().toString();

        //checks username and password
        SharedPreferences login = getApplicationContext().getSharedPreferences(username, 0);
        if (!login.contains("password")) {
            //TODO have a button to create a new account
            SharedPreferences.Editor edit = login.edit();
            Log.e("create new password!!", "I HAD TO MAKE A NEW PASSWORD!");
            edit.putString("password", password);
            edit.commit();
        }

        if (password.equals(login.getString("password", null))) {
            intent.putExtra(EXTRA_MESSAGE, username);
            startActivity(intent);
        } else {
            Toast toast = Toast.makeText( getApplicationContext(),
                    "incorrect password", Toast.LENGTH_SHORT);
            toast.show();
        }
    }
}
