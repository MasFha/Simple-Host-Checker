package com.host.checker;

import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import java.io.IOException;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class MainActivity extends AppCompatActivity {

  private EditText urlEditText;
  private Button checkButton;
  private TextView resultTextView;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_main);

    urlEditText = findViewById(R.id.urlEditText);
    checkButton = findViewById(R.id.checkButton);
    resultTextView = findViewById(R.id.resultTextView);

    checkButton.setOnClickListener(
        new View.OnClickListener() {
          @Override
          public void onClick(View view) {
            String url = urlEditText.getText().toString();
            new HostCheckTask().execute(url);
          }
        });
  }

  private class HostCheckTask extends AsyncTask<String, Void, String> {

    @Override
    protected String doInBackground(String... urls) {
      String result = "";
      OkHttpClient client = new OkHttpClient();

      for (String url : urls) {
        // Ensure the URL has a valid scheme
        if (!url.startsWith("http://") && !url.startsWith("https://")) {
          // Handle the error or provide a valid default scheme
          result += "Error: Invalid URL scheme\n";
          continue;
        }

        Request request = new Request.Builder().url(url).build();

        try {
          Response response = client.newCall(request).execute();
          result += "URL: " + url + "\n";
          result += "Status Code: " + response.code() + "\n";
          result += "Headers:\n";
          result += response.headers().toString() + "\n\n";
        } catch (IOException e) {
          result += "Error: " + e.getMessage() + "\n";
          // Handle the exception or log it accordingly
        }
      }

      return result;
    }

    @Override
    protected void onPostExecute(String result) {
      resultTextView.setText(result);
    }
  }
}
