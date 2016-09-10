package com.socratesdiaz.top10downloader;

import android.os.AsyncTask;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.ListView;
import android.widget.TextView;

import org.w3c.dom.Text;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    private ListView itemListView;
    private SwipeRefreshLayout swipeRefreshLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        itemListView = (ListView) findViewById(R.id.rss_items_list);
        DownloadData downloadData = new DownloadData();
        downloadData.execute(getString(R.string.url_download));

        swipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.swipe_refresh_layout);
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                swipeRefreshLayout.setRefreshing(true);
                DownloadData downloadDataTask = new DownloadData();
                downloadDataTask.execute(getString(R.string.url_download));
            }
        });

    }

    private class DownloadData extends AsyncTask<String, Void, String> {

        public final String LOG_TAG = this.getClass().getSimpleName();
        private String mFileContents;

        @Override
        protected String doInBackground(String... strings) {
            mFileContents = downloadXMLFile(strings[0]);
            if(mFileContents == null) {
                Log.d(LOG_TAG, "Error downloading");
            }
            else {
                RSSItemParser parser = new RSSItemParser(mFileContents);
                parser.proccess();
                ArrayList<RSSItem> items = parser.getRssItems();
            }

            return mFileContents;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            Log.d(LOG_TAG, "Result was: " + s);
            swipeRefreshLayout.setRefreshing(false);
        }

        private String downloadXMLFile(String urlPath) {
            StringBuilder tempBuffer = new StringBuilder();
            try {
                URL url = new URL(urlPath);
                HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                int response = connection.getResponseCode();
                Log.d(LOG_TAG, "The response code was " + response);
                InputStream is = connection.getInputStream();
                InputStreamReader isr = new InputStreamReader(is);

                int charRead;
                char[] inputBuffer = new char[500];
                while(true) {
                    charRead = isr.read(inputBuffer);
                    if(charRead <= 0) {
                        break;
                    }
                    tempBuffer.append(String.copyValueOf(inputBuffer, 0, charRead));
                }

                return tempBuffer.toString();

            } catch (IOException e) {
                Log.d(LOG_TAG, "IO Exception reading data: " + e.getMessage());
                e.printStackTrace();
            } catch(SecurityException sece) {
                Log.d(LOG_TAG, "Security exception. Needs permissions? " + sece.getMessage());
            }

            return null;
        }
    }
}
