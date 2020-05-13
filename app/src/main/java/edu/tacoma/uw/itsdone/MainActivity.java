package edu.tacoma.uw.itsdone;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
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
    public void loginCheck(View view) {
        String password = ((EditText) findViewById(R.id.editText)).getText().toString();
        String username = ((EditText) findViewById(R.id.editText2)).getText().toString();


    }

    /** acually does the loging in */
    public void login(String username) {
        Intent intent = new Intent(this, JobListActivity.class);
        intent.putExtra(EXTRA_MESSAGE, username);
        startActivity(intent);
        finish();
    }

    /** creates an account */
    public void createAccount(View view) {
        Intent intent = new Intent(this, CreateAccountActivity.class);
        //intent.putExtra(EXTRA_MESSAGE, username);
        startActivity(intent);
    }
}
