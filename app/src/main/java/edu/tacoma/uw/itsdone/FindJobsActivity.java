package edu.tacoma.uw.itsdone;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
//TODO all of this class tbh
public class FindJobsActivity extends AppCompatActivity {
    //button to addJOb Not working!!!!

    private Button addJobButton;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_find_jobs);
        //removes the back button
        getSupportActionBar().setDisplayHomeAsUpEnabled(false);
        // Get the Intent that started this activity and extract the string
        //Intent intent = getIntent();

        // Makes Job Button go to Add Job Activity //button to addJOb Not working!!!!
        //TODO i have no idea why this is crashing when clicked. fucking stupid
        addJobButton = findViewById(R.id.AddJobButton);
        addJobButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                GoToAddJobPage();
            }
        });

        // Capture the layout's TextView and set the string as its text
        //TextView textView = findViewById(R.id.textView4);

        //textView.setText(getApplicationContext().getSharedPreferences("autoLogin", 0)
                //.getString("username", null));

    }
    //button to addJOb Not working!!!!

    public void GoToAddJobPage() {
        Intent intent = new Intent(this, AddJob.class);
        startActivity(intent);
        finish();
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
