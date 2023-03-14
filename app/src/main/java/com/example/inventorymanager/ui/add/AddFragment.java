package com.example.inventorymanager.ui.add;

import android.content.Context;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
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
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;

import com.example.inventorymanager.RecyclerViewAdapter;

import static android.content.ContentValues.TAG;

public class AddFragment extends Fragment {
    public MainActivity MyMainActivity;
    JSONObject MyJson;
    JSONArray ListOfEntries;
    JSONObject DataEntry;
    
    EditText NameAdd;
    EditText ModAdd;
    EditText LocationAdd;
    EditText QuantityAdd;
    Button ButtonAdd;
    
    String EntryName = "Name";
    String EntryMod = "Mod";
    int EntryQuantity = 0;
    String EntryLocation = "Loc";
    
    private final ArrayList<String> mCardNames = new ArrayList<>();
    private final ArrayList<String> mCardMods = new ArrayList<>();
    private final ArrayList<String> mCardQuantities = new ArrayList<>();
    private final ArrayList<String> mCardLocations = new ArrayList<>();
    
    public final ArrayList<String> toRecyclerNames = new ArrayList<>();
    public final ArrayList<String> toRecyclerMods = new ArrayList<>();
    public final ArrayList<String> toRecyclerQuantities = new ArrayList<>();
    public final ArrayList<String> toRecyclerLocations = new ArrayList<>();
    
