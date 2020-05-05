package com.darrenmleith.todolist;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.HashSet;

public class MainActivity extends AppCompatActivity {

    static ArrayList<String> _notes = new ArrayList<>();
    static ArrayAdapter _arrayAdapter;
    static SharedPreferences _sharedPreferences;

    //Could have used a HashSet here to store (unordered) all the notes. This would have meant no need for ObjectSerializer code
    //HashSet<String> set = (HashSet<String>) sharedPreferences.getStringSet("notes", null);

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.menu_main, menu);
        return super.onCreateOptionsMenu(menu);
    }


    //add item
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        super.onOptionsItemSelected(item);
        _notes.add("");
        Intent intent = new Intent(getApplicationContext(), SecondActivity.class);
        intent.putExtra("indexOfNote", _notes.size() - 1);
        startActivity(intent);
        return true;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        _sharedPreferences = this.getSharedPreferences("package com.darrenmleith.todolist", Context.MODE_PRIVATE);
        ArrayList<String> returnedNotes = new ArrayList<>();
        try {
            returnedNotes = (ArrayList<String>) ObjectSerializer.deserialize(_sharedPreferences.getString("notes", ObjectSerializer.serialize(new ArrayList<String>())));

            if (returnedNotes.size()>0) {
                _notes = returnedNotes;
            } else {
                _notes.add("My first to do");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        ListView listView = findViewById(R.id.listView);
        _arrayAdapter = new ArrayAdapter(this, android.R.layout.simple_list_item_1, _notes);
        listView.setAdapter(_arrayAdapter);

        //edit item
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Intent intent = new Intent(getApplicationContext(), SecondActivity.class);
                intent.putExtra("indexOfNote", i);
                startActivity(intent);
            }
        });

        //delete
        listView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> adapterView, View view, int i, long l) {

                final int itemToDelete = i;

                new AlertDialog.Builder(MainActivity.this)
                        .setIcon(android.R.drawable.ic_dialog_alert)
                        .setTitle("Delete?")
                        .setMessage("Are you sure?")
                        .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                _notes.remove(itemToDelete);

                                //using a HashSet
                                //HashSet<String> set = new HashSet<>(MainActivity.notes);
                                //sharedPreferences.edit().putStringSet("notes", set).apply();

                                try {
                                    _sharedPreferences.edit().putString("notes", ObjectSerializer.serialize(_notes)).apply();
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                                _arrayAdapter.notifyDataSetChanged();
                            }
                        })
                        .setNegativeButton("No", null)
                        .show();
                return true;
            }
        });
    }
}
