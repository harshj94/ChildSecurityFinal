package com.example.harsh.childsecurity;

/**
 * Created by om on 5/13/2016.
 */
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;


/**
 * Created by hp 1 on 19-02-2016.
 */
public class EmailActivity extends AppCompatActivity implements View.OnClickListener {

    EditText personsEmail, intro, personsName, stupidThings, hatefulAction;
    String emailAdd, beginning, name, stupidAction, hatefulAct;
    Button sendEmail;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_email);
        initializeVars();
        sendEmail.setOnClickListener(this);
    }

    private void initializeVars() {
        // TODO Auto-generated method stub
        personsEmail = (EditText) findViewById(R.id.etEmails);
        intro = (EditText) findViewById(R.id.etIntro);
        personsName = (EditText) findViewById(R.id.etName);
        stupidThings = (EditText) findViewById(R.id.etThings);
        hatefulAction = (EditText) findViewById(R.id.etAction);

        sendEmail = (Button) findViewById(R.id.bSentEmail);
    }

    public void onClick(View v) {
        // TODO Auto-generated method stub

        if(v == sendEmail){


            convertEditTextVarsIntoStringsAndYesThisIsAMethodWeCreated();
            String emailaddress[] = { emailAdd };
            String message = "hello "
                    + "My Name is "+ name +'\n'
                    + " I just wanted to report a Child labour incident is happening in the shop named as: " + beginning
                    + stupidAction
                    + ", Please Do Come quick and take Action  "
                    + hatefulAct
                    + ". "

                    + '\n' + "Thank you...   :(";

            Intent emailIntent = new Intent(Intent.ACTION_SEND);
            emailIntent.putExtra(Intent.EXTRA_EMAIL, emailaddress);
            emailIntent.putExtra(Intent.EXTRA_SUBJECT, "Child Labour Report!!");
            emailIntent.setType("plain/text");
            emailIntent.putExtra(Intent.EXTRA_TEXT, message);
            startActivity(emailIntent);
        }
    }

    private void convertEditTextVarsIntoStringsAndYesThisIsAMethodWeCreated() {
        // TODO Auto-generated method stub
        emailAdd = personsEmail.getText().toString();
        beginning = intro.getText().toString();
        name = personsName.getText().toString();
        stupidAction = stupidThings.getText().toString();
        hatefulAct = hatefulAction.getText().toString();

    }

    @Override
    protected void onPause() {
        // TODO Auto-generated method stub
        super.onPause();
        finish();
    }

}