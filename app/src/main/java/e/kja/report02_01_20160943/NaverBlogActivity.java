package e.kja.report02_01_20160943;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

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

import androidx.annotation.Nullable;

public class NaverBlogActivity extends Activity { //Blog 리스트 화면을 위한 Activity
    public static final String TAG = "MyBlogActivity";

    ListView listView;
    String apiAddress;
    String  title;
    String category;
    String query;

    MyBlogAdapter adapter;
    ArrayList<NaverBlogDto> resultList;
    NaverBlogXmlParser parser;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.naver_blog_list);

        Intent intent = getIntent();

        title = intent.getStringExtra("title");
        //category =intent.getStringExtra("category");

        StringBuilder sb = new StringBuilder(); //intent에서 받아온 값을 합쳐서 query로 넘겨준다.

        sb.append(title);
        sb.append(category);

        query = sb.toString();

        listView = (ListView)findViewById(R.id.bloglistview);
        resultList = new ArrayList();

        adapter = new MyBlogAdapter(this, R.layout.blog_infor, resultList);
        listView.setAdapter(adapter);

        apiAddress = getResources().getString(R.string.api_url2);
        parser = new NaverBlogXmlParser();

        try{
            new NaverAsyncTask().execute(apiAddress + URLEncoder.encode(query, "UTF-8"));
        }catch (UnsupportedEncodingException e){
            e.printStackTrace();
        }


    }


    class NaverAsyncTask extends AsyncTask<String,Integer, String> {
        ProgressDialog progressDialog;


        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressDialog = progressDialog.show(NaverBlogActivity.this, "Wait", "loading");
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
