package com.example.wiehan.tinnitusmanagement;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.text.InputFilter;
import android.text.InputType;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

/**
 * Created by wiehan on 2017/09/23.
 */

public class Login  extends AppCompatActivity {

    Boolean close ;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        final AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setCancelable(false);
        builder.setTitle("User ID");
        final EditText input = new EditText(this);
        input.setGravity(Gravity.CENTER);
        int maxLength = 16;
        input.setFilters(new InputFilter[] {new InputFilter.LengthFilter(maxLength)});

        input.setInputType(InputType.TYPE_CLASS_TEXT);
        builder.setView(input);
        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        });

        final AlertDialog dialogInput = builder.create();
        dialogInput.getWindow().setBackgroundDrawableResource(R.drawable.textview_custom);
        dialogInput.show();

        dialogInput.getButton(AlertDialog.BUTTON_POSITIVE).setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                close = false;

                if(input.length() != 0) {
                    close = true ;
                } else {
                    Toast.makeText(getApplicationContext(), "PLEASE ENTER AN ID", Toast.LENGTH_LONG).show(); ;
                }

                if(close) {
                    dialogInput.dismiss();
                    MainScreen.setFileName(input.getText().toString());
                    MainScreen.writeFile("Insert Name");
                    Intent intent = new Intent(getApplicationContext(), MainScreen.class);
                    startActivity(intent);
                }
                //else dialog stays open. Make sure you have an obvious way to close the dialog especially if you set cancellable to false.
            }
        });





    }
}
