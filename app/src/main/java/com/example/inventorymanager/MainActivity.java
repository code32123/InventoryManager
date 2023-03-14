package com.example.inventorymanager;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import androidx.appcompat.app.AppCompatActivity;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.*;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.StringWriter;
import java.util.Scanner;
import static android.content.ContentValues.TAG;

public class MainActivity extends AppCompatActivity {
    
    public JSONObject MyJSON;
    public String jsonFileString = "";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        BottomNavigationView navView = findViewById(R.id.nav_view);
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        AppBarConfiguration appBarConfiguration = new AppBarConfiguration.Builder(
                R.id.navigation_search, R.id.navigation_add, R.id.navigation_settings)
                .build();
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        NavigationUI.setupActionBarWithNavController(this, navController, appBarConfiguration);
        NavigationUI.setupWithNavController(navView, navController);
    
        File file = new File(getFilesDir(), "config.json");
    
        String config = "";
        if(file.exists()) {
            Log.d(TAG, "onCreate: Data from file");
            Scanner myReader = null;
            try {
                myReader = new Scanner(file);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
            while (myReader.hasNextLine()) {
                String line = myReader.nextLine();
                Log.d(TAG, "onCreate: Line: " + line);
                jsonFileString += line;
            }
            Log.d(TAG, "onCreate: File contents: " + jsonFileString);
            myReader.close();
            try {
                MyJSON = (JSONObject) new JSONParser().parse(jsonFileString);
            } catch (ParseException e) {
                e.printStackTrace();
            }
        } else {
            jsonFileString = Utils.getJsonFromAssets(getApplicationContext(), "test.json");
            Log.d(TAG, "onCreate: Data from asset");
            FileOutputStream outputStream = null;
            try {
                outputStream = openFileOutput("config.json", Context.MODE_PRIVATE);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
            try {
                MyJSON = (JSONObject) new JSONParser().parse(jsonFileString);
            } catch (ParseException e) {
                e.printStackTrace();
            } //catch (ClassCastException a) {
//                try {
//                    MyJSON = (JSONObject) new JSONParser().parse("{}");
//                    MyJSON.put( (JSONObject) new JSONParser().parse(jsonFileString));
//                } catch (ParseException e) {
//                    e.printStackTrace();
//                }
//            }
            Log.d(TAG, "onCreate: Saving to file: " + MyJSON);
            StringWriter out = new StringWriter();
            try {
                MyJSON.writeJSONString(out);
            } catch (IOException e) {
                e.printStackTrace();
            }
            try {
                outputStream.write(out.toString().getBytes());
            } catch (IOException e) {
                e.printStackTrace();
            }
            try {
                outputStream.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
            Log.d(TAG, "onCreate: File created: " + file.exists());
        }
    }
}