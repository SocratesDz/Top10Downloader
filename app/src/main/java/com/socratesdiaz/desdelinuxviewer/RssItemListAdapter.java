package com.socratesdiaz.desdelinuxviewer;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by Sócrates Díaz S on 9/11/2016.
 */
public class RssItemListAdapter extends ArrayAdapter<RssItem> {

    private ArrayList<RssItem> rssItems;
    private LayoutInflater inflater;
    private Context context;
    private int layoutResource;

    public RssItemListAdapter(Context context, int resourceId) {
        super(context, resourceId);
        rssItems = new ArrayList<RssItem>();
        this.context = context;
        this.inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        layoutResource = resourceId;
    }

    public ArrayList<RssItem> getRssItems() {
        return rssItems;
    }

    public void setRssItems(ArrayList<RssItem> rssItems) {
        this.rssItems = rssItems;
    }

    @Override
    public boolean areAllItemsEnabled() {
        return false;
    }

    @Override
    public boolean isEnabled(int position) {
        return false;
    }

    @Override
    public int getCount() {
        return rssItems.size();
    }

    @Override
    public RssItem getItem(int position) {
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

        if(convertView == null) {
            convertView = inflater.inflate(layoutResource, parent, false);
            holder = new ViewHolder(convertView);
            convertView.setTag(holder);
        }
        else {
            holder = (ViewHolder) convertView.getTag();
        }

        final RssItem rssItem = rssItems.get(position);

        holder.title.setText(Html.fromHtml(rssItem.getTitle()));
        holder.description.setText(Html.fromHtml(rssItem.getDescription()));

        convertView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, WebActivity.class);
                intent.putExtra(WebActivity.WEB_URL_EXTRA, rssItem.getLink());
                context.startActivity(intent);
            }
        });

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

    public void clear() {

        this.rssItems.clear();
        notifyDataSetChanged();
    }

    public void add(RssItem item) {

        this.rssItems.add(item);
        notifyDataSetChanged();
    }

    public void addAll(ArrayList<RssItem> items) {

        this.rssItems.addAll(items);
        notifyDataSetChanged();
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
