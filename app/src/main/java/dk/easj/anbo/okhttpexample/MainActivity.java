package dk.easj.anbo.okhttpexample;

import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.ResponseBody;

// https://www.journaldev.com/13629/okhttp-android-example-tutorial

// Problems running in emulator:
//     Unable to resolve host ... No address associated with hostname
// Solution: Restart emulator
// Course? Emulator was used with different real life IP-addresses (laptop was moved)
public class MainActivity extends AppCompatActivity {
    public static final String TAG = "okhttptag";
    private String url = "http://jsonplaceholder.typicode.cffom/comments";
    // private String url = "http://anbo-restserviceproviderbooks.azurewebsites.net/Service1.svc/books";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        OkHttpHandler handler = new OkHttpHandler();
        handler.execute(url);
    }

    class OkHttpHandler extends AsyncTask<String, Void, String> {
        private OkHttpClient client = new OkHttpClient();
        private TextView messageView = findViewById(R.id.mainMessageTextView);

        @Override
        protected String doInBackground(String... urls) {
            String url = urls[0];
            Request.Builder builder = new Request.Builder(); // builder pattern
            builder.url(url);
            Request request = builder.build();
            try {
                Call call = client.newCall(request);
                Response response = call.execute();
                // response.isSuccessful() ...
                ResponseBody responseBody = response.body();
                String jsonString = responseBody.string();
                return jsonString;
            } catch (IOException ex) {
                cancel(true);
                String errorMessage = ex.getMessage();
                Log.e(TAG, errorMessage);
                return errorMessage;
            }
        }

        @Override
        protected void onPostExecute(String jsonString) {
            super.onPostExecute(jsonString);
            messageView.setText(jsonString);
            // Show in ListView
        }

        @Override
        protected void onCancelled(String errorMessage) {
            super.onCancelled(errorMessage);
            messageView.setText(errorMessage);
        }
    }
}
