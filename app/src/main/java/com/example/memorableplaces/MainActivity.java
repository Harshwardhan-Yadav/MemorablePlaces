package com.example.memorableplaces;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;

public class MainActivity extends AppCompatActivity {

    ArrayList<Double> lat;
    ArrayList<Double> lng;
    SharedPreferences sharedPreferences;
    Crypto ob = new Crypto();

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(resultCode == 1){
            //System.out.println(Arrays.toString(data.getStringArrayListExtra("list").toArray()));

            lat = (ArrayList<Double>) data.getSerializableExtra("lat");
            lng = (ArrayList<Double>) data.getSerializableExtra("lng");

            ArrayList<String> parameter1 = new ArrayList<>();
            for(int i=0;i<lat.size();i++){
                parameter1.add(lat.get(i)+"");
            }
            ArrayList<String> parameter2 = new ArrayList<>();
            for(int i=0;i<lng.size();i++){
                parameter2.add(lng.get(i)+"");
            }

            ListView listView = (ListView) findViewById(R.id.listView);

            ArrayList<String> arrayList = data.getStringArrayListExtra("list");

            ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1,arrayList);

            sharedPreferences.edit().putString("arr",ob.Encode(arrayList)).apply();
            sharedPreferences.edit().putString("lat",ob.Encode(parameter1)).apply();
            sharedPreferences.edit().putString("lng",ob.Encode(parameter2)).apply();

            listView.setAdapter(arrayAdapter);

            listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    Intent intent = new Intent(getApplicationContext(),MapsActivity.class);
                    intent.putExtra("list",arrayList);
                    intent.putExtra("lat",lat);
                    intent.putExtra("lng",lng);
                    intent.putExtra("position",position);
                    startActivityForResult(intent,1);
                }
            });
        }

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        sharedPreferences = this.getSharedPreferences("com.example.memorableplaces", Context.MODE_PRIVATE);
        ListView listView;
        ArrayList<String> arrayList = new ArrayList<String>();
        lat = new ArrayList<>();
        lng = new ArrayList<>();
        if(sharedPreferences.getString("arr","Null").compareTo("Null")!=0){
            listView = (ListView) findViewById(R.id.listView);
            String[] list = ob.Decode(sharedPreferences.getString("arr","null"));
            for(int i=0;i<list.length;i++){
                arrayList.add(list[i]);
            }
            list = ob.Decode(sharedPreferences.getString("lat","null"));
            for(int i=0;i<list.length;i++){
                lat.add(Double.parseDouble(list[i]));
            }
            list = ob.Decode(sharedPreferences.getString("lng","null"));
            for(int i=0;i<list.length;i++){
                lng.add(Double.parseDouble(list[i]));
            }
        }
        else {

            lat.add((double) 0);
            lng.add((double) 0);

            listView = (ListView) findViewById(R.id.listView);

            arrayList.add("Add a new place...");
        }
        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1,arrayList);

        listView.setAdapter(arrayAdapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(getApplicationContext(),MapsActivity.class);
                intent.putExtra("list",arrayList);
                intent.putExtra("lat",lat);
                intent.putExtra("lng",lng);
                intent.putExtra("position",position);
                startActivityForResult(intent,1);
            }
        });
    }
}