package e.kja.report02_01_20160943;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ListView;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;

import javax.net.ssl.HttpsURLConnection;

public class MainActivity extends AppCompatActivity {


    public static final String TAG = "MainActivity";

    EditText etTarget;
    ListView listView;
    String apiAddress;
    String query;

    MyRestaurantAdapter adapter;
    ArrayList<NaverLocalDto> resultList;
    NaverLocalXmlParser parser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        etTarget = findViewById(R.id.etTarget);
        listView = (ListView)findViewById(R.id.listView);

        resultList = new ArrayList();
        adapter = new MyRestaurantAdapter(this, R.layout.restaurant_infor, resultList);
        listView.setAdapter(adapter);

        apiAddress = getResources().getString(R.string.api_url);
        parser = new NaverLocalXmlParser();

    }

    public void OnClick(View v){
        switch (v.getId()){
            case R.id.button:
                query = etTarget.getText().toString();
                try{
                    new NaverAsyncTask().execute(apiAddress + URLEncoder.encode(query, "UTF-8"));
                }catch (UnsupportedEncodingException e){
                    e.printStackTrace();
                }
                break;
        }
    }

    class NaverAsyncTask extends AsyncTask<String,Integer, String> {
        ProgressDialog progressDialog;


        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressDialog = progressDialog.show(MainActivity.this, "Wait", "loading");
        }

        @Override
        protected String doInBackground(String... strings) {
            String address = strings[0];
            String result = downloadContents(address);
            if (result == null) return "Error!";
            return result;
        }

        @Override
        protected void onPostExecute(String result) {

            Log.i(TAG, result);

            resultList = parser.parse(result);

            adapter.setList(resultList);
            adapter.notifyDataSetChanged();

            progressDialog.dismiss();

        }

        private boolean isOnline() {
            ConnectivityManager connMgr = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();
            return (networkInfo != null && networkInfo.isConnected());
        }

        private InputStream getNetworkConnection(HttpURLConnection conn) throws Exception {

            conn.setReadTimeout(3000);
            conn.setConnectTimeout(3000);
            conn.setRequestMethod("GET");
            conn.setDoInput(true);
            conn.setRequestProperty("X-Naver-Client-Id", getResources().getString(R.string.client_id));
            conn.setRequestProperty("X-Naver-Client-Secret", getResources().getString(R.string.client_secret));

            if (conn.getResponseCode() != HttpsURLConnection.HTTP_OK) {
                throw new IOException("HTTP error code: " + conn.getResponseCode());
            }

            return conn.getInputStream();
        }

        protected String readStreamToString(InputStream stream) {
            StringBuilder result = new StringBuilder();

            try {
                InputStreamReader inputStreamReader = new InputStreamReader(stream);
                BufferedReader bufferedReader = new BufferedReader(inputStreamReader);

                String readLine = bufferedReader.readLine();

                while (readLine != null) {
                    result.append(readLine + "\n");
                    readLine = bufferedReader.readLine();
                }

                bufferedReader.close();
            } catch (IOException e) {
                e.printStackTrace();
            }

            return result.toString();
        }


        protected String downloadContents(String address) {
            HttpURLConnection conn = null;
            InputStream stream = null;
            String result = null;

            try {
                URL url = new URL(address);
                conn = (HttpURLConnection)url.openConnection();
                stream = getNetworkConnection(conn);
                result = readStreamToString(stream);
                if (stream != null) stream.close();
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                if (conn != null) conn.disconnect();
            }

            return result;
        }
    }
}