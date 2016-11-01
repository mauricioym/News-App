package com.yuddi.newsapp;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.List;

/**
 * Created by Mauricio on 11/1/2016.
 */
public class NewsAdapter extends ArrayAdapter<Story> {

    public NewsAdapter(Context context, List<Story> stories) {
        super(context, 0, stories);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        ViewHolder holder;

        View listItemView = convertView;
        if (listItemView == null) {
            listItemView = LayoutInflater.from(getContext()).inflate(R.layout.list_item, parent, false);

            holder = new ViewHolder();
            holder.sectionTextView = (TextView) listItemView.findViewById(R.id.section_textview);
            holder.titleTextView = (TextView) listItemView.findViewById(R.id.title_textview);
            holder.dateTextView = (TextView) listItemView.findViewById(R.id.date_textview);

            listItemView.setTag(holder);
        } else {
            holder = (ViewHolder) listItemView.getTag();
        }

        Story currentStory = getItem(position);

        holder.sectionTextView.setText(currentStory.getSection());
        holder.titleTextView.setText(currentStory.getTitle());
        holder.dateTextView.setText(currentStory.getDate());

        return listItemView;
    }

    static class ViewHolder {
        TextView sectionTextView;
        TextView titleTextView;
        TextView dateTextView;
    }

}
