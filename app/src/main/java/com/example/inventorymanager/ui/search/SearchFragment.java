package com.example.inventorymanager.ui.search;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.inventorymanager.MainActivity;
import com.example.inventorymanager.R;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;

import com.example.inventorymanager.RecyclerViewAdapter;

import static android.content.ContentValues.TAG;

public class SearchFragment extends Fragment {
    public MainActivity MyMainActivity;
    JSONObject MyJson;
    JSONArray ListOfEntries;
    JSONObject DataEntry;
    
    EditText NameSearch;
    EditText ModSearch;
    Button ButtonSearch;
    
    String EntryName = "Name";
    String EntryMod = "Mod";
    String EntryQuantity = "Qty";
    String EntryLocation = "Loc";
    
    private ArrayList<String> mCardNames = new ArrayList<>();
    private ArrayList<String> mCardMods = new ArrayList<>();
    private ArrayList<String> mCardQuantities = new ArrayList<>();
    private ArrayList<String> mCardLocations = new ArrayList<>();
    
    public ArrayList<String> toRecyclerNames = new ArrayList<>();
    public ArrayList<String> toRecyclerMods = new ArrayList<>();
    public ArrayList<String> toRecyclerQuantities = new ArrayList<>();
    public ArrayList<String> toRecyclerLocations = new ArrayList<>();
    
    public RecyclerViewAdapter adapter;

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_search, container, false);

        MyMainActivity = (MainActivity) getActivity();
        MyJson = (JSONObject) MyMainActivity.MyJSON;
    
        NameSearch = root.findViewById(R.id.NameSearch);
        ModSearch = root.findViewById(R.id.ModSearch);
        ButtonSearch = root.findViewById(R.id.ButtonSearch);
    
        ButtonSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                searchButton();
            }
        });

        ListOfEntries = (JSONArray) MyJson.get("data");
        for (int i = 0; i < ListOfEntries.size(); i++) {
            DataEntry = (JSONObject) ListOfEntries.get(i);
            EntryName = (String) DataEntry.get("Name");
            EntryMod = (String) DataEntry.get("Mod");
            EntryQuantity = (String) DataEntry.get("Qty");
            EntryLocation = (String) DataEntry.get("Loc");
            
            mCardNames.add(EntryName);
            mCardMods.add(EntryMod);
            mCardQuantities.add(EntryQuantity);
            mCardLocations.add(EntryLocation);
        }
        toRecyclerNames.addAll(mCardNames);
        toRecyclerMods.addAll(mCardMods);
        toRecyclerQuantities.addAll(mCardQuantities);
        toRecyclerLocations.addAll(mCardLocations);
    
        Log.d(TAG, "onCreateView: Started");
    
        initRecyclerView(root);
        
        return root;
    }
    @Override
    public boolean onContextItemSelected(MenuItem item) {
        Log.d(TAG, "onContextItemSelected: " + item.getTitle() + item.getGroupId());
        mCardNames.clear();
        mCardMods.clear();
        mCardQuantities.clear();
        mCardLocations.clear();
        ListOfEntries = (JSONArray) MyJson.get("data");
        for (int i = 0; i < ListOfEntries.size(); i++) {
            DataEntry = (JSONObject) ListOfEntries.get(i);
            EntryName = (String) DataEntry.get("Name");
            EntryMod = (String) DataEntry.get("Mod");
            EntryQuantity = (String) DataEntry.get("Qty");
            EntryLocation = (String) DataEntry.get("Loc");
        
            mCardNames.add(EntryName);
            mCardMods.add(EntryMod);
            mCardQuantities.add("" + EntryQuantity);
            mCardLocations.add(EntryLocation);
        }
//        Log.d(TAG, "onContextItemSelected: " + item.getTitle() + " = Delete: " + (item.getTitle() == ("Delete")));
        if (item.getTitle().equals("Delete")) {
            toRecyclerNames.clear();
            toRecyclerMods.clear();
            toRecyclerQuantities.clear();
            toRecyclerLocations.clear();
            for (int i2 = 0; i2< mCardNames.size(); i2++) {
                Log.d(TAG, "onContextItemSelected: i2 " + i2);
                if (i2 != item.getGroupId()) {
                    toRecyclerNames.add(mCardNames.get(i2));
                    toRecyclerMods.add(mCardMods.get(i2));
                    toRecyclerLocations.add(mCardLocations.get(i2));
                    toRecyclerQuantities.add(mCardQuantities.get(i2));
                    Log.d(TAG, "onContextItemSelected: adding" + mCardNames.get(i2) + mCardMods.get(i2) + mCardQuantities.get(i2) + mCardLocations.get(i2));
                } else {
                    ListOfEntries.remove(i2);
                }
            }
            MyJson.put("data", ListOfEntries);
            FileOutputStream outputStream = null;
            try {
                outputStream = this.getContext().openFileOutput("config.json", Context.MODE_PRIVATE);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
            try {
                outputStream.write(MyJson.toString().getBytes());
            } catch (IOException e) {
                e.printStackTrace();
            }
            try {
                outputStream.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
            Log.d(TAG, "onContextItemSelected: Updating with " + toRecyclerNames);
            adapter.notifyDataSetChanged();
        }
        return true;
    }
    private void initRecyclerView(View view) {
        Log.d(TAG, "initRecyclerView: init recyclerview");
        RecyclerView recyclerView = view.findViewById(R.id.SearchRecyclerView);
        ArrayList<TextView> list = new ArrayList<>();
        list.add(NameSearch);
        list.add(ModSearch);
        adapter = new RecyclerViewAdapter(getContext(), toRecyclerNames, toRecyclerMods, toRecyclerQuantities, toRecyclerLocations, list);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
    }
    public void searchButton() {
        if ((NameSearch.getText().length() != 0) || (ModSearch.getText().length() != 0)) {
            Log.d(TAG, "searchButton: called");
            toRecyclerNames.clear();
            toRecyclerMods.clear();
            toRecyclerQuantities.clear();
            toRecyclerLocations.clear();
            for (int i = 0; i < mCardNames.size(); i++) {
                if ((mCardNames.get(i).toLowerCase().contains(NameSearch.getText().toString().toLowerCase()) || (NameSearch.getText().length() == 0)) &&
                    ( mCardMods.get(i).toLowerCase().contains( ModSearch.getText().toString().toLowerCase()) || ( ModSearch.getText().length() == 0))) {
                    toRecyclerNames.add((mCardNames.get(i)));
                    toRecyclerMods.add((mCardMods.get(i)));
                    toRecyclerQuantities.add((mCardQuantities.get(i)));
                    toRecyclerLocations.add((mCardLocations.get(i)));
                }
            }
        } else {
            toRecyclerNames.clear();
            toRecyclerMods.clear();
            toRecyclerQuantities.clear();
            toRecyclerLocations.clear();
            toRecyclerNames.addAll(mCardNames);
            toRecyclerMods.addAll(mCardMods);
            toRecyclerQuantities.addAll(mCardQuantities);
            toRecyclerLocations.addAll(mCardLocations);
        }
        adapter.notifyDataSetChanged();
    }
}