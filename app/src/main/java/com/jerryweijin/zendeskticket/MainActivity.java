package com.jerryweijin.zendeskticket;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.analytics.FirebaseAnalytics;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class MainActivity extends AppCompatActivity {
    private static final String TAG = MainActivity.class.getSimpleName();
    private TextView ticketContentTextView;
    private TextView ticketSubjectTextView;
    private Button sendButton;
    private String ticketSubject;
    private String ticketContent;
    //justin.chi@ismartalarm.com/token:h5kVKlGBeJ7vXYLp10fJJ5YHS5tu5TtFWc8b5xRM
    //private String credential = "Basic anVzdGluLmNoaUBpc21hcnRhbGFybS5jb20vdG9rZW46aDVrVktsR0JlSjd2WFlMcDEwZkpKNVlIUzV0dTVUdEZXYzhiNXhSTQ==";
    //jiafangrong@china.ismartalarm.com/token:h5kVKlGBeJ7vXYLp10fJJ5YHS5tu5TtFWc8b5xRM
    private String credential = "Basic amlhZmFuZ3JvbmdAY2hpbmEuaXNtYXJ0YWxhcm0uY29tL3Rva2VuOmg1a1ZLbEdCZUo3dlhZTHAxMGZKSjVZSFM1dHU1VHRGV2M4YjV4Uk0=";
    private String apiUrl = "https://ismartalarm.zendesk.com/api/v2/tickets.json";
    private MediaType JSON = MediaType.parse("application/json; charset=utf-8");
    private ProgressBar progressBar;
    private FirebaseAnalytics mFirebaseAnalytics;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mFirebaseAnalytics = FirebaseAnalytics.getInstance(this);

        ticketSubjectTextView = (TextView) findViewById(R.id.ticketSubjectContent);
        ticketContentTextView = (TextView) findViewById(R.id.ticketDescriptionContent);
        sendButton = (Button) findViewById(R.id.sendButton);
        progressBar = (ProgressBar) findViewById(R.id.progressBar);

        ticketSubjectTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bundle bundle = new Bundle();
                bundle.putString(FirebaseAnalytics.Param.ITEM_ID, "ticketSubjectTextView");
                mFirebaseAnalytics.logEvent(FirebaseAnalytics.Event.SELECT_CONTENT, bundle);
                Log.i(TAG, "Subject TextView is tapped");
            }
        });

        ticketContentTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bundle bundle = new Bundle();
                bundle.putString(FirebaseAnalytics.Param.ITEM_ID, "ticketContentTextView");
                mFirebaseAnalytics.logEvent(FirebaseAnalytics.Event.SELECT_CONTENT, bundle);
                Log.i(TAG, "Content TextView is tapped");
            }
        });

        sendButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ticketSubject = ticketSubjectTextView.getText().toString();
                ticketContent = ticketContentTextView.getText().toString();
                String json = "{\"ticket\":{\"subject\":" + "\"" + ticketSubject + "\"" + ",\"comment\":{\"body\":" + "\"" + ticketContent + "\"" + "}}}";

                OkHttpClient client = new OkHttpClient();
                RequestBody body = RequestBody.create(JSON, json);
                Request request = new Request.Builder()
                        .url(apiUrl)
                        .header("Authorization", credential)
                        .header("Content-Type", "application/json")
                        .header("Accept", "application/json")
                        .post(body)
                        .build();
                client.newCall(request).enqueue(new Callback() {
                    @Override
                    public void onFailure(Call call, IOException e) {
                        MainActivity.this.runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                Toast.makeText(MainActivity.this, "Network unavailable. Please check your network.", Toast.LENGTH_LONG).show();
                                progressBar.setVisibility(View.INVISIBLE);
                            }
                        });
                    }

                    @Override
                    public void onResponse(Call call, Response response) throws IOException {
                        if (response.isSuccessful()) {
                            MainActivity.this.runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    ticketSubjectTextView.setText("");
                                    ticketContentTextView.setText("");
                                    Toast.makeText(MainActivity.this, "Success", Toast.LENGTH_LONG).show();
                                    progressBar.setVisibility(View.INVISIBLE);
                                }
                            });
                            Log.v(TAG, response.body().string());
                        } else {
                            MainActivity.this.runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    Toast.makeText(MainActivity.this, "Unable to submit your ticket. Please try agian.", Toast.LENGTH_LONG).show();
                                    progressBar.setVisibility(View.INVISIBLE);
                                }
                            });
                            throw new IOException("Unexpected code " + response);
                        }

                    }
                });
                progressBar.setVisibility(View.VISIBLE);
            }
        });
    }
}
