package com.jerryweijin.zendeskticket;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import okhttp3.OkHttpClient;
import okhttp3.Request;

public class MainActivity extends AppCompatActivity {
    private TextView ticketContentTextView;
    private TextView ticketSubjectTextView;
    private Button sendButton;
    private String ticketSubject;
    private String ticketContent;
    private String email = "justin.chi@ismartalarm.com";
    private String token = "h5kVKlGBeJ7vXYLp10fJJ5YHS5tu5TtFWc8b5xRM";
    private String apiUrl = "https://ismartalarm.zendesk.com/api/v2/tickets.json";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ticketSubjectTextView = (TextView) findViewById(R.id.ticketSubjectContent);
        ticketContentTextView = (TextView) findViewById(R.id.ticketDescriptionContent);
        sendButton = (Button) findViewById(R.id.sendButton);

        sendButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ticketSubject = ticketSubjectTextView.getText().toString();
                ticketContent = ticketContentTextView.getText().toString();
                Toast.makeText(MainActivity.this, ticketSubject + " " + ticketContent, Toast.LENGTH_LONG).show();
                OkHttpClient client = new OkHttpClient();
                Request request = new Request.Builder().url()
            }
        });
    }
}
