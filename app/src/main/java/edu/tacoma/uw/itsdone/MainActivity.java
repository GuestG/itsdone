package edu.tacoma.uw.itsdone;

import androidx.appcompat.app.AppCompatActivity;
import android.util.Log;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

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
        EditText passwordEditText = findViewById(R.id.editText);
        EditText usernameEditText = findViewById(R.id.editText2);

        // TODO actually check these values... maybe
        String password = passwordEditText.getText().toString();
        String username = usernameEditText.getText().toString();
        intent.putExtra(EXTRA_MESSAGE, username);
        startActivity(intent);
    }
}
