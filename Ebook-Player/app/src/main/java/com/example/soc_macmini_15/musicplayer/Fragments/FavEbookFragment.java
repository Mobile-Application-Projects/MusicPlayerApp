package com.example.soc_macmini_15.musicplayer.Fragments;


import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
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
import android.widget.Toast;

import com.example.soc_macmini_15.musicplayer.Adapter.EbookAdapter;
import com.example.soc_macmini_15.musicplayer.DB.FavoritesOperations;
import com.example.soc_macmini_15.musicplayer.Model.EbooksList;
import com.example.soc_macmini_15.musicplayer.R;

import java.util.ArrayList;

public class FavEbookFragment extends ListFragment {

    private FavoritesOperations favoritesOperations;


    public ArrayList<EbooksList> ebooksList;
    public ArrayList<EbooksList> newList;

    private ListView listView;

    private createDataParsed createDataParsed;

    public static Fragment getInstance(int position) {
        Bundle bundle = new Bundle();
        bundle.putInt("pos", position);
        FavEbookFragment tabFragment = new FavEbookFragment();
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
        favoritesOperations = new FavoritesOperations(context);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_tab, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        listView = view.findViewById(R.id.list_playlist);
        setContent();
    }

    /**
     * Setting the content in the listView and sending the data to the Activity
     */
    public void setContent() {
        boolean searchedList = false;
        ebooksList = new ArrayList<>();
        newList = new ArrayList<>();
        ebooksList = favoritesOperations.getAllFavorites();
        EbookAdapter adapter = new EbookAdapter(getContext(), ebooksList);
        if (!createDataParsed.queryText().equals("")) {
            adapter = onQueryTextChange();
            adapter.notifyDataSetChanged();
            searchedList = true;
        } else {
            searchedList = false;
        }

        listView.setAdapter(adapter);

        final boolean finalSearchedList = searchedList;
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                // Toast.makeText(getContext(), "You clicked :\n" + songsList.get(position), Toast.LENGTH_SHORT).show();
                if (!finalSearchedList) {
                    createDataParsed.onDataPass(ebooksList.get(position).getTitle(), ebooksList.get(position).getPath());
                    createDataParsed.fullEbooksList(ebooksList, position);
                } else {
                    createDataParsed.onDataPass(newList.get(position).getTitle(), newList.get(position).getPath());
                    createDataParsed.fullEbooksList(ebooksList, position);
                }
            }
        });
        listView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                deleteOption(position);
                return true;
            }
        });
    }

    private void deleteOption(int position) {
        if (position != createDataParsed.getPosition())
            showDialog(ebooksList.get(position).getPath(), position);
        else
            Toast.makeText(getContext(), "You Can't delete the Current Song", Toast.LENGTH_SHORT).show();
    }

    public interface createDataParsed {
        public void onDataPass(String name, String path);

        public void fullEbooksList(ArrayList<EbooksList> ebookList, int position);

        public int getPosition();

        public String queryText();
    }

    public EbookAdapter onQueryTextChange() {
        String text = createDataParsed.queryText();
        for (EbooksList ebooks : ebooksList) {
            String title = ebooks.getTitle().toLowerCase();
            if (title.contains(text)) {
                newList.add(ebooks);
            }
        }
        return new EbookAdapter(getContext(), newList);

    }

    private void showDialog(final String index, final int position) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setTitle(getString(R.string.delete))
                .setMessage(getString(R.string.delete_text))
                .setCancelable(true)
                .setNegativeButton(R.string.no, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                })
                .setPositiveButton(R.string.yes, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        favoritesOperations.removeEbook(index);
                        createDataParsed.fullEbooksList(ebooksList, position);
                        setContent();
                    }
                });
        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }

}
