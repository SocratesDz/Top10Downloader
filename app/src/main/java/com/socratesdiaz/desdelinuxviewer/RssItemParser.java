package com.socratesdiaz.desdelinuxviewer;

import android.util.Log;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.StringReader;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Locale;

/**
 * Created by Sócrates Díaz S on 9/10/2016.
 */
public class RssItemParser {
    public static final String LOG_TAG = RssItem.class.getSimpleName();
    private String xmlData;
    private ArrayList<RssItem> rssItems;

    public RssItemParser(String xmlData) {
        this.xmlData = xmlData;
        this.rssItems = new ArrayList<RssItem>();
    }

    public ArrayList<RssItem> getRssItems() {
        return rssItems;
    }

    public boolean process() {
        boolean status = true;
        RssItem currentRecord = null;
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
                        if(tagName.equalsIgnoreCase("item")) {
                            inEntry = true;
                            currentRecord = new RssItem();
                        }
                        break;
                    case XmlPullParser.TEXT:
                        textValue = xpp.getText();
                        break;
                    case XmlPullParser.END_TAG:
                        if(tagName.equalsIgnoreCase("item")) {
                            inEntry = false;
                            rssItems.add(currentRecord);
                        }
                        if(tagName.equalsIgnoreCase("title") && inEntry
                                && currentRecord != null) {
                            currentRecord.setTitle(textValue);
                        }
                        if(tagName.equalsIgnoreCase("link") && inEntry
                                && currentRecord != null) {
                            currentRecord.setLink(textValue);
                        }
                        if(tagName.equalsIgnoreCase("comments") && inEntry
                                && currentRecord != null) {
                            currentRecord.setComments(textValue);
                        }
                        if(tagName.equalsIgnoreCase("description") && inEntry
                                && currentRecord != null) {
                            currentRecord.setDescription(textValue);
                        }
                        if (tagName.equalsIgnoreCase("pubDate") && inEntry
                                && currentRecord != null) {
                            try {
                                SimpleDateFormat formatter =
                                        new SimpleDateFormat("E, d MMM yyyy HH:mm:ss Z", Locale.US);
                                currentRecord.setPubDate(formatter.parse(textValue));
                            }
                            catch(Exception e) {
                                Log.v(LOG_TAG, e.getMessage());
                            }
                        }
                        if(tagName.equalsIgnoreCase("image") && inEntry
                                && currentRecord != null) {
                            currentRecord.setImageUrl(textValue);
                        }
                        break;

                    default:
                }
                eventType = xpp.next();
            }
        } catch (Exception e) {
            status = false;
            e.printStackTrace();
        }
        return status;
    }

}