    public RecyclerViewAdapter adapter;
    
//    FileOutputStream outputStream;
    
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_add, container, false);
        
        MyMainActivity = (MainActivity) getActivity();
        MyJson = MyMainActivity.MyJSON;
        
        NameAdd = root.findViewById(R.id.NameAdd);
        ModAdd = root.findViewById(R.id.ModAdd);
        LocationAdd = root.findViewById(R.id.LocationAdd);
        QuantityAdd = root.findViewById(R.id.QuantityAdd);
        ButtonAdd = root.findViewById(R.id.ButtonAdd);
    
        registerForContextMenu(root.findViewById(R.id.AddRecyclerView));
    
        NameAdd.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
        
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                InstantSearch();}
        
            @Override
            public void afterTextChanged(Editable s) {}
        });
        ModAdd.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
        
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                InstantSearch();}
        
            @Override
            public void afterTextChanged(Editable s) {}
        });
        LocationAdd.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
        
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                InstantSearch();}
        
            @Override
            public void afterTextChanged(Editable s) {}
        });
        QuantityAdd.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
        
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                InstantSearch();}
        
            @Override
            public void afterTextChanged(Editable s) {}
        });
        
        ButtonAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    addButton();
                } catch (ParseException | IOException e) {
                    e.printStackTrace();
                }
            }
        });
        
        ListOfEntries = (JSONArray) MyJson.get("data");
        for (int i = 0; i < ListOfEntries.size(); i++) {
            DataEntry = (JSONObject) ListOfEntries.get(i);
            EntryName = (String) DataEntry.get("Name");
            EntryMod = (String) DataEntry.get("Mod");
            EntryQuantity = Integer.parseInt("" + DataEntry.get("Qty"));
            EntryLocation = (String) DataEntry.get("Loc");
            
            mCardNames.add(EntryName);
            mCardMods.add(EntryMod);
            mCardQuantities.add("" + EntryQuantity);
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
            EntryQuantity = Integer.parseInt("" + DataEntry.get("Qty"));
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
        RecyclerView recyclerView = view.findViewById(R.id.AddRecyclerView);
        ArrayList<TextView> list = new ArrayList<>();
        list.add(NameAdd);
        list.add(ModAdd);
        list.add(LocationAdd);
//        list.add(QuantityAdd);
        adapter = new RecyclerViewAdapter(getContext(), toRecyclerNames, toRecyclerMods, toRecyclerQuantities, toRecyclerLocations, list);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
    }
    public void addButton() throws ParseException, IOException {
        boolean found = false;
        ListOfEntries = (JSONArray) MyJson.get("data");
        mCardNames.clear();
        mCardMods.clear();
        mCardQuantities.clear();
        mCardLocations.clear();
        for (int i = 0; i < ListOfEntries.size(); i++) {
            DataEntry = (JSONObject) ListOfEntries.get(i);
            EntryName = (String) DataEntry.get("Name");
            EntryMod = (String) DataEntry.get("Mod");
            EntryQuantity = Integer.parseInt("" + DataEntry.get("Qty"));
            EntryLocation = (String) DataEntry.get("Loc");
        
            mCardNames.add(EntryName);
            mCardMods.add(EntryMod);
            mCardQuantities.add("" + EntryQuantity);
            mCardLocations.add(EntryLocation);
        }
        for (int i = 0; i < ListOfEntries.size(); i++) {
            DataEntry = (JSONObject) ListOfEntries.get(i);
            EntryName = (String) DataEntry.get("Name");
            EntryMod = (String) DataEntry.get("Mod");
            EntryQuantity = Integer.parseInt("" + DataEntry.get("Qty"));
            EntryLocation = (String) DataEntry.get("Loc");
            if (EntryName.equals(NameAdd.getText().toString()) && EntryMod.equals(ModAdd.getText().toString())) {
                Log.d(TAG, "addButton: Inc Qty: " + ListOfEntries.toString());
                EntryQuantity += Integer.parseInt("" + QuantityAdd.getText());
                DataEntry.put("Qty", "" + EntryQuantity);
                Log.d(TAG, "addButton: Inc Qty: " + ListOfEntries.toString());
                found = true;
                toRecyclerNames.clear();
                toRecyclerMods.clear();
                toRecyclerQuantities.clear();
                toRecyclerLocations.clear();
                for (int i2 = 0; i2< mCardNames.size(); i2++) {
                    toRecyclerNames.add(mCardNames.get(i2));
                    toRecyclerMods.add(mCardMods.get(i2));
                    toRecyclerLocations.add(mCardLocations.get(i2));
                    if (mCardNames.get(i2).equals(NameAdd.getText().toString())) {
                        toRecyclerQuantities.add("" + EntryQuantity);
                    } else {
                        toRecyclerQuantities.add(mCardQuantities.get(i2));
                    }
                }
            }
        }
        if (!found) {
            Log.d(TAG, "addButton: Inc LoE: " + ListOfEntries);
            Log.d(TAG, "addButton: With " +                     "{\"Name\":\"" + NameAdd.getText().toString() + "\",\"Mod\":\"" + ModAdd.getText().toString() + "\",\"Qty\":\"" + QuantityAdd.getText().toString() + "\",\"Loc\":\"" + LocationAdd.getText().toString() + "\",}");
            ListOfEntries.add(new JSONParser().parse("{\"Name\":\"" + NameAdd.getText().toString() + "\",\"Mod\":\"" + ModAdd.getText().toString() + "\",\"Qty\":\"" + QuantityAdd.getText().toString() + "\",\"Loc\":\"" + LocationAdd.getText().toString() + "\",}"));
            Log.d(TAG, "addButton: Inc LoE: " + ListOfEntries.toString());
            toRecyclerNames.add(NameAdd.getText().toString());
            toRecyclerMods.add(ModAdd.getText().toString());
            toRecyclerLocations.add(LocationAdd.getText().toString());
            toRecyclerQuantities.add(QuantityAdd.getText().toString());
        }
        NameAdd.setText("");
        ModAdd.setText("");
        LocationAdd.setText("");
        QuantityAdd.setText("");
        MyJson.put("data", ListOfEntries);
        FileOutputStream outputStream = this.getContext().openFileOutput("config.json", Context.MODE_PRIVATE);
        outputStream.write(MyJson.toString().getBytes());
        outputStream.close();
        adapter.notifyDataSetChanged();
    }
    public void InstantSearch() {
        if ((NameAdd.getText().length() != 0) || (ModAdd.getText().length() != 0)) {
            toRecyclerNames.clear();
            toRecyclerMods.clear();
            toRecyclerQuantities.clear();
            toRecyclerLocations.clear();
            for (int i = 0; i < mCardNames.size(); i++) {
                if ((mCardNames.get(i).toLowerCase().contains(NameAdd.getText().toString().toLowerCase()) || (NameAdd.getText().length() == 0)) &&
                        ( mCardMods.get(i).toLowerCase().contains( ModAdd.getText().toString().toLowerCase()) || ( ModAdd.getText().length() == 0))) {
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