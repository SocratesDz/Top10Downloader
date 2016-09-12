package com.socratesdiaz.desdelinuxviewer;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.ListView;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    private ListView itemListView;
    private SwipeRefreshLayout swipeRefreshLayout;
    private RssItemListAdapter listAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        itemListView = (ListView) findViewById(R.id.rss_items_list);

        listAdapter = new RssItemListAdapter(this, R.layout.rss_item_list);
        itemListView.setAdapter(listAdapter);

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

    private class DownloadData extends AsyncTask<String, Void, ArrayList<RssItem>> {

        public final String LOG_TAG = this.getClass().getSimpleName();
        private String mFileContents;

        @Override
        protected ArrayList<RssItem> doInBackground(String... strings) {
            mFileContents = downloadXMLFile(strings[0]);
            if(mFileContents == null) {
                Log.d(LOG_TAG, "Error downloading");
            }

            RssItemParser parser = new RssItemParser(mFileContents);
            parser.process();
            ArrayList<RssItem> items = parser.getRssItems();
            return items;
        }

        @Override
        protected void onPostExecute(ArrayList<RssItem> items) {
            super.onPostExecute(items);
            if(items != null) {
                listAdapter.clear();
                listAdapter.addAll(items);
            }
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
