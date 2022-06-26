package com.example.soc_macmini_15.musicplayer.Adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Filterable;
import android.widget.TextView;

import com.example.soc_macmini_15.musicplayer.Model.EbooksList;
import com.example.soc_macmini_15.musicplayer.R;

import java.util.ArrayList;

public class EbookAdapter extends ArrayAdapter<EbooksList> implements Filterable{

    private Context mContext;
    private ArrayList<EbooksList> ebooksList = new ArrayList<>();

    public EbookAdapter(Context mContext, ArrayList<EbooksList> ebooksList) {
        super(mContext, 0, ebooksList);
        this.mContext = mContext;
        this.ebooksList = ebooksList;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View listItem = convertView;
        if (listItem == null) {
            listItem = LayoutInflater.from(mContext).inflate(R.layout.playlist_items, parent, false);
        }
        EbooksList currentEbook = ebooksList.get(position);
        TextView tvTitle = listItem.findViewById(R.id.tv_music_name);
        TextView tvSubtitle = listItem.findViewById(R.id.tv_music_subtitle);
        tvTitle.setText(currentEbook.getTitle());
        tvSubtitle.setText(currentEbook.getSubTitle());
        return listItem;
    }
}
