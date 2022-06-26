package com.example.soc_macmini_15.musicplayer.Fragments;


import android.content.ContentResolver;
import android.content.Context;
import android.content.DialogInterface;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.ListFragment;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import com.example.soc_macmini_15.musicplayer.Adapter.EbookAdapter;
import com.example.soc_macmini_15.musicplayer.Model.EbooksList;
import com.example.soc_macmini_15.musicplayer.R;

import java.util.ArrayList;

public class AllEbookFragment extends ListFragment {


    private static ContentResolver contentResolver1;

    public ArrayList<EbooksList> ebooksList;
    public ArrayList<EbooksList> newList;

    private ListView listView;

    private createDataParse createDataParse;
    private ContentResolver contentResolver;

    public static Fragment getInstance(int position, ContentResolver mcontentResolver) {
        Bundle bundle = new Bundle();
        bundle.putInt("pos", position);
        AllEbookFragment tabFragment = new AllEbookFragment();
        tabFragment.setArguments(bundle);
        contentResolver1 = mcontentResolver;
        return tabFragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        createDataParse = (createDataParse) context;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_tab, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        listView = view.findViewById(R.id.list_playlist);
        contentResolver = contentResolver1;
        setContent();
    }

    /**
     * Setting the content in the listView and sending the data to the Activity
     */
    public void setContent() {
        boolean searchedList = false;
        ebooksList = new ArrayList<>();
        newList = new ArrayList<>();
        getEbook();
        EbookAdapter adapter = new EbookAdapter(getContext(), ebooksList);
        if (!createDataParse.queryText().equals("")) {
            adapter = onQueryTextChange();
            adapter.notifyDataSetChanged();
            searchedList = true;
        } else {
            searchedList = false;
        }
        createDataParse.getLength(ebooksList.size());
        listView.setAdapter(adapter);

        final boolean finalSearchedList = searchedList;
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                // Toast.makeText(getContext(), "You clicked :\n" + songsList.get(position), Toast.LENGTH_SHORT).show();
                if (!finalSearchedList) {
                    createDataParse.onDataPass(ebooksList.get(position).getTitle(), ebooksList.get(position).getPath());
                    createDataParse.fullEbooksList(ebooksList, position);
                } else {
                    createDataParse.onDataPass(newList.get(position).getTitle(), newList.get(position).getPath());
                    createDataParse.fullEbooksList(ebooksList, position);
                }
            }
        });

        listView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                showDialog(position);
                return true;
            }
        });
    }


    public void getEbook() {
        Uri songUri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
        Cursor ebookCursor = contentResolver.query(songUri, null, null, null, null);
        if (ebookCursor != null && ebookCursor.moveToFirst()) {
            int ebookTitle = ebookCursor.getColumnIndex(MediaStore.Audio.Media.TITLE);
            int ebookArtist = ebookCursor.getColumnIndex(MediaStore.Audio.Media.ARTIST);
            int ebookPath = ebookCursor.getColumnIndex(MediaStore.Audio.Media.DATA);

            do {
                ebooksList.add(new EbooksList(ebookCursor.getString(ebookTitle), ebookCursor.getString(ebookArtist), ebookCursor.getString(ebookPath)));
            } while (ebookCursor.moveToNext());
            ebookCursor.close();
        }
    }

    public EbookAdapter onQueryTextChange() {
        String text = createDataParse.queryText();
        for (EbooksList ebooks : ebooksList) {
            String title = ebooks.getTitle().toLowerCase();
            if (title.contains(text)) {
                newList.add(ebooks);
            }
        }
        return new EbookAdapter(getContext(), newList);

    }

    private void showDialog(final int position) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setMessage(getString(R.string.play_next))
                .setCancelable(true)
                .setNegativeButton(R.string.no, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                })
                .setPositiveButton(R.string.yes, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        createDataParse.currentEbook(ebooksList.get(position));
                        setContent();
                    }
                });
        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }

    public interface createDataParse {
        public void onDataPass(String name, String path);

        public void fullEbooksList(ArrayList<EbooksList> ebookList, int position);

        public String queryText();

        public void currentEbook(EbooksList ebooksList);
        public void getLength(int length);
    }

}
