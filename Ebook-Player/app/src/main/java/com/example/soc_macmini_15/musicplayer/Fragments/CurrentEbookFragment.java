package com.example.soc_macmini_15.musicplayer.Fragments;


import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.ListFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import com.example.soc_macmini_15.musicplayer.Adapter.EbookAdapter;
import com.example.soc_macmini_15.musicplayer.Model.EbooksList;
import com.example.soc_macmini_15.musicplayer.R;

import java.util.ArrayList;

public class CurrentEbookFragment extends ListFragment {

    public ArrayList<EbooksList> ebooksList = new ArrayList<>();

    private ListView listView;

    private createDataParsed createDataParsed;

    public static Fragment getInstance(int position) {
        Bundle bundle = new Bundle();
        bundle.putInt("pos", position);
        CurrentEbookFragment tabFragment = new CurrentEbookFragment();
        tabFragment.setArguments(bundle);
        return tabFragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        createDataParsed = (createDataParsed) context;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_tab, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        listView = view.findViewById(R.id.list_playlist);
        //songsList = new ArrayList<>();
        setContent();
    }

    /**
     * Setting the content in the listView and sending the data to the Activity
     */
    public void setContent() {
        if (createDataParsed.getEbook() != null)
            ebooksList.add(createDataParsed.getEbook());

        EbookAdapter adapter = new EbookAdapter(getContext(), ebooksList);

        if (ebooksList.size() > 1)
            if (createDataParsed.getPlaylistFlag()) {
                ebooksList.clear();
            }

        listView.setAdapter(adapter);
        adapter.notifyDataSetChanged();
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                // Toast.makeText(getContext(), "You clicked :\n" + songsList.get(position), Toast.LENGTH_SHORT).show();
                createDataParsed.onDataPass(ebooksList.get(position).getTitle(), ebooksList.get(position).getPath());
                createDataParsed.fullEbooksList(ebooksList, position);
            }
        });
        listView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {

                return true;
            }
        });
    }

    public interface createDataParsed {
        public void onDataPass(String name, String path);

        public void fullEbooksList(ArrayList<EbooksList> ebookList, int position);

        public EbooksList getEbook();

        public boolean getPlaylistFlag();
    }


}
