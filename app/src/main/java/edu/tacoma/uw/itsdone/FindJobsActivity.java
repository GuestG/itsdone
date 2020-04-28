package edu.tacoma.uw.itsdone;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
//TODO all of this class tbh
public class FindJobsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_find_jobs);
        //removes the back button
        getSupportActionBar().setDisplayHomeAsUpEnabled(false);
        // Get the Intent that started this activity and extract the string
        Intent intent = getIntent();

        // Capture the layout's TextView and set the string as its text
        TextView textView = findViewById(R.id.textView4);

        textView.setText(getApplicationContext().getSharedPreferences("autoLogin", 0)
                .getString("username", null));


    }

    public void logout(View view){
        SharedPreferences autoLogin = getApplicationContext().getSharedPreferences("autoLogin", 0);
        SharedPreferences.Editor edit = autoLogin.edit();
        edit.putBoolean("autoLogin", false);
        edit.commit();
        startActivity(new Intent(this, MainActivity.class));

        finish();
    }

    /**makes back button do nothing */
    @Override
    public void onBackPressed(){

    }
}
