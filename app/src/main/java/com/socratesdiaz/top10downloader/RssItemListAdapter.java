package com.socratesdiaz.top10downloader;

import android.app.Activity;
import android.database.DataSetObserver;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListAdapter;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by Sócrates Díaz S on 9/11/2016.
 */
public class RssItemListAdapter implements ListAdapter {

    private ArrayList<RssItem> rssItems;

    public RssItemListAdapter() {
        rssItems = new ArrayList<RssItem>();
    }

    public ArrayList<RssItem> getRssItems() {
        return rssItems;
    }

    public void setRssItems(ArrayList<RssItem> rssItems) {
        this.rssItems = rssItems;
    }

    @Override
    public boolean areAllItemsEnabled() {
        return true;
    }

    @Override
    public boolean isEnabled(int position) {
        return false;
    }

    @Override
    public void registerDataSetObserver(DataSetObserver observer) {

    }

    @Override
    public void unregisterDataSetObserver(DataSetObserver observer) {

    }

    @Override
    public int getCount() {
        return rssItems.size();
    }

    @Override
    public Object getItem(int position) {
        return rssItems.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public boolean hasStableIds() {
        return false;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        ViewHolder holder;

        if(convertView != null) {
            convertView = LayoutInflater.from(parent.getContext()).inflate(R.layout.rss_item_list, parent);
            holder = new ViewHolder(convertView);
            convertView.setTag(holder);
        }
        else {
            holder = (ViewHolder) convertView.getTag();
        }

        RssItem rssItem = rssItems.get(position);

        holder.title.setText(rssItem.getTitle());
        holder.description.setText(rssItem.getDescription());

        return convertView;
    }

    @Override
    public int getItemViewType(int position) {
        return 0;
    }

    @Override
    public int getViewTypeCount() {
        return 1;
    }

    @Override
    public boolean isEmpty() {
        return rssItems.isEmpty();
    }

    public static class ViewHolder {
        public final TextView title;
        public final TextView description;

        public ViewHolder(View v) {
            title = (TextView) v.findViewById(R.id.rss_item_title);
            description = (TextView) v.findViewById(R.id.rss_item_description);
        }
    }
}
