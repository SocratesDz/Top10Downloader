package com.socratesdiaz.top10downloader;

import android.util.Log;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.StringReader;
import java.util.ArrayList;

/**
 * Created by Sócrates Díaz S on 9/10/2016.
 */
public class RSSItemParser {
    public static final String LOG_TAG = RSSItem.class.getSimpleName();
    private String xmlData;
    private ArrayList<RSSItem> rssItems;

    public RSSItemParser(String xmlData) {
        this.xmlData = xmlData;
        this.rssItems = new ArrayList<RSSItem>();
    }

    public ArrayList<RSSItem> getRssItems() {
        return rssItems;
    }

    public boolean proccess() {
        boolean status = true;
        RSSItem currentRecord;
        boolean inEntry = false;
        String textValue = "";

        try {
            XmlPullParserFactory factory = XmlPullParserFactory.newInstance();
            factory.setNamespaceAware(true);
            XmlPullParser xpp = factory.newPullParser();
            xpp.setInput(new StringReader(this.xmlData));
            int eventType = xpp.getEventType();

            while (eventType != XmlPullParser.END_DOCUMENT) {
                String tagName = xpp.getName();
                switch (eventType) {
                    case XmlPullParser.START_TAG:
                        Log.d(LOG_TAG, "Starting tag for " + tagName);
                        if(tagName.equalsIgnoreCase("item")) {
                            inEntry = true;
                            currentRecord = new RSSItem();
                            break;
                        }
                    case XmlPullParser.END_TAG:
                        Log.d(LOG_TAG, "Ending tag for " + tagName);
                        if(tagName.equalsIgnoreCase("item")) {
                            inEntry = false;
                        }
                        break;

                    default:
                }
                eventType = xpp.next();
            }

            return true;
        } catch (Exception e) {
            status = false;
            e.printStackTrace();
            return false;
        }
    }

}
