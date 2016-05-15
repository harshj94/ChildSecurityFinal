package com.example.harsh.childsecurity;

import android.app.Activity;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.parse.FindCallback;
import com.parse.GetCallback;
import com.parse.ParseException;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import java.util.List;
import java.util.Map;

public class TrackingActivity extends Activity {

    EditText tracking_email;
    Button tracking_button;
    ParseQuery<ParseUser> parseUserParseQuery;

    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tracking);

        tracking_email=(EditText)findViewById(R.id.tracking_email);
        tracking_button=(Button)findViewById(R.id.tracking_button);

        parseUserParseQuery= ParseUser.getQuery();

        tracking_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(tracking_email.getText().toString().isEmpty())
                {
                    Toast.makeText(getApplicationContext(),"Tracking email is empty!!!",Toast.LENGTH_SHORT).show();
                }
                else
                {
                    parseUserParseQuery.whereEqualTo("username",tracking_email.getText().toString());
                    parseUserParseQuery.getFirstInBackground(new GetCallback<ParseUser>() {
                        @Override
                        public void done(ParseUser object, ParseException e) {
                            Double latitude= (Double) object.getNumber("latitude");
                            Double longitude=(Double) object.getNumber("longitude");
                            String username=object.getUsername();
                            MapsActivity.latitude=latitude;
                            MapsActivity.longitude=longitude;
                            MapsActivity.username=username;
                            finish();
                        }
                    });
                }
            }
        });
    }
}
